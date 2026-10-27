import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Checks for Backend.transfer, run with scripts/check.sh.
 *
 * 1. Bad amounts (negative, zero, NaN, infinite) and overdrafts are rejected.
 * 2. Many threads moving money at once never lose or create money, never overdraw
 *    an account, and never deadlock.
 */
public class TransferChecks {

    private static final int ACCOUNTS = 10;
    private static final double STARTING_BALANCE = 1_000;
    private static final int THREADS = 8;
    private static final int TRANSFERS_PER_THREAD = 25_000;

    private static int failures = 0;

    public static void main(String[] args) throws Exception {
        rejectsBadAmounts();
        rejectsOverdraftsAndSelfTransfers();
        keepsMoneyConstantUnderConcurrency();

        if (failures > 0) {
            System.out.println(failures + " check(s) failed");
            System.exit(1);
        }
        System.out.println("All checks passed");
    }

    private static void rejectsBadAmounts() throws Exception {
        List<User> users = accounts(2);
        User a = users.get(0), b = users.get(1);
        for (double bad : new double[] {-100, 0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}) {
            check(!Backend.transfer(a, b, bad), "rejects amount " + bad);
        }
        check(a.getBalance() == STARTING_BALANCE && b.getBalance() == STARTING_BALANCE,
                "bad amounts leave both balances unchanged");
    }

    private static void rejectsOverdraftsAndSelfTransfers() throws Exception {
        List<User> users = accounts(2);
        User a = users.get(0), b = users.get(1);
        check(!Backend.transfer(a, b, STARTING_BALANCE + 1), "rejects overdraft");
        check(!Backend.transfer(a, a, 10), "rejects paying yourself");
        check(Backend.transfer(a, b, 250), "accepts a valid transfer");
        check(a.getBalance() == 750 && b.getBalance() == 1_250, "valid transfer moves exactly 250");
    }

    private static void keepsMoneyConstantUnderConcurrency() throws Exception {
        List<User> users = accounts(ACCOUNTS);
        double expectedTotal = ACCOUNTS * STARTING_BALANCE;
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger succeeded = new AtomicInteger();

        long began = System.nanoTime();
        for (int t = 0; t < THREADS; t++) {
            long seed = t;
            pool.submit(() -> {
                Random random = new Random(seed);
                start.await();
                for (int i = 0; i < TRANSFERS_PER_THREAD; i++) {
                    User from = users.get(random.nextInt(ACCOUNTS));
                    User to = users.get(random.nextInt(ACCOUNTS));
                    if (Backend.transfer(from, to, 1 + random.nextInt(50))) {
                        succeeded.incrementAndGet();
                    }
                }
                return null;
            });
        }
        start.countDown();
        pool.shutdown();
        boolean finished = pool.awaitTermination(30, TimeUnit.SECONDS);
        long millis = (System.nanoTime() - began) / 1_000_000;

        check(finished, "finishes without deadlock");
        double total = users.stream().mapToDouble(User::getBalance).sum();
        check(total == expectedTotal, "total stays " + expectedTotal + " (was " + total + ")");
        check(users.stream().allMatch(u -> u.getBalance() >= 0), "no account goes negative");
        System.out.printf("  %,d transfers attempted on %d threads, %,d succeeded, in %d ms%n",
                THREADS * TRANSFERS_PER_THREAD, THREADS, succeeded.get(), millis);
    }

    private static List<User> accounts(int count) throws Exception {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = new User("User " + i, "user" + i + "@example.com", "unused");
            user.setAccountID(700_000_000_001L + i);
            user.setBalance(STARTING_BALANCE);
            users.add(user);
        }
        return users;
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "  PASS  " : "  FAIL  ") + name);
        if (!ok) {
            failures++;
        }
    }
}
