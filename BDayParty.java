import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BDayParty {

    public static int nInf = -9999999;
    public static int pInf = 9999999;
    public static int[] presents = new int[500000];
    public static AtomicInteger presentBag = new AtomicInteger(0); // how we keep track of presents left in bag
    public static AtomicInteger sIndex = new AtomicInteger(0);
    public static AtomicBoolean[] thanks = new AtomicBoolean[500000];
    public static Node leftSentinel = new Node(nInf, null); // sentinel node left
    public static Node rightSentinel = new Node(pInf, null); // sentinel node right

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(4); // 4 servents
        leftSentinel.next.set(rightSentinel, false); // linking sentinels together

        for (int i = 0; i < 500000; i++) {
            presents[i] = i; // populate presents with 500,000 entries
            thanks[i] = new AtomicBoolean(false); // makes it so we haven't marked any presents
        }

    }

    public static void shuffle(int[] arr) {
        Random rand = new Random();
        int index = 0;
        int length = arr.length;

        for (int i = 0; i < length; i++) {
            index = rand.nextInt(length);
            int temp = arr[index];
            arr[index] = arr[i]; // performs a random swap.
            arr[i] = temp;
        }
        return;
    }
}