import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by weili on 16-3-28.
 */
public class BruteCollinearPoints {
    private LineSegment[] result;
    private List<Point> alreadyPoints = new ArrayList<>();
    private int k = 0;

    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new NullPointerException();
        for (Point point : points) {
            if (point == null)
                throw new NullPointerException();
        }
        result = new LineSegment[points.length];

        for (int i = 0; i < points.length; i++)
            for (int j = i+1; j < points.length; j++)
                for (int m = j+1; m < points.length; m++)
                    for (int n = m+1; n < points.length; n++) {
                        if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[m])
                                && points[i].slopeTo(points[j]) == points[i].slopeTo(points[n])) {
                            if ((!contains(points[i])) && (!contains(points[n]))) {
                                result[k++] = new LineSegment(points[i], points[n]);
                                alreadyPoints.add(points[i]);
                                alreadyPoints.add(points[n]);
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

    public boolean contains(Point y) {
        for (Point alreadyPoint : alreadyPoints) {
            if (alreadyPoint.compareTo(y) == 0) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);

        int n = in.readInt();
        Point[] points = new Point[n];
        for(int i = 0; i < n; i++) {
            Point point = new Point(in.readInt(), in.readInt());
            points[i] = point;
        }

        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        for (Point point : points) {
            System.out.println(point);
        }

        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);

        for (LineSegment lineSegment : bruteCollinearPoints.segments()) {
            System.out.println(lineSegment);
            lineSegment.draw();
        }
    }
}
