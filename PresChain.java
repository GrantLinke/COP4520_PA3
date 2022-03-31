import java.util.concurrent.atomic.AtomicBoolean;

public class PresChain {
    int val;
    AtomicBoolean marked;
    PresChain next; // when adding, make a new PresChain and set this reference

    public PresChain(int val, PresChain next) {
        this.val = val;
        this.marked.set(false);
        this.next = next;
    }

    public void add() {

    }

    public void delete() {

    }

    public int contains() {
        return val;
    }
}
