import java.io.FileNotFoundException;

public class Grid {
    private GlobalData gData = new GlobalData();

    private Element[][] elements = new Element[(int)gData.getnL()-1][(int)gData.getnH()-1];
    private Node[][] nodes = new Node[(int)gData.getnL()][(int) gData.getnH()];
//(int)gData.getnH()

    public Grid() throws FileNotFoundException {
    }

    public void createGrid() throws FileNotFoundException {
        createNodes();
        createElements();
        checkHMatrixForElement();
    }

    private void createElements() throws FileNotFoundException {
        int tmp=1;
        for(int i=0; i<gData.getnL()-1; i++){
            for(int j=0; j<gData.nH-1; j++){
                elements[i][j]=new Element(tmp, nodes, i, j);
                tmp++;
            }
        }
        //printNodesForElements();
    }

    private void createNodes() throws FileNotFoundException {
        double dx= gData.getL()/(gData.getnL()-1);
        double dy= gData.getH()/(gData.getnH()-1);
        int temp=1;
        for(int i=0; i<gData.getnL(); i++){
            for(int j=0; j<gData.getnH(); j++){
                nodes[i][j]= new Node(i*dx,j*dy,temp);
                //System.out.println(i*dx+" "+i*dy+" "+dx+" "+dy);
                temp ++;
            }
        }
        //printNodes();
    }
    //----------wypisywanie-------------
    public void printNodes(){
        for(int i=0; i<gData.getnL(); i++){
            for(int j=0; j<gData.getnH(); j++){
                System.out.println("Node: "+nodes[i][j].nodeID+", x :" + nodes[i][j].x + ", y :" + nodes[i][j].y + ", is boder : " + nodes[i][j].border_flag);
            }
        }
    }

    public void printNodesForElements(){
        for(int i=0; i<gData.getnL()-1; i++){
            for(int j=0; j<gData.nH-1; j++){
                System.out.println("Element:" +elements[i][j].elementID+", nodes for element: ");
                for(int k=0; k<4;k++){
                    System.out.println(elements[i][j].nodesIdForElement[k]);
                }
            }
        }
    }

    void checkJacobianForElement(){
        //Element 10:
        elements[2][4].printJacobianForElement(15);
    }

    void checkHMatrixForElement(){
        //Element 1:
        elements[0][0].printHMatrixForElement();
    }
    void checkdNdXanddNdYForElement(){
        //Element 1:
        elements[0][0].printmatrixOfdNdXanddNdYForElement();
    }
}
