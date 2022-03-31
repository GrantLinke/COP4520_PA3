import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BDayParty {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(4); // 4 servents
        PresChain presents = new PresChain(-9999999, null); // sentinel node left
        PresChain es = new PresChain(9999999, null);
        presents.next = es; // linking sentinels together
    }
}