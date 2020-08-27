/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {

    private static class CircularSuffix implements Comparable<CircularSuffix> {
        StringBuilder s;
        int pt;

        public CircularSuffix(StringBuilder s, int pt) {
            this.s = s;
            this.pt = pt;
        }

        public int compareTo(CircularSuffix that) {
            int len = this.s.length();
            assert (len == that.s.length());
            for (int i = 0; i < len; i++) {
                char t1 = this.s.charAt((this.pt + i) % len);
                char t2 = that.s.charAt((that.pt + i) % len);
                if (t1 != t2)
                    return t1 - t2;
            }
            return 0;
        }
    }

    private int[] array;
    private int size;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        MinPQ<CircularSuffix> pq = new MinPQ<>();
        StringBuilder sb = new StringBuilder(s);
        size = s.length();
        array = new int[size];
        for (int i = 0; i < size; i++)
            pq.insert(new CircularSuffix(sb, i));
        for (int i = 0; i < size; i++) {
            CircularSuffix c = pq.delMin();
            array[i] = c.pt;
        }
    }

    // length of s
    public int length() {
        return size;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= size) throw new IllegalArgumentException();
        return array[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        //CircularSuffixArray c = new CircularSuffixArray("Abracadabra!");
        //int x = c.length();
        In in = new In(args[0]);
        String s = in.readAll();
        CircularSuffixArray p = new CircularSuffixArray(s);
        StdOut.println(s);
        for (int i = 0; i < p.length(); i++)
            StdOut.println(p.index(i));
        /*CircularSuffix c1 = new CircularSuffix(new StringBuilder("ABRACADABRA!"), 11);
        CircularSuffix c2 = new CircularSuffix(new StringBuilder("ABRACADABRA!"), 4);
        StdOut.println(c1.compareTo(c2) > 0);*/
    }

}
