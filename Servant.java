import java.util.Random;

public class Servant implements Runnable {
    @Override
    public void run() {
        Random rand = new Random();
        Node head = BDayParty.leftSentinel;
        int index = BDayParty.sIndex.getPlain();

        while (BDayParty.presentBag.getPlain() < 500000) // still got some thanks to write.
        {
            int task = rand.nextInt(3);
            switch (task) {
                case 0:
                    // add / put pres on chain
                    break;
                case 1:
                    // delete / thanks
                    break;
                case 2:
                    // contains
                    break;
                default:
                    System.out.printf("How?\n");
            }
        }

    }

}
