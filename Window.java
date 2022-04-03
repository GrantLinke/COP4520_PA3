public class Window {
    public Node pred;
    public Node curr;

    public Window(Node pred, Node curr) {
        this.pred = pred;
        this.curr = curr;
    }

    public Window find(Node head, int key) {
        Node pred = null, curr = null, succ = null;
        boolean[] marked = { false };
        boolean snip;

        retry: while (true) {
            pred = head;
            curr = pred.next.getReference();
            while (true) {
                succ = curr.next.get(marked);

                while (marked[0]) {
                    snip = pred.next.compareAndSet(curr, succ, false, false);
                    if (!snip) {
                        continue retry;
                    }
                    curr = succ;
                    succ = curr.next.get(marked);
                }

                if (curr.key >= key) { // we have a hit
                    return new Window(pred, curr);
                }

                pred = curr; // keep traversing
                curr = succ;
            }
        }
    }
}
