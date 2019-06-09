package info.project.hey.Class.Model;

public class Users {
    public String username;
    public String id;
    public String status;
    public String imageURL;
    public String search;
    public String level;

    public Users(){

    }

    public Users(String username,String level,String imageURL,String id,String status,String search){
        this.username = username;
        this.level = level;
        this.imageURL = imageURL;
        this.id = id;
        this.status = status;
        this.search = search;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
