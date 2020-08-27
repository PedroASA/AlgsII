/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.util.ArrayList;

public class SeamCarver {

    private Picture pic;
    private static final int DEFAULT_ENERGY = 1000;
    private int w, h;
    private double[][] energy;
    private boolean VERTICAL;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        pic = picture;
        w = pic.width();
        h = pic.height();
        VERTICAL = true;
    }

    // current picture
    public Picture picture() {
        return new Picture(pic);
    }

    // width of current picture
    public int width() {
        return pic.width();
    }

    // height of current picture
    public int height() {
        return pic.height();
    }

    private boolean inBorder(int x, int y) {
        return (x == 0 || x >= h - 1
                || y == 0 || y >= w - 1);
    }

    private int red(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    private int green(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    private int blue(int rgb) {
        return (rgb >> 0) & 0xFF;
    }

    private double delta(int rgb1, int rgb2) {
        return Math.pow(red(rgb1) - red(rgb2), 2) +
                Math.pow(green(rgb1) - green(rgb2), 2) +
                Math.pow(blue(rgb1) - blue(rgb2), 2);

    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x < h
                && y >= 0 || y < w;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (!inBounds(x, y)) throw new IllegalArgumentException();
        if (inBorder(y, x)) return DEFAULT_ENERGY;
        return Math.sqrt(delta(pic.getRGB(x + 1, y), pic.getRGB(x - 1, y))
                                 + delta(pic.getRGB(x, y + 1), pic.getRGB(x, y - 1)));
    }

    private int[] pair(int x, int y) {
        int[] res = new int[2];
        res[0] = x;
        res[1] = y;
        return res;
    }

    private ArrayList<int[]> adj(int x, int y) {
        ArrayList<int[]> res = new ArrayList<int[]>();
        if (y > 0) res.add(pair(x + 1, y - 1));
        if (y < w - 1) res.add(pair(x + 1, y + 1));
        res.add(pair(x + 1, y));
        return res;
    }

    private double weight(int x, int y) {
        return energy[y][x];
    }

    private void relax(double[][] distTo, int[][] PixelTo) {
        int y_next;
        double weight, min = Double.MAX_VALUE;
        for (int v = 0; v < h; v++)
            for (int k = 0; k < w; k++)
                distTo[k][v] = Double.POSITIVE_INFINITY;
        for (int y = 0; y < w; y++) distTo[y][0] = 0.0;
        for (int x = 0; x < h - 1; x++) {
            for (int y = 0; y < w; y++) {
                for (int[] pair : adj(x, y)) {
                    weight = weight(pair[0], pair[1]);
                    if (weight < min) y_next = pair[1];
                    if (distTo[pair[1]][pair[0]] > distTo[y][x] + weight) {
                        PixelTo[pair[1]][pair[0]] = y;
                        distTo[pair[1]][pair[0]] = distTo[y][x] + weight;
                    }
                }
                //y = y_next;
            }
        }
    }

    // sequence of indices for horizontal seam

    private void calcEnergy() {
        energy = new double[w][h];
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                energy[col][row] = energy(col, row);
            }
        }
    }

    private void Transpose() {
        Picture p = new Picture(h, w);
        for (int i = 0; i < h; i++)
            for (int j = 0; j < w; j++)
                p.setRGB(i, j, pic.getRGB(j, i));
        pic = p;
        h = pic.height();
        w = pic.width();
        VERTICAL = !VERTICAL;
    }

    public int[] findHorizontalSeam() {
        boolean x = VERTICAL;
        if (x) Transpose();
        int[] tmp = findVerticalSeam();
        if (x) Transpose();
        return tmp;
    }

    private int[] shortestPath(double[][] distTo, int[][] pixelTo) {
        double min_dist = Double.MAX_VALUE;
        int min_pix = 0;
        int[] res = new int[h];
        for (int v = 0; v < w; v++)
            if (distTo[v][h - 1] < min_dist) {
                min_dist = distTo[v][h - 1];
                min_pix = v;
            }
        for (int v = h - 1; v >= 0; v--) {
            res[v] = min_pix;
            min_pix = pixelTo[min_pix][v];
        }
        return res;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        calcEnergy();
        int[][] pixelTo = new int[w][h];
        double[][] distTo = new double[w][h];
        //double min = Double.MAX_VALUE, sum;
        relax(distTo, pixelTo);
        //for (double[] x : distTo) System.out.println(Arrays.toString(x));
        return shortestPath(distTo, pixelTo);
    }

    private boolean check(int[] seam) {
        for (int col = 0; col < seam.length - 1; col++)
            if (Math.abs(seam[col] - seam[col + 1]) > 1) return false;
        return true;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || !check(seam) || h <= 1 || seam.length != w)
            throw new IllegalArgumentException();
        boolean x = VERTICAL;
        if (x) Transpose();
        removeVerticalSeam(seam);
        if (x) Transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || !check(seam) || w <= 1 || seam.length != h)
            throw new IllegalArgumentException();
        Picture p = new Picture(w - 1, h);
        for (int k = 0; k < seam.length; k++) {
            for (int col = 0; col < seam[k]; col++)
                p.setRGB(col, k, pic.getRGB(col, k));
            for (int col = seam[k]; col < w - 1; col++)
                p.setRGB(col, k, pic.getRGB(col + 1, k));
        }
        pic = p;
        w--;
    }

    //  unit testing (optional)

    public static void main(String[] args) {
        SeamCarver s = new SeamCarver(new Picture(args[0]));
        s.removeVerticalSeam(s.findVerticalSeam());

    }

}
