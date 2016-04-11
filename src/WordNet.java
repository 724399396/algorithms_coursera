import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordNet {
    private Digraph digraph;
    private Map<Integer, List<String>> id2Wrods;
    private Map<String, Integer> word2ID;
    private Set<String> nouns;
    private SAP sap;

    public WordNet(String synsets, String hypernyms) {
        checkArgNull(synsets, hypernyms);
        In in = new In(synsets);
        id2Wrods = new HashMap<>();
        nouns = new HashSet<>();
        word2ID = new HashMap<>();
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] splits = line.split(",");
            Integer id = Integer.parseInt(splits[0]);
            String[] words = splits[1].split(" ");
            List<String> wordsList = new LinkedList<>();
            for (String word : words) {
                wordsList.add(word);
                nouns.add(word);
                word2ID.put(word, id);
            }
            String gloss = splits[2];
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
