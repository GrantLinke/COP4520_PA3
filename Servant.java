import java.util.Random;

public class Servant implements Runnable {
    @Override
    public void run() {
        Random rand = new Random();

        while (BDayParty.presentBag.getPlain() < 500000) // still got some thanks to write.
        {

        }

    }

}
