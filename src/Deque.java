import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by weili on 16-3-24.
 */
public class Deque<Item> implements Iterable<Item> {
    private Node<Item> head;
    private Node<Item> last;
    private int size;

    public Deque() {

    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        checkArg(item);
        Node oldHead = head;
        head = new Node<Item>();
        head.item = item;
        head.next = oldHead;
        if (oldHead == null) {
            last = head;
        } else {
            oldHead.prev = head;
        }
        size++;
    }

    public void addLast(Item item) {
        checkArg(item);
        Node oldLast = last;
        last = new Node<Item>();
        last.item = item;
        last.prev = oldLast;
        if (oldLast == null) {
            head = last;
        } else {
            oldLast.next = last;
        }
        size++;
    }

    private void checkArg(Item item) {
        if (item == null)
            throw new NullPointerException();
    }

    private void checkEmpty() {
        if (size() == 0)
            throw new NoSuchElementException();
    }

    public Item removeFirst() {
        checkEmpty();
        Item item = head.item;
        head = head.next;
        if (head == null) {
            last = null;
        } else {
            head.prev = null;
        }
        size--;
        return item;
    }

    public Item removeLast() {
        checkEmpty();
        Item item = last.item;
        last = last.prev;
        if (last == null) {
            head = null;
        } else {
            last.next = null;
        }
        size--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Node<Item> current = head;
            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Item next() {
                if (current == null)
                    throw new NoSuchElementException();
                Item item = current.item;
                current = current.next;
                return item;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(10);
        deque.addLast(2);
        deque.addLast(3);
        System.out.println("first iterator");
        for (Integer integer : deque) {
            System.out.println(integer);
        }
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeLast());
        System.out.println("second iterator");
        for (Integer integer : deque) {
            System.out.println(integer);
        }
    }
}

class Node<Item> {
    Item item;
    Node<Item> prev;
    Node<Item> next;

    public Node() {
    }

    public Node(Item item, Node prev, Node next) {
        this.item = item;
        this.prev = prev;
        this.next = next;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}


