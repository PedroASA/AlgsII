/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
// Qu unresolved
// backtarck not good

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashSet;

public class BoggleSolver {
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    private Dictionary dictionary;

    public BoggleSolver(String[] dictionary) {
        this.dictionary = new Dictionary();
        for (String s : dictionary)
            if (s.length() >= 3) this.dictionary.add(s);
    }

    private static class Dictionary {
        static final int R = 26;
        static final char fst_char = 'A';

        Node root;

        private static class Node {
            boolean is_end;
            Node[] children = new Node[R];
        }

        private Node put(Node root, String s, int i) {
            if (root == null) root = new Node();
            if (i == s.length())
                root.is_end = true;
            else {
                int c = s.charAt(i) - fst_char;
                root.children[c] = put(root.children[c], s, i + 1);
            }
            return root;
        }

        private Node get(Node root, String s, int i) {
            if (root != null) {
                if (i != s.length())
                    return get(root.children[s.charAt(i) - fst_char], s, i + 1);
            }
            return root;
        }

        private void add(String s) {
            //if (!this.contains(s))
            root = put(root, s, 0);
        }

        private boolean contains(String s) {
            Node t = get(root, s, 0);
            if (t != null) return get(root, s, 0).is_end;
            return false;
        }

        private boolean containsPrefix(String s) {
            return get(root, s, 0) != null;
        }
    }

    private static class Pair {
        int fst;
        int snd;

        Pair(int x, int y) {
            fst = x;
            snd = y;
        }
    }

    private ArrayList<Pair> adj(Pair square, int m, int n) {
        ArrayList<Pair> res = new ArrayList<Pair>();
        for (int i = square.fst - 1; i <= square.fst + 1 && i < m; i++) {
            if (i < 0) continue;
            for (int j = square.snd - 1; j <= square.snd + 1 && j < n; j++) {
                if (j < 0 || (i == square.fst && j == square.snd)) continue;
                res.add(new Pair(i, j));
            }
        }
        return res;
    }

    private void backtrack(Pair square, String prefix, boolean[] marked, HashSet<String> res,
                           BoggleBoard board, int m, int n) {
        if (dictionary.contains(prefix)) res.add(prefix);
        for (Pair neighboor : adj(square, m, n)) {
            if (!marked[neighboor.fst * n + neighboor.snd]) {
                char tmp = board.getLetter(neighboor.fst, neighboor.snd);
                String t;
                if (tmp == 'Q') t = prefix + "QU";
                else t = prefix + tmp;
                if (dictionary.containsPrefix(prefix)) {
                    marked[neighboor.fst * n + neighboor.snd] = true;
                    backtrack(neighboor, t, marked, res, board, m, n);
                    marked[neighboor.fst * n + neighboor.snd] = false;
                }
            }
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> res = new HashSet<>();
        int m = board.rows(), n = board.cols();
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) {
                boolean[] marked = new boolean[m * n];
                marked[i * n + j] = true;
                char tmp = board.getLetter(i, j);
                String s;
                if (tmp == 'Q') s = "QU";
                else s = String.valueOf(tmp);
                backtrack(new Pair(i, j), s, marked, res, board,
                          m, n);
            }
        return res;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dictionary.contains(word)) return 0;
        int size;
        switch (size = word.length()) {
            case 3:
                return 1;
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                if (size < 3) return 0;
                return 11;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0, i = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        /*In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (BoggleSolver.Pair x : solver.adj(new BoggleSolver.Pair(i, j), 4, 4))
                    System.out.print(x.fst + "\t" + x.snd + "\t");
                System.out.println();
            }
        }*/
    }
}
