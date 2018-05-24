/**
 * Created by harry on 2018/5/23.
 */


import java.awt.*;       // Using AWT's Graphics and Color
        import java.awt.event.*; // Using AWT event classes and listener interfaces
import java.awt.geom.AffineTransform;
import javax.swing.*;    // Using Swing's components and containers

/** Custom Drawing Code Template */
// A Swing application extends javax.swing.JFrame
public class drawingCanvas extends JFrame {
    // Define constants
    public static final int CANVAS_WIDTH  = 800;
    public static final int CANVAS_HEIGHT = 600;

    // Declare an instance of the drawing canvas,
    // which is an inner class called DrawCanvas extending javax.swing.JPanel.
    private DrawCanvas canvas;
    static private MinkConvol mc;

    // Constructor to set up the GUI components and event handlers
    public drawingCanvas() {
        canvas = new DrawCanvas();    // Construct the drawing canvas
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        // Set the Drawing JPanel as the JFrame's content-pane
        Container cp = getContentPane();
        cp.add(canvas);
        // or "setContentPane(canvas);"

        setDefaultCloseOperation(EXIT_ON_CLOSE);   // Handle the CLOSE button
        pack();              // Either pack() the components; or setSize()
        setTitle("......");  // "super" JFrame sets the title
        setVisible(true);    // "super" JFrame show
    }

    protected void paint2D (Graphics2D g2) {
        AffineTransform tform = AffineTransform.getTranslateInstance( CANVAS_WIDTH/2, CANVAS_HEIGHT/2);
        tform.scale( 1, -1);    // NOTE -- to make 1.0 'full width'.
        g2.setTransform( tform);
    }
    /**
     * Define inner class DrawCanvas, which is a JPanel used for custom drawing.
     */
    private class DrawCanvas extends JPanel {
        // Override paintComponent to perform your own painting
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);     // paint parent's background
            paint2D((Graphics2D)g);

            g.drawLine(-CANVAS_WIDTH/2,0,CANVAS_WIDTH/2,0);
            g.drawLine(0,-CANVAS_HEIGHT/2,0,CANVAS_HEIGHT/2);
            mc.drawResult(g);



            //setBackground(Color.BLACK);  // set background color for this JPanel

            // Your custom painting codes. For example,
            // Drawing primitive shapes
//            g.setColor(Color.YELLOW);    // set the drawing color
//            g.drawLine(30, 40, 100, 200);
//            g.drawOval(150, 180, 10, 10);
//            g.drawRect(200, 210, 20, 30);
//            g.setColor(Color.RED);       // change the drawing color
//            g.fillOval(300, 310, 30, 50);
//            g.fillRect(400, 350, 60, 50);
            // Printing texts
            //g.setColor(Color.WHITE);
            //g.setFont(new Font("Monospaced", Font.PLAIN, 12));
            //g.drawString("Testing custom drawing ...", 10, 20);
        }
    }

    // The entry main method
    public static void main(String[] args) {

        mc = new MinkConvol();
        mc.initialise();
        mc.Start(mc.first, mc.second);
        // Run the GUI codes on the Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new drawingCanvas(); // Let the constructor do the job
            }
        });
    }
}
