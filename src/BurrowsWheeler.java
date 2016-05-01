import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.TST;

import java.util.Arrays;

public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        String s = StdIn.readAll();
        int length = s.length();
        TST<Integer> sorted = new TST<>();
        String[] substrings = new String[length];
        for (int i = 0; i < length; i++)
            substrings[i] = s.substring(i, length) + s.substring(0, i);
        String[] sortedHelp = Arrays.copyOf(substrings, length);
        Arrays.sort(sortedHelp);
        for (int i = 0; i < length; i++) {
            sorted.put(substrings[i], i);
        }
        int[] index = new int[length];
        for (int i = 0; i < length; i++)
            index[i] = sorted.get(sortedHelp[i]);
        for (int i = 0; i < length; i++)
            if (index[i] == 0)
                BinaryStdOut.write(i);
        for (int i = 0; i < length; i++)
            BinaryStdOut.write(sortedHelp[i].charAt(length-1), 8);
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {

    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        if (args[0].equals("+"))
            decode();
    }
}
