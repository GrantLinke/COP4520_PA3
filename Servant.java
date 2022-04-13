import java.util.Random;

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

            switch (rand.nextInt(2)) { // servants have specific tasks assigned to them
                case 0: // add then delete
                    if (BDayParty.addIndex.get() == -1) {
                        result = delete(BDayParty.delPresents[BDayParty.delIndex.get()]);
                        break;
                    }

                    i = BDayParty.addIndex.getAndIncrement(); // grab our add index
                    result = Node.add(head, BDayParty.presents[i]); // add that boi

                    if (result) {
                        BDayParty.presentBag.getAndIncrement(); // juss added yknow
                        result = delete(BDayParty.presents[i]); // time to sleep with the fishes
                    }
                    break;

                case 1: // contains
                    i = rand.nextInt(BDayParty.numPres);
                    result = Node.contains(head, i);
                    BDayParty.check.getAndIncrement();

                    if (result) {
                        BDayParty.checkY.getAndIncrement();

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
