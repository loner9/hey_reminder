package info.project.hey.Class;

public class MyEvent {
    public String judulevnt,ketevnt,wktevnt,tglevnt,keyevnt,active;

    public MyEvent() {
    }

    public MyEvent(String judulevnt, String ketevnt,String wktevnt, String tglevnt, String keyevnt,String active) {
        this.judulevnt = judulevnt;
        this.ketevnt = ketevnt;
        this.wktevnt = wktevnt;
        this.tglevnt = tglevnt;
        this.keyevnt = keyevnt;
        this.active = active;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getWktevnt() {
        return wktevnt;
    }

    public void setWktevnt(String wktevnt) {
        this.wktevnt = wktevnt;
    }

    public String getKeyevnt() {
        return keyevnt;
    }

    public void setKeyevnt(String keyevnt) {
        this.keyevnt = keyevnt;
    }

    public String getJudulevnt() {
        return judulevnt;
    }

    public void setJudulevnt(String judulevnt) {
        this.judulevnt = judulevnt;
    }

    public String getKetevnt() {
        return ketevnt;
    }

    public void setKetevnt(String ketevnt) {
        this.ketevnt = ketevnt;
    }

    public String getTglevnt() {
        return tglevnt;
    }

    public void setTglevnt(String tglevnt) {
        this.tglevnt = tglevnt;
    }
}
