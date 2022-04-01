public class Window {
    public PresChain pred;
    public PresChain curr;

    public Window find(PresChain head, int key) {
        Window window = new Window();

        window.pred = head.contains(head, key);
        if (window.pred != null) {
            window.curr = pred.next;
            return window; // we found it
        }

        return null; // we couldn't find it
    }
}
