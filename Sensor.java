import java.util.Random;

public class Sensor implements Runnable {

    @Override
    public void run() {
        // TODO Auto-generated method stub
        Random rand = new Random();
        int temp = 0;
        for (int i = 0; i < 60; i++) {
            temp = rand.nextInt(171) - 100;

            if (isBetween(i, 0, 9)) { // assigns to our node array the random temperature
                Node.add(ATRM.nodeArray[0], temp);
            } else if (isBetween(i, 10, 19)) {
                Node.add(ATRM.nodeArray[1], temp);
            } else if (isBetween(i, 20, 29)) {
                Node.add(ATRM.nodeArray[2], temp);
            } else if (isBetween(i, 30, 39)) {
                Node.add(ATRM.nodeArray[3], temp);
            } else if (isBetween(i, 40, 49)) {
                Node.add(ATRM.nodeArray[4], temp);
            } else if (isBetween(i, 50, 59)) {
                Node.add(ATRM.nodeArray[5], temp);
            }

            try { // we sleep to simulate time passing.
                Thread.sleep(250);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

}
