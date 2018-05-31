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
public class DrawingCanvas extends JFrame {
    // Define constants
    public static final int CANVAS_WIDTH  = 1800;
    public static final int CANVAS_HEIGHT = 1600;

    

    // Declare an instance of the drawing canvas,
    // which is an inner class called DrawCanvas extending javax.swing.JPanel.
    private DrawCanvas canvas;
    static private List<drawableObj> drawObjList = new ArrayList<drawableObj>();

    // Constructor to set up the GUI components and event handlers
    public DrawingCanvas() {
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
        AffineTransform tform = AffineTransform.getTranslateInstance( CANVAS_WIDTH/4, CANVAS_HEIGHT/4);
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



    static class drawableVertexListList extends ADrawableObj{

        List<cVertexList> triList;
        drawableVertexListList(List<cVertexList> list){
            triList = list;
        }
        public void drawResult(Graphics g){
            if(triList!=null){
                for(cVertexList vl:triList){
                    drawList(g,vl,Color.blue);
                }

            }
        }
    }

    static class drawableVertexList extends ADrawableObj{

        cVertexList triList;
        Color m_color;
        drawableVertexList(cVertexList list,Color color){
            triList = list;
            m_color = color;
        }
        public void drawResult(Graphics g){
            if(triList!=null){
                    drawList(g,triList,m_color);
            }
        }
    }

    private static List<cVertexList> triangulatePolygon(cVertexList list){
        cPolygoni polygoni = new cPolygoni(list);
        polygoni.start();
        List<cVertexList> triList = polygoni.getTriList();
        return triList;
    }

    private static void  getNFP(cVertexList P, cVertexList R){

        List<cVertexList> triListP = triangulatePolygon(P);
        drawableVertexListList drawableP = new drawableVertexListList(triListP);
        drawObjList.add(drawableP);
        List<cVertexList> triListR = triangulatePolygon(R);
        drawableVertexListList drawableR = new drawableVertexListList(triListR);
        drawObjList.add(drawableR);



        List<Color> color_list = new ArrayList<Color>();

        color_list.add(Color.yellow);
        color_list.add(Color.green);
        color_list.add(Color.cyan);
        color_list.add(Color.black);
        int i = 0;
        //for (cVertexList p:triListP) {
            //for(cVertexList r:triListR) {
                MinkConvol mink = new MinkConvol();
                mink.initialise(P, R);
                mink.start();
                cVertexList output = mink.getMinkConvolResult();

                drawObjList.add(new drawableVertexList(output,color_list.get(i)));
                i=(++i)%3;
                //drawObjList.add(mink);
            //}
        //}

    }

    private static cVertexList initSqureList(){
        cVertexList list = new cVertexList();
        list.InsertBeforeHead(new cVertex(0,0));
        list.InsertBeforeHead(new cVertex(30,0));
        list.InsertBeforeHead(new cVertex(30,30));
        list.InsertBeforeHead(new cVertex(0,30));
        cVertex head = list.head;
        do{
            head.multiWith(10);
            head=head.next;
        }
        while(head!=list.head);
        return list;
    }

    private static cVertexList initSqureList2(){
        cVertexList list = new cVertexList();
        //list.InsertBeforeHead(new cVertex(1,1));
        list.InsertBeforeHead(new cVertex(20,0));

        list.InsertBeforeHead(new cVertex(20,20));
        list.InsertBeforeHead(new cVertex(0,20));


        return list;
    }


    private static void test1()    {
        MinkConvol mc = new MinkConvol();

        mc.initialise(initSqureList(),initSqureList2());
        mc.start();
        drawObjList.add(mc);

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
        //second.InsertBeforeHead(new cVertex(20,20));
        second.InsertBeforeHead(new cVertex(0,20));

        mc.initialise(first,second);
        mc.start();
        drawObjList.add(mc);

    }

    private static void testTri(){
        System.out.println("test testTri");



        cVertexList list = initSqureList();
        cVertexList second =initSqureList2();

        getNFP(list,second);

        //List<cVertexList> triList = polygoni.getTriList();
//        for (cVertexList l:triList
//             ) {
//            MinkConvol mink = new MinkConvol();
//            mink.initialise(l,second);
//            mink.start();
//            drawObjList.add(mink);
//        }

    }
    public static void main(String[] args) {

        //testTri();
        test1();

        // Run the GUI codes on the Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DrawingCanvas(); // Let the constructor do the job
            }
        });
    }
}
