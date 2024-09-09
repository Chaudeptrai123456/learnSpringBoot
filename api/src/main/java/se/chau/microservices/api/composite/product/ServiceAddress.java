package se.chau.microservices.api.composite.product;

public class ServiceAddress {
    private  String cmp;
    private  String pro;
    private  String rev;
    private  String rec;
    public ServiceAddress() {
        this.cmp = null;
        this.pro = null;
        this.rev = null;
        this.rec = null;
    }

    public ServiceAddress(String cmp, String pro, String rev, String rec) {
        this.cmp = cmp;
        this.pro = pro;
        this.rev = rev;
        this.rec = rec;
    }

    public String getCmp() {
        return cmp;
    }

    public void setCmp(String cmp) {
        this.cmp = cmp;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getRec() {
        return rec;
    }

    public void setRec(String rec) {
        this.rec = rec;
    }
}
