import org.jetbrains.annotations.NotNull;
import java.io.FileNotFoundException;
import org.ejml.simple.SimpleMatrix;

public class Element {
    int elementID = 0;
    int[] nodesIdForElement;
    Node[] nodesForElement;
    Jacobian[] Jacobian2D;
    double[] detJacobian2D;
    double[] localHMatrix; //localHMatrix- H dla kazdego PC
    //double [][] matrixH; //matrixH-dla El.
    double[][] matrixOfdNdX;
    double[][] matrixOfdNdY;
    //--------------
    SimpleMatrix matrixH;
    SimpleMatrix[] hLocal;
    double cw = 700;
    double ro = 7800;
    double k = 30;
    double alfa = 25;


    public Element(int id, Node nodes[][], int i, int j) {
        this.elementID = id;
        nodesIdForElement = new int[4];
        nodesForElement = new Node[4];
        Jacobian2D = new Jacobian[4];
        detJacobian2D = new double[4];
        localHMatrix = new double[UniversalElement.numberOfIntegrationPoints];
        //matrixH = new double[UniversalElement.numberOfIntegrationPoints][UniversalElement.numberOfIntegrationPoints];
        matrixH = new SimpleMatrix(UniversalElement.numberOfIntegrationPoints, UniversalElement.numberOfIntegrationPoints);
        hLocal = new SimpleMatrix[4];
        matrixOfdNdX = new double[4][4];
        matrixOfdNdY = new double[4][4];
        ;
        setNodesForElement(nodes, i, j, id);
        createJacobianForPcOfElement(id);
        createHMatrix();
    }

    public Element() throws FileNotFoundException {
    }

    public void setNodesForElement(Node nodes[][], int i, int j, int ID) {
        int id = ID - 1;
        nodesIdForElement[0] = nodes[i][j].nodeID;
        nodesIdForElement[1] = nodes[i + 1][j].nodeID;
        nodesIdForElement[2] = nodes[i + 1][j + 1].nodeID;
        nodesIdForElement[3] = nodes[i][j + 1].nodeID;

        nodesForElement[0] = nodes[i][j];
        nodesForElement[1] = nodes[i + 1][j];
        nodesForElement[2] = nodes[i + 1][j + 1];
        nodesForElement[3] = nodes[i][j + 1];
    }

    void createJacobianForPcOfElement(int ID) {
        int id = ID - 1;
        for (int i = 0; i < UniversalElement.numberOfIntegrationPoints; i++) {
            double dXdKsi, dYdKsi, dXdEta, dYdEta;
            dXdKsi = (UniversalElement.matrixOfdNdKsi[i][0] * nodesForElement[0].x) +
                    (UniversalElement.matrixOfdNdKsi[i][1] * nodesForElement[1].x) +
                    (UniversalElement.matrixOfdNdKsi[i][2] * nodesForElement[2].x) +
                    (UniversalElement.matrixOfdNdKsi[i][3] * nodesForElement[3].x);

            dXdEta = UniversalElement.matrixOfdNdEta[i][0] * nodesForElement[0].x +
                    UniversalElement.matrixOfdNdEta[i][1] * nodesForElement[1].x +
                    UniversalElement.matrixOfdNdEta[i][2] * nodesForElement[2].x +
                    UniversalElement.matrixOfdNdEta[i][3] * nodesForElement[3].x;

            dYdKsi = (UniversalElement.matrixOfdNdKsi[i][0] * nodesForElement[0].y) +
                    (UniversalElement.matrixOfdNdKsi[i][1] * nodesForElement[1].y) +
                    (UniversalElement.matrixOfdNdKsi[i][2] * nodesForElement[2].y) +
                    (UniversalElement.matrixOfdNdKsi[i][3] * nodesForElement[3].y);

            dYdEta = UniversalElement.matrixOfdNdEta[i][0] * nodesForElement[0].y +
                    UniversalElement.matrixOfdNdEta[i][1] * nodesForElement[1].y +
                    UniversalElement.matrixOfdNdEta[i][2] * nodesForElement[2].y +
                    UniversalElement.matrixOfdNdEta[i][3] * nodesForElement[3].y;

            Jacobian2D[i] = new Jacobian(dXdKsi, dYdKsi, dXdEta, dYdEta);
            //i-numer PC
            detJacobian2D[i] = (Jacobian2D[i].dXdKsi * Jacobian2D[i].dYdEta) - (Jacobian2D[i].dXdEta * Jacobian2D[i].dYdKsi);  // wyznacznik jakobianów 2D
        }
        //printJacobianForElement(ID);
    }

    void createHMatrix() {
//localHMatrix- H dla kazdego PC, matrixH-dla El.
        for (int i = 0; i < UniversalElement.numberOfIntegrationPoints; i++) {
            for (int j = 0; j < 4; j++) {
                //i-number of PC, j- number of shape funcion
                matrixOfdNdX[i][j] = setDNdX(i, j);
                matrixOfdNdY[i][j] = setDNdY(i, j);
            }
        }
        //czesci {dN/dx}{dN/dx}T i {dN/dy}{dN/dy}T
        SimpleMatrix dNdx = new SimpleMatrix(matrixOfdNdX);
        SimpleMatrix dNdy = new SimpleMatrix(matrixOfdNdY);
        //dNdx.transpose();
        //dNdx.print();

        for (int i = 0; i < UniversalElement.numberOfIntegrationPoints; i++) {
            // {dN/dx}*{dN/dx}T , {dN/dy}*{dN/dy}T
            SimpleMatrix dx = dNdx.mult(dNdx.transpose());
            SimpleMatrix dy = dNdy.mult(dNdy.transpose());
            // {dN/dx}{dN/dx}T*detJ
            dx = dx.scale(this.detJacobian2D[i]);
            // {dN/dy}{dN/dy}T*detJ
            dy = dy.scale(this.detJacobian2D[i]);
            // [{dN/dx}{dN/dx}T + {dN/dy}{dN/dy}T ]*detJ
            hLocal[i] = dx.plus(dy);
        }
        // [{dN/dx}{dN/dx}T + {dN/dy}{dN/dy}T ]*detJ*k
        for (int i = 0; i < UniversalElement.numberOfIntegrationPoints; i++) {
            hLocal[i] = hLocal[i].scale(k * UniversalElement.getIntegrationPoints()[i].WeightKsi
                    * UniversalElement.getIntegrationPoints()[i].WeightEta);
            matrixH=matrixH.plus(hLocal[i]);
        }
    }

    double setDNdX(int pointIndex, int nIndex) {
        //i-number of PC, j- number of shape funcion
        return 1 / detJacobian2D[pointIndex] * (Jacobian.inverseJacobian(Jacobian2D[pointIndex]).dXdKsi
                * UniversalElement.matrixOfdNdKsi[pointIndex][nIndex]
                + Jacobian.inverseJacobian(Jacobian2D[pointIndex]).dXdEta
                * UniversalElement.matrixOfdNdEta[pointIndex][nIndex]);
    }
    double setDNdY(int pointIndex, int nIndex) {
        //nIndex - numer funkcji ksztaltu
        return 1 / detJacobian2D[pointIndex] * (Jacobian.inverseJacobian(Jacobian2D[pointIndex]).dYdKsi//dY/dEta
                * UniversalElement.matrixOfdNdKsi[pointIndex][nIndex]
                + Jacobian.inverseJacobian(Jacobian2D[pointIndex]).dYdEta//dY/dKsi
                * UniversalElement.matrixOfdNdEta[pointIndex][nIndex]);
    }
    /*double setDNdY(int pointIndex, int nIndex) {
        //nIndex - numer funkcji ksztaltu
        return 1 / detJacobian2D[pointIndex] * (Jacobian.inverseJacobian(Jacobian2D[pointIndex]).dYdKsi//dY/dEta
                * UniversalElement.matrixOfdNdKsi[nIndex][pointIndex]
                + Jacobian.inverseJacobian(Jacobian2D[pointIndex]).dYdEta//dY/dKsi
                * UniversalElement.matrixOfdNdEta[nIndex][pointIndex]);
    }*/

    //----------wypisywanie-------------
    void printJacobianForElement(int ID) {
        int id = ID - 1;

        System.out.println("Element " + (id + 1));
        for (int i = 0; i < UniversalElement.numberOfIntegrationPoints; i++) {
            System.out.println("PC: " + (i + 1));
            System.out.println("Node1 : " + "x: " + nodesForElement[0].x + ", y: " + nodesForElement[0].y);
            System.out.println("Node2 : " + "x: " + nodesForElement[1].x + ", y: " + nodesForElement[1].y);
            System.out.println("Node3 : " + "x: " + nodesForElement[2].x + ", y: " + nodesForElement[2].y);
            System.out.println("Node4 : " + "x: " + nodesForElement[3].x + ", y: " + nodesForElement[3].y);
            System.out.println(Jacobian2D[i].dXdKsi + " " + Jacobian2D[i].dXdEta + " " + Jacobian2D[i].dYdKsi + " " + Jacobian2D[i].dYdEta + " ");
            System.out.println("wyznacznik: " + detJacobian2D[i]);
        }
    }

    void printHMatrixForElement() {
        System.out.println("local matrix H:");
        for (int i = 0; i < UniversalElement.numberOfIntegrationPoints; i++) {
            hLocal[i].print();}
        System.out.println("matrix H:");
        matrixH.print();
    }

    void printmatrixOfdNdXanddNdYForElement() {
        System.out.println("matrixOfdNdX");
        for (int i = 0; i < UniversalElement.numberOfIntegrationPoints; i++) {
            System.out.println("PC"+(i+1));
            for (int j = 0; j < 4; j++) {
                //i-number of PC, j- number of shape funcion
                System.out.println("N"+(j+1)+" "+matrixOfdNdX[i][j]);
            }
        }
        System.out.println("matrixOfdNdY");
        for (int i = 0; i < UniversalElement.numberOfIntegrationPoints; i++) {
            System.out.println("PC"+(i+1));
            for (int j = 0; j < 4; j++) {
                //i- numer indeksu & j-numer f.ksztaltu
                System.out.println("N"+(j+1)+" "+matrixOfdNdY[i][j]);
            }
        }
    }
}