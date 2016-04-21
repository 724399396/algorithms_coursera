import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by weili on 16-4-21.
 */
public class BaseballElimination {
    private int teamNumbers;
    private ST<String, Integer> name2Index;
    private ST<Integer, String> index2Name;
    private int[] w;
    private int[] l;
    private int[] r;
    private int[][] g;
    private FordFulkerson maxFlow;
    private ST<Integer, String> ref;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        teamNumbers = in.readInt();
        name2Index = new ST<>();
        index2Name = new ST<>();
        w = new int[teamNumbers];
        l = new int[teamNumbers];
        r = new int[teamNumbers];
        g = new int[teamNumbers][teamNumbers];
        for (int i = 0; i < teamNumbers; i++) {
            String name = in.readString();
            name2Index.put(name, i);
            index2Name.put(i, name);
            w[i] = Integer.parseInt(in.readString());
            l[i] = Integer.parseInt(in.readString());
            r[i] = Integer.parseInt(in.readString());
            for (int j = 0; j < teamNumbers; j++) {
                g[i][j] = Integer.parseInt(in.readString());
            }
        }
    }

    public int numberOfTeams() {
        return teamNumbers;
    }

    public Iterable<String> teams() {
        return name2Index.keys();
    }

    public int wins(String team) {
        checkTeam(team);
        return w[name2Index.get(team)];
    }

    public int losses(String team) {
        checkTeam(team);
        return l[name2Index.get(team)];
    }

    public int remaining(String team) {
        checkTeam(team);
        return r[name2Index.get(team)];
    }

    public int against(String team1, String team2) {
        checkTeam(team1);
        checkTeam(team2);
        return g[name2Index.get(team1)][name2Index.get(team2)];
    }

    public boolean isEliminated(String team) {
        checkTeam(team);
        for (String other : teams()) {
            if (!other.equals(team)) {
                if (wins(other) > wins(team) + remaining(team)) {
                    return true;
                }
            }
        }
        int compareTeamIndex = name2Index.get(team);
        int composeNumbers = (numberOfTeams() - 1) * (numberOfTeams() - 2) / 2;
        int vertices = 2 + composeNumbers + numberOfTeams() - 1;
        FlowNetwork flowNetWord = new FlowNetwork(vertices);
        int s = 0;
        int t = vertices - 1;
        int i = 1;
        ST<Integer, Double> acc = new ST<>();
        ref = new ST<>();
        for (String s1 : teams()) {
            if (s1.equals(team))
                continue;
            for (String s2 : teams()) {
                if (s2.equals(team))
                    continue;
                int s2Index = name2Index.get(s2);
                int s1Index = name2Index.get(s1);
                if (s2Index <= s1Index)
                    continue;
                flowNetWord.addEdge(new FlowEdge(s, i, against(s1, s2)));
                if (s2Index > compareTeamIndex)
                    s2Index = s2Index + composeNumbers;
                else
                    s2Index = s2Index + 1 + composeNumbers;
                if (s1Index > compareTeamIndex)
                    s1Index =  s1Index + composeNumbers;
                else
                    s1Index = s1Index + 1 + composeNumbers;
                flowNetWord.addEdge(new FlowEdge(i, s1Index, Double.POSITIVE_INFINITY));
                flowNetWord.addEdge(new FlowEdge(i, s2Index, Double.POSITIVE_INFINITY));
                Double oldS1 = acc.get(s1Index);
                Double oldS2 = acc.get(s2Index);
                if (oldS1 == null)
                    oldS1 = (double) wins(team) + remaining(team) - wins(s1);
                if (oldS2 == null)
                    oldS2 = (double) wins(team) + remaining(team) - wins(s2);
                acc.put(s1Index, oldS1);
                acc.put(s2Index, oldS2);
                ref.put(s1Index, s1);
                ref.put(s2Index, s2);
                i++;
            }
        }
        for (int j = 0; j < numberOfTeams() - 1; j++) {
            int v = t-1-j;
            flowNetWord.addEdge(new FlowEdge(v, t, acc.get(v)));
        }
        maxFlow = new FordFulkerson(flowNetWord, s, t);
        for (int j = 0; j < numberOfTeams() - 1; j++) {
            if (maxFlow.inCut(t - j - 1)) {
                return true;
            }
        }
        return false;
    }

    public Iterable<String> certificateOfElimination(String team) {
        checkTeam(team);
        Bag<String> res = new Bag<>();
        for (String other : teams()) {
            if (!other.equals(team)) {
                if (wins(other) > wins(team) + remaining(team)) {
                    res.add(other);
                }
            }
        }
        if (!res.isEmpty()) {
            return res;
        }
        int composeNumbers = (numberOfTeams() - 1) * (numberOfTeams() - 2) / 2;
        int vertices = 2 + composeNumbers + numberOfTeams() - 1;
        int t = vertices - 1;
        for (int j = 0; j < numberOfTeams() - 1; j++) {
            if (maxFlow.inCut(t - j - 1)) {
                res.add(ref.get(t - j - 1));
            }
        }
        if (res.isEmpty()) {
            return null;
        }
        return res;
    }

    private void checkTeam(String team) {
        if (!name2Index.contains(team)) {
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
