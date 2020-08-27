/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.HashMap;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output

    private static final int R = 256;

    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray a = new CircularSuffixArray(s);
        StringBuilder z = new StringBuilder();
        int size = a.length(), x = 0;
        for (int i = 0; i < size; i++) {
            int tmp = a.index(i);
            if (tmp == 0)
                x = i;
            z.append(s.charAt((tmp - 1 + size) % size));
        }
        BinaryStdOut.write(x);
        BinaryStdOut.write(z.toString());
        BinaryStdOut.close();
    }

    private static int[] create_next(String s, char[] fr) {
        int size = s.length();
        int[] next = new int[size];
        HashMap<Character, Integer> m = new HashMap<>();
        for (int i = 0; i < size; i++) {
            char c = fr[i];
            int t = 0;
            if (m.containsKey(c))
                t = m.get(c) + 1;
            next[i] = s.indexOf(c, t);
            m.put(c, t);
        }
        return next;
    }

    private static char[] sort(char[] array) {
        int size = array.length;
        char[] res = new char[size];
        int[] count = new int[R + 1];
        for (int i = 0; i < size; i++)
            count[array[i] + 1]++;
        for (int i = 0; i < R; i++) {
            count[i + 1] += count[i];
        }
        for (int i = 0; i < size; i++) {
            res[count[array[i]]++] = array[i];
        }
        return res;
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int fst = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        char[] ss = sort(s.toCharArray());
        int[] next = create_next(s, ss);
        StringBuilder z = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            z.append(ss[fst]);
            fst = next[fst];
        }
        BinaryStdOut.write(z.toString());
        BinaryStdOut.close();

    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].compareTo("-") == 0)
            transform();
        else if (args[0].compareTo("+") == 0)
            inverseTransform();


    }
}
