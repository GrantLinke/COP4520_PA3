import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class BDayParty {

    public static int nInf = -9999999;
    public static int pInf = 9999999;
    public static int numPres = 100000;
    public static int[] presents = new int[500000];
    public static int[] delPresents = new int[500000];
    public static AtomicInteger index = new AtomicInteger(0); // index thru array presents
    public static AtomicInteger presentBag = new AtomicInteger(0); // how we keep track of presents left in bag
    public static AtomicInteger thanks = new AtomicInteger(0); // to keep track of thanks written
    public static AtomicLong check = new AtomicLong(0); // counts times we contains() as requested
    public static AtomicLong checkY = new AtomicLong(0); // check Yes
    public static AtomicLong checkN = new AtomicLong(0); // check No
    public static Node leftSentinel = new Node(nInf); // sentinel node left
    public static Node rightSentinel = new Node(pInf); // sentinel node right

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(4); // 4 servents
        leftSentinel.next = new AtomicMarkableReference<Node>(rightSentinel, false); // linking sentinels together

        for (int i = 0; i < numPres; i++) {
            presents[i] = i; // populate presents with 500,000 entries
            delPresents[i] = i;
        }

        shuffle(presents); // giving a random order to insert

        Servant serv = new Servant();
        for (int i = 0; i < 4; i++) {
            threadPool.execute(serv);
        }

        for (int i = 0; i < 4; i++) {
            threadPool.shutdown();
        }

        try {
            threadPool.awaitTermination(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.printf(
                "Results:\nThanks written: %d\nTimes checked: %d\n\tCheck success: %d\n\tCheck unsuccessful: %d\nPresents added: %d",
                thanks.getPlain(), check.getPlain(), checkY.getPlain(), checkN.getPlain(), presentBag.get());

        return;
    }

    public static void shuffle(int[] arr) {
        Random rand = new Random();
        int index = 0;
        int length = numPres;
        int temp;

        for (int i = 0; i < length; i++) {
            index = rand.nextInt(length);
            temp = arr[index];
            arr[index] = arr[i]; // performs a random swap.
            arr[i] = temp;
        }
        return;
    }
}