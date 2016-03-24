import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by weili on 16-3-24.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int loc;

    public RandomizedQueue() {
        items = (Item[]) new Object[1];
    }
    public boolean isEmpty() {
        return size() == 0;
    }
    public int size() {
        return loc;
    }
    public void enqueue(Item item) {
        checkArg(item);
        if (loc >= items.length) {
            resize(items.length * 2);
        }
        items[loc++] = item;
    }

    private void resize(int newsize) {
        Item[] newItems = (Item[])new Object[newsize];
        for(int i = 0; i < loc; i++) {
            newItems[i] = items[i];
        }
        items = newItems;
    }

    private void swap(int i, int j) {
        Item tmp = items[i];
        items[i] = items[j];
        items[j] = tmp;
    }

    public Item dequeue() {
        checkEmpty();
        int find = StdRandom.uniform(loc);
        Item item = items[find];
        swap(find,loc-1);
        items[--loc] = null;
        if (loc <= items.length / 3 && items.length >= 2) {
            resize(items.length / 2);
        }
        return item;
    }
    public Item sample() {
        checkEmpty();
        int find = StdRandom.uniform(loc);
        Item item = items[find];
        return item;
    }

    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Item[] copy = Arrays.copyOf(items,loc);
            {
                for (int i = 1; i < copy.length; i++) {
                    swap(i, StdRandom.uniform(i));
                }
            }
            private int i = 0;
            @Override
            public boolean hasNext() {
                return i < copy.length;
            }

            @Override
            public Item next() {
                if (i >= copy.length)
                    throw new NoSuchElementException();
                return copy[i++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private void checkArg(Item item) {
        if (item == null)
            throw new NullPointerException();
    }

    private void checkEmpty() {
        if (size() == 0)
            throw new NoSuchElementException();
    }


    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue(3);
        randomizedQueue.enqueue(4);
        randomizedQueue.enqueue(1);
        randomizedQueue.enqueue(10);
        System.out.println("first iterator");
        for (Integer integer : randomizedQueue) {
            System.out.println(integer);
        }
        System.out.println("second iterator");
        for (Integer integer : randomizedQueue) {
            System.out.println(integer);
        }
        System.out.println("sample");
        System.out.println(randomizedQueue.sample());
        System.out.println(randomizedQueue.sample());
        System.out.println("deque");
        System.out.println(randomizedQueue.dequeue());
        System.out.println(randomizedQueue.dequeue());
        System.out.println("third iterator");
        for (Integer integer : randomizedQueue) {
            System.out.println(integer);
        }
        System.out.println("fourth iterator");
        for (Integer integer : randomizedQueue) {
            System.out.println(integer);
        }
    }
}
