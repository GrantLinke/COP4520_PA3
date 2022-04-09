import java.util.Random;

public class Servant implements Runnable {
    @Override
    public void run() {
        Random rand = new Random();
        boolean result = false;
        Node head = BDayParty.leftSentinel;
        int i = 0;

        while (BDayParty.thanks.get() < BDayParty.numPres) // still got some thanks to write.
        {
            if (BDayParty.addIndex.get() >= BDayParty.numPres) {
                BDayParty.addIndex.set(-1);
            }
            if (BDayParty.delIndex.get() >= BDayParty.numPres) {
                BDayParty.delIndex.set(0);
                System.out.printf("Thank yous written: %d\n", BDayParty.thanks.get());
            }

            switch ((int) Thread.currentThread().getId() % 4) { // servants have specific tasks assigned to them
                case 0: // add
                case 1:
                    if (BDayParty.addIndex.get() == -1) {
                        continue;
                    }
                    i = BDayParty.addIndex.getAndIncrement(); // grab our add index
                    result = Node.add(head, BDayParty.presents[i]);
                    if (result) {
                        // letting del servant know that we added a present
                        BDayParty.delPresents[BDayParty.comms.getAndIncrement()] = BDayParty.presents[i];
                        BDayParty.presents[i] = -1;
                        BDayParty.presentBag.getAndIncrement(); // just removed a present from bag.

                    }
                    break;
                case 2: // del
                    if (BDayParty.delPresents[0] == -1) { // I do this because delete has to be told when to start
                        break;
                    }

                    result = Node.delete(head, BDayParty.delPresents[BDayParty.delIndex.get()]);
                    if (result) { // wrote a thank you
                        BDayParty.thanks.getAndIncrement();
                        BDayParty.delIndex.getAndIncrement();
                    } else {
                        BDayParty.delIndex.getAndIncrement(); // in case we get stuck at something c3 deleted.
                    }
                    break;
                case 3: // contains
                    i = rand.nextInt(BDayParty.numPres);
                    result = Node.contains(BDayParty.leftSentinel, i);
                    BDayParty.check.getAndIncrement();

                    if (result) { // making contains useful, delete on find
                        BDayParty.checkY.getAndIncrement();
                        result = Node.delete(head, BDayParty.presents[i]);

                        if (result) {
                            BDayParty.thanks.getAndIncrement();
                        }

                    } else {
                        BDayParty.checkN.getAndIncrement();
                    }
                    break;
            }
        }
        return;
    }

}
