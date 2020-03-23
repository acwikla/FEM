import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GlobalData {
    static double H,L,nL,nH;
    static double nW = nH * nL;//ilosc wezlow->ilosc elementow w wysokosci* ilosc elementow w szerokosci
    static double nE = (nH-1)*(nL-1);//ilosc elelemtow
    File file = new File("C:\\Users\\Acer\\MES-projekt\\src\\com\\company\\data.txt");
    Scanner scanner = new Scanner(file);

    public GlobalData() throws FileNotFoundException {
        String string = scanner.nextLine();
        H = Double.parseDouble(string);
        string = scanner.nextLine();
        L = Double.parseDouble(string);
        string = scanner.nextLine();
        nH = Double.parseDouble(string);
        string = scanner.nextLine();
        nL = Double.parseDouble(string);
        //System.out.println(H+" "+L+" "+nH+" "+nL);
    }
    
    public double getH() {
        return H;
    }

    public double getL() {
        return L;
    }

    public double getnH() {
        return nH;
    }

    public double getnL() {
        return nL;
    }

    public double getnW() {
        return nW;
    }

    public double getnE() {
        return nE;
    }
}
