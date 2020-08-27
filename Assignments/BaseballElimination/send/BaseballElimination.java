/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class BaseballElimination {
    private int n;
    private TreeMap<String, Integer> name_to_id;
    private ArrayList<Team> teams;
    private HashMap<Integer, HashSet<Integer>> Eliminated;

    private class Team {
        String name;
        int wins;
        int losses;
        int r;
        int[] games;

        public Team() {
            games = new int[n];
        }
    }

    private void readFile(String filename) {
        In in = new In(filename);
        n = in.readInt();
        name_to_id = new TreeMap<String, Integer>();
        teams = new ArrayList<Team>();
        int id = 0;
        for (int i = 0; i < n; i++) {
            Team t = new Team();
            t.name = in.readString();
            name_to_id.put(t.name, id++);
            t.wins = in.readInt();
            t.losses = in.readInt();
            t.r = in.readInt();
            ArrayList<Integer> games = new ArrayList<Integer>();
            for (int j = 0; j < n; j++)
                t.games[j] = in.readInt();
            teams.add(t);
        }
        Eliminated = new HashMap<Integer, HashSet<Integer>>();
    }

    private FlowNetwork make_network(int x) {
        int v = 1, k = (n - 1) * (n - 2) / 2, size = k + n + 1;
        FlowNetwork res = new FlowNetwork(size);
        int max = teams.get(x).wins + teams.get(x).r;
        for (int i = 0; i < n; i++) {
            if (i == x) continue;
            res.addEdge(new FlowEdge(k + i + 1, size - 1, max - teams.get(i).wins));
            for (int j = i + 1; j < n - 1; j++) {
                if (j != x) {
                    res.addEdge(new FlowEdge(0, v, teams.get(i).games[j]));
                    res.addEdge(new FlowEdge(v, k + i + 1, Double.POSITIVE_INFINITY));
                    res.addEdge(new FlowEdge(v, k + j + 1, Double.POSITIVE_INFINITY));
                    v++;
                }
            }
        }
        return res;

    }

    private boolean trivialElimination(int x) {
        int max = teams.get(x).wins + teams.get(x).r;
        for (int i = 0; i < n; i++)
            if (teams.get(i).wins > max) {
                HashSet<Integer> R = new HashSet<Integer>();
                R.add(i);
                Eliminated.put(x, R);
                return true;
            }
        return false;
    }

    private void nonTrivialElimination(FlowNetwork G, int x) {
        int k = (n - 1) * (n - 2) / 2;
        FordFulkerson F = new FordFulkerson(G, 0, G.V() - 1);
        boolean eliminated = false;
        int flow = 0;
        for (FlowEdge e : G.adj(0)) {
            if (e.residualCapacityTo(e.other(0)) != 0) {
                eliminated = true;
                break;
            }
        }
        if (eliminated) {
            HashSet<Integer> R = new HashSet<Integer>();
            for (int v = k + 1; v < G.V(); v++) {
                if (F.inCut(v))
                    R.add(v - k - 1);
            }
            Eliminated.put(x, R);
        }
    }


    public BaseballElimination(String filename) {
        readFile(filename);
        for (int x = 0; x < n; x++) {
            if (!trivialElimination(x)) {
                FlowNetwork f = make_network(x);
                nonTrivialElimination(f, x);
            }
        }
    }

    // create a baseball division from given filename in format specified below
    public int numberOfTeams() {
        return n;
    }               // number of teams

    public Iterable<String> teams() {
        return name_to_id.keySet();
    }                // all teams

    public int wins(String team) {
        if (team == null) throw new IllegalArgumentException();
        return teams.get(name_to_id.get(team)).wins;
    }        // number of wins for given team

    public int losses(String team) {
        if (team == null) throw new IllegalArgumentException();
        return teams.get(name_to_id.get(team)).losses;
    }             // number of losses for given team

    public int remaining(String team) {
        if (team == null) throw new IllegalArgumentException();
        return teams.get(name_to_id.get(team)).r;
    }              // number of remaining games for given team

    public int against(String team1,
                       String team2) {
        if (team1 == null || team2 == null) throw new IllegalArgumentException();
        return teams.get(name_to_id.get(team1)).games[name_to_id.get(team2)];
    }// number of remaining games between team1 and team2

    public boolean isEliminated(String team) {
        if (team == null) throw new IllegalArgumentException();
        return Eliminated.containsKey(name_to_id.get(team));
    }            // is given team eliminated?

    public Iterable<String> certificateOfElimination(String team) {
        if (team == null) throw new IllegalArgumentException();
        if (isEliminated(team)) {
            ArrayList<String> res = new ArrayList<String>();
            for (int x : Eliminated.get(name_to_id.get(team))) {
                res.add(teams.get(x).name);
            }
            return res;
        }
        else return null;
    }// subset R of teams that eliminates given team; null if not eliminated

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
