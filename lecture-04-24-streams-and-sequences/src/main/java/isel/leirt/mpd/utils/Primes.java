package isel.leirt.mpd.utils;

import static java.lang.Math.sqrt;

public class Primes {
    
    public static boolean isPrime(long n) {
        if (n ==2) return true;
        if (n % 2 ==0 || n < 2) return false;
        for(long t = 3; t < sqrt(n); t += 2)
            if (n % t == 0) return false;
        return true;
    }
    
    public static long nextPrime(long from) {
        if (from <= 1) return 2;
        long candidate = (from % 2 == 0) ? from + 1 : from + 2;
        while(!isPrime(candidate)) {
            candidate += 2;
        }
        return candidate;
    }
}
