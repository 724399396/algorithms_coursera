import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WordNet {
    private Digraph digraph;
    private ST<Integer, List<String>> map;
    private Set<String> nouns;

    public WordNet(String synsets, String hypernyms) {
        checkArgNull(synsets, hypernyms);
        In in = new In(synsets);
        map = new ST();
        nouns = new HashSet<>();
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] splits = line.split(",");
            Integer id = Integer.parseInt(splits[0]);
            String[] words = splits[1].split(" ");
            List<String> wordsList = new LinkedList<>();
            for (String word : words) {
                wordsList.add(word);
                nouns.add(word);
            }
            String gloss = splits[2];
            map.put(id, wordsList);
        }
        In in2 = new In(hypernyms);
        String[] lines = in2.readAllLines();
        digraph = new Digraph(map.size());
        for (String line : lines) {
            String[] ids = line.split(",");
            for (int i = 1; i < ids.length; i++) {
                digraph.addEdge(Integer.parseInt(ids[0]), Integer.parseInt(ids[i]));
            }
        }
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
        return 0;
    }

    public String sap(String nounA, String nounB) {
        checkArgNull(nounA, nounB);
        return "";
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
