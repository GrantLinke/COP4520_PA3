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

    public PresChain contains(PresChain head, int key) {
        PresChain curr = head;

        while (curr.val < key) {
            curr = curr.next;
        }
        if (curr.val == key && curr.marked.getPlain() == false) {
            return curr;
        }
        return null;
    }
}
