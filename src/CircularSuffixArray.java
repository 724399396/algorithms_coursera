import java.util.Arrays;

public class CircularSuffixArray {
    private int length;
    private int[] index;

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
    }

    public CircularSuffixArray(String s) {
        if (s == null)
            throw new NullPointerException();
        this.length = s.length();
        StringNode[] substrings = new StringNode[length];
        for (int i = 0; i < length; i++)
            substrings[i] = new StringNode(s, i);
        Arrays.sort(substrings);
        index = new int[length];
        for (int i = 0; i < length; i++)
            index[i] = substrings[i].getK();
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
