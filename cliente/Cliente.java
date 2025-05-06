//Librerias para generar el ticker

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

//Librerias de 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Cliente {

    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public static void limpiarCarrito(List<String> carrito) {
        carrito.removeIf(item -> item == null || item.trim().isEmpty());
    }
    

    public static void listarProductos(List<String> carrito) {

        for (String carritos : carrito) {
            System.out.println("\n-----------\n" + carritos);

        }
    }

    public static void agregarProducto(Scanner sc, List<String> carrito, BufferedReader input, PrintWriter output) {
        try {
            System.out.print("Ingresa el nombre del producto: ");
            String nombre = sc.nextLine();
    
            output.println("AGREGAR:" + nombre);
    
            String linea;
            StringBuilder productoAgregado = new StringBuilder();
            clear();
    
            boolean productoNoEncontrado = false;
    
            while (!(linea = input.readLine()).equals("__END__")) {
                System.out.println(linea);
                if (linea.trim().equalsIgnoreCase("Producto no encontrado.")) {
                    productoNoEncontrado = true;
                }
                productoAgregado.append(linea).append("\n");
            }
    
            if (productoNoEncontrado) {
                System.out.println("No se agregó nada al carrito: producto no encontrado.");
                return;
            }
    
            String productoFinal = productoAgregado.toString().trim();
    
            if (!carrito.contains(productoFinal)) {
                carrito.add(productoFinal);
                System.out.println("Producto agregado al carrito.");
            } else {
                System.out.println("Ese producto ya está en el carrito.");
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public static void eliminarProducto(Scanner sc, List<String> carrito, BufferedReader input, PrintWriter output) {

        try {
            System.out.print("Ingresa el nombre del producto: ");
            String nombre = sc.nextLine();

            output.println("AGREGAR:" + nombre);
            String linea;
            StringBuilder productoEliminado = new StringBuilder();
            clear();
            while (!(linea = input.readLine()).equals("__END__")) {

                System.out.println(linea);
                productoEliminado.append(linea).append("\n");
            }

            carrito.remove(productoEliminado.toString().trim());
        } catch (Exception e) {
        }

    }

    public static void main(String[] args) {

        List<String> carrito = new ArrayList<>();

        try {
            // Conectar al servidor (pon la IP o "localhost" si es local)
            Socket cl = new Socket("localhost", 5050);

            BufferedReader input = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            PrintWriter output = new PrintWriter(cl.getOutputStream(), true);

            boolean condition = true;
            Scanner sc = new Scanner(System.in);

            while (condition) {
                clear(); // Limpia la pantalla antes del menú
                System.out.println("--------------> Menu Carrito <-----------------");
                System.out.println("-----   1. Ver catalogo    2. Ver carrito   ---");
                System.out.println("-----   3. Salir                           ---");
                System.out.println("------------------------------------------------");
                System.out.print("---R: ");

                int opt = sc.nextInt();
                sc.nextLine(); // Consumir salto de línea
                // Opciones de servidor
                switch (opt) {
                    case 1 -> {
                        output.println(opt); // Enviar opción al servidor

                        // Leer respuesta del servidor
                        String respuesta;
                        while ((respuesta = input.readLine()) != null) {
                            if (respuesta.equals("__END__")) {
                                break; // Fin de respuesta para esa opción
                            }
                            System.out.println(respuesta);
                        }
                        while (condition) {
                            System.out.print("\n¿Deseas agregar un producto al carrito? (s/n): ");
                            String confirmacion = sc.nextLine();

                            if (confirmacion.equalsIgnoreCase("s")) {
                                agregarProducto(sc, carrito, input, output);
                                break;
                            }
                        }

                    }
                    case 2 -> {
                        clear();
                        System.out.println("------------------>  Menu Carrito  <------------------");
                        System.out.println("\n-------  LISTA: \n");
                        if (carrito.isEmpty()) {
                            System.out.println("NO HAY ELEMENTOS EN EL CARRITO");
                            break;
                        } else {
                            listarProductos(carrito);
                            System.out.println("\n------------------> Opciones  <------------------");
                            System.out.println("---- 1. Editar  2. Agregar  3. Eliminar 5. Comprar");
                            System.out.print("---- R: ");
                            opt = sc.nextInt();
                            sc.nextLine();
                            switch (opt) {
                                case 1:
                                    System.out.print("\nIngrese el nombre del producto: ");
                                    String name = sc.nextLine();
                                    output.println(name);

                                    System.out.println("Modificar:");
                                    System.out.println("1.Nombre");
                                    break;
                                case 2:
                                    agregarProducto(sc, carrito, input, output);
                                    break;
                                case 3:
                                    eliminarProducto(sc, carrito, input, output);
                                    break;
                                case 5:
                                    clear();
                                    System.out.println("\n------------------>  TICKER  <------------------\n");
                                    int n = carrito.size();
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                    Date date = new Date();
                                    System.out.println("Fecha: " + dateFormat.format(date) + "\n");
                                    System.out.println("Productos en el carrito: " + n + "\n");
                                    listarProductos(carrito);
                                    Document doc = new Document();

                                    try {
                                        PdfWriter.getInstance(doc, new FileOutputStream("ticket.pdf"));
                                        doc.open();

                                        // Título centrado
                                        Paragraph titulo = new Paragraph("TICKET DE COMPRA\n\n");
                                        titulo.setAlignment(Paragraph.ALIGN_CENTER);
                                        doc.add(titulo);

                                        // Fecha
                                        
                                        String fecha = dateFormat.format(new Date());
                                        doc.add(new Paragraph("Fecha: " + fecha + "\n\n"));

                                        // Detalles de productos
                                        doc.add(new Paragraph("Productos comprados:\n"));

                                        int contador = 1;
                                        for (String item : carrito) {
                                            doc.add(new Paragraph(contador++ + ". " + item + "\n"));
                                        }

                                        doc.add(new Paragraph("\nTotal de productos: " + carrito.size()));

                                        // Mensaje de agradecimiento
                                        Paragraph gracias = new Paragraph("\n¡Gracias por su compra!");
                                        gracias.setAlignment(Paragraph.ALIGN_CENTER);
                                        doc.add(gracias);

                                        doc.close();
                                        System.out.println("Ticket generado en ticket.pdf");

                                    } catch (DocumentException | IOException e) {
                                        e.printStackTrace();
                                    }

                                    break;

                                default:
                                    break;
                            }
                        }
                    }
                    case 3 ->
                        condition = false;
                    default -> {
                    }
                }

                System.out.println("\nPresiona Enter para continuar...");
                sc.nextLine(); // Esperar entrada para volver al menú
            }

            cl.close();
            System.out.println("Cliente desconectado.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
