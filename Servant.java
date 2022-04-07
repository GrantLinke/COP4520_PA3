import java.util.Random;

public class Servant implements Runnable {
    @Override
    public void run() {
        Random rand = new Random();
        boolean result = false;

        while (BDayParty.presentBag.getPlain() < 15) // still got some thanks to write.
        {
            int task = rand.nextInt(3);
            if (BDayParty.index.getPlain() > 14) { // if it's max but we didn't loop out, here we go again
                BDayParty.index.set(0);
            }
            int i = BDayParty.index.getAndIncrement();

            while (true) {
                if (BDayParty.presents[i] == -1) { // present is either on chain or ty note written.
                    i = BDayParty.index.getAndIncrement();
                } else {
                    break;
                }
            }

            switch (task) {
                case 0:
                    // add / put pres on chain
                    result = Node.add(BDayParty.leftSentinel, BDayParty.presents[i]);
                    if (result) { // add worked! no longer in bag
                        BDayParty.presents[i] = -1;
                    }
                    break;
                case 1:
                    // delete / thanks
                    result = Node.delete(BDayParty.leftSentinel, BDayParty.presents[i]);
                    if (result) { // delete worked! We just wrote a thank you.
                        BDayParty.thanks.getAndIncrement();
                    }
                    break;
                case 2:
                    // contains
                    result = Node.contains(BDayParty.leftSentinel, BDayParty.presents[i]);
                    BDayParty.check.getAndIncrement();
                    if (result) { // it was in there
                        BDayParty.checkY.getAndIncrement();
                    } else { // not in there
                        BDayParty.checkN.getAndIncrement();
                    }
                    break;
                default:
                    System.out.printf("How?\n");
            }
        }

    }

}
