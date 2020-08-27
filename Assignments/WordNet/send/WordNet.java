/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class WordNet {

    // constructor takes the name of the two input file
    private SAP sap;
    private HashMap<Integer, String> syn;
    private LinkedHashSet<String> lst;
    private int size;

    private void readSyn(String synsets) {
        String line;
        In br = new In(synsets);
        while ((line = br.readLine()) != null) {
            String[] str = line.split(",");
            lst.addAll(Arrays.asList(str[1].split(" ")));
            syn.put(Integer.parseInt(str[0]), str[1]);
            size++;
        }
    }

    private HashMap<Integer, HashSet<Integer>> readHyp(String hypernyms) {
        String line;
        In br = new In(hypernyms);
        HashMap<Integer, HashSet<Integer>> res
                = new HashMap<Integer, HashSet<Integer>>();
        while ((line = br.readLine()) != null) {
            String[] str = line.split(",");
            HashSet<Integer> tmp = new HashSet<Integer>();
            for (int i = 0; i < str.length; i++) tmp.add(Integer.parseInt(str[i]));
            res.put(Integer.parseInt(str[0]), tmp);
        }
        return res;
    }

    private static boolean isDAG(Digraph G) {
        if (G.V() == 0) return true;
        boolean[] marked = new boolean[G.V()];
        Stack<Integer> s = new Stack<Integer>();
        s.push(0);
        while (!s.isEmpty()) {
            int x = s.pop();
            if (marked[x]) return false;
            for (int y : G.adj(x)) {
                if (marked[y]) return false;
                s.push(y);
            }
            marked[x] = true;
        }
        return true;
    }

    public WordNet(String synsets, String hypernyms) {
        if (hypernyms == null || synsets == null)
            throw new IllegalArgumentException("null file name");
        size = 0;
        lst = new LinkedHashSet<String>();
        syn = new HashMap<Integer, String>();
        readSyn(synsets);
        HashMap<Integer, HashSet<Integer>> hyp = readHyp(hypernyms);
        Digraph g = new Digraph(size);
        for (int v = 0; v < size; v++)
            for (int w : hyp.get(v))
                if (v != w) g.addEdge(v, w);
        if (isDAG(g)) this.sap = new SAP(g);
        else throw new IllegalArgumentException("null file name");


    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return lst;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return lst.contains(word);
    }

    private HashSet<Integer> nounSet(String s) {
        HashSet<Integer> res = new HashSet<Integer>();

        for (int x = 0; x < size; x++)
            for (String z : syn.get(x).split(" "))
                if (z.compareTo(s) == 0) res.add(x);
        return res;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA))
            throw new IllegalArgumentException(nounA + " does not belong to Nouns");
        if (!isNoun(nounB))
            throw new IllegalArgumentException(nounB + " does not belong to Nouns");
        HashSet<Integer> A = nounSet(nounA);
        HashSet<Integer> B = nounSet(nounB);
        return sap.length(A, B);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        HashSet<Integer> A = nounSet(nounA);
        HashSet<Integer> B = nounSet(nounB);
        System.out.println(A);
        return syn.get(sap.ancestor(A, B));
    }

    // do unit testing of this class

    public static void main(String[] args) {
        WordNet w = new WordNet(args[0], args[1]);
        //System.out.println(w.distance("table", "zebra"));
        System.out.println(w.sap("bird", "worm"));

    }
}
