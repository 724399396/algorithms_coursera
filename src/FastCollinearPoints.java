import edu.princeton.cs.algs4.In;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by weili on 16-3-28.
 */
public class FastCollinearPoints {
    private LineSegment[] result;
    private int k = 0;
    private List<Pair<Point, Point>> alreadyGet = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        checkArg(points);
        result = new LineSegment[points.length];
        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        for (Point point : points) {
            Point origin = point;
            Arrays.sort(pointsCopy, origin.slopeOrder());
            double[] slopes = new double[pointsCopy.length];
            for (int i = 0; i < pointsCopy.length; i++) {
                slopes[i] = origin.slopeTo(pointsCopy[i]);
            }
            int match = 1;
            int base = 0;
            for (int i = 0; i < slopes.length; i++) {
                if (slopes[base] == slopes[i]) {
                    match++;
                    if (i == slopes.length - 1) {
                        if (match >= 3) {
                            Point min = origin;
                            Point max = origin;
                            for (int tmp = base; tmp <= i; tmp++) {
                                min = min(min, pointsCopy[tmp]);
                                max = max(max, pointsCopy[tmp]);
                            }
                            if (!alreadyGet.contains(new Pair<Point, Point>(min, max))) {
                                alreadyGet.add(new Pair<Point, Point>(min, max));
                                if (k == points.length)
                                    resize(2*points.length);
                                result[k++] = new LineSegment(min, max);
                            }
                        }
                    }
                } else {
                    if (match >= 3) {
                        Point min = origin;
                        Point max = origin;
                        for (int tmp = base; tmp < i; tmp++) {
                            min = min(min, pointsCopy[tmp]);
                            max = max(max, pointsCopy[tmp]);
                        }
                        if (!alreadyGet.contains(new Pair<Point, Point>(min, max))) {
                            alreadyGet.add(new Pair<Point, Point>(min, max));
                            if (k == points.length)
                                resize(2*points.length);
                            result[k++] = new LineSegment(min, max);
                        }
                    }
                    base = i;
                    match = 1;
                }

            }
        }
    }
    public int numberOfSegments() {
        return k;
    }
    public LineSegment[] segments() {
        return Arrays.copyOf(result, k);
    }

    private void checkArg(Point[] points) {
        if (points == null)
            throw new NullPointerException();
        for (Point point : points) {
            if (point == null)
                throw new NullPointerException();
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++)
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();
        }
    }

    private Point min(Point x, Point y) {
        if (x.compareTo(y) < 0)
            return x;
        else
            return y;
    }

    private Point max(Point x, Point y) {
        if (x.compareTo(y) > 0)
            return x;
        else
            return y;
    }

    private void resize(int size) {
        LineSegment[] res = new LineSegment[size];
        for (int i = 0; i < k; i++) {
            res[i] = result[i];
        }
        result = res;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);

        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            Point point = new Point(in.readInt(), in.readInt());
            points[i] = point;
        }

        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);

        for (LineSegment lineSegment : fastCollinearPoints.segments()) {
            System.out.println(lineSegment);
        }
        System.out.println(fastCollinearPoints.numberOfSegments());
    }
}
