package org.example.utils;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

import java.util.List;

public class ProductoService {

    private MongoCollection<Document> collection;

    //
    public ProductoService(MongoDatabase database){
        this.collection=database.getCollection("productos");

    }

    public List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = new ArrayList<>();

        for (Document doc : collection.find()) {
            Producto p = new Producto(
                doc.getString("_id"),  // si ObjectId
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
