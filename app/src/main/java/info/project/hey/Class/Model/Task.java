package info.project.hey.Class.Model;

public class Task {
    private String judul;
    private String arahan;

    public Task(String judul,String arahan){

        this.judul = judul;
        this.arahan = arahan;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getArahan() {
        return arahan;
    }

    public void setArahan(String arahan) {
        this.arahan = arahan;
    }
}
