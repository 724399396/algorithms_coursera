import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.Iterator;

/**
 * Created by weili on 16-4-14.
 */
public class SeamCarver {
    private Picture picture;
    private DijkstraSP verticalSP;
    private DijkstraSP horizontalSP;
    private Color[][] colors;

    public SeamCarver(Picture picture) {
        checkArgNull(picture);
        this.picture = new Picture(picture);
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public double energy(int x, int y) {
        if (invalidCoordinate(x, y))
            throw new IndexOutOfBoundsException();
        if (x == 0 || x == picture.width() - 1 || y == 0 || y == picture.height() - 1)
            return 1000.0;

        Color x2Color = picture.get(x+1, y);
        Color x1Color = picture.get(x-1, y);
        double rx = x2Color.getRed() - x1Color.getRed();
        double gx = x2Color.getGreen() - x1Color.getGreen();
        double bx = x2Color.getBlue() - x1Color.getBlue();

        Color y2Color = picture.get(x, y+1);
        Color y1Color = picture.get(x, y-1);
        double ry = y2Color.getRed() - y1Color.getRed();
        double gy = y2Color.getGreen() - y1Color.getGreen();
        double by = y2Color.getBlue() - y1Color.getBlue();

        return Math.sqrt(Math.pow(rx, 2) + Math.pow(gx, 2) + Math.pow(bx, 2)
                       + Math.pow(ry, 2) + Math.pow(gy, 2) + Math.pow(by, 2));
    }

    public int[] findHorizontalSeam() {
        // horizontal SP
        int height = height();
        int width = width();
        int top = height() * width();
        int bottom = height() * width() + 1;
        EdgeWeightedDigraph horizontalGraph = new EdgeWeightedDigraph(height * width + 2);
        for (int j = 0; j < height; j++) {
            horizontalGraph.addEdge(new DirectedEdge(top, j * width, energy(0, j)));
            horizontalGraph.addEdge(new DirectedEdge(j*width + width - 1, bottom, 0));
        }
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width-1; i++) {
                int cur = j * width + i;
                horizontalGraph.addEdge(new DirectedEdge(cur, cur + 1, energy(i + 1, j)));
                if (j > 0)
                    horizontalGraph.addEdge(new DirectedEdge(cur, cur + 1 - width, energy(i + 1, j - 1)));
                if (j < height - 1)
                    horizontalGraph.addEdge(new DirectedEdge(cur, cur + 1 + width, energy(i + 1, j + 1)));
            }
        }
        horizontalSP = new DijkstraSP(horizontalGraph, top);

        Iterator<DirectedEdge> paths = horizontalSP.pathTo(bottom).iterator();
        int[] res = new int[width()];
        for (int i = 0; i < width(); i++) {
            DirectedEdge edge = paths.next();
            res[i] = edge.to() / width();
        }
        return res;
    }

    public int[] findVerticalSeam() {
        // vertical SP
        int height = height();
        int width = width();
        int top = height() * width();
        int bottom = height() * width() + 1;
        EdgeWeightedDigraph verticalGraph = new EdgeWeightedDigraph(height * width + 2);
        for (int i = 0; i < width; i++) {
            verticalGraph.addEdge(new DirectedEdge(top, i, energy(i, 0)));
            verticalGraph.addEdge(new DirectedEdge(top - 1 - i, bottom, 0));
        }
        for (int j = 0; j < height - 1; j++) {
            for (int i = 0; i < width; i++) {
                int cur = j * width + i;
                verticalGraph.addEdge(new DirectedEdge(cur, cur + width, energy(i, j + 1)));
                if (i > 0)
                    verticalGraph.addEdge(new DirectedEdge(cur, cur + width - 1, energy(i - 1, j + 1)));
                if (i < width - 1)
                    verticalGraph.addEdge(new DirectedEdge(cur, cur + width + 1, energy(i + 1, j + 1)));
            }
        }
        verticalSP = new DijkstraSP(verticalGraph, top);
        Iterator<DirectedEdge> paths = verticalSP.pathTo(bottom).iterator();
        int[] res = new int[height()];
        for (int i = 0; i < height(); i++) {
            DirectedEdge edge = paths.next();
            res[i] = edge.to() % width();
        }
        return res;
    }

    public void removeHorizontalSeam(int[] seam) {
        checkArgNull(seam);
        if (seam.length != picture.width() || picture.height() <= 1)
            throw new IllegalArgumentException();
        for (int i = 0; i < picture.width(); i++) {
            if (invalidCoordinate(i, seam[i]))
                throw new IllegalArgumentException();
            if (i > 0)
                if (Math.abs(seam[i] - seam[i-1]) > 1)
                    throw new IllegalArgumentException();
        }
        Picture newPicture = new Picture(width(), height()-1);
        for (int i = 0; i < width(); i++)
            for (int j = 0; j < height()-1; j++) {
                if (j >= seam[i])
                    newPicture.set(i, j, picture.get(i, j+1));
                else
                    newPicture.set(i, j, picture.get(i, j));
            }
        picture = newPicture;
    }

    public void removeVerticalSeam(int[] seam) {
        checkArgNull(seam);
        if (seam.length != picture.height() || picture.width() <= 1)
            throw new IllegalArgumentException();
        for (int i = 0; i < picture.height(); i++) {
            if (invalidCoordinate(seam[i], i))
                throw new IllegalArgumentException();
            if (i > 0)
                if (Math.abs(seam[i] - seam[i-1]) > 1)
                    throw new IllegalArgumentException();
        }
        Picture newPicture = new Picture(width()-1, height());
        for (int j = 0; j < height(); j++)
            for (int i = 0; i < width()-1; i++) {
                if (i >= seam[j])
                    newPicture.set(i, j, picture.get(i + 1, j));
                else
                    newPicture.set(i, j, picture.get(i, j));
            }
        picture = newPicture;
    }

    private void checkArgNull(Object... args) {
        for (Object arg : args) {
            if (arg == null)
                throw new NullPointerException();
        }
    }

    private boolean invalidCoordinate(int x, int y) {
        return (x > picture.width() - 1 || x < 0
                || y > picture.height() - 1 || y < 0);
    }

    public static void main(String[] args) {
        Picture picture = new Picture(8, 8);
        picture.set(0, 0, new Color ( 5, 9, 5));
        picture.set(1, 0, new Color ( 8, 1, 7));
        picture.set(2, 0, new Color ( 3, 0, 6));
        picture.set(3, 0, new Color ( 0, 8, 1));
        picture.set(4, 0, new Color ( 1, 4, 9));
        picture.set(5, 0, new Color ( 3, 9, 7));
        picture.set(6, 0, new Color ( 1, 8, 9));
        picture.set(7, 0, new Color ( 3, 9, 1));
        picture.set(0, 1, new Color ( 6, 4, 9));
        picture.set(1, 1, new Color ( 0, 6, 3));
        picture.set(2, 1, new Color ( 9, 3, 3));
        picture.set(3, 1, new Color ( 9, 8, 5));
        picture.set(4, 1, new Color ( 3, 5, 8));
        picture.set(5, 1, new Color ( 4, 2, 2));
        picture.set(6, 1, new Color ( 9, 8, 3));
        picture.set(7, 1, new Color ( 2, 6, 5));
        picture.set(0, 2, new Color ( 2, 7, 9));
        picture.set(1, 2, new Color ( 4, 8, 7));
        picture.set(2, 2, new Color ( 9, 4, 0));
        picture.set(3, 2, new Color ( 9, 3, 7));
        picture.set(4, 2, new Color ( 9, 7, 0));
        picture.set(5, 2, new Color ( 2, 7, 9));
        picture.set(6, 2, new Color ( 3, 7, 8));
        picture.set(7, 2, new Color ( 8, 6, 7));
        picture.set(0, 3, new Color ( 6, 1, 8));
        picture.set(1, 3, new Color ( 4, 0, 8));
        picture.set(2, 3, new Color ( 0, 4, 3));
        picture.set(3, 3, new Color ( 4, 4, 4));
        picture.set(4, 3, new Color ( 2, 0, 9));
        picture.set(5, 3, new Color ( 1, 4, 6));
        picture.set(6, 3, new Color ( 0, 5, 6));
        picture.set(7, 3, new Color ( 5, 3, 5));
        picture.set(0, 4, new Color ( 5, 5, 2));
        picture.set(1, 4, new Color ( 2, 7, 3));
        picture.set(2, 4, new Color ( 1, 4, 0));
        picture.set(3, 4, new Color ( 0, 7, 6));
        picture.set(4, 4, new Color ( 0, 9, 2));
        picture.set(5, 4, new Color ( 1, 3, 2));
        picture.set(6, 4, new Color ( 6, 0, 5));
        picture.set(7, 4, new Color ( 7, 7, 8));
        picture.set(0, 5, new Color ( 8, 8, 3));
        picture.set(1, 5, new Color ( 4, 4, 3));
        picture.set(2, 5, new Color ( 4, 1, 8));
        picture.set(3, 5, new Color ( 9, 2, 6));
        picture.set(4, 5, new Color ( 3, 7, 2));
        picture.set(5, 5, new Color ( 7, 4, 8));
        picture.set(6, 5, new Color ( 0, 7, 4));
        picture.set(7, 5, new Color ( 0, 7, 5));
        picture.set(0, 6, new Color ( 6, 4, 7));
        picture.set(1, 6, new Color ( 0, 1, 9));
        picture.set(2, 6, new Color ( 7, 4, 7));
        picture.set(3, 6, new Color ( 5, 1, 3));
        picture.set(4, 6, new Color ( 9, 3, 9));
        picture.set(5, 6, new Color ( 9, 3, 2));
        picture.set(6, 6, new Color ( 4, 0, 1));
        picture.set(7, 6, new Color ( 1, 8, 1));
        picture.set(0, 7, new Color ( 5, 8, 0));
        picture.set(1, 7, new Color ( 1, 1, 2));
        picture.set(2, 7, new Color ( 6, 1, 7));
        picture.set(3, 7, new Color ( 6, 7, 4));
        picture.set(4, 7, new Color ( 9, 8, 1));
        picture.set(5, 7, new Color ( 8, 4, 8));
        picture.set(6, 7, new Color ( 1, 9, 1));
        picture.set(7, 7, new Color ( 5, 5, 3));
        SeamCarver seamCarver = new SeamCarver(picture);
        seamCarver.width();
        seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
        seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
        seamCarver.energy(1,1);
        seamCarver.findVerticalSeam();
    }
}

