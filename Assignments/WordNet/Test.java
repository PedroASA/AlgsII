/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

import java.util.HashSet;

public class Test {
    public static boolean isDAG(Digraph G) {
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

    public static void tst(Digraph g) {
        Digraph wn = new Digraph(new In("digraph-wordnet.txt"));
        assert (wn.equals(g));
        System.out.println(wn.V() == g.V());
        System.out.println(wn.E() + "\n" + g.E());
    }

    public static HashSet<Integer> nounSet(String s, String[] str) {
        HashSet<Integer> res = new HashSet<Integer>();
        for (int x = 0; x < str.length; x++)
            if (str[x].contains(s))
                res.add(x);
        return res;
    }

    public static void main(String[] args) {
        /*In in = new In(args[0]);
        Digraph g = new Digraph(in);
        Topological t = new Topological(g);
        System.out.println(isDAG(g) == t.hasOrder());*/
        String[] s = { "tabletop table top toptable", "slkdjvkvjvs", "ukgvkdtabletop" };
        for (int x = 0; x < s.length; x++)
            if (s[x].contains("table"))
                System.out.println(s[x]);
        ;
    }
}
