/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wn;

    public Outcast(WordNet wordnet) {
        wn = wordnet;
    }    // constructor takes a WordNet object

    public String outcast(String[] nouns) {
        int sum, max = Integer.MIN_VALUE;
        String s = "";
        for (int i = 0; i < nouns.length; i++) {
            sum = 0;
            for (int j = 0; j < nouns.length; j++) {
                sum += wn.distance(nouns[i], nouns[j]);
                //if (nouns[i].compareTo("table") == 0) System.out.println(sum);
            }
            if (max < sum) {
                max = sum;
                s = nouns[i];
            }
            //System.out.println(sum);
        }
        return s;

    }  // given an array of WordNet nouns, return an outcast

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }// see test client below
}
