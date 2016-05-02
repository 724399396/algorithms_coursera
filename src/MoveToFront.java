import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    public static void encode() {
        int[] table = asciiTableBuild();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar(8);
            BinaryStdOut.write(table[c], 8);
            for (int i = 0; i < R; i++)
                if (table[i] < table[c])
                    table[i] += 1;
            table[c] = 0;
        }
        BinaryStdOut.flush();
    }

    private static int[] asciiTableBuild() {
        int[] table = new int[R];
        for (int i = 0; i < 256; i++) {
            table[i] = i;
        }
        return table;
    }

    public static void decode() {
        int[] table = asciiTableBuild();
        while (!BinaryStdIn.isEmpty()) {
            int loc = BinaryStdIn.readChar(8);
            char cur = (char) table[loc];
            BinaryStdOut.write(cur, 8);
            for (int i = loc; i > 0; i--) {
                table[i] = table[i-1];
            }
            table[0] = cur;
        }
        BinaryStdOut.flush();
    }

    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        if (args[0].equals("+"))
            decode();
    }
}
