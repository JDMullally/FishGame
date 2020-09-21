
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class XGui extends JFrame {

    Integer size;
    Point point;
    public XGui(Integer size) {
        super("XGui");
        setSize(3*size, 2*size); //sets the dimensions of the window to be equal to the input
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //allows the user to exit the window
        this.size = size;
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) { }

            @Override
            public void mouseMoved(MouseEvent e) {
                System.out.println(e.getLocationOnScreen().getClass());
            }
        });

    }

    /**
     * Paint method required to paint on the JFrame.
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        makeHexagon(g);

    }

    /**
     * Makes a hexagon with lines using Graphics2D from swing
     * @param g
     */
    private void makeHexagon(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.RED);
        g2d.draw(new Line2D.Double(0, this.size, this.size, 0));
        g2d.draw(new Line2D.Double(this.size, 0, this.size*2, 0));
        g2d.draw(new Line2D.Double(this.size*2, 0, this.size*3, this.size));
        g2d.draw(new Line2D.Double(this.size*3, this.size, this.size*2, this.size*2));
        g2d.draw(new Line2D.Double(this.size*2, this.size*2, this.size, this.size*2));
        g2d.draw(new Line2D.Double(this.size, this.size*2, 0, this.size));
    }

    /**
     * Takes in the arguments for the program and returns either an Integer or null depending
     * on whether the String is an Integer or not.
     * @param args  arguments from main method.
     * @return Integer
     */
    public static Integer tryParse(String[] args) {
        if(args.length > 0) {
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



}
