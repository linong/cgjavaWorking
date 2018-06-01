import java.awt.*;
import java.util.Random;

/**
 * Created by harry on 2018/5/31.
 */
public abstract class ADrawableObj implements drawableObj {
    protected void drawList(Graphics g, cVertexList list, Color color)
    {
        cVertex v1 = list.head;
        if(list== null || list.head==null||list.head.next==list.head)
            return;
        cVertex v2 = null;


        do {
            v2 = v1.next;
            g.setColor(color);
             g.drawLine(v1.v.x+randomInt(), v1.v.y+randomInt(), v2.v.x+randomInt(), v2.v.y+randomInt());
             g.drawString(v1.v.x+","+v1.v.y,v1.v.x,v1.v.y);

            //g.fillOval(v1.v.x - (int)(w/2), v1.v.y - (int)(h/2), w, h);
            // g.fillOval(v2.v.x - (int)(w/2), v2.v.y - (int)(h/2), w, h);
            v1 = v1.next;
        } while (v1 != list.head.prev);
        g.drawLine(v1.v.x+randomInt(), v1.v.y+randomInt(), v1.next.v.x+randomInt(), v1.next.v.y+randomInt());
    }
    static Random r = new Random(123);

    private static int randomInt(){
        return r.nextInt(2)-1;

    }
}
