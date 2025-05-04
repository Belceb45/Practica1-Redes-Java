package org.example.utils;
import java.io.Serializable;



public class Producto implements Serializable{

    //
    private static final long serialVersionUID=1L;

    //Atributos
    protected String _id;
    protected String title;
    protected String type;
    protected String description;
    protected String pathFile;
    protected double price;
    protected int rating;

    // Constructor
    public Producto(String _id, String title, String type, String description, String pathFile, double price,
            int rating) {
        this._id = _id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.pathFile = pathFile;
        this.price = price;
        this.rating = rating;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


    @Override
    public String toString(){
        return "Producto{" +
               "_id='" + _id + '\'' +
               ", title='" + title + '\'' +
               ", type=" + type +
               ", description='" + description + '\'' +
               ", price=" + price + '\''+
               ", rating" + rating+
               '}';
    }
}
