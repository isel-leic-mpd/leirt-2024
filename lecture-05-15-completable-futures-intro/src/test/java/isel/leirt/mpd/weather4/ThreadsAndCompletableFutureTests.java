package isel.leirt.mpd.weather4;

import isel.leirt.mpd.nio2.NioUtilsTests;
import isel.leirt.mpd.weather4.dto.WeatherInfoDto;
import isel.leirt.mpd.weather4.requests.HttpRequest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;

import static isel.leirt.mpd.weather4.utils.ThreadUtils.join;
import static isel.leirt.mpd.weather4.utils.ThreadUtils.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreadsAndCompletableFutureTests {
    private final static double LISBON_LAT  =  38.7071;
    private final static double LISBON_LONG = -9.1359;
    
    private final static double PORTO_LAT  =  41.1494512;
    private final static double PORTO_LONG = -8.6107884;
    
    
    private static Logger logger =
        LoggerFactory.getLogger(ThreadsAndCompletableFutureTests.class);
    
    CompletableFuture<Integer> inc(int i) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("inc start");
            sleep(2000);
            logger.info("inc end");
            return i + 1;
        });
    }
    
    CompletableFuture<Integer> square(int i) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("square start");
            sleep(3000);
            logger.info("square end");
            return i * i;
        });
    }
   
    
    
    @Test
    public void firstExampleWithThreads() {
        Thread t = new Thread(() -> {
            sleep(500);
            System.out.println("Hello from thread " +
                                   Thread.currentThread());
        });
        
        t.start();
        
        join(t);
        System.out.println("Test thread is  " +
                               Thread.currentThread());
    }
    
    // what can go wrong with this code?
    private List<WeatherInfoDto> getWeatherFromLisbonAndPortoInParallel0() {
        var webApi = new OpenWeatherWebApi(new HttpRequest());
        var citiesWeather = new LinkedList<WeatherInfoDto>();
        var mutex = new ReentrantLock();
        
        var tLisbon = new Thread(() -> {
            mutex.lock();
            var res = webApi.weatherAt(LISBON_LAT, LISBON_LONG);
            citiesWeather.add(res);
            mutex.unlock();
        });
        tLisbon.start();
        
        var tPorto = new Thread(() -> {
            mutex.lock();
            var res = webApi.weatherAt(PORTO_LAT, PORTO_LONG);
            citiesWeather.add(res);
            mutex.unlock();
        });
        tPorto.start();
        
        join(tLisbon);
        join(tPorto);
        
        return citiesWeather;
    }
    
    private List<WeatherInfoDto> getWeatherFromLisbonAndPortoInParallel() {
        var webApi = new OpenWeatherWebApi(new HttpRequest());
        
        WeatherInfoDto[] wi = new WeatherInfoDto[2];
        
        var tLisbon = new Thread(() -> {
            wi[0] = webApi.weatherAt(LISBON_LAT, LISBON_LONG);
        });
        tLisbon.start();
        
        var tPorto = new Thread(() -> {
            wi[1] = webApi.weatherAt(PORTO_LAT, PORTO_LONG);
        });
        tPorto.start();
        
        join(tLisbon);
        join(tPorto);
        
        return Arrays.asList(wi);
    }
    
    
    @Test
    // ok with current free key
    public void get_weather_at_lisbon_oporto_in_parallel_using_threads() {
        var weather =
            getWeatherFromLisbonAndPortoInParallel0();
        assertEquals(2, weather.size());
        
        for(var wi : weather) {
            System.out.println(wi);
        }
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void fistUseOfCompletableFutures() {
        // launch a completable future...
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("supplier 1 in thread " + Thread.currentThread());
            sleep(2000);
            return 10;
        });
        
        // launch another completable future...
        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("supplier 2 in thread " + Thread.currentThread());
            sleep(1000);
            return 20;
        });
        
        // and wait for both... ugh!
        var f1Res = f1.join();
        var f2Res = f2.join();
        
        assertEquals(10, f1Res);
        assertEquals(20, f2Res);
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void useOfCompletableFuturesUSingCombination() {
        // launch a completable future...
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("supplier 1 in thread " + Thread.currentThread());
            sleep(2000);
            return 10;
        });
        
        // launch another completable future...
        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("supplier 2 in thread " + Thread.currentThread());
            sleep(1000);
            return 20;
        });
        
        // and combine them.. ok!
        var fComb =
                f1.thenCombine(f2, (r1, r2) -> r1 + r2)
                .thenApply(i -> i.toString())
                .thenAccept(System.out::println);
               
        
        // since we are in a test we must wait anyway...
        fComb.join();
       
    }
    
    @SuppressWarnings("unchecked")
    private CompletableFuture<List<Integer>> incAll(List<Integer> values) {
        CompletableFuture<Integer>[] futArray = values.stream()
                .map( i -> inc(i))
                .toArray(n -> new CompletableFuture[n]);
        return CompletableFuture.allOf(futArray)
                     .thenApply(__ ->
                        Arrays.stream(futArray)
                        .map(o -> o.join())
                        .toList()
                     );
        
    }
    
    /**
     * Running the incAll3Test, explain the different behaviours between the uncommented
     * and the comment versions of the code below
     * @param values
     */
    private void incAll3(List<Integer> values) {
        
        values.stream()
                      .map( i -> inc(i))
                      .forEach(f -> System.out.println(f.join()));
        
//        var l = values.stream()
//                     .map( i -> inc(i))
//                    .toList();
//
//       l.stream().forEach(f -> System.out.println(f.join()));
    }
    
    @SuppressWarnings("unchecked")
    private CompletableFuture<List<Integer>> incAll2(List<Integer> values) {
        CompletableFuture<List<Integer>> start =
            CompletableFuture.completedFuture(new ArrayList<>());
            return values.stream()
                  .map( i -> inc(i))
                  .reduce(start,
                    (acc, curr) ->
                        acc.thenCombine(curr, (l, v) -> {
                            l.addLast(v);
                            return l;
                        })
                    ,
                    (a1, a2) ->
                        a1.thenCombine(a2, (l1, l2) -> {
                            l1.addAll(l2);
                            return l1;
                        })
              );
          
    }
    
    @Test
    public void sequenceOfAsyncOperationsUsingFutures() {
       var cf = inc(2)
                .thenCompose(n -> {
                    logger.info("in  compose");
                    return square(n);
                });
       
       
       assertEquals(9, cf.join());
       logger.info("test end");
    }
    
    @Test
    public void incAllTest() {
        logger.info("test start");
        var values = List.of(1,2,3,4);
        var expected = List.of(2,3,4,5);
        
        var futResult = incAll(values);
        assertEquals(expected, futResult.join());
        logger.info("test end");
    }
    
    @Test
    public void incAll2Test() {
        logger.info("test start");
        var values = List.of(1,2,3,4);
        var expected = List.of(2,3,4,5);
        
        var futResult = incAll2(values);
        assertEquals(expected, futResult.join());
        logger.info("test end");
    }
    
    @Test
    public void incAll3Test() {
        var values = List.of(1,2,3,4);
        incAll3(values);
    }
}
