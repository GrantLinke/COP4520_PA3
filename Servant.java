import java.util.Random;

public class Servant implements Runnable {
    @Override
    public void run() {
        Random rand = new Random();
        boolean result = false;

        while (BDayParty.thanks.get() < 15) // still got some thanks to write.
        {
            int task = rand.nextInt(3);
            if (BDayParty.index.getPlain() > 14) { // if it's max but we didn't loop out, here we go again
                BDayParty.index.set(0);
            }
            int i = BDayParty.index.getAndIncrement();

            while (true) { // present is either on chain or ty note written.
                if (BDayParty.presents[i] == -1 && BDayParty.presentBag.get() < 15) {
                    i = BDayParty.index.getAndIncrement();
                } else {
                    break;
                }
            }

            switch (task) {
                case 0:
                    // add / put pres on chain
                    if (BDayParty.presentBag.get() > 15) { // we can just skip add, nothing left to add
                        continue;
                    }
                    result = Node.add(BDayParty.leftSentinel, BDayParty.presents[i]);
                    if (result) { // add worked! no longer in bag
                        BDayParty.presents[i] = -1;
                        System.out.printf("Add worked!\n");
                        BDayParty.presentBag.incrementAndGet(); // we just took a present out the bag
                    } else {
                        // System.out.printf("Something went wrong in add.\n");
                    }
                    break;
                case 1:
                    // delete / thanks
                    result = Node.delete(BDayParty.leftSentinel, BDayParty.presents[i]);
                    if (result) { // delete worked! We just wrote a thank you.
                        BDayParty.thanks.getAndIncrement();
                    } else {
                        // System.out.printf("Something went wrong in delete.\n");
                    }
                    break;
                case 2:
                    // contains
                    result = Node.contains(BDayParty.leftSentinel, BDayParty.presents[i]);
                    BDayParty.check.getAndIncrement();
                    if (result) { // it was in there
                        BDayParty.checkY.get();
                    } else { // not in there
                        BDayParty.checkN.get();
                    }
                    break;
                default:
                    System.out.printf("How?\n");
            }
        }

        return;
    }

}
