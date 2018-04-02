package chat.labs.test.chat.myapplication;

public class Model {
   public String imageurl;
   public String _name;
   public String rating;
   public String location;
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Model() {
    }

    public Model(String imageurl, String _name, String rating, String location) {
        this.imageurl = imageurl;
        this._name = _name;
        this.rating = rating;
        this.location = location;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
