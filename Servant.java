import java.util.Random;

public class Servant implements Runnable {
    @Override
    public void run() {
        Random rand = new Random();
        boolean result = false;

        while (BDayParty.thanks.get() < BDayParty.numPres) // still got some thanks to write.
        {
            int task = 0;

            // the idea is to fill in chunks. 5000 at a time for efficiency
            if ((BDayParty.presentBag.get() + 1) % 5000 != 0
                    || BDayParty.thanks.get() >= BDayParty.presentBag.get() - 1000) {
                task = 0;
            } else {
                task = rand.nextInt(2) + 1;
            }

            if (BDayParty.index.getPlain() > BDayParty.numPres - 1) { // if it's max but we didn't loop out, here we go
                BDayParty.index.set(0);
            }

            // don't want to increment just to check a number
            int i = BDayParty.index.get();
            if (task != 1) {
                i = BDayParty.index.getAndIncrement();
            }

            while (true) { // present is on chain already, pls skip
                if (BDayParty.presents[i] == -1 && task == 0) {
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
                        BDayParty.presentBag.incrementAndGet(); // we just took a present out the bag
                    } else {
                        // System.out.println("Add failed.");
                    }
                    break;
                case 2:
                    // delete / thanks
                    result = Node.delete(BDayParty.leftSentinel, BDayParty.delPresents[i]);
                    if (result) { // delete worked! We just wrote a thank you.
                        BDayParty.thanks.getAndIncrement();
                    }
                    break;
                case 1:
                    // contains
                    result = Node.contains(BDayParty.leftSentinel, rand.nextInt(BDayParty.numPres));
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

        return;
    }

}
