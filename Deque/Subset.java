import java.util.Iterator;
import java.util.NoSuchElementException;

public class Subset {

    public static void main(String[] args) {

        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<String>();

        int k = Integer.parseInt(args[0]);
        if (k < 0) {
            throw new IllegalArgumentException();
        }

        for (;;) {
            try {
                String string = StdIn.readString();
                randomizedQueue.enqueue(string);
            } catch (NoSuchElementException e) {
                break;
            }
        }

        if (k > randomizedQueue.size()) {
            throw new IllegalArgumentException();
        }

        Iterator<String> iterator = randomizedQueue.iterator();

        for (int i = 0; i < k; i++) {
            String element = iterator.next();
            System.out.println(element);
        }

    }

}