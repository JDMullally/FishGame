import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;

import javax.swing.*;

public class XGui extends JFrame implements MouseListener {

    // size of the hexagon
    private Integer size;

    private XGui(Integer size) {
        super("XGui");

        this.size = size;

        this.setSize(3 * size, 2 * size + 1); //sets the dimensions of the window to be equal to the input
        this.setResizable(false);
        this.addMouseListener(this);
        this.setLayout(null);
        this.setUndecorated(true);
        this.setVisible(true);
    }

    /*
     * Entry point for the application
     * @param args program arguments
     */
    public static void main(String[] args) {
        Integer size = tryParse(args);
        if (size != null && size > 0) {
            new XGui(size);
        } else {
            System.out.println("usage: ./xgui positive-integer");
        }
    }

    /**
     * Takes in the arguments for the program and returns either an Integer or null depending on
     * whether the String is an Integer or not.
     *
     * @param args arguments from main method
     * @return Integer
     */
    private static Integer tryParse(String[] args) {
        try {
            return Integer.parseInt(args[0]);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Paint method required to paint on the JFrame.
     *
     * @param g graphics component
     */
    public void paint(Graphics g) {
        super.paint(g);
        makeHexagon(g);
    }

    /**
     * Makes a hexagon with lines using Graphics2D from swing
     *
     * @param g graphics component
     */
    private void makeHexagon(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(1));
        g2d.draw(new Line2D.Double(0, this.size, this.size, 0));
        g2d.draw(new Line2D.Double(this.size, 0, this.size * 2, 0));
        g2d.draw(new Line2D.Double(this.size * 2, 0, this.size * 3, this.size));
        g2d.draw(new Line2D.Double(this.size * 3, this.size, this.size * 2, this.size * 2));
        g2d.draw(new Line2D.Double(this.size * 2, this.size * 2, this.size, this.size * 2));
        g2d.draw(new Line2D.Double(this.size, this.size * 2, 0, this.size));
    }

    /*
     * checks if the x & y is inside the hexagon
     */
    private boolean insideHexagon(int x, int y) {
        // if point is inside middle rectangle
        if (x > size && x < size * 2 && y > 0 && y < size * 2) {
            return true;
        }

        // points of triangle
        Point[] ps = new Point[]{
                new Point(0, this.size),
                new Point(this.size, 0),
                new Point(this.size, this.size * 2),
                new Point(this.size * 2, 0),
                new Point(this.size * 2, this.size * 2),
                new Point(this.size * 3, this.size),
        };

        Point p = new Point(x, y);
        return insideTriangle(p, ps[0], ps[1], ps[2]) || insideTriangle(p, ps[3], ps[4], ps[5]);
    }

    /*
     * checks if a point is inside a triangle
     * used help of: https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle
     */
    private boolean insideTriangle(Point s, Point a, Point b, Point c)
    {
        int as_x = s.x-a.x;
        int as_y = s.y-a.y;

        boolean s_ab = (b.x-a.x)*as_y-(b.y-a.y)*as_x > 0;

        if((c.x-a.x)*as_y-(c.y-a.y)*as_x > 0 == s_ab) return false;

        if((c.x-b.x)*(s.y-b.y)-(c.y-b.y)*(s.x-b.x) > 0 != s_ab) return false;

        return true;
    }

    // closes the JFrame and application
    private void close() {
        this.setVisible(false);
        dispose();
        System.exit(0);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (this.insideHexagon(x, y)) {
            this.close();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
}
