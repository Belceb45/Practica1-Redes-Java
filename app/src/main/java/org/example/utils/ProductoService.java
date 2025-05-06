package org.example.utils;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.regex;
public class ProductoService {

    private MongoCollection<Document> collection;

    //
    public ProductoService(MongoDatabase database) {
        this.collection = database.getCollection("productos");

    }


    public Producto obtenerProductoPorNombre(String nombre) {
        Document doc = collection.find(regex("title", "^" + nombre + "$", "i")).first();

        if (doc != null) {
            return new Producto(
                    doc.getString("_id"),
                    doc.getString("title"),
                    doc.getString("type"),
                    doc.getString("description"),
                    doc.getString("pathFile"),
                    doc.getDouble("price"),
                    doc.getInteger("rating")
            );
        }

        return null;
    }

    public List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = new ArrayList<>();

        for (Document doc : collection.find()) {
            Producto p = new Producto(
                    doc.getString("_id"), // si ObjectId
                    doc.getString("title"),
                    doc.getString("type"),
                    doc.getString("description"),
                    doc.getString("pathFile"),
                    doc.getDouble("price"),
                    doc.getInteger("rating")
            );
            productos.add(p);
        }

        return productos;
    }
}
