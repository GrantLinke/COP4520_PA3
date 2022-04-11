import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class ATRM {
    // temp readings every 1 minute, for an hour at a time (report)
    // nThreads = 8;
    // sensors don't need to create report but the unit does

    // array of linked lists - each spot in array for a 10 min interval.
    // how to grab top 5? calculations! - we know if 8 sensors take an interval
    // every 1 minute, that's 80/10-min-interval
    // **well, if we see a duplicate value it won't be 80... how to accomodate?
    // loop thru and grab length I guess...
    // so we simply loop thru the list until we reach 75-80.
    // as far as the biggest temp diff, our list is already sorted, so we grab node
    // 1, we grab node 80, put the difference of those in an array,
    // and compare across array, grab the index of the largest number

    public static int nInf = -9999999;
    public static int pInf = 9999999;
    public static Node[] nodeArray = new Node[6]; // where we're storing our stuff

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(8); // 8 temp readers

        for (int i = 0; i < 6; i++) { // populating our array with references.
            Node lSent = new Node(nInf);
            Node rSent = new Node(pInf);
            lSent.next = new AtomicMarkableReference<Node>(rSent, false);

            nodeArray[i] = lSent;
        }

        Sensor task = new Sensor();
        for (int i = 0; i < 8; i++) {
            threadPool.execute(task);
        }

        for (int i = 0; i < 4; i++) {
            threadPool.shutdown();
        }

        try {
            System.out.print("Collecting data.");
            Thread.sleep(1000);
            System.out.print(".");
            Thread.sleep(1000);
            System.out.print(".");
            Thread.sleep(1000);
            System.out.print(".");
            Thread.sleep(1000);
            System.out.println(".");

            threadPool.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        generateReport();
        // sanityCheck();
        return;
    }

    public static void generateReport() {
        int[] length = getLength();
        int[][] tempMax = new int[6][5]; // stores local maxes for each 10min
        int[] trueMax = new int[5]; // stores global max
        int[][] tempLow = new int[6][5]; // stores local mins for each 10min
        int[] trueLow = new int[5]; // stores global min
        int[] preDiff = new int[6]; // stores the diffs of each interval
        int trueDiff = 0; // stores the actuall temp diff (10 min interval)
        int diffIndex = 0; // stores the actual interval of time
        int[][] indexesMax = new int[5][2]; // index values to not repeat for max
        int[][] indexesLow = new int[5][2]; // index values to not repeat for low
        Node temp = nodeArray[0].next.getReference();

        for (int i = 0; i < 5; i++) { // fills with fake indexes to prevent missed data
            for (int j = 0; j < 2; j++) {
                indexesLow[i][j] = -1;
                indexesMax[i][j] = -1;
            }
        }

        for (int i = 0; i < 6; i++) {
            temp = nodeArray[i];
            for (int j = 0, k = 0, z = 0; j < length[i] - 1; j++) {
                temp = temp.next.getReference(); // keep going thru chain

                if (j >= length[i] - 6) { // grab our maxes
                    tempMax[i][k] = temp.key;
                    k++;
                } else if (j < 5) { // grab our mins
                    tempLow[i][z] = temp.key;
                    z++;
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) { // since global min coulda all been in one 10-min span, we do this
                for (int k = 0; k < 5; k++) {

                    if (!indexMatch(indexesLow, j, k)) { // do we already have this min?
                        trueLow[i] = Math.min(trueLow[i], tempLow[j][k]);
                        if (trueLow[i] == tempLow[j][k]) { // setting low indexes so we don't hit it again.
                            indexesLow[i][0] = j;
                            indexesLow[i][1] = k;
                        }
                    }

                    if (!indexMatch(indexesMax, j, k)) { // do we already have this max?
                        trueMax[i] = Math.max(trueMax[i], tempMax[j][k]);
                        if (trueMax[i] == tempMax[j][k]) {
                            indexesMax[i][0] = j;
                            indexesMax[i][1] = k;
                        }
                    }

                } // inner indexing
            } // outer indexing
            preDiff[i] = trueMax[i] - trueLow[i];
            trueDiff = Math.max(trueDiff, preDiff[i]); // bigger number go brr
            diffIndex = trueDiff == preDiff[i] ? i : diffIndex; // if we changed value, then this is index
        } // global indexing

        printReport(trueMax, trueLow, trueDiff, diffIndex);
        return;
    }

    public static int[] getLength() {
        int[] length = new int[6];
        int count = 0;
        Node curr = nodeArray[0];

        for (int i = 0; i < 6; i++) {
            count = 0;
            curr = nodeArray[i];
            while (curr.key != pInf) {
                curr = curr.next.getReference();
                count++;
            }
            length[i] = count;
        }
        return length;
    }

    public static boolean indexMatch(int[][] indexes, int i, int j) {
        for (int z = 0; z < 5; z++) {
            if (indexes[z][0] == i && indexes[z][1] == j) {
                return true;
            }
        }
        return false;
    }

    public static void printReport(int[] high, int[] low, int diff, int diffInterval) {
        System.out.printf("REPORT:\n--------------------\n");

        System.out.printf("LOWEST TEMPS RECORDED: [");
        for (int i = 0; i < 4; i++) {
            System.out.printf("%d, ", low[i]);
        }
        System.out.printf("%d]\n", low[4]);

        System.out.printf("HIGHEST TEMPS RECORDED: [");
        for (int i = 0; i < 4; i++) {
            System.out.printf("%d, ", high[i]);
        }
        System.out.printf("%d]\n", high[4]);

        System.out.printf("LARGEST TEMP DIFF: %d\nTIME INTERVAL: %d-%dmin\n", diff, diffInterval * 10,
                diffInterval * 10 + 10);
    }

    public static void sanityCheck() {
        int[] length = getLength();
        Node temp = nodeArray[0];

        System.out.println("\n\n");

        for (int i = 0; i < 6; i++) {
            temp = nodeArray[i].next.getReference();
            System.out.printf("Nums for interval [%d]:\n", i);

            for (int j = 0; j < length[i] - 1; j++) {
                System.out.printf("%d ", temp.key);
                temp = temp.next.getReference();
            }

            System.out.println("\n");
        }
    }

}
