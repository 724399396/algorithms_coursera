import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by weili on 16-3-28.
 */
public class FastCollinearPoints {
    private LineSegment[] result;
    private List<Point> alreadyPoints = new ArrayList<>();
    private int k = 0;

    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new NullPointerException();
        for (Point point : points) {
            if (point == null)
                throw new NullPointerException();
        }
        Point origin = points[0];
        Arrays.sort(points, origin.slopeOrder());
        result = new LineSegment[points.length];
        int match = 0;
        int base = 0;
        for (int i = 0; i < points.length; i++) {
            if (points[base].slopeTo(origin) == points[i].slopeTo(origin)) {
                match++;
            } else {
                if (match >= 4) {
                    if ((!contains(points[base])) && (!contains(points[i-1]))) {
                        result[k++] = new LineSegment(points[base], points[i - 1]);
                    }
                }
                base = i;
                match = 1;
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

        for (Point point : points) {
            System.out.println(point);
            point.draw();
        }

        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);

        for (LineSegment lineSegment : fastCollinearPoints.segments()) {
            System.out.println(lineSegment);
            lineSegment.draw();
        }
    }
}
