import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Servant implements Runnable {
    Random rand = new Random();
    Node head = BDayParty.leftSentinel;

    @Override
    public void run() {
        boolean result = false;
        int i = 0;

        while (BDayParty.thanks.get() < BDayParty.numPres) // still got some thanks to write.
        {
            if (BDayParty.addIndex.get() >= BDayParty.numPres) {
                BDayParty.addIndex.set(-1);
            }
            if (BDayParty.delIndex.get() >= BDayParty.numPres) {
                BDayParty.delIndex.set(0);
            }
            if (BDayParty.comms.get() >= BDayParty.numPres) {
                BDayParty.comms.set(-1);
            }

            switch ((int) Thread.currentThread().getId() % 4) { // servants have specific tasks assigned to them
                case 0: // add
                case 1:
                    // don't want chain to exceed length of 3000
                    if (BDayParty.addIndex.get() == -1 || BDayParty.presentBag.get() - BDayParty.thanks.get() > 3000) {
                        result = delete(BDayParty.delPresents[BDayParty.delIndex.get()]);
                        break;
                    }

                    i = BDayParty.addIndex.getAndIncrement(); // grab our add index
                    result = Node.add(head, BDayParty.presents[i]);
                    if (result) {
                        // letting del servant know that we added a present
                        BDayParty.delPresents[BDayParty.comms.getAndIncrement()] = BDayParty.presents[i];
                        BDayParty.presents[i] = -1;
                        BDayParty.presentBag.getAndIncrement(); // just removed a present from bag.
                        BDayParty.flag.set(true);
                    }
                    break;
                case 2: // del
                    result = delete(BDayParty.delPresents[BDayParty.delIndex.get()]);

                    break;
                case 3: // contains
                    i = rand.nextInt(BDayParty.numPres);
                    result = Node.contains(BDayParty.leftSentinel, i);
                    BDayParty.check.getAndIncrement();

                    if (result) { // making contains useful, delete on find
                        BDayParty.checkY.getAndIncrement();

                        result = delete(BDayParty.delPresents[BDayParty.delIndex.get()]);

                    } else {
                        BDayParty.checkN.getAndIncrement();
                    }
                    break;
            }
        }
        return;
    }

    public boolean delete(int key) {
        boolean result = false;
        if (BDayParty.flag.get() == false) { // I do this because delete has to be told when to start
            return false;
        }

        result = Node.delete(head, key);
        if (result) { // wrote a thank you
            // System.out.printf("Just deleted node (%d)\n", key);
            BDayParty.thanks.getAndIncrement();
            BDayParty.delIndex.getAndIncrement();
            return true;
        } else {
            BDayParty.delIndex.getAndIncrement(); // in case we get stuck

            return false;
        }
    }

}
