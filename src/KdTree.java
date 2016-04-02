import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

/**
 * Created by weili on 16-4-1.
 */
public class KdTree {
    private static class Node {
        private Point2D ele;
        private RectHV rect;
        private boolean horizontal;
        private Node left;
        private Node right;

        public Node(Point2D ele, RectHV rect, boolean horizontal, Node left, Node right) {
            this.ele = ele;
            this.rect = rect;
            this.horizontal = horizontal;
            this.left = left;
            this.right = right;
        }
    }
    public Node root;
    public int size;

    public KdTree() {

    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        checkArg(p);
        if (root == null)
            root = insert(p, root, null, false, false);
        else
            root = insert(p, root, null, root.horizontal, false);
    }

    private Node insert(Point2D p, Node node, Node parent, boolean horizontal, boolean less) {
        if (node == null) {
            if (parent == null)
                return new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0),
                    true, null, null);
            else {
                if (!horizontal) {
                    if (less)
                        return new Node(p, new RectHV(parent.rect.xmin(), parent.rect.ymin()
                                 , parent.ele.x(), parent.rect.ymax()), horizontal, null, null);
                    else
                        return new Node(p, new RectHV(parent.ele.x(), parent.rect.ymin()
                                , parent.rect.xmax(), parent.rect.ymax()), horizontal, null, null);
                } else {
                    if (less)
                        return new Node(p, new RectHV(parent.rect.xmin(), parent.rect.ymin()
                                , parent.rect.xmax(), parent.ele.y()), horizontal, null, null);
                    else
                        return new Node(p, new RectHV(parent.rect.xmin(), parent.ele.y()
                                , parent.rect.xmax(), parent.rect.ymax()), horizontal, null, null);
                }

            }
        }
        if (horizontal) {
            double parentX = node.ele.x();
            if (parentX > p.x())
                return insert(p, node.left, node, !node.horizontal, true);
            else
                return insert(p, node.right, node, !node.horizontal, false);
        } else {
            double parentY = node.ele.y();
            if (parentY > p.y())
                return insert(p, node.left, node, !node.horizontal, true);
            else
                return insert(p, node.right, node, !node.horizontal, false);
        }
    }

    public boolean contains(Point2D p) {
        checkArg(p);
        return contains(p, root);
    }

    private boolean contains(Point2D p, Node node) {
        if (node == null)
            return false;
        if (node.horizontal) {
            double parentX = node.ele.x();
            if (parentX > p.x())
                return contains(p, node.left);
            else
                return contains(p, node.right);
        } else {
            double parentY = node.ele.y();
            if (parentY > p.y())
                return contains(p, node.left);
            else
                return contains(p, node.right);
        }
    }

    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        draw(root);
    }

    private void draw(Node node) {
        if (node == null)
            return;
        if (node.horizontal) {
            StdDraw.setPenColor(Color.red);
            StdDraw.line(node.ele.x(), node.rect.ymin(), node.ele.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(Color.blue);
            StdDraw.line(node.rect.xmin(), node.ele.y(), node.rect.xmax(), node.ele.y());
        }
        if (node.left != null) {
            draw(node.left);
        }
        if (node.right != null) {
            draw(node.right);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        checkArg(rect);
        return null;
    }

    public Point2D nearest(Point2D p) {
        checkArg(p);
        return null;
    }

    private void checkArg(Object p) {
        if (p == null)
            throw new NullPointerException();
    }

    public static void main(String[] args) {

    }
}
