package biogenome.m6;

import java.math.BigInteger;
import java.util.*;

/**
 * M6 - Randomized and Parallel Algorithms (CO6)
 * Miller-Rabin Primality Test for Large Numerical Processing.
 * Time Complexity: O(K * log^3 N)
 * Generates certified prime moduli for rolling-hash DNA algorithms.
 */
public class MillerRabinPrimality {

    public static boolean isPrime(long n, int kRounds) {
        if (n < 2) return false;
        if (n == 2 || n == 3 || n == 5 || n == 7) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;

        long d = n - 1;
        int r = 0;
        while (d % 2 == 0) {
            d /= 2;
            r++;
        }

        Random rand = new Random();
        BigInteger bigN = BigInteger.valueOf(n);
        BigInteger bigD = BigInteger.valueOf(d);

        for (int i = 0; i < kRounds; i++) {
            long a = 2 + (Math.abs(rand.nextLong()) % (n - 3));
            BigInteger bigA = BigInteger.valueOf(a);
            BigInteger x = bigA.modPow(bigD, bigN);

            if (x.equals(BigInteger.ONE) || x.equals(BigInteger.valueOf(n - 1))) {
                continue;
            }

            boolean composite = true;
            for (int j = 0; j < r - 1; j++) {
                x = x.modPow(BigInteger.valueOf(2), bigN);
                if (x.equals(BigInteger.valueOf(n - 1))) {
                    composite = false;
                    break;
                }
            }

            if (composite) return false;
        }

        return true;
    }

    public static Map<String, Object> generateLargePrime(int bitLength, int kRounds) {
        long t0 = System.nanoTime();
        Random rand = new Random();
        int candidatesTested = 0;
        BigInteger primeCandidate;

        while (true) {
            candidatesTested++;
            primeCandidate = BigInteger.probablePrime(bitLength, rand);
            if (isPrime(primeCandidate.longValue(), kRounds)) {
                break;
            }
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "Miller-Rabin Primality Generator");
        result.put("prime", primeCandidate.longValue());
        result.put("bitLength", bitLength);
        result.put("roundsTested", kRounds);
        result.put("candidatesEvaluated", candidatesTested);
        result.put("errorProbability", "<= 4^(-" + kRounds + ")");
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "O(K * log^3 N)");
        return result;
    }
}
