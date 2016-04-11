import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;


public class WordNet {
    private Digraph digraph;
    private ST<Integer, Bag<String>> id2Wrods;
    private ST<String, Integer> word2ID;
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
            Integer id = Integer.parseInt(splits[0]);
            String[] words = splits[1].split(" ");
            Bag<String> wordsList = new Bag<>();
            for (String word : words) {
                wordsList.add(word);
                nouns.add(word);
                word2ID.put(word, id);
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
        return sap.length(word2ID.get(nounA), word2ID.get(nounB));
    }

    public String sap(String nounA, String nounB) {
        checkArgNull(nounA, nounB);
        return id2Wrods.get(sap.ancestor(word2ID.get(nounA), word2ID.get(nounB))).toString();
    }

    private void checkArgNull(Object... input) {
        for (Object o : input) {
            if (o == null)
                throw new NullPointerException();
        }
    }

    public static void main(String[] args) {

    }
}
