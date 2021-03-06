import java.util.concurrent.atomic.AtomicMarkableReference;

// almost all code found here was inspired from the course slides ch.9
public class Node {
    int key;
    AtomicMarkableReference<Node> next;

    public Node(int key) {
        this.key = key;
        this.next = null;
    }

    public static boolean add(Node head, int key) {
        Node pred, curr;

        retry: while (true) {
            Window window = new Window(null, null);
            window = window.find(head, key);

            pred = window.pred;
            curr = window.curr;
            // System.out.printf("pred, curr: (%s, %s)\n", pred, curr);

            if (curr.key == key) { // key already exists (this shouldn't happen, our array has only unique values)
                // System.out.printf("Tried inserting %d, but %d already exists.\n", key,
                // curr.key);
                return false;
            }
            Node newNode = new Node(key);
            newNode.next = new AtomicMarkableReference<Node>(curr, false);
            if (pred.next.compareAndSet(curr, newNode, false, false)) {
                return true;
            } else {
                continue retry;
            }
        }
    }

    public static boolean delete(Node head, int key) {
        boolean snip;

        while (true) {
            Window window = new Window(null, null);
            window = window.find(head, key);

            Node pred = window.pred, curr = window.curr; // if it exists it returns our key

            if (curr.key != key) {
                // System.out.printf("Curr.key: %d\tkey: %d\n", curr.key, key);
                return false;
            }
            // else conceptual
            Node succ = curr.next.getReference();
            snip = curr.next.compareAndSet(succ, succ, false, true);

            if (!snip) {
                continue;
            }

            pred.next.compareAndSet(curr, succ, false, false); // removed!
            return true;
        }

    }

    public static boolean contains(Node head, int key) {
        Node curr = head;
        boolean[] marked = { true };

        while (curr.key < key) {
            curr = curr.next.getReference();
        }

        if (curr.next != null) {
            Node succ = curr.next.get(marked);
        }

        return (curr.key == key && !marked[0]);
    }
}
