import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
public class MoveToFront {

    private static class Sequence {
        private static final int R = 256;
        private char[] seq = new char[R];

        private Sequence() {
            for (int i = 0; i < R; i++)
                seq[i] = (char) i;
        }

        private int size() {
            return R;
        }

        private char indexOf(char c) {
            int res = -1, i = 0;
            for (; i < R; i++)
                if (seq[i] == c) {
                    res = i;
                    break;
                }
            return (char) res;

        }

        private char valueOf(int t) {
            return seq[t];
        }

        private void move(char c, int index) {
            for (; index > 0; index--)
                seq[index] = seq[index - 1];
            seq[0] = c;
        }
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        Sequence s = new Sequence();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar(8);
            char t = s.indexOf(c);
            s.move(c, t);
            BinaryStdOut.write(t);
        }
        BinaryStdOut.close();

    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        Sequence s = new Sequence();
        while (!BinaryStdIn.isEmpty()) {
            int t = BinaryStdIn.readChar(8);
            char c = s.valueOf(t);
            BinaryStdOut.write(c);
            s.move(c, t);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].compareTo("-") == 0)
            encode();
        else if (args[0].compareTo("+") == 0)
            decode();
    }

}
