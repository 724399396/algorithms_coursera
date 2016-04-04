import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.Iterator;

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
    private Node root;
    private int size;

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
            size++;
            if (parent == null) {
                return new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0),
                        true, null, null);
            }
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
            if (parentX > p.x()) {
                node.left = insert(p, node.left, node, !node.horizontal, true);
            }
            else {
                node.right = insert(p, node.right, node, !node.horizontal, false);
            }
        } else {
            double parentY = node.ele.y();
            if (parentY > p.y())
                node.left = insert(p, node.left, node, !node.horizontal, true);
            else
                node.right = insert(p, node.right, node, !node.horizontal, false);
        }
        return node;
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
            else if (parentX < p.x())
                return contains(p, node.right);
            else
                return node.ele.y() == p.y();
        } else {
            double parentY = node.ele.y();
            if (parentY > p.y())
                return contains(p, node.left);
            else if (parentY < p.y())
                return contains(p, node.right);
            else
                return node.ele.x() == p.x();
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
        java.util.List<Point2D> res = new java.util.ArrayList<>();
        range(rect, root, res);
        return new Iterable<Point2D>() {
            @Override
            public Iterator<Point2D> iterator() {
                return res.iterator();
            }
        };
    }

    private void range(RectHV rect, Node node, java.util.List<Point2D> list) {
        if (node == null || !rect.intersects(node.rect))
            ;
        else {
            boolean contain = rect.contains(node.ele);
            if (contain) {
                list.add(node.ele);
            }
            range(rect, node.left, list);
            range(rect, node.right, list);
        }
    }

    public Point2D nearest(Point2D p) {
        checkArg(p);
        return nearest(p, root, null, Double.MAX_VALUE);
    }

    private Point2D nearest(Point2D p, Node node, Point2D nearest, double value) {
        if (node == null || node.rect.distanceTo(p) >= value)
            return nearest;
        else {
            if (nearest == null || p.distanceTo(node.ele) < value) {
                nearest = node.ele;
                value = p.distanceTo(node.ele);
            }
            Point2D left = nearest(p, node.left, nearest, value);
            double leftValue = p.distanceTo(left);
            Point2D right = nearest(p, node.right, nearest, value);
            double rightValue = p.distanceTo(right);
            if (leftValue < value) {
                nearest = left;
                value = leftValue;
            }
            if (rightValue < value) {
                nearest = right;
            }
            return nearest;
        }
    }

    private void checkArg(Object p) {
        if (p == null)
            throw new NullPointerException();
    }

    public static void main(String[] args) {

    }
}
