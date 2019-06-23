package info.project.hey.Class.Model;

public class Contacts {
    public String name;
    public String status;
    public String level;
    public String image;

    public Contacts(){

    }

    public Contacts(String name, String status, String level, String image) {
        this.name = name;
        this.status = status;
        this.level = level;
        this.image = image;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
