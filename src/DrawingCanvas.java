/**
 * Created by harry on 2018/5/23.
 */


import java.awt.*;       // Using AWT's Graphics and Color
import java.awt.geom.*;
import javax.swing.*;    // Using Swing's components and containers
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Custom Drawing Code Template */
// A Swing application extends javax.swing.JFrame
public class DrawingCanvas extends JFrame {
    // Define constants
    public static final int CANVAS_WIDTH  = 1600;
    public static final int CANVAS_HEIGHT = 1200;
    AffineTransform tform = new AffineTransform();

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

        tform.setToTranslation(CANVAS_WIDTH/2,CANVAS_HEIGHT/2);
        tform.scale( 0.8, -0.8);    // NOTE -- to make 1.0 'full width'.
    }

    protected void paint2D (Graphics2D g2) {
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
            g.drawLine(100,100,200,200);


            for (drawableObj obj:drawObjList
                 ) {
                obj.drawResult(g);
            }

            g.drawLine(-CANVAS_WIDTH/2,0,CANVAS_WIDTH/2,0);
            g.drawLine(0,-CANVAS_HEIGHT/2,0,CANVAS_HEIGHT/2);
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
    static class DrawableArea extends ADrawableObj{
        Area m_area;
        DrawableArea(Area area){
            m_area=area;
        }
        public void drawResult(Graphics g){
            if(m_area!=null){
                Graphics2D g2d = (Graphics2D) g;
                g.setColor(Color.lightGray);
                g2d.fill(m_area);
            }
        }

    }

    static class drawablePolygon extends ADrawableObj{

        Polygon m_p;

        drawablePolygon(Polygon polygon){
            m_p = polygon;
        }
        public void drawResult(Graphics g){
            if(m_p!=null){
                g.drawPolygon(m_p);


            }
        }
    }

    private static List<cVertexList> triangulatePolygon(cVertexList list){

        cPolygoni polygoni = new cPolygoni(list);
        polygoni.start();
        List<cVertexList> triList = polygoni.getTriList();
        return triList;
    }

    private static NFPPolygon getNFP(cVertexList P, cVertexList R){

        List<cVertexList> triListP = null;
        if(P.CheckForConvexity()) {
            triListP = new ArrayList<cVertexList>();
            triListP.add(P);
        }
        else {
            triListP = triangulatePolygon(P);
        }
        List<cVertexList> triListR = null;
        if(R.CheckForConvexity()) {

            triListR = new ArrayList<cVertexList>();
            triListR.add(R);
        }
        else
        {
            triListR = triangulatePolygon(R);
        }
        drawableVertexListList drawableP = new drawableVertexListList(triListP);
        drawableVertexListList drawableR = new drawableVertexListList(triListR);
        //drawObjList.add(drawableP);
        //drawObjList.add(drawableR);

        List<Color> color_list = new ArrayList<Color>();

        color_list.add(Color.yellow);
        color_list.add(Color.green);
        color_list.add(Color.cyan);
        color_list.add(Color.black);
        int i = 0;

        //List<Shape> shapes = ....
        Path2D path = new Path2D.Float();
//        for (Shape shape : shapes) {
//            path.append(shape, false);
//        }
        //Area compound = new Area(path);

        NFPPolygon nfpPolygon = new NFPPolygon();

        for (cVertexList p:triListP) {
            for(cVertexList r:triListR) {
                MinkConvol mink = new MinkConvol();
                mink.initialise(p, r);
                mink.start();
                cVertexList output = mink.getMinkConvolResult();
                nfpPolygon.add(output);
                //drawObjList.add(new drawableVertexList(output,color_list.get(i)));
                i=(++i)%3;
                //drawObjList.add(mink);
            }
        }
        return nfpPolygon;
    }

    private static cVertexList initSqureList(){

        cVertexList first = new cVertexList();
        first.InsertBeforeHead( new cVertex(0,0));
        first.InsertBeforeHead(new cVertex(30,40));
        first.InsertBeforeHead(new cVertex(40,20));
        first.InsertBeforeHead(new cVertex(50,40));

        first.InsertBeforeHead(new cVertex(60,0));
        first.InsertBeforeHead(new cVertex(40,10));
        first = multiPolygon(first,2);
        first = addPolygon(first,0,0);
        first.ReverseList();
        return first;
    }

    private static cVertexList initSqureList2(){
        cVertexList list = new cVertexList();

        list.InsertBeforeHead(new cVertex(0,0));
        list.InsertBeforeHead(new cVertex(20,0));
        list.InsertBeforeHead(new cVertex(20,20));
        list.InsertBeforeHead(new cVertex(0,20));
        return list;
    }

    private static  cVertexList multiPolygon(cVertexList list,double i){
        cVertex head = list.head;
        do{
            head.v.x*=i;
            head.v.y*=i;
            head=head.next;
        }
        while(head!=list.head);
        return list;
    }
    private static  cVertexList addPolygon(cVertexList list,int x,int y){
        cVertex head = list.head;
        do{
            head.v.x+=x;
            head.v.y+=y;
            head=head.next;
        }
        while(head!=list.head);
        return list;
    }

    private static cVertexList initSqureList3(){
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
        list.InsertBeforeHead(new cVertex(7,15));

        list.InsertBeforeHead(new cVertex(0,16));
        list.InsertBeforeHead(new cVertex(1,13));
        list.InsertBeforeHead(new cVertex(3,15));
        list.InsertBeforeHead(new cVertex(5,8));
        list.InsertBeforeHead(new cVertex(-2,9));
        list.InsertBeforeHead(new cVertex(5,5));

        list = multiPolygon(list,10);
        return list;
    }

    private static cVertexList initHungryCat(){
        cVertexList list = new cVertexList();

        list.InsertBeforeHead(new cVertex(0,0));
        list.InsertBeforeHead(new cVertex(20,20));
        list.InsertBeforeHead(new cVertex(60,100));
        list.InsertBeforeHead(new cVertex(22,100));



//        first.InsertBeforeHead( new cVertex(0,0));
//        first.InsertBeforeHead(new cVertex(100,100));
//        first.InsertBeforeHead(new cVertex(0,200));
//        first.InsertBeforeHead(new cVertex(-100,100));


        //list = multiPolygon(list,2);


        return list;
    }

    private static cVertexList initHungryCat1(){
        cVertexList list = new cVertexList();
        list.InsertBeforeHead(new cVertex(22,290));
        list.InsertBeforeHead(new cVertex(81,290));
        list.InsertBeforeHead(new cVertex(91,274));
        list.InsertBeforeHead(new cVertex(95,235));
        list.InsertBeforeHead(new cVertex(153,235));
        list.InsertBeforeHead(new cVertex(162,204));
        list.InsertBeforeHead(new cVertex(241,204));
        list.InsertBeforeHead(new cVertex(250,265));
        list.InsertBeforeHead(new cVertex(307,265));
        list.InsertBeforeHead(new cVertex(311,204));
        //list.InsertBeforeHead(new cVertex(311,85));
        //list.InsertBeforeHead(new cVertex(307,55));
        //list.InsertBeforeHead(new cVertex(250,55));
        list.InsertBeforeHead(new cVertex(241,85));
        list.InsertBeforeHead(new cVertex(162,85));
        list.InsertBeforeHead(new cVertex(153,55));

        list.InsertBeforeHead(new cVertex(95,55));
        list.InsertBeforeHead(new cVertex(91,15));
        list.InsertBeforeHead(new cVertex(81,0));
        list.InsertBeforeHead(new cVertex(22,0));
        list.InsertBeforeHead(new cVertex(11,15));
        list.InsertBeforeHead(new cVertex(11,85));

        list.InsertBeforeHead(new cVertex(0,88));
        list.InsertBeforeHead(new cVertex(0,201));
        list.InsertBeforeHead(new cVertex(11,204));
        list.InsertBeforeHead(new cVertex(11,274));

        list.ReverseList();
        //list = multiPolygon(list,0.8);

        return list;
    }

    private static cVertexList rotatecVertexList(cVertexList list) {
        cVertexList rotatedList = new cVertexList();
        list.deepCopy(rotatedList);

        int minx= Integer.MAX_VALUE;
        int miny= Integer.MAX_VALUE;
        int maxx= Integer.MIN_VALUE;
        int maxy= Integer.MIN_VALUE;
        cVertex v = rotatedList.head;
        do{
            minx = Math.min(minx,v.v.x);
            miny = Math.min(miny,v.v.y);
            maxx = Math.max(maxx,v.v.x);
            maxy = Math.max(maxy,v.v.y);
            v=v.next;
        }while(v!=rotatedList.head);

        AffineTransform rotate180 = new AffineTransform();

        rotate180.setToTranslation(minx,miny);
        rotate180.translate(maxx-minx,maxy-miny);
        rotate180.rotate(Math.PI);

        do{

            Point2D point = new Point2D.Double(v.v.x,v.v.y);
            Point2D np = rotate180.transform(point,new Point2D.Double(0,0));
            v.v.x = (int)np.getX();
            v.v.y = (int)np.getY();
            v=v.next;


        }while(v!=rotatedList.head);

        return rotatedList;
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


    private NFPPolygon getHashedNFP(cVertexList R, cVertexList P){
        return twoD.get(R).get(P);
    }

    static Map<cVertexList,Map<cVertexList,NFPPolygon>> twoD;


    private static Point getOffsetPoint(ArrayList<double[]> areaPoints,Filter filter){

        double minx=0, miny=0;
        double min=Integer.MAX_VALUE;
        for(int i=0;i<areaPoints.size();i++){
            double[] point = areaPoints.get(i);
            if(filter.filt(new Point((int)point[1],(int)point[2]))){
                double distance = Math.sqrt(Math.pow(point[1],2)+Math.pow(point[2],2));
                if(min>distance){
                    minx = point[1];
                    miny = point[2];
                    min = distance;
                }
            }
        }
        return new Point((int)minx,(int)miny);


    }
    interface Filter{
        boolean filt(Point p);
    }

    static class UpperLeft implements Filter{
        public boolean filt(Point p){
            if(p.x<0 && p.y>0)
                return true;
            else
                return false;
        }
    }
    static class UpperRight implements Filter{
        public boolean filt(Point p){
            if(p.x>0 && p.y>0)
                return true;
            else
                return false;
        }
    }
    static class BottomLeft implements Filter{
        public boolean filt(Point p){
            if(p.x<0 && p.y<0)
                return true;
            else
                return false;
        }
    }
    static class BottomRight implements Filter{
        public boolean filt(Point p){
            if(p.x>0 && p.y<0)
                return true;
            else
                return false;
        }
    }


    private static cVertexList movePolygon(cVertexList list, Point point){
        cVertexList listcopy = new cVertexList();
        list.deepCopy(listcopy);
        cVertexList polygonBottomLeft = addPolygon(listcopy,point.x,point.y);
        return listcopy;
    }


    private static void testTri(){
        System.out.println("test testTri");
        //cVertexList list = initSqureList();
        cVertexList list = initHungryCat1();
        cVertexList rotatedlist = rotatecVertexList(list);
        cVertexList second =initSqureList2();

        NFPPolygon nfpPolygon = getNFP(rotatedlist,list);
        Area area = nfpPolygon.getUnion();
        drawObjList.add(new DrawableArea(nfpPolygon.getUnion()));


        // Note: we're storing double[] and not Point2D.Double
        ArrayList<double[]> areaPoints = new ArrayList<double[]>();
        double[] coords = new double[6];

        for (PathIterator pi = area.getPathIterator(null); !pi.isDone(); pi.next()) {

            // Because the Area is composed of straight lines
            int type = pi.currentSegment(coords);
            // We record a double array of {segment type, x coord, y coord}
            double[] pathIteratorCoords = {type, coords[0], coords[1]};
            areaPoints.add(pathIteratorCoords);
        }


        List<cVertexList> pList = new ArrayList<cVertexList>();
        Rectangle2D b = area.getBounds2D();


        pList.add(movePolygon(list,new Point((int)b.getMaxX(),0)));
        pList.add(movePolygon(list,new Point((int)b.getMinX(),0)));

//
        Point point = getOffsetPoint(areaPoints,new BottomLeft());
//        cVertexList polygonBL = movePolygon(list,point);
//        pList.add(polygonBL);

        point = getOffsetPoint(areaPoints,new BottomRight());
        cVertexList polygonBR = movePolygon(list,point);
        pList.add(polygonBR);

        point = getOffsetPoint(areaPoints,new UpperRight());
        cVertexList polygonUR = movePolygon(list,point);
        pList.add(polygonUR);

        pList.add(list);
        for (cVertexList p: pList ) {
            drawObjList.add(new drawableVertexList(p, Color.black));
        }
//        drawObjList.add(new drawableVertexList(list, Color.black));
//        drawObjList.add(new drawableVertexList(polygonBL, Color.black));
//        drawObjList.add(new drawableVertexList(polygonBR, Color.black));
//        drawObjList.add(new drawableVertexList(polygonUR, Color.black));


//        twoD = new HashMap<cVertexList, Map<cVertexList, NFPPolygon>>();
//
//        Map<cVertexList,NFPPolygon> map = new HashMap<cVertexList, NFPPolygon>();
//        map.put(list,nfpPolygon);
//        twoD.put(second,map);




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

        testTri();
        //test1();

        // Run the GUI codes on the Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DrawingCanvas(); // Let the constructor do the job
            }
        });
    }
}
