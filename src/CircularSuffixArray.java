import edu.princeton.cs.algs4.TST;

import java.util.Arrays;

public class CircularSuffixArray {
    private int length;
    private int[] index;
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new NullPointerException();
        this.length = s.length();
        TST<Integer> sorted = new TST<>();
        String[] substrings = new String[length];
        for (int i = 0; i < length; i++)
            substrings[i] = s.substring(i, length) + s.substring(0, i);
        String[] sortedHelp = Arrays.copyOf(substrings, length);
        Arrays.sort(sortedHelp);
        for (int i = 0; i < length; i++) {
            sorted.put(substrings[i], i);
        }
        index = new int[length];
        for (int i = 0; i < length; i++)
            index[i] = sorted.get(sortedHelp[i]);
    }

    public int length() {
        return length;
    }

    public int index(int i) {
        if (i < 0 || i > length()-1)
            throw new IndexOutOfBoundsException();
        return index[i];
    }

    public static void main(String[] args) {
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < 12; i++)
            System.out.println(circularSuffixArray.index(i));
    }
}
