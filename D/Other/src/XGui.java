
import com.sun.xml.internal.bind.v2.TODO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class XGui extends JFrame {

    Integer size;
    Point point;

    public XGui(Integer size) {
        super("XGui");
        setSize(3 * size, 4 * size); //sets the dimensions of the window to be equal to the input
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //allows the user to exit the window
        this.size = size;
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Integer x = e.getLocationOnScreen().x;
                Integer y = e.getLocationOnScreen().y;
                if (x >= size && x <= size * 2) {
                    if (y >= size * Math.sqrt(2) && y <= size * 2 + size * Math.sqrt(2)) {
                        System.out.println(y);
                    }
                } else {
                    boolean bottom = y >= size * Math.sqrt(2) + size
                        && y <= size*Math.sqrt(2) + size*2;
                    boolean top = y <= size * Math.sqrt(2) + size && y >= size * Math.sqrt(2);
                    if (x > size * 2 && x < size * 3) {
                        if (bottom && validPoint(x, y, Space.BOTTOM_RIGHT, size)) {
                            System.out.println(y);
                        }
                        else if (top && validPoint(x, y, Space.TOP_RIGHT, size)) {
                            System.out.println(y);
                        }
                    }
                    else if (x < size && x > 0) {
                        if (bottom && validPoint(x, y, Space.BOTTOM_LEFT, size)) {
                            System.out.println(y);
                        }
                        else if (top && validPoint(x, y, Space.TOP_LEFT, size)) {
                            System.out.println(y);
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

    }

    /**
     * Paint method required to paint on the JFrame.
     *
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        makeHexagon(g);

    }

    /**
     * Makes a hexagon with lines using Graphics2D from swing
     *
     * @param g
     */
    private void makeHexagon(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(4));
        g2d.draw(new Line2D.Double(0, this.size * 2, this.size, this.size));
        g2d.draw(new Line2D.Double(this.size, this.size, this.size * 2, this.size));
        g2d.draw(new Line2D.Double(this.size * 2, this.size, this.size * 3, this.size * 2));
        g2d.draw(new Line2D.Double(this.size * 3, this.size * 2, this.size * 2, this.size * 3));
        g2d.draw(new Line2D.Double(this.size * 2, this.size * 3, this.size, this.size * 3));
        g2d.draw(new Line2D.Double(this.size, this.size * 3, 0, this.size * 2));
    }

    /**
     * Takes in the arguments for the program and returns either an Integer or null depending on
     * whether the String is an Integer or not.
     *
     * @param args arguments from main method.
     * @return Integer
     */
    public static Integer tryParse(String[] args) {
        if (args.length > 0) {
            try {
                return Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        Integer size = tryParse(args);
        if (size != null && size > 0) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new XGui(size).setVisible(true);
                }
            });
        } else {
            System.out.println("usage: ./xgui positive-integer");
        }
    }
    //TODO Bottom left amd Top Right Detection
    public static boolean validPoint(Integer x, Integer y, Space space, Integer size) {
        if (Space.TOP_RIGHT == space) {
            System.out.println("Top Right");
            System.out.println("X: " + x);
            System.out.println("Y: " + y);
        } else if (Space.TOP_LEFT == space &&
            x + y > size + size*Math.sqrt(2)) {
                return true;
        } else if (Space.BOTTOM_RIGHT == space &&
            (x + y - size*4 < size*Math.sqrt(2))) {
                return true;
        } else if (Space.BOTTOM_LEFT == space) {
            System.out.println("Bottom Left");
            System.out.println("X: " + x);
            System.out.println("Y: " + y);
        }
        return false;
    }

}
