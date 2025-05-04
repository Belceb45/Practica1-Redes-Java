package org.example.servidor;

import java.io.*;
import java.net.*;
import java.util.*;

import org.example.conexion.MongoDBConnection;
import org.example.utils.Producto;
import org.example.utils.ProductoService;

public class Servidor {

    public static void main(String[] args) {

        // Cadena de conexión de MongoDB Atlas

        String connectionStringDiego = "mongodb+srv://rubiodiego001:DRH7udJA6cATX@catalogo.uvho718.mongodb.net/?retryWrites=true&w=majority&appName=catalogo";
        String connectionStringAlma;
        String dbName = "catalogo";

        try {
            // Conexion MongoDB --- crear una conexión a MongoDB
            MongoDBConnection dbConnection = new MongoDBConnection(connectionStringDiego, dbName);
            ProductoService productoService = new ProductoService(dbConnection.getDatabase());

            //Comunicacion
            int puerto = 5050;
            ServerSocket serverSocket = new ServerSocket(puerto);
            System.out.println("\nServidor esperando cliente..........");

            Socket cl = serverSocket.accept();

            System.out.println("\n----------->  Cliente conectado  <----------------");
            System.out.println("-- Conexcion desde: " + cl.getInetAddress());


            //Obtener todos los productos en una lista para enviarlos
            List<Producto> productos= productoService.obtenerTodosLosProductos();
            for (Producto producto : productos) {
                System.out.println(producto.getTitle());
            }  
          
            // Obtener producto desde la base de datos


            cl.close();
            serverSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

       
    }

}
