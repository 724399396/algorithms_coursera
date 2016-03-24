import edu.princeton.cs.algs4.StdIn;

/**
 * Created by weili on 16-3-24.
 */
public class Subset {
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        for (int i = 0; i < N; i++) {
            randomizedQueue.enqueue(StdIn.readString());
        }
        for (String s : randomizedQueue) {
            System.out.println(s);
        }
    }
}
