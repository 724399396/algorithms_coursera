
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.TST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;
import java.util.Arrays;

public class BurrowsWheeler {
    private static class StringNode implements Comparable<StringNode> {
        private String str;
        private int k;

        public StringNode(String str, int k) {
            this.str = str;
            this.k = k;
        }

        @Override
        public int compareTo(StringNode that) {
            int len = str.length();
            for (int i = 0; i < len; i++) {
                char c1 = str.charAt((k + i) % len);
                char c2 = that.str.charAt((that.k + i) % len);
                if (c1 < c2)
                    return -1;
                else if (c1 > c2)
                    return 1;
                else
                    continue;
            }
            return 0;
        }

        public int getK() {
            return k;
        }

        public String getStr() {
            return str;
        }
    }

    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        String s = BinaryStdIn.readString();
        int length = s.length();
        StringNode[] substrings = new StringNode[length];
        for (int i = 0; i < length; i++)
            substrings[i] = new StringNode(s, i);
        int[] index = new int[length];
        Arrays.sort(substrings);
        for (int i = 0; i < length; i++)
            index[i] = substrings[i].getK();
        for (int i = 0; i < length; i++)
            if (index[i] == 0)
                BinaryStdOut.write(i);
        for (int i = 0; i < length; i++) {
            StringNode node = substrings[i];
            String str = node.getStr();
            BinaryStdOut.write(str.charAt((node.getK()+str.length()-1) % str.length()), 8);
        }
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        char[] t = s.toCharArray();
        char[] i = Arrays.copyOf(t, t.length);
        Arrays.sort(i);
        ST<Character, Queue<Integer>> tHelp = new ST<>();
        for (int j = 0; j < i.length; j++) {
            Queue<Integer> old = tHelp.get(t[j]);
            if (old == null) {
                old = new Queue<>();
                tHelp.put(t[j], old);
            }
            old.enqueue(j);
        }
        int[] next = new int[i.length];
        for (int j = 0; j < i.length; j++) {
            next[j] = tHelp.get(i[j]).dequeue();
        }
        int start = next[first];
        for (int j = 0; j < i.length - 1; j++) {
            BinaryStdOut.write(t[start]);
            start = next[start];
        }
        BinaryStdOut.write(t[first]);
        BinaryStdOut.flush();
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
