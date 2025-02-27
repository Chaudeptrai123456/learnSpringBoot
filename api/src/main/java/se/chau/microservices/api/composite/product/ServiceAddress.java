package se.chau.microservices.api.composite.product;

public class ServiceAddress {
    private  String cmp;
    private  String pro;
    private  String rev;
    private  String rec;

    private String dis;
    private String fea;
    public ServiceAddress() {
        this.cmp = null;
        this.pro = null;
        this.rev = null;
        this.rec = null;
        this.fea = null;
    }

    public ServiceAddress(String cmp, String pro, String rev, String rec,String dis,String fea) {
        this.cmp = cmp;
        this.pro = pro;
        this.rev = rev;
        this.rec = rec;
        this.fea=fea;
        this.dis=dis;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public String getFea() {
        return fea;
    }

    public void setFea(String fea) {
        this.fea = fea;
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
