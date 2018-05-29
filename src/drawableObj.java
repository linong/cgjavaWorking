import java.awt.*;

/**
 * Created by harry on 2018/5/28.
 */
public interface drawableObj {
    public void drawResult(Graphics g);
    public void initialise(cVertexList first, cVertexList second);
    public boolean start();
}
