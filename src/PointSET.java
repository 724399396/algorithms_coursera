import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by weili on 16-4-1.
 */
public class PointSET {
    private Set<Point2D> set;

    public PointSET() {
        set = new TreeSet<>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
        checkArg(p);
        set.add(p);
    }

    public boolean contains(Point2D p) {
        checkArg(p);
        return set.contains(p);
    }

    public void draw() {
        for (Point2D point2D : set) {
            point2D.draw();
        }

    }
    public Iterable<Point2D> range(RectHV rect) {
        checkArg(rect);
        final Set<Point2D> match = new TreeSet<>();
        for (Point2D point2D : set) {
            if (rect.contains(point2D))
                match.add(point2D);
        }
        return new Iterable<Point2D>() {
            @Override
            public Iterator<Point2D> iterator() {
                return match.iterator();
            }
        };
    }
    public Point2D nearest(Point2D p) {
        checkArg(p);
        double min = Double.MAX_VALUE;
        Point2D nearestPoint = null;
        for (Point2D point2D : set) {
            double tmp = point2D.distanceTo(p);
            if (tmp < min) {
                min = tmp;
                nearestPoint = point2D;
            }
        }
        return nearestPoint;
    }

    private void checkArg(Object p) {
        if (p == null)
            throw new NullPointerException();
    }

    public static void main(String[] args) {

    }
}
