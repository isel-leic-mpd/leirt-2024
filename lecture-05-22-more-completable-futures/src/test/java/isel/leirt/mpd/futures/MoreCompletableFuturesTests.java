package isel.leirt.mpd.futures;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static isel.leirt.mpd.futures.ThreadUtils.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoreCompletableFuturesTests {
    private static Logger logger =
        LoggerFactory.getLogger(MoreCompletableFuturesTests.class);
    
    
    CompletableFuture<Integer> oper1Async(int i1, int i2) {
        CompletableFuture<Integer> res = CompletableFuture.supplyAsync(() -> {
            logger.info("oper1 start");
            sleep(2000);
            logger.info("oper1 end");
            return i1 + i2;
        });
        return res;
    }
    
    CompletableFuture<Integer> oper2Async(int i) {
        CompletableFuture<Integer> res = CompletableFuture.supplyAsync(() -> {
            logger.info("square start");
            sleep(3000);
            logger.info("square end");
            return i * i;
        });
        return res;
    }
    
    @SuppressWarnings("unchecked")
    private CompletableFuture<Integer> sumOfOper2Async(List<Integer> values) {
        
        CompletableFuture<Integer>[] futs = values.stream()
            .map( i -> oper2Async(i))
            .toArray(n -> new CompletableFuture[n]);
        
       return CompletableFuture.allOf(futs)
                               .thenApply( __ -> {
//            return Arrays.stream(futs)
//            .mapToInt(f -> f.join())
//            .sum();
           
           return Arrays.stream(futs)
                      .map(f -> f.join())
                      .reduce(0, (acc, n) -> acc + n);
        });
        
    }
    
    @SuppressWarnings("unchecked")
    private CompletableFuture<Integer> sum2OfOper2Async(List<Integer> values) {
        var initial = CompletableFuture.completedFuture(0);
        return values.stream()
        .map( i -> oper2Async(i))
        .reduce(initial, (accFt, nFt) -> accFt.thenCombine(nFt, (n1, n2) -> n1 + n2));
    }
    
    
    // changed to reciving an iterator of the list since
    // the get operation on a list can have a cost proportional to lis tsize
    // and so this use could have a quadratic cost
    private CompletableFuture<Integer>
            accumulateAuxAsync(Iterator<Integer> iter,  int acc) {
        if (!iter.hasNext()) {
            return CompletableFuture.completedFuture(acc);
        }
        var n = iter.next();
        return oper1Async(acc, n)
               .thenCompose(acc1 -> {
                   logger.info("before  accumulateAuxAsync");
                   var res = accumulateAuxAsync(iter, acc1);
                   logger.info("after  accumulateAuxAsync");
                   return res.whenComplete((res1, err) -> {
                       logger.info("completion of n {} with accumulation {}", n, res1);
                   });
               });
    }
    
    private CompletableFuture<Integer> accumulateOper1Async(List<Integer> values) {
//        return oper1Async(values.get(0), values.get(1))
//                    .thenCompose( acc -> oper1Async(acc, values.get(2)));
        
        logger.info("start accumulateOper1Async");
          var fut = accumulateAuxAsync(values.iterator(),  0);
        logger.info("end accumulateOper1Async");
        return fut;
    }
   
    
    @Test
    public void sumOfOper2AsyncTest() {
        var values = List.of(1,2,3,-1, 7, 1);
        
        var sum = sumOfOper2Async(values).join();
        assertEquals(65, sum);
        
    }
    
    @Test
    public void sum2OfOper2AsyncTest() {
        var values = List.of(1,2,3,-1, 7, 1);
        
        var sum = sum2OfOper2Async(values).join();
        assertEquals(65, sum);
    }
    
    @Test
    public void accOper1AsyncTest() {
        var values = List.of(1,2,3);
        
        logger.info("before accumulate values async test");
        var cf = accumulateOper1Async(values);
        logger.info("after accumulate values async test");
        var sum = cf.join();
        
        logger.info("get values async value {}", sum);
       
        assertEquals(6, sum);
    }
    
    @Test
    public void chainOfSquaresTest() {
        var futRes = oper2Async(2)
                         .whenComplete((v, e) -> logger.info("first square completed:{}", v))
                         .thenCompose( r -> oper2Async(r))
                         .whenComplete((v, e) -> logger.info("second square completed:{}", v))
                         .thenCompose( r -> oper2Async(r))
                         .whenComplete((v, e) -> logger.info("third square completed:{}", v));
        
        logger.info("futRes completed: {}", futRes.join());
    }
    
}
