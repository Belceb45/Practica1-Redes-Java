package org.example.servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.example.conexion.MongoDBConnection;
import org.example.utils.Producto;
import org.example.utils.ProductoService;

public class Servidor {

    public static void main(String[] args) {

        String connectionStringDiego = "mongodb+srv://rubiodiego001:DRH7udJA6cATX@catalogo.uvho718.mongodb.net/?retryWrites=true&w=majority&appName=catalogo";
        String dbName = "catalogo";

        try {
            MongoDBConnection dbConnection = new MongoDBConnection(connectionStringDiego, dbName);
            ProductoService productoService = new ProductoService(dbConnection.getDatabase());

            int puerto = 5050;
            ServerSocket serverSocket = new ServerSocket(puerto);
            System.out.println("\nServidor esperando cliente..........");

            Socket cl = serverSocket.accept();
            System.out.println("\n----------->  Cliente conectado  <----------------");
            System.out.println("-- Conexión desde: " + cl.getInetAddress());

            PrintWriter output = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(cl.getInputStream()));

            boolean continuar = true;

            while (continuar) {
                String recibido = input.readLine();

                if (recibido == null) {
                    System.out.println("Cliente desconectado.");
                    break;
                }

                System.out.println("Opción recibida: " + recibido);
                if (recibido.startsWith("AGREGAR:") || recibido.startsWith("Producto")) {
                    String nombre = recibido.substring("AGREGAR:".length()).trim();

                    Producto prod = productoService.obtenerProductoPorNombre(nombre);
                
                    if (prod != null) {
                        output.println("\nProducto: " + prod.getTitle());
                        output.println("Tipo: " + prod.getType());
                        output.println("Descripcion: " + prod.getDescription());
                        output.println("Precio: $" + prod.getPrice());
                    } else {
                        output.println("Producto no encontrado.");
                    }
                    output.println("__END__");
                    continue; // Saltar el switch para no entrar en case default
                }
                
                switch (recibido) {
                    case "1": // Ver catálogo
                        List<Producto> productos = productoService.obtenerTodosLosProductos();
                        if (productos.isEmpty()) {
                            output.println("Catálogo vacío.");
                        } else {
                            for (Producto producto : productos) {
                                output.println("\n"+producto.toString());
                            }
                        }
                        output.println("__END__"); // Marcador de fin para el cliente
                        break;
                    case "2": // Salir
                        output.println("Desconectando del servidor...");
                        output.println("__END__");
                        continuar = false;
                        break;

                    default:
                        output.println("Opción no válida.");
                        output.println("__END__");
                        break;
                }
            }

            cl.close();
            serverSocket.close();
            System.out.println("Servidor cerrado.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
