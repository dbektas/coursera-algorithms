import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node<T> {

        private T element;
        private Node<T> pred;
        private Node<T> next;

        public Node(T element) {
            this.element = element;
        }

        public T getElement() {
            return element;
        }

        @Override
        public String toString() {
            return element.toString();
        }

    }

    private Node<Item> first;
    private Node<Item> last;
    private int size;

    // construct an empty deque
    public Deque() {
        first = last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    private void checkElementIntegrity(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
    }

    // insert the item at the front
    public void addFirst(Item item) {
        checkElementIntegrity(item);

        Node<Item> node = new Node<Item>(item);

        if (size == 0) {
            first = last = node;
        }

        else {
            Node<Item> old = first;
            first = node;
            first.pred = old;
            old.next = first;
        }

        size++;
    }

    // insert the item at the end
    public void addLast(Item item) {
        checkElementIntegrity(item);

        Node<Item> node = new Node<Item>(item);

        if (size == 0) {
            first = last = node;
        }

        else {
            Node<Item> old = last;
            last = node;
            last.next = old;
            old.pred = last;
        }

        size++;
    }

    // delete and return the item at the front
    public Item removeFirst() {

        if (size == 0) {
            throw new NoSuchElementException();
        }

        Node<Item> removed = first;
        Node<Item> node = first.pred;
        first = node;

        if (first != null) {
            first.next = null;
        }

        if (size == 1) {
            first = last = null;
        }

        size--;

        return removed.getElement();
    }

    // delete and return the item at the end
    public Item removeLast() {

        if (size == 0) {
            throw new NoSuchElementException();
        }

        Node<Item> removed = last;
        Node<Item> node = last.next;
        last = node;

        if (last != null) {
            last.pred = null;
        }

        if (size == 1) {
            first = last = null;
        }

        size--;

        return removed.getElement();

    }

    // return an iterator over items in order from front to end
    public java.util.Iterator<Item> iterator() {
        return new Iterator<Item>() {

            private Node<Item> node = first;

            @Override
            public boolean hasNext() {
                return node != null;
            }

            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Node<Item> toReturn = node;
                node = node.pred;
                return toReturn.getElement();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }
}