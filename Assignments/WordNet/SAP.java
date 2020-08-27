/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.LinkedHashMap;

public class SAP {

    static private class MyPair {
        private final int fst;
        private final int snd;
        private static final MyPair val = new MyPair(-1, -1);

        MyPair() {
            fst = -1;
            snd = -1;
        }

        MyPair(int x, int y) {
            this.fst = x;
            this.snd = y;
        }

        public boolean isDefault() {
            return this.equals(val);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MyPair)) return false;
            MyPair pair = (MyPair) o;
            return (fst == pair.fst && snd == pair.snd)
                    || (fst == pair.snd && snd == pair.fst);
        }

        @Override
        public int hashCode() {
            int result = fst;
            result = 31 * result + snd;
            return result;
        }
    }

    private Digraph myGraph;
    private LinkedHashMap<MyPair, Integer> size_cache;
    private LinkedHashMap<MyPair, Integer> ancestor_cache;

    // constructor takes a digraph (not necessarily a DAG)

    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("Null Graph");
        myGraph = new Digraph(G);
        size_cache = new LinkedHashMap<MyPair, Integer>();
        ancestor_cache = new LinkedHashMap<MyPair, Integer>();

    }

    private MyPair checkKey(MyPair key) {
        if (size_cache.containsKey(key) &&
                ancestor_cache.containsKey(key)) {
            return new MyPair(size_cache.get(key),
                              ancestor_cache.get(key));
        }
        return new MyPair();
    }

    private MyPair helper(BreadthFirstDirectedPaths bv, BreadthFirstDirectedPaths bw) {
        int tmp = -1, min = -1, ancestor = -1;
        for (int i = 0; i < myGraph.V(); i++) {
            if (bv.hasPathTo(i) && bw.hasPathTo(i)) {
                tmp = bv.distTo(i) + bw.distTo(i);
                if (min < 0 || tmp < min) {
                    min = tmp;
                    ancestor = i;
                }
            }

        }
        return new MyPair(min, ancestor);
    }

    private MyPair minPath(Iterable<Integer> v, Iterable<Integer> w) {
        return helper(new BreadthFirstDirectedPaths(myGraph, v),
                      new BreadthFirstDirectedPaths(myGraph, w));
    }

    private MyPair minPath(int v, int w) {
        MyPair key = checkKey(new MyPair(v, w));
        if (!key.isDefault()) return key;
        MyPair tmp = helper(new BreadthFirstDirectedPaths(myGraph, v),
                            new BreadthFirstDirectedPaths(myGraph, w));
        size_cache.put(key, tmp.fst);
        ancestor_cache.put(key, tmp.snd);
        return tmp;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return minPath(v, w).fst;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return minPath(v, w).snd;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return minPath(v, w).fst;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return minPath(v, w).snd;
    }

    // do unit testing of this class*/
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        int x = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);
        System.out.println(sap.length(x, y));
        System.out.println(sap.ancestor(x, y));
        /* Integer[] arr1 = { 13, 23, 24 };
        Integer[] arr2 = { 6, 16, 17 };
        System.out.println(
                sap.length(Arrays.asList(arr1),
                           Arrays.asList(arr2)));
        System.out.println(sap.ancestor(Arrays.asList(arr1),
                                        Arrays.asList(arr2)));

        /*twhile (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }*/
    }
}
