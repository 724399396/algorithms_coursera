import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Arrays;

/**
 * Created by weili on 16-3-28.
 */
public class BruteCollinearPoints {
    private LineSegment[] result;
    private int k = 0;

    public BruteCollinearPoints(Point[] points) {
        checkArg(points);
        result = new LineSegment[points.length];

        for (int i = 0; i < points.length; i++)
            for (int j = i + 1; j < points.length; j++) {
                double slope1 = points[i].slopeTo(points[j]);
                for (int m = j + 1; m < points.length; m++) {
                    double slope2 = points[i].slopeTo(points[m]);
                    for (int n = m + 1; n < points.length; n++) {
                        if (slope1 == slope2
                                && slope1 == points[i].slopeTo(points[n])) {
                            result[k++] = new LineSegment(
                                    min(min(points[i], points[j]), min(points[m], points[n])),
                                    max(max(points[i], points[j]), max(points[m], points[n]))
                            );
                        }
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

    public static void main(String[] args) {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        In in = new In(args[0]);

        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            Point point = new Point(in.readInt(), in.readInt());
            point.draw();
            points[i] = point;
        }

        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);

        for (LineSegment lineSegment : bruteCollinearPoints.segments()) {
            System.out.println(lineSegment);
            lineSegment.draw();
        }
    }
}
