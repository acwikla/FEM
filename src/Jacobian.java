public class Jacobian {
    double dXdKsi;
    double dYdKsi;
    double dXdEta;
    double dYdEta;

    public Jacobian(double dXdKsi, double dYdKsi, double dXdEta, double dYdEta) {
        this.dXdKsi = dXdKsi;
        this.dYdKsi = dYdKsi;
        this.dXdEta = dXdEta;
        this.dYdEta = dYdEta;
    }

    public Jacobian() {}

    //((A)D)T
    static public Jacobian inverseJacobian(Jacobian J){
        Jacobian tmp = new Jacobian();
        tmp.dXdKsi = J.dYdEta;
        tmp.dYdKsi = -J.dYdKsi;
        tmp.dXdEta = -J.dXdEta;
        tmp.dYdEta = J.dXdKsi;
        return tmp;
    }

    static public double getDetJacobian1D(double a){
        return a/2;
    }
}
