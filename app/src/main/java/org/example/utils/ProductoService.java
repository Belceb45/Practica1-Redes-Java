package org.example.utils;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.Date;

public class ProductoService {

      private MongoCollection<Document> collection;

    public ProductoService(MongoDatabase database) {
        this.collection = database.getCollection("productos");
    }

    public void agregarProducto(String nombre, String descripcion, double precio, String categoria, int cantidadStock) {
        Document producto = new Document("nombre", nombre)
                .append("descripcion", descripcion)
                .append("precio", precio)
                .append("categoria", categoria)
                .append("cantidad_stock", cantidadStock)
                .append("fecha_creacion", new Date());

        collection.insertOne(producto);
        System.out.println("Producto agregado al catalogo: " + nombre);
    }

    public void encontrarProducto(String nombre) {
        Document producto = collection.find(new Document("nombre", nombre)).first();
        if (producto != null) {
            System.out.println("Producto encontrado: " + producto.toJson());
        } else {
            System.out.println("Producto no encontrado.");
        }
    }
}
