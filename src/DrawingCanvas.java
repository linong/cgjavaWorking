/**
 * Created by harry on 2018/5/23.
 */


import java.awt.*;       // Using AWT's Graphics and Color
import java.awt.geom.*;
import javax.swing.*;    // Using Swing's components and containers
import java.util.*;
import java.util.List;

/** Custom Drawing Code Template */
// A Swing application extends javax.swing.JFrame
public class DrawingCanvas extends JFrame {
    // Define constants
    public static final int CANVAS_WIDTH  = 1600;
    public static final int CANVAS_HEIGHT = 1200;
    static AffineTransform tform = new AffineTransform();

    private static double scale = 1;



    // Declare an instance of the drawing canvas,
    // which is an inner class called DrawCanvas extending javax.swing.JPanel.
    private DrawCanvas canvas;
    private static List<drawableObj> drawObjList = new ArrayList<drawableObj>();

    private static cVertexList A;
    private static cVertexList B;
    private static cVertexList nl;
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
        Point m_offset;
        drawableVertexListList(List<cVertexList> list,Point offset){
            triList = list;
            m_offset = offset;
        }
        drawableVertexListList(List<cVertexList> list){
            triList = list;
            m_offset = new Point(0,0);
        }
        public void drawResult(Graphics g){
            if(triList!=null){
                for(cVertexList vl:triList){
                    drawList(g,vl,Color.blue,m_offset);
                }

            }
        }
    }
    static class drawableVertexList extends ADrawableObj{

        cVertexList triList;
        Color m_color;
        Point m_offset;
        drawableVertexList(cVertexList list,Color color, Point offset){
            triList = list;
            m_color = color;
            m_offset = offset;
        }
        drawableVertexList(cVertexList list,Color color){
            triList = list;
            m_color = color;
            m_offset = new Point(0,0);
        }
        public void drawResult(Graphics g){
            if(triList!=null){
                    drawList(g,triList,m_color,m_offset);
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

    static Map<cVertexList, List<cVertexList>> triangulateMap = new HashMap<cVertexList, List<cVertexList>>();


    private static List<cVertexList> triangulatePolygon(cVertexList list){

        cPolygoni polygoni = new cPolygoni(list);
        polygoni.start();
        List<cVertexList> triList = polygoni.getTriList();
        triangulateMap.put(list,triList);
        return triList;
    }

    private static NFPPolygon getNFP(cVertexList P, cVertexList R){

        List<cVertexList> triListP = null;
//        if(P.CheckForConvexity()) {
//            triListP = new ArrayList<cVertexList>();
//            triListP.add(P);
//        }
//        else {
            triListP = triangulatePolygon(P);
        //}
        List<cVertexList> triListR = null;
//        if(R.CheckForConvexity()) {
//
//            triListR = new ArrayList<cVertexList>();
//            triListR.add(R);
//        }
//        else
//        {
            triListR = triangulatePolygon(R);
        //}
        //drawableVertexListList drawableP = new drawableVertexListList(triListP);
        //drawableVertexListList drawableR = new drawableVertexListList(triListR);
        //drawObjList.add(drawableP);
        //drawObjList.add(drawableR);

//        List<Color> color_list = new ArrayList<Color>();
//
//        color_list.add(Color.yellow);
//        color_list.add(Color.green);
//        color_list.add(Color.cyan);
//        color_list.add(Color.black);
       //int i = 0;

        //List<Shape> shapes = ....
        //Path2D path = new Path2D.Float();
//        for (Shape shape : shapes) {
//            path.append(shape, false);
//        }
        //Area compound = new Area(path);

        NFPPolygon nfpPolygon = new NFPPolygon();
        return getNFPofTriList(nfpPolygon,triListP,triListR);

    }

    private static NFPPolygon getNFPofTriList(NFPPolygon nfpPolygon,List<cVertexList> triListP, List<cVertexList> triListR) {

        for (cVertexList p:triListP) {
            for(cVertexList r:triListR) {
                MinkConvol mink = new MinkConvol();
                mink.initialise(p, r);
                mink.start();
                cVertexList output = mink.getMinkConvolResult();
                nfpPolygon.add(output);
            }
        }

        return nfpPolygon;

    }

    private static cVertexList initSqureList(){

        cVertexList first = new cVertexList();

        first.InsertBeforeHead(new cVertex(40,10));
        first.InsertBeforeHead(new cVertex(60,0));
        first.InsertBeforeHead(new cVertex(50,40));

        //first.InsertBeforeHead(new cVertex(40,20));
        first.InsertBeforeHead(new cVertex(30,40));
        first.InsertBeforeHead(new cVertex(0,0));
        first = multiPolygon(first,2);
        first = addPolygon(first,0,0);

        return first;
    }

    private static cVertexList initSqureList2(){
        cVertexList list = new cVertexList();

        list.InsertBeforeHead(new cVertex(0,0));
        list.InsertBeforeHead(new cVertex(20,0));
        list.InsertBeforeHead(new cVertex(30,20));
        list.InsertBeforeHead(new cVertex(0,20));
        list = multiPolygon(list,5);
        list = rotatecVertexList(list);
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
        list.InsertBeforeHead(new cVertex(63,0));
        list.InsertBeforeHead(new cVertex(52,8));
        list.InsertBeforeHead(new cVertex(43,19));
        list.InsertBeforeHead(new cVertex(38,31));
        list.InsertBeforeHead(new cVertex(36,44));
        list.InsertBeforeHead(new cVertex(34,44));
        list.InsertBeforeHead(new cVertex(34,241));
        list.InsertBeforeHead(new cVertex(0,251));
        list.InsertBeforeHead(new cVertex(0,572));
        list.InsertBeforeHead(new cVertex(34,582));
        list.InsertBeforeHead(new cVertex(34,779));
        list.InsertBeforeHead(new cVertex(35,779));
        list.InsertBeforeHead(new cVertex(38,792));
        list.InsertBeforeHead(new cVertex(43,804));
        list.InsertBeforeHead(new cVertex(52,815));
        list.InsertBeforeHead(new cVertex(63,823));
        list.InsertBeforeHead(new cVertex(232,823));
        list.InsertBeforeHead(new cVertex(243,815));
        list.InsertBeforeHead(new cVertex(252,804));
        list.InsertBeforeHead(new cVertex(257,792));
        list.InsertBeforeHead(new cVertex(259,779));
        list.InsertBeforeHead(new cVertex(261,779));
        list.InsertBeforeHead(new cVertex(261,582));
        list.InsertBeforeHead(new cVertex(268,589));
        list.InsertBeforeHead(new cVertex(272,667));
        list.InsertBeforeHead(new cVertex(434,667));
        list.InsertBeforeHead(new cVertex(451,601));
        list.InsertBeforeHead(new cVertex(457,596));
        list.InsertBeforeHead(new cVertex(457,582));
        list.InsertBeforeHead(new cVertex(688,582));
        list.InsertBeforeHead(new cVertex(688,596));
        list.InsertBeforeHead(new cVertex(694,601));
        list.InsertBeforeHead(new cVertex(711,667));
        list.InsertBeforeHead(new cVertex(872,667));
        list.InsertBeforeHead(new cVertex(876,589));
        list.InsertBeforeHead(new cVertex(883,582));
        //list.InsertBeforeHead(new cVertex(1200,500));
        list.InsertBeforeHead(new cVertex(883,241));
        list.InsertBeforeHead(new cVertex(876,234));
        list.InsertBeforeHead(new cVertex(872,156));
        list.InsertBeforeHead(new cVertex(711,156));
        list.InsertBeforeHead(new cVertex(694,222));
        list.InsertBeforeHead(new cVertex(688,227));
        list.InsertBeforeHead(new cVertex(688,241));
        list.InsertBeforeHead(new cVertex(457,241));
        list.InsertBeforeHead(new cVertex(457,227));
        list.InsertBeforeHead(new cVertex(451,222));
        list.InsertBeforeHead(new cVertex(434,156));
        list.InsertBeforeHead(new cVertex(272,156));
        list.InsertBeforeHead(new cVertex(268,234));
        list.InsertBeforeHead(new cVertex(261,241));
        list.InsertBeforeHead(new cVertex(261,44));
        list.InsertBeforeHead(new cVertex(259,44));
        list.InsertBeforeHead(new cVertex(257,31));
        list.InsertBeforeHead(new cVertex(252,19));
        list.InsertBeforeHead(new cVertex(243,8));
        list.InsertBeforeHead(new cVertex(232,0));
        list.ReverseList();

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
        cVertexList rotatedList = list.deepCopy();
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
        first.InsertBeforeHead(new cVertex(0,0));
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


    private static  List<Point> getOffsetPointList(NFPPolygon nfp,Filter filter) throws Exception {

        double[] coords = new double[6];

        List<Double[]> pointList = new ArrayList<Double[]>();
        HashSet<Double[]> pointset = new HashSet<Double[]>();

        for (PathIterator pi = nfp.getUnion().getPathIterator(null); !pi.isDone(); pi.next()) {
            // Because the Area is composed of straight lines
            int type = pi.currentSegment(coords);
            Point.Double point = new Point.Double( coords[0],coords[1]);
            if (filter.filt(point)) {
                double distance = Math.pow(point.getX(), 2) + Math.pow(point.getY(), 2);
                Double[] pathIteratorCoords = {distance, coords[0], coords[1]};
                pointList.add(pathIteratorCoords);
            }
        }
        //magic numbers, we only get specific number.
        //// TODO: 2018/6/21 make this configurable.
        if(pointList.size()==0)
            throw new Exception("there is not point");
        int rc = Math.max(3,pointList.size());
        Collections.sort(pointList, new Comparator<Double[]>() {
            @Override
            public int compare(Double[] o1, Double[] o2) {
                if(o1[0]<o2[0])
                    return -1;
                else if(o1[0]>o2[1])
                    return 1;
                else{
                    return 0;
                }
            }
        });


        for(int i=0;i<rc;i++) {
            Double[] d = pointList.get(i);
            pointset.add(d);
        }

        Collections.sort(pointList, new Comparator<Double[]>() {
            @Override
            public int compare(Double[] o1, Double[] o2) {
                Double y1 = Math.abs(o1[2]);
                Double y2 = Math.abs(o2[2]);
                Double x1 = Math.abs(o1[1]);
                Double x2 = Math.abs(o2[1]);
                if(y1<y2)
                    return -1;
                else if(y1>y2)
                    return 1;
                else{
                    if(x1<x2)
                        return -1;
                    else if(x1>x2)
                        return 1;
                    else
                        return 0;
                }
            }
        });

        for(int i=0;i<rc;i++) {
            Double[] d = pointList.get(i);
            pointset.add(d);
        }

        List<Point> resultList = new ArrayList<Point>();
        for(Double[] d : pointset){

            resultList.add(new Point((int)(double)d[1],(int)(double)d[2]));
        }
        return resultList;

    }

    private static Point getOffsetPoint(NFPPolygon nfp,Filter filter)
    {
        ArrayList<double[]> areaPoints = new ArrayList<double[]>();
        double[] coords = new double[6];

        double minx=0, miny=0;
        double min=Integer.MAX_VALUE;

        for (PathIterator pi = nfp.getUnion().getPathIterator(null); !pi.isDone(); pi.next()) {
            // Because the Area is composed of straight lines
            int type = pi.currentSegment(coords);
            // We record a double array of {segment type, x coord, y coord}
            double[] pathIteratorCoords = {type, coords[0], coords[1]};
            areaPoints.add(pathIteratorCoords);
            double[] point = pathIteratorCoords;
            if(filter.filt(new Point.Double(point[1],point[2]))){
                double distance = Math.sqrt(Math.pow(point[1],2)+Math.pow(point[2],2));
                if(min>distance){
                    minx = point[1];
                    miny = point[2];
                    min = distance;
                }
            }
        }
        return new Point((int)minx,(int)miny);


        //draw the polygonAB around the nfp area
//        ArrayList<Line2D.Double > areaSegments = new ArrayList<Line2D.Double>();
//        double[] start = new double[3]; // To record where each polygonAB starts
//        for (int i = 0; i < areaPoints.size(); i++) {
//            // If we're not on the last point, return a line from this point to the next
//            double[] currentElement = areaPoints.get(i);
//
//            // We need a default value in case we've reached the end of the ArrayList
//            double[] nextElement = {-1, -1, -1};
//            if (i < areaPoints.size() - 1) {
//                nextElement = areaPoints.get(i + 1);
//            }
//
//            // Make the lines
//            if (currentElement[0] == PathIterator.SEG_MOVETO) {
//                start = currentElement; // Record where the polygonAB started to close it later
//            }
//
//            if (nextElement[0] == PathIterator.SEG_LINETO) {
//                areaSegments.add(
//                        new Line2D.Double(
//                                currentElement[1], currentElement[2],
//                                nextElement[1], nextElement[2]
//                        )
//                );
//            } else if (nextElement[0] == PathIterator.SEG_CLOSE) {
//                areaSegments.add(
//                        new Line2D.Double(
//                                currentElement[1], currentElement[2],
//                                start[1], start[2]
//                        )
//                );
//            }
//        }
//        cVertexList lines = new cVertexList();
//        for (Line2D.Double line:areaSegments
//             ) {
//            lines.InsertBeforeHead(new cVertex((int)line.getX1(),(int)line.getY1()));
//        }
//
//        drawObjList.add(new drawableVertexList(lines,Color.RED));

    }

    interface Filter{
        boolean filt(Point.Double p);
    }

    static class UpperLeft implements Filter{
        public boolean filt(Point.Double p){
            if(p.x<0 && p.y>0)
                return true;
            else
                return false;
        }
    }
    static class UpperRightX implements Filter{
        public boolean filt(Point.Double p){
            if(p.x>0 && p.y>=0 && p.x>2*p.y)
                return true;
            else
                return false;
        }
    }
    static class BottomLeftY implements Filter{
        public boolean filt(Point.Double p){
            if(p.x<0 && p.y<0 && (-p.y)>(-2*p.x))
                return true;
            else
                return false;
        }
    }

    static class BottomRightY implements Filter{
        public boolean filt(Point.Double p){
            if(p.x>0 && p.y<0 && (-p.y)>2*p.x)
                return true;
            else
                return false;
        }
    }

    static class BottomRightX implements Filter{
        public boolean filt(Point.Double p){
            if(p.x>0 && p.y<0 && p.x>-(2*p.y))
                return true;
            else
                return false;
        }
    }

    static class RightFilter implements Filter{
        public boolean filt(Point.Double p){
            if(p.x>0 && p.x>(2*Math.abs(p.y)))
                return true;
            else
                return false;
        }
    }


    static class Bottom implements Filter{
        public boolean filt(Point.Double p){
            if(p.y<0 && 2*Math.abs(p.x)<-(2*p.y))
                return true;
            else
                return false;
        }
    }
    private static cVertexList movePolygon(cVertexList list, Point point){
        cVertexList listcopy = list.deepCopy(point.x,point.y);
        return listcopy;
    }


    static class TriangulatedPolygonUnion {
        private List<cVertexList> m_triList;
        private Point m_pA;


        public TriangulatedPolygonUnion(){
            m_triList = new ArrayList<cVertexList>();
        }

        public void addTriList(List<cVertexList> newList, Point offset){
            m_pA = (Point)offset.clone();
            for(cVertexList t:newList){
                m_triList.add(t.deepCopy(offset.x,offset.y));
            }
        }

        public Point getpA() {
            return (Point)m_pA.clone();
        }

        public List<cVertexList> getTriList() {
            return m_triList;
        }
    }


    private static void initTest(){

        cVertexList list = initHungryCat1();
        //cVertexList list = initSqureList3();
        //cVertexList list = initSqureList2();
        A = list;

        cVertexList rotatedList = rotatecVertexList(list);
        B = rotatedList;

//        cVertexList temp = B;
//        B=A;
//        A=temp;
        twoD = new HashMap<cVertexList, Map<cVertexList, NFPPolygon>>();

        Map<cVertexList,NFPPolygon> map = new HashMap<cVertexList, NFPPolygon>();
        NFPPolygon nfpPolygon = getNFP(rotatedList,list);
        map.put(list,nfpPolygon);
        nfpPolygon = getNFP(rotatedList,rotatedList);
        map.put(rotatedList,nfpPolygon);

        nl = movePolygon(list,new Point(2000,0));
        nfpPolygon = getNFP(rotatedList,nl);
        map.put(nl,nfpPolygon);

        twoD.put(rotatedList,map);

        map = new HashMap<cVertexList, NFPPolygon>();
        nfpPolygon = getNFP(list,rotatedList);
        map.put(rotatedList,nfpPolygon);
        nfpPolygon = getNFP(list,list);
        map.put(list,nfpPolygon);

        twoD.put(list,map);



    }



    private static List<NestingResult> Stage1(cVertexList A, cVertexList B){
        List<Point> points = null;
        try {
            points = getOffsetPointList(twoD.get(A).get(B), new BottomLeftY());
        }
        catch (Exception ex){
            // TODO: 2018/6/21 find more appropriate means to handle this situation.
        }

        List<NestingResult> resultList = new ArrayList<NestingResult>();

        for(Point point:points){
            NestingResult r = new NestingResult();
            r.A = A;
            r.B = B;
            r.PA = new Point(-point.x,-point.y);
            resultList.add(r);
        }
        return resultList;
    }

    private static List<NestingResult> Stage2(List<NestingResult> stage1List){

        List<NestingResult> resultList = new ArrayList<NestingResult>();

        for(NestingResult r: stage1List){

            TriangulatedPolygonUnion triangulatedPolygonUnionBA = new TriangulatedPolygonUnion();
            triangulatedPolygonUnionBA.addTriList(triangulateMap.get(A),new Point(r.PA.x,r.PA.y));
            triangulatedPolygonUnionBA.addTriList(triangulateMap.get(B),new Point(0,0));

            NFPPolygon npBA = new NFPPolygon();
            npBA = getNFPofTriList(npBA,triangulatedPolygonUnionBA.getTriList(),triangulatedPolygonUnionBA.getTriList());
            Point pBA = getOffsetPoint(npBA,new UpperRightX());
            NestingResult copy = r.copyOf();
            copy.setPBA(pBA);
            resultList.add(copy);
        }

        return resultList;
    }

    private static List<NestingResult> Stage3(List<NestingResult> stage2List){
        List<NestingResult> resultList = new ArrayList<NestingResult>();

        for(NestingResult r: stage2List) {

            Point pBA = r.getPBA();

            TriangulatedPolygonUnion triangulatedPolygonUnionBB = new TriangulatedPolygonUnion();
            triangulatedPolygonUnionBB.addTriList(triangulateMap.get(B),pBA);
            triangulatedPolygonUnionBB.addTriList(triangulateMap.get(B),new Point(0,0));

            TriangulatedPolygonUnion triangulatedPolygonUnionAA = new TriangulatedPolygonUnion();
            triangulatedPolygonUnionAA.addTriList(triangulateMap.get(A),pBA);
            triangulatedPolygonUnionAA.addTriList(triangulateMap.get(A),new Point(0,0));

            NFPPolygon npBBAA = new NFPPolygon();
            npBBAA = getNFPofTriList(npBBAA,triangulatedPolygonUnionBB.getTriList(),triangulatedPolygonUnionAA.getTriList());
            Point pBBAA = getOffsetPoint(npBBAA,new BottomRightY());
            NestingResult copy = r.copyOf();
            copy.setPBBAA(pBBAA);

            Rectangle rA = A.getBoundingBox();
            Rectangle rB = B.getBoundingBox();

            int AreaAB = rA.height*rA.width + rB.height*rB.width;

            int AreaNested1 = pBA.x*(r.getPA().x)- r.getPA().y*pBA.y;
            int AreaNested2 = pBBAA.x*pBA.y-pBA.x*pBBAA.y;

            int firstArea = rB.width*(r.getPA().y);

            //double firstratio = (firstArea+ rA.height*rA.width)/(double)AreaAB;
            double ratio = (AreaNested1+AreaNested2)/(double)AreaAB;

            copy.set_ratio(ratio);

            resultList.add(copy);
        }

        return resultList;
    }

    private static void drawNestingResult(NestingResult r,Point offset){
        List<cVertexList> pList = new ArrayList<cVertexList>();
        cVertexList A1 = movePolygon(r.getA(),r.PA);
        cVertexList C = movePolygon(r.getA(),r.getPBBAA());
        cVertexList C1 = movePolygon(C,r.getPBA());

        cVertexList B1 = movePolygon(B,r.getPBA());
        cVertexList A2 = movePolygon(A1,r.getPBA());

        pList.add(A1);
        pList.add(B);
        pList.add(C);
        pList.add(C1);
        pList.add(B1);
        pList.add(A2);

        for (cVertexList p: pList ) {
            drawObjList.add(new drawableVertexList(p, Color.black,offset));
        }


    }



    private static NestingResult strategy(Filter f1,Filter f2,Filter f3,Point offset){
        System.out.println("strategy:"+ f1.toString()+","+f2.toString()+","+f3.toString());
        long starttime = System.currentTimeMillis();

        List<cVertexList> pList = new ArrayList<cVertexList>();
        Point point = getOffsetPoint(twoD.get(A).get(B), f1);
        TriangulatedPolygonUnion triangulatedPolygonUnionBA = new TriangulatedPolygonUnion();
        triangulatedPolygonUnionBA.addTriList(triangulateMap.get(A),new Point(-point.x,-point.y));
        triangulatedPolygonUnionBA.addTriList(triangulateMap.get(B),new Point(0,0));

        NFPPolygon npBA = new NFPPolygon();
        npBA = getNFPofTriList(npBA,triangulatedPolygonUnionBA.getTriList(),triangulatedPolygonUnionBA.getTriList());
        //only move the triangles of A,B

        //move A to the opposite position of B, as we move B to original.
        cVertexList A1 = movePolygon(A,new Point(-point.x,-point.y));

        //point from nfp of union BA.
        Point pBA = getOffsetPoint(npBA,f2);

        TriangulatedPolygonUnion triangulatedPolygonUnionBB = new TriangulatedPolygonUnion();
        triangulatedPolygonUnionBB.addTriList(triangulateMap.get(B),pBA);
        triangulatedPolygonUnionBB.addTriList(triangulateMap.get(B),new Point(0,0));

        TriangulatedPolygonUnion triangulatedPolygonUnionAA = new TriangulatedPolygonUnion();
        triangulatedPolygonUnionAA.addTriList(triangulateMap.get(A),pBA);
        triangulatedPolygonUnionAA.addTriList(triangulateMap.get(A),new Point(0,0));

        NFPPolygon npBBAA = new NFPPolygon();
        npBBAA = getNFPofTriList(npBBAA,triangulatedPolygonUnionBB.getTriList(),triangulatedPolygonUnionAA.getTriList());
        Point pBBAA = getOffsetPoint(npBBAA,f3);

        cVertexList C = movePolygon(A,pBBAA);
        cVertexList C1 = movePolygon(C,pBA);

        pList.add(C);
        pList.add(C1);
        cVertexList B1 = movePolygon(B,pBA);

        pList.add(B);
        pList.add(B1);

        cVertexList A2 = movePolygon(A1,pBA);
        //cVertexList A3 = movePolygon(A2,pBA);
        pList.add(A1);
        pList.add(A2);
        //pList.add(A3);
        //drawObjList.add(new DrawableArea(polygonAB.getUnion()));

        System.out.println("execution time:"+ (System.currentTimeMillis()-starttime));

        Rectangle rA = A.getBoundingBox();
        Rectangle rB = B.getBoundingBox();

        int AreaAB = rA.height*rA.width + rB.height*rB.width;

        int AreaNested1 = pBA.x*(-point.y)-(-point.x)*pBA.y;
        int AreaNested2 = pBBAA.x*pBA.y-pBA.x*pBBAA.y;

        int firstArea = rB.width*(-point.y);

        double firstratio = (firstArea+ rA.height*rA.width)/(double)AreaAB;
        double ratio = (AreaNested1+AreaNested2)/(double)AreaAB;
        System.out.println("point:"+point.x+","+point.y);
        System.out.println("first compress ratio is:" + firstratio);

        System.out.println("compress ratio is:" + ratio);

        for (cVertexList p: pList ) {
            drawObjList.add(new drawableVertexList(p, Color.black,offset));
        }

        NestingResult r = new NestingResult(A,B,new Point(-point.x,-point.y),pBA,pBBAA,ratio);
        return r;
    }

    private static class NestingResult{
        private cVertexList A;
        private cVertexList B;
        private Point PA;
        private Point PBA;
        private Point PBBAA;
        private double m_ratio;

        public NestingResult(cVertexList a,cVertexList b, Point pa, Point pba, Point pbbaa, double ratio){
            A=a;
            B=b;
            PA = pa;
            PBA = pba;
            PBBAA = pbbaa;
            m_ratio = ratio;
        }

        public NestingResult(){

        }

        public NestingResult copyOf(){
            NestingResult copy = new NestingResult();
            copy.setA(this.getA());
            copy.setB(this.getB());
            copy.setPA(this.getPA());
            copy.setPBA(this.getPBA());
            copy.setPBBAA(this.getPBBAA());
            copy.set_ratio(this.getRatio());

            return copy;
        }

        public cVertexList getA() {
            return A;
        }

        public cVertexList getB() {
            return B;
        }

        public Point getPA() {
            return PA;
        }

        public Point getPBA() {
            return PBA;
        }

        public Point getPBBAA() {
            return PBBAA;
        }

        public double getRatio() {
            return m_ratio;
        }

        public void setA(cVertexList a) {
            A = a;
        }

        public void setB(cVertexList b) {
            B = b;
        }

        public void setPA(Point PA) {
            this.PA = PA;
        }

        public void setPBA(Point PBA) {
            this.PBA = PBA;
        }

        public void setPBBAA(Point PBBAA) {
            this.PBBAA = PBBAA;
        }

        public void set_ratio(double m_ratio) {
            this.m_ratio = m_ratio;
        }
    }


    private static void testInThreeStage(){

        System.out.println("test testInThreeStage");
        scale = 0.3;
        tform.setToTranslation(200,200);
        tform.scale( scale, -scale);    // NOTE -- to make 1.0 'full width'.
        initTest();


        List<NestingResult> results = Stage1(A, B);
        results = Stage2(results);
        results = Stage3(results);

        int step = 1000;
        int count=0;
        for(NestingResult result: results){


            Point offset = new Point((count%5)*step,count/5*step);
            drawNestingResult(result,offset);
            count++;
            System.out.println("ratio: "+ result.getRatio());
        }



    }
    private static void testTri(){
        System.out.println("test testTri");
        scale = 0.3;
        tform.setToTranslation(200,200);
        tform.scale( scale, -scale);    // NOTE -- to make 1.0 'full width'.
        initTest();

        int step = 1000;

        int i = 0;

       // PriorityQueue

        strategy(new BottomLeftY(),new UpperRightX(),new BottomRightY(),new Point((i++)*step,0));
        strategy(new BottomLeftY(),new BottomRightX(),new BottomRightY(),new Point((i++)*step,0));
        strategy(new BottomLeftY(),new BottomRightX(),new BottomLeftY(),new Point((i++)*step,0));
        strategy(new Bottom(),new UpperRightX(),new BottomLeftY(),new Point((i++)*step,0));

        //examine how NFP would move if we move P or R
//        NFPPolygon polygonAB = twoD.get(B).get(A);
//        NFPPolygon polygonnl = twoD.get(B).get(nl);
//        drawObjList.add(new DrawableArea(polygonAB.getUnion()));
//        drawObjList.add(new DrawableArea(polygonnl.getUnion()));

        //strategy(new BottomRightY(),new UpperRightX(),new BottomRightY(),new Point(2500,0));

        //strategy(new BottomLeftY(),new UpperRightX(),new BottomRightY(),new Point(5000,0));

//        long starttime = System.currentTimeMillis();
//
//        NFPPolygon polygonAB = twoD.get(A).get(B);
//        List<cVertexList> pList = new ArrayList<cVertexList>();
//        Point point = getOffsetPoint(twoD.get(A).get(B),new BottomLeftY());
//        TriangulatedPolygonUnion triangulatedPolygonUnionBA = new TriangulatedPolygonUnion();
//        triangulatedPolygonUnionBA.addTriList(triangulateMap.get(A),new Point(-point.x,-point.y));
//        triangulatedPolygonUnionBA.addTriList(triangulateMap.get(B),new Point(0,0));
//
//        NFPPolygon npBA = new NFPPolygon();
//        npBA = getNFPofTriList(npBA,triangulatedPolygonUnionBA.getTriList(),triangulatedPolygonUnionBA.getTriList());
//        //only move the triangles of A,B
//
//        //move A to the opposite position of B, as we move B to original.
//        cVertexList A1 = movePolygon(A,new Point(-point.x,-point.y));
//
//        //point from nfp of union BA.
//        Point pBA = getOffsetPoint(npBA,new UpperRightX());
//
//        TriangulatedPolygonUnion triangulatedPolygonUnionBB = new TriangulatedPolygonUnion();
//        triangulatedPolygonUnionBB.addTriList(triangulateMap.get(B),pBA);
//        triangulatedPolygonUnionBB.addTriList(triangulateMap.get(B),new Point(0,0));
//
//        TriangulatedPolygonUnion triangulatedPolygonUnionAA = new TriangulatedPolygonUnion();
//        triangulatedPolygonUnionAA.addTriList(triangulateMap.get(A),pBA);
//        triangulatedPolygonUnionAA.addTriList(triangulateMap.get(A),new Point(0,0));
//
//        NFPPolygon npBBAA = new NFPPolygon();
//        npBBAA = getNFPofTriList(npBBAA,triangulatedPolygonUnionBB.getTriList(),triangulatedPolygonUnionAA.getTriList());
//        Point pBBAA = getOffsetPoint(npBBAA,new BottomRightY());
//
//        cVertexList C = movePolygon(A,pBBAA);
//        cVertexList C1 = movePolygon(C,pBA);
//
//        pList.add(C);
//        pList.add(C1);
//        cVertexList B1 = movePolygon(B,pBA);
//
//        pList.add(B);
//        pList.add(B1);
//
//        cVertexList A2 = movePolygon(A1,pBA);
//        //cVertexList A3 = movePolygon(A2,pBA);
//        pList.add(A1);
//        pList.add(A2);
//        //pList.add(A3);
//        drawObjList.add(new DrawableArea(polygonAB.getUnion()));
//
//        for (cVertexList p: pList ) {
//            drawObjList.add(new drawableVertexList(p, Color.black));
//        }
//
//
//
//        System.out.println("execution time:"+ (System.currentTimeMillis()-starttime));
//
//        Rectangle rA = A.getBoundingBox();
//        Rectangle rB = B.getBoundingBox();
//
//        int AreaAB = rA.height*rA.width + rB.height*rB.width;
//
//        int AreaNested1 = pBA.x*(-point.y)-(-point.x)*pBA.y;
//        int AreaNested2 = pBBAA.x*pBA.y-pBA.x*pBBAA.y;
//        double ratio = (AreaNested1+AreaNested2)/(double)AreaAB;
//
//        System.out.println("compress ratio is:" + ratio);


    }
    public static void main(String[] args) {

        //testTri();
        testInThreeStage();
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
