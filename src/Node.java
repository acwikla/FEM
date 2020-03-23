import java.io.FileNotFoundException;

public class Node {
    double x,y;
    boolean border_flag;
    int t, nodeID = 0;
    static GlobalData gData;

    static {
        try {
            gData = new GlobalData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public Node () throws FileNotFoundException {}

    public Node(double x, double y, int id) throws FileNotFoundException {
        this.x = x;
        this.y = y;
        this.nodeID = id;
        this.border_flag = is_border(x,y);
    }

    static boolean is_border(double x, double y){

        if((x == 0) || (x == gData.getnL()-1))
            return  true;
        if((y == 0) || (y == gData.getnH()-1))
            return true;
        else
            return false;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getNodeID() {
        return nodeID;
    }

    public boolean getboundaryFlag() {
        return border_flag;
    }

}
