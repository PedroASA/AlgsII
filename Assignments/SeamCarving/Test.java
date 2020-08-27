import edu.princeton.cs.algs4.Picture;

public class Test {
    public static void main(String[] args) {
        SeamCarver s = new SeamCarver(new Picture(args[0]));
        s.calcEnergy();
        int[] path = new int[h], res = new int[h];
        double[][] distTo = new double[w][h];
        double min = Double.MAX_VALUE, sum;
        for (int v = 0; v < h; v++)
            for (int k = 0; k < w; k++)
                distTo[k][v] = Double.POSITIVE_INFINITY;
        //for (int i = 0; i < w; i++) distTo[i][0] = 0.0;
        for (int i = 0; i < w; i++) {
            distTo[i][0] = 0.0;
            sum = shortestPath(i, distTo, path);
            if (sum < min) {
                min = sum;
                System.arraycopy(path, 0, res, 0, h);
            }
        }
        return res;
    }


}
}
