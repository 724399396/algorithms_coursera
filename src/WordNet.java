import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;

public class WordNet {
    private Digraph digraph;
    private ST<Integer, Bag<String>> id2Wrods;
    private ST<String, Bag<Integer>> word2ID;
    private SET<String> nouns;
    private SAP sap;

    public WordNet(String synsets, String hypernyms) {
        checkArgNull(synsets, hypernyms);
        In in = new In(synsets);
        id2Wrods = new ST<>();
        nouns = new SET<>();
        word2ID = new ST<>();
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] splits = line.split(",");
            int id = Integer.parseInt(splits[0]);
            String[] words = splits[1].split(" ");
            Bag<String> wordsList = new Bag<>();
            for (String word : words) {
                wordsList.add(word);
                nouns.add(word);
                Bag<Integer> ids = word2ID.get(word);
                if (ids == null) {
                    ids = new Bag<>();
                    word2ID.put(word, ids);
                }
                ids.add(id);
            }
            id2Wrods.put(id, wordsList);
        }
        In in2 = new In(hypernyms);
        String[] lines = in2.readAllLines();
        digraph = new Digraph(id2Wrods.size());
        for (String line : lines) {
            String[] ids = line.split(",");
            for (int i = 1; i < ids.length; i++) {
                digraph.addEdge(Integer.parseInt(ids[0]), Integer.parseInt(ids[i]));
            }
        }
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException();
        }
        sap = new SAP(digraph);
    }

    public Iterable<String> nouns() {
        return nouns;
    }

    public boolean isNoun(String word) {
        checkArgNull(word);
        return nouns.contains(word);
    }

    public int distance(String nounA, String nounB) {
        checkArgNull(nounA, nounB);
        Iterable<Integer> nounAIds = word2ID.get(nounA);
        Iterable<Integer> nounBIds = word2ID.get(nounB);
        if (nounAIds == null || nounBIds == null) {
            throw new IllegalArgumentException();
        }
        return sap.length(nounAIds, nounBIds);
    }

    public String sap(String nounA, String nounB) {
        checkArgNull(nounA, nounB);
        Iterable<Integer> nounAIds = word2ID.get(nounA);
        Iterable<Integer> nounBIds = word2ID.get(nounB);
        if (nounAIds == null || nounBIds == null) {
            throw new IllegalArgumentException();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String word : id2Wrods.get(sap.ancestor(nounAIds, nounBIds))) {
            stringBuilder.insert(0, word + " ");
        }
        String tmp =  stringBuilder.toString();
        return tmp.substring(0, tmp.length() - 1);
    }

    private void checkArgNull(Object... input) {
        for (Object o : input) {
            if (o == null)
                throw new NullPointerException();
        }
    }

    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        System.out.println(wordNet.sap("Alfred_Alistair_Cooke", "tiddler"));
    }
}
