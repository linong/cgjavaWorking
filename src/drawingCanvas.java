/**
 * Created by harry on 2018/5/23.
 */


import java.awt.*;       // Using AWT's Graphics and Color
import java.awt.geom.AffineTransform;
import javax.swing.*;    // Using Swing's components and containers
import java.util.ArrayList;
import java.util.List;

/** Custom Drawing Code Template */
// A Swing application extends javax.swing.JFrame
public class drawingCanvas extends JFrame {
    // Define constants
    public static final int CANVAS_WIDTH  = 800;
    public static final int CANVAS_HEIGHT = 600;

    // Declare an instance of the drawing canvas,
    // which is an inner class called DrawCanvas extending javax.swing.JPanel.
    private DrawCanvas canvas;
    static private List<drawableObj> drawObjList = new ArrayList<drawableObj>();

    // Constructor to set up the GUI components and event handlers
    public drawingCanvas() {
        canvas = new DrawCanvas();    // Construct the drawing canvas
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        // Set the Drawing JPanel as the JFrame's content-pane
        Container cp = getContentPane();
        cp.add(canvas);
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

            for (drawableObj obj:drawObjList
                 ) {
                obj.drawResult(g);
            }
        }
    }

    private static void testTri(){
        System.out.println("test testTri");
        cVertexList list = new cVertexList();
        list.InsertBeforeHead(new cVertex(0,0));
        list.InsertBeforeHead(new cVertex(10,7));
        list.InsertBeforeHead(new cVertex(12,3));
        list.InsertBeforeHead(new cVertex(20,8));
        list.InsertBeforeHead(new cVertex(13,17));
        list.InsertBeforeHead(new cVertex(10,12));
        list.InsertBeforeHead(new cVertex(12,14));
        list.InsertBeforeHead(new cVertex(14,9));
        list.InsertBeforeHead(new cVertex(8,10));
        list.InsertBeforeHead(new cVertex(6,14));
        list.InsertBeforeHead(new cVertex(10,15));
        list.InsertBeforeHead(new cVertex(7,20));
        list.InsertBeforeHead(new cVertex(0,16));
        list.InsertBeforeHead(new cVertex(1,13));
        list.InsertBeforeHead(new cVertex(3,15));
        list.InsertBeforeHead(new cVertex(5,8));
        list.InsertBeforeHead(new cVertex(-2,9));
        list.InsertBeforeHead(new cVertex(5,5));

        cVertex head = list.head;
        do{
            head.multiWith(10);
            head=head.next;
        }
        while(head!=list.head);


        cPolygoni polygoni = new cPolygoni(list);
        polygoni.start();

        drawObjList.add(polygoni);

        cVertexList second = new cVertexList();

        second.InsertBeforeHead(new cVertex(0,0));
        second.InsertBeforeHead(new cVertex(20,0));
        second.InsertBeforeHead(new cVertex(20,20));
        second.InsertBeforeHead(new cVertex(0,20));

        List<cVertexList> triList = polygoni.getTriList();
        for (cVertexList l:triList
             ) {
            MinkConvol mink = new MinkConvol();
            mink.initialise(l,second);
            mink.start();
            drawObjList.add(mink);
        }

    }


    private static void test1()
    {
        MinkConvol mc = new MinkConvol();
        cVertexList first = new cVertexList();
        first.InsertBeforeHead( new cVertex(0,0));
        first.InsertBeforeHead(new cVertex(100,100));
        first.InsertBeforeHead(new cVertex(0,200));
        first.InsertBeforeHead(new cVertex(-100,100));
        //first.InsertBeforeHead(new cVertex(20,120));

        cVertexList second = new cVertexList();

        second.InsertBeforeHead(new cVertex(0,0));
        second.InsertBeforeHead(new cVertex(20,0));
        second.InsertBeforeHead(new cVertex(20,20));
        second.InsertBeforeHead(new cVertex(0,20));
        mc.initialise(first,second);
        mc.start();

    }
    private static void test2()
    {
        MinkConvol mc = new MinkConvol();
        cVertexList first = new cVertexList();
        first.InsertBeforeHead( new cVertex(0,0));
        first.InsertBeforeHead(new cVertex(100,100));
        first.InsertBeforeHead(new cVertex(0,200));
        first.InsertBeforeHead(new cVertex(-100,100));
        first.InsertBeforeHead(new cVertex(20,120));

        cVertexList second = new cVertexList();


        second.InsertBeforeHead(new cVertex(0,0));
        second.InsertBeforeHead(new cVertex(20,0));
        second.InsertBeforeHead(new cVertex(20,20));
        second.InsertBeforeHead(new cVertex(0,20));

        mc.initialise(first,second);
        mc.start();

    }
    public static void main(String[] args) {

        testTri();

        // Run the GUI codes on the Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new drawingCanvas(); // Let the constructor do the job
            }
        });
    }
}
