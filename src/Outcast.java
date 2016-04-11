import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;

    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    public String outcast(String[] nouns) {
        int maxLength = Integer.MIN_VALUE;
        String maxString = null;
        for (String noun1 : nouns) {
            int length = 0;
            for (String noun2 : nouns) {
                length += wordNet.distance(noun1, noun2);
            }
            if (length > maxLength) {
                maxLength = length;
                maxString = noun1;
            }
        }
        return maxString;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
