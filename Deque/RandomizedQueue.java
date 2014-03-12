import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private class Node<T> {

        private T element;
        private Node<T> next;
        private Node<T> prev;

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
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
    }

    // is the queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {

        if (item == null) {
            throw new NullPointerException();
        }

        Node<Item> toInsert = new Node<Item>(item);

        if (size == 0) {
            first = toInsert;
        }

        else {
            Node<Item> old = first;
            first = toInsert;
            first.prev = old;
            first.prev.next = first;
        }

        size++;
    }

    // delete and return a random item
    public Item dequeue() {

        if (size == 0) {
            throw new NoSuchElementException();
        }

        int randomIndex = randomIndex();

        Node<Item> toDelete = first;
        for (int i = 1; i < randomIndex; i++) {
            toDelete = toDelete.prev;
        }

        Node<Item> node = toDelete;
        if (toDelete == first) {
            toDelete.prev = first;
            toDelete = null;
        } else if (toDelete.prev == null) {
            toDelete = null;
        } else {
            toDelete.prev.next = toDelete.next;
            toDelete.next.prev = toDelete.prev;
        }

        if (size == 1) {
            first = null;
        }

        size--;

        return node.getElement();

    }

    // return (but do not delete) a random item
    public Item sample() {

        int randomIndex = randomIndex();

        Node<Item> node = first;
        for (int i = 1; i < randomIndex; i++) {
            node = node.prev;
        }

        return node.getElement();
    }

    private int randomIndex() {
        return 1 + StdRandom.uniform(size);
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {

            private boolean[] alreadySelected = new boolean[size];
            private int countOfIterations = 0;

            @Override
            public boolean hasNext() {
                return countOfIterations < size;
            }

            @Override
            public Item next() {

                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                Node<Item> node = first;

                int randomIndex;
                do {
                    randomIndex = randomIndex() - 1;
                } while (alreadySelected[randomIndex] == true);

                alreadySelected[randomIndex] = true;
                countOfIterations++;

                for (int i = 0; i < randomIndex; i++) {
                    node = node.prev;
                }

                return node.getElement();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            private int randomIndex() {
                return 1 + StdRandom.uniform(size);
            }
        };
    }
}