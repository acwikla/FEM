import static java.lang.Math.sqrt;

public class UniversalElement {
    static int numberOfIntegrationPoints=4;
    static public IntegrationPoint [] integrationPoints = new IntegrationPoint[4];
    double matrixOfN[][]=new double[4][4];
    static double matrixOfdNdEta[][]= new double[4][4];
    static double matrixOfdNdKsi[][]= new double[4][4];
    double nVector[][]= new double[4][1];

    public UniversalElement() {
        setIntegrationPoints();
        setMatrixOfN();
        setdNdEta();
        setdNdKsi();
    }

    private void setIntegrationPoints() { //create and set Ksi & Eta for all PC
        integrationPoints[0] = new IntegrationPoint(-1/sqrt(3),-1/sqrt(3),1,1);
        integrationPoints[1] = new IntegrationPoint(1/sqrt(3),-1/sqrt(3),1,1);
        integrationPoints[2] = new IntegrationPoint(1/sqrt(3),1/sqrt(3),1,1);
        integrationPoints[3] = new IntegrationPoint(-1/sqrt(3),1/sqrt(3),1,1);
    }

    void setMatrixOfN(){
        //i-number of PC, 1...4- number of shape funcion
        for (int i = 0; i < 4; i++) {
            //i-
            matrixOfN[i][0] = (0.25 * (1 - integrationPoints[i].Ksi) * (1 - integrationPoints[i].Eta));
            matrixOfN[i][1] = (0.25 * (1 + integrationPoints[i].Ksi) * (1 - integrationPoints[i].Eta));
            matrixOfN[i][2] = (0.25 * (1 + integrationPoints[i].Ksi) * (1 + integrationPoints[i].Eta));
            matrixOfN[i][3] = (0.25 * (1 - integrationPoints[i].Ksi) * (1 + integrationPoints[i].Eta));
        }
        //printMatrixOfNandKsiEtaValues();
    }
    void setdNdEta(){ //set dN/dEta for all PC
        //i-number of PC, 1...4- number of shape funcion
        for(int i=0;i<4;i++){
            matrixOfdNdEta[i][0] = (-0.25 * (1 - integrationPoints[i].Ksi));
            matrixOfdNdEta[i][1] = (-0.25 * (1 + integrationPoints[i].Ksi));
            matrixOfdNdEta[i][2] = (0.25 * (1 + integrationPoints[i].Ksi));
            matrixOfdNdEta[i][3] = (0.25 * (1 - integrationPoints[i].Ksi));
        }
        //printMatrixOfdNdEta();
    }
    void setdNdKsi() {
        //i-number of PC, 1...4- number of shape funcion
        for (int i = 0; i < 4; i++) {
            matrixOfdNdKsi[i][0] = (-0.25 * (1 - integrationPoints[i].Eta));
            matrixOfdNdKsi[i][1] = (0.25 * (1 - integrationPoints[i].Eta));
            matrixOfdNdKsi[i][2] = (0.25 * (1 + integrationPoints[i].Eta));
            matrixOfdNdKsi[i][3] = (-0.25 * (1 + integrationPoints[i].Eta));
        }
        //printMatrixOfdNdKsi();
    }


    void getNVector(){

    }
    static public IntegrationPoint[] getIntegrationPoints() {
        return integrationPoints;
    }
    //----------wypisywanie-------------
    void printMatrixOfNandKsiEtaValues(){
        System.out.println("Matrix of N:");
        for (int i = 0; i < 4; i++) {
            System.out.println("Point: "+(i+1)+", N1: "+matrixOfN[i][0]+", N2: "+matrixOfN[i][1]+
                    ", N3: "+matrixOfN[i][2]+", N4: "+matrixOfN[i][3]);
        }
        System.out.println("-----------------------");
        for (int i = 0; i < 4; i++) {
            System.out.println("Point: "+(i+1)+", Ksi: "+integrationPoints[i].Ksi+" Eta: "+integrationPoints[i].Eta);
        }

    }
    void printMatrixOfdNdEta() {
        System.out.println("Matrix of dNdEta:");
        for(int i=0;i<4;i++){
            System.out.println("Point: "+(i+1)+", N1/Eta: "+matrixOfdNdEta[i][0]+", N2/Eta: "+matrixOfdNdEta[i][1]+
                    ", N3/Eta: "+matrixOfdNdEta[i][2]+", N4/Eta: "+matrixOfdNdEta[i][3]);
        }

    }
    void printMatrixOfdNdKsi() {
        System.out.println("Matrix of dNdKsi:");
        for(int i=0;i<4;i++){
            System.out.println("Point: "+(i+1)+", N1/Ksi: "+matrixOfdNdKsi[i][0]+", N2/Ksi: "+matrixOfdNdKsi[i][1]+
                    ", N3/Ksi: "+matrixOfdNdKsi[i][2]+", N4/Ksi: "+matrixOfdNdKsi[i][3]);
        }

    }

}
