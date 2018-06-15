/*----------------------------------------------------------------------------
 * Class MinkCovol  -- computes Minkowski Convolution of two polygons
 *
 * the second polygon, which is moved around the first, must be convex
 *---------------------------------------------------------------------------*/
import java.awt.*;

public class MinkConvol extends ADrawableObj {

  private cVertexList P, B;   /* list of vectors, list of the 2nd polygon */
  private cVertexList output; /* list capturing  the enlarged, resulting polygon */ 
  private int m;         /* Total number of points in both polygons */
  private int n;         /* Number of points in primary polygon */
  private int s;         /* Number of points in secondary polygon */
  private int j0;
  private cPointi p0;
  private cPointi last;  /* Holds the last vector difference. */

  private cVertexList first;
  private cVertexList second;
    private static boolean debug = false;

  public static void main(String[] args){
    MinkConvol mc = new MinkConvol();

    mc.first = new cVertexList();
    mc.first.InsertBeforeHead( new cVertex(0,0));
    mc.first.InsertBeforeHead(new cVertex(100,100));
    mc.first.InsertBeforeHead(new cVertex(0,200));
    mc.first.InsertBeforeHead(new cVertex(-100,100));
    mc.first.InsertBeforeHead(new cVertex(20,120));

    mc.second = new cVertexList();
    //mc.second.InsertBeforeHead(new cVertex(0,0));
    mc.second.InsertBeforeHead(new cVertex(20,0));
    mc.second.InsertBeforeHead(new cVertex(40,0));
    mc.second.InsertBeforeHead(new cVertex(40,20));
    mc.second.InsertBeforeHead(new cVertex(20,20));

    //mc.initialise(mc.first,mc.second);
    mc.start();
  }
  //first, second initialized here for calling start
  public  void initialise(cVertexList a, cVertexList b)
  {
      first = a;
      second = b;
  }
  public void drawResult(Graphics g){

    debug("before drawing enlarged polygon, its vertices:");
    output.PrintVertices();

    drawList(g,first,Color.black);
    drawList(g,second,Color.black);
    drawList(g,output,Color.pink);

    debug("the enlarged polygon has been drawn");
  }

  public void test(){

  }
  MinkConvol()
  {
    p0 = new cPointi();
    last = new cPointi();
  }

  public void ClearMinkConvol()
  {
    P.ClearVertexList();
    output.ClearVertexList();
    m = n = s = 0;
    p0.x = p0.y = 0;
  }

  public boolean start()
  {

    cVertexList p = first;
    cVertexList q = second;
    p0.x = p0.y = 0;
    if (!CheckForConvexity(p,q)) {
      debug("Second polygon is  not convex...");
      return false;
    }
    else {
      B = new cVertexList();
      q.ListCopy(B);

      n = p.n;
      s = q.n;
      m = n + m;
      P = new cVertexList();
      cVertex v = p.head;
      do { 
	cVertex t = new cVertex(v.v.x, v.v.y);
	P.InsertBeforeHead(t);
	v = v.next;
      } while (v != p.head);
      j0 = ReadVertices();
      output = new cVertexList();
    }
    Vectorize();
    debug("Before sorting ...");
    PrintPoints();
    Qsort();
    debug("After sorting ... ");
    PrintPoints();
    Convolve();
    return true;
  }

    public cVertexList getMinkConvolResult(){
        return output;
    }
  private boolean CheckForConvexity(cVertexList A, cVertexList B)
  {
    if (A.Ccw() != 1)	
      A.ReverseList();
    if (B.Ccw() != 1)
      B.ReverseList();
      
    if (!B.CheckForConvexity())  /* Second polygon must be convex */
      return false;

    //I believe because we will reflect B with (0.0)
    B.ReverseList();

    return true;
  }

  private int ReadVertices()
  {
    cVertex v = P.head;
    int i = 0;
    do {
      v.vnum = i++;
      v.mark = true;
      v = v.next;
    } while ( v != P.head );


      B.ReverseList();

      v = B.head;
    do {
      cVertex temp = new cVertex(v.v.x, v.v.y);
      P.InsertBeforeHead(temp);
      v = v.next;
    } while ( v != B.head );

    v = P.GetElement(n);    i = 0;
    do {
      /* Reflect secondary polygon */
      v.v.x = - v.v.x;
      v.v.y = - v.v.y;
      v.vnum = i++;
      v.mark = false;
      v = v.next;
    } while ( v != P.head );


   int xmin, ymin, xmax, ymax;     /* Primary min & max */
   int sxmin, symin, sxmax, symax; /* Secondary min & max */
   int mp, ms;   /* i index of max (u-r) primary and secondary points */
   xmin = ymin = xmax = ymax = 0;   
   sxmin = symin = sxmax = symax = 0;
   mp = ms = 0;     v = P.head;
   xmin = xmax = v.v.x;
   ymin = ymax = v.v.y;
   int xwithYMax = v.v.x;
   mp = 0; i = 1; 
   v = v.next;
   cVertex startB = P.GetElement(n);

   do {
     if      ( v.v.x > xmax ) xmax = v.v.x;
     else if ( v.v.x < xmin ) xmin = v.v.x;
     if      ( v.v.y > ymax ) {ymax = v.v.y; mp = i; xwithYMax = v.v.x;}
     else if ( v.v.y == ymax && (v.v.x > xwithYMax) ) { mp = i; xwithYMax = v.v.x; }
     else if ( v.v.y < ymin ) ymin = v.v.y;
     v = v.next; i++;
   } while ( v != startB );
   /*debug("Index of upper rightmost primary, i=mp = "+mp);*/
   v = startB;
   sxmin = sxmax = v.v.x;
   symin = symax = v.v.y;
   int sxwithYMax = v.v.x;
   ms = n; v = v.next; i = 1;
   do {
     if      ( v.v.x > sxmax )
       sxmax = v.v.x;
     else if ( v.v.x < sxmin )
       sxmin = v.v.x;
     if      ( v.v.y > symax )
     {
       symax = v.v.y;
       ms = i+n;
       sxwithYMax = v.v.x;
     }
     else if
             ( v.v.y == symax && (v.v.x > sxwithYMax) )
     {
       ms = i+n ;
       sxwithYMax = v.v.x;
     }
     else if ( v.v.y < symin )
       symin = v.v.y;
     v = v.next; i++;
   } while ( v != P.head );
   /*debug("Index of upper rightmost secondary, i=ms = "+ms);*/

   /* Compute the start point: upper rightmost of both. */
   debug("p0:");
   p0.PrintPoint();
   debug("mp is: "+mp);
   debug("mp element:"+P.GetElement(mp).v.x+","+P.GetElement(mp).v.y);
   AddVec( p0, P.GetElement(mp).v, p0 );
   debug("p0 after addvec:");
   p0.PrintPoint();
   debug("ms is: "+ms);
   debug("ms element:"+P.GetElement(ms).v.x+","+P.GetElement(ms).v.y);
    //This is required, if the reference of the second rectangle is not (0,0), the first point should be changed.
   AddVec( p0, P.GetElement(ms).v, p0 );
   debug("p0 after another addvec:");
   p0.PrintPoint();
   return mp;
  }

  private void   PrintPoints()
  {
    debug("Combined list of points, P: ");
    P.PrintDetailed();
  }

  private void Qsort()
  {
    P.Sort2(0,P.n-1);
    PrintPoints();
    P.ReverseListCompletely();
    debug("list reversed...");
  }

  private void   Vectorize()
  {
    int i;
    cVertex v;
    v = P.head;
    debug("Vectorize: ");
    debug("list before victorization");
    P.PrintVertices();
    cVertex startB = P.GetElement(n);
    debug("startB !!!: ");
    startB.PrintVertex();

    SubVec( P.head.v, startB.prev.v, last);
    do {
      cPointi c = SubVec( v.next.v, v.v);
      debug("("+v.next.v.x+","+v.next.v.y+") - ("+v.v.x+","+v.v.y+")");
      v.v.x = c.x;
      v.v.y = c.y;
      v = v.next;
    } while (v != startB.prev);
    startB.prev.v.x = last.x;
    startB.prev.v.y = last.y;

    SubVec( startB.v, P.head.prev.v,  last);
    v = startB;
    do {
      cPointi c = SubVec( v.next.v, v.v);
      debug("("+v.next.v.x+","+v.next.v.y+") - ("+v.v.x+","+v.v.y+")");
      v.v.x = c.x;
      v.v.y = c.y;
      v = v.next;
    } while ( v != P.head.prev );
    P.head.prev.v.x = last.x;
    P.head.prev.v.y = last.y;
  }
    private void debug(String str){
        if(debug)
        System.out.print(str);

    }
  
  private void    Convolve()
  {
    int i = 0;      /* Index into sorted edge vectors P */
    int j = 0;      /* Primary polygon indices */
    cVertex v = P.head;

    debug("Convolve: start array i = "+i+", primary j0="+j0);
    debug("move to:" + p0.x +"," +p0.y);
    PutInOutput(p0.x,p0.y);
    
    i = 0;  /* start at angle -pi, rightward vector. */
    j = j0; /* start searching for j0. */
    v = P.GetElement(i);
    debug("Convolve, getElement(0)..."+v.v.x+", "+v.v.y);
    do {
      
      /* Advance around secondary edges until next j reached. */
      while ( !(v.mark && v.vnum == j) ){
	if ( !v.mark ) {
	  p0 = AddVec( p0, v.v );
	  PutInOutput(p0.x,p0.y);
      debug("line to:" + p0.x +"," +p0.y);
	}
	v = v.next;
	i = (i+1)%m;
	//	debug("X: i incremented to "+i);
      }
      
      /* Advance one primary edge. */
      debug("X: j="+j+" found at i="+i);
      p0 = AddVec( p0, v.v);
      PutInOutput(p0.x,p0.y);
      debug("line to:" + p0.x +"," +p0.y);
      j = (j+1)%n;
      debug("X: j incremented to "+j);
      
    } while ( j != j0);

   /* Finally, complete circuit on secondary/robot polygon. */
    while (i != 0) {
      if ( !v.mark ) {
	p0 = AddVec (p0, v.v);
	PutInOutput (p0.x, p0.y);
    debug("line to:" + p0.x +"," +p0.y);
      }
      i = (i+1)%m;
    }
    debug("X: i incremented to " + i + " in final circuit");
  }

  private void PutInOutput( int x, int y )
  {
    cVertex v = new cVertex(x, y);
    output.InsertBeforeHead(v);
  }
  
  /*---------------------------------------------------------------------
    a - b ==> c.
    ---------------------------------------------------------------------*/
  private void    SubVec( cPointi a, cPointi b, cPointi c )
  {
    c.x = a.x - b.x;
    c.y = a.y - b.y;
  }

  private cPointi    SubVec( cPointi a, cPointi b )
  {
    cPointi c = new cPointi();
    c.x = a.x - b.x;
    c.y = a.y - b.y;
    return c;
  }

  /*---------------------------------------------------------------------
    a + b ==> c.
    ---------------------------------------------------------------------*/
  private void    AddVec( cPointi a, cPointi b, cPointi c )
  {
    c.x = a.x + b.x;
    c.y = a.y + b.y;
  }

  private cPointi    AddVec( cPointi a, cPointi b )
  {
    cPointi c = new cPointi();
    c.x = a.x + b.x;
    c.y = a.y + b.y;
    return c;
  }

  /* Draws the Minkowski Convolution (e.g. only the enlarged polygon)
   */
  public void DrawMinkConvol(Graphics g, int w, int h)
  {
   // not used
//    debug("before drawing enlarged polygon, its vertices:");
//    output.PrintVertices();
//
//    cVertex v1 = output.head;
//    cVertex v2;
//
//    do {
//      v2 = v1.next;
//      g.setColor(Color.pink);
//      if(P.n >= 2)
//	g.drawLine(v1.v.x, v1.v.y, v2.v.x, v2.v.y);
//      g.fillOval(v1.v.x - (int)(w/2), v1.v.y - (int)(h/2), w, h);
//      g.fillOval(v2.v.x - (int)(w/2), v2.v.y - (int)(h/2), w, h);
//      v1 = v1.next;
//    } while (v1 != output.head.prev);
//    g.drawLine(v1.v.x, v1.v.y, v1.next.v.x, v1.next.v.y);
//    debug("the enlarged polygon has been drawn");
  }


}
