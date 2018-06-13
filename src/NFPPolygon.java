import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by harry on 2018/6/5.
 */
//This class holds the list of NFP of triangulated area, with some properties of this area.
public class NFPPolygon {

    int boundsMinX = Integer.MAX_VALUE;
    int boundsMinY = Integer.MAX_VALUE;
    int boundsMaxX = Integer.MIN_VALUE;
    int boundsMaxY = Integer.MIN_VALUE;

    //the items are triangles
    private List<cVertexList> NFPList = new ArrayList<cVertexList>();
    private Area union = new Area();

    public Area getUnion(){
        return union;
    }

    public void add(cVertexList vl){
        NFPList.add(vl);

        Path2D.Double triangle = new Path2D.Double();
        cVertex cv = vl.head;
        triangle.moveTo(cv.v.x,cv.v.y);

        do {
            boundsMinX = Math.min(boundsMinX, cv.v.x);
            boundsMaxX = Math.max(boundsMaxX, cv.v.x);
            boundsMinY = Math.min(boundsMinY, cv.v.y);
            boundsMaxY = Math.max(boundsMaxY, cv.v.y);
            cv=cv.next;
            triangle.lineTo(cv.v.x,cv.v.y);
        }
        while(cv!=vl.head);
        union.add(new Area(triangle));
    }

//    //need to be used after initialized
//    public cVertexList getBoudingPolygon(){
//        // Note: we're storing double[] and not Point2D.Double
//        ArrayList<double[]> areaPoints = new ArrayList<double[]>();
//        cVertexList boundingPolygong = new cVertexList();
//        double[] coords = new double[6];
//        for (PathIterator pi = union.getPathIterator(null); !pi.isDone(); pi.next()) {
//            // Because the Area is composed of straight lines
//            int type = pi.currentSegment(coords);
//            // We record a double array of {segment type, x coord, y coord}
//            double[] pathIteratorCoords = {type, coords[0], coords[1]};
//            boundingPolygong.InsertBeforeHead(new cVertex((int)coords[0],(int)coords[1]));
//            areaPoints.add(pathIteratorCoords);
//        }
//        return boundingPolygong;
//    }

    public List<cVertexList> getList(){
        return NFPList;
    }

    public Rectangle getBounds() {
        return new Rectangle(boundsMinX, boundsMinY,
                boundsMaxX - boundsMinX,
                boundsMaxY - boundsMinY);
    }



    //for neighborhood searching, calculate the intersection between horizontal line and NFP are.
//    public int getVij(int rx,int ry, int px,int py){
//        //quick check if r is outside the bounding box.
//        if(rx<(px+boundsMinX)|| rx>(px+boundsMaxX)||ry<(py+boundsMinY)||ry>(py+boundsMaxY))
//            return 0;
//
//        boolean inPolygon = false;
//        for(int i=0;i<NFPList.size();i++) {
//            cVertexList triangle = NFPList.get(i);
//            if(triangle.C)
//
//        }
 //   }

}
