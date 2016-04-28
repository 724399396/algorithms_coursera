import edu.princeton.cs.algs4.TST;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoggleSolver {
    private TST<Integer> dictionary;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dictionary = new TST<>();
        for (String s : dictionary) {
            this.dictionary.put(s, 0);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> res = new HashSet<>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                res.addAll(findString(i, j, "", new HashSet<Location>(), board));
            }
        }
        return res;
    }

    private List<String> findString(int i, int j, String prefix, Set<Location> prefixLocation, BoggleBoard boggleBoard) {
        List<String> res = new ArrayList<>();
        char c = boggleBoard.getLetter(i, j);
        String newPrefix;
        if (c == 'Q') {
            newPrefix = prefix + "QU";
        } else {
            newPrefix = prefix + c;
        }

        if (prefixLocation.contains(new Location(i, j)))
            return res;
        Set<Location> newPrefixLocation = new HashSet<>(prefixLocation);
        newPrefixLocation.add(new Location(i, j));

        if (dictionary.contains(newPrefix)) {
            if (newPrefix.length() >= 3)
              res.add(newPrefix);
        }

        if (dictionary.keysWithPrefix(newPrefix).iterator().hasNext()) {
            List<Location> neighbors = neighbor(new Location(i, j), boggleBoard);
            for (Location location : neighbors) {
                res.addAll(findString(location.getX(), location.getY(), newPrefix, newPrefixLocation, boggleBoard));
            }
        }
        return res;
    }

    private List<Location> neighbor(Location loc, BoggleBoard boggleBoard) {
        int x = loc.getX();
        int y = loc.getY();
        List<Location> res = new ArrayList<>();
        if (x < boggleBoard.rows()-1) {
            res.add(new Location(x+1, y));
            if (y < boggleBoard.cols() - 1) {
                res.add(new Location(x+1, y+1));
            }
            if (y > 0) {
                res.add(new Location(x+1, y-1));
            }
        }
        if (x > 0) {
            res.add(new Location(x-1, y));
            if (y > 0) {
                res.add(new Location(x-1, y-1));
            }
            if (y < boggleBoard.cols() - 1) {
                res.add(new Location(x-1, y+1));
            }
        }
        if (y > 0) {
            res.add(new Location(x, y-1));
        }
        if (y < boggleBoard.cols() - 1) {
            res.add(new Location(x, y+1));
        }
        return res;
    }

    private static class Location {
        private int x;
        private int y;

        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public String toString() {
            return "Location{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (this == o) return true;
            if (!(o.getClass().equals(Location.class))) return false;

            Location location = (Location) o;

            if (x != location.x) return false;
            return y == location.y;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        boolean contain = dictionary.contains(word);
        if (contain) {
            switch (word.length()) {
                case 0:
                case 1:
                case 2:
                    return 0;
                case 3:
                case 4:
                    return 1;
                case 5:
                    return 2;
                case 6:
                    return 3;
                case 7:
                    return 5;
                case 8:
                default:
                    return 11;

            }
        } else {
            return 0;
        }
    }
}
