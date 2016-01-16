/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import AGCH.main;
import Help.DataBase;
import administracionDeClientes.Clientes;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel Pazos y Emiliano Franco
 */
public class LogEdit {
    //Damos los valores de los colores
    private static final String reset = "\u001B[0m";
    private static final String rojo = "\u001B[31m";
    private static final String verde = "\u001B[32m";
    private static final String azul = "\u001B[34m";
    private static final String clase = "LogEdit";
    /**
     * Guarda las except en un fichero de texto para poder soluccionarlas posteriormente
     * @param e la exception
     * @param clase la clase donde se ha producido
     * @param metodo el metodo donde a sucedido
     */
    public static void annadirLog(Exception e, String clase, String metodo)  {
        //Creamos el escritor, abrimos el archivo y añadimos la nueva exception junto a la fecha
        FileWriter fw = null;
        try {
            fw = new FileWriter(new File("log.txt"), true);
            fw.append("[");
            fw.append(String.valueOf(LocalDate.now()));
            fw.append("] ");
            fw.append(clase);
            fw.append(" : ");
            fw.append(metodo);
            fw.append(" : ");
            fw.append(e.toString());
            fw.append("\n");
            fw.close();
        } catch (IOException ex) {
            //Si falla el metodo encargado de guardar los errores, no podemos guardar ese error utilizandolo a sí mismo, porque crearia un bucle, pero es muy poco probable que no funcione
            Logger.getLogger(LogEdit.class.getName()).log(Level.SEVERE, null, ex);
            //finalmente hay que cerrar el archivo
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                //puede que falle al intentar cerrarlo
                Logger.getLogger(LogEdit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    /**
     * Añade una estadistica de ayuda para mandarla a un server posteriormente
     * @param codigo codigo de respuesta deducido de la pregunta
     * @param pregunta pregunta realizada
     * @param opinion opinion sobre la respuesta
     * @param veracidad veracidad de la respuesta
     * @param comentarios comentarios sobre la respuesta
     */
    public static void annadirEstadistica(String codigo, String pregunta, int opinion, int veracidad, String comentarios)  {
        //Creamos el escritor, abrimos el archivo y añadimos la nueva exception junto a la fecha
        FileWriter fw = null;
        try {
            fw = new FileWriter(new File("estadisticas.tni"), true);
            fw.append("[");
            fw.append(String.valueOf(LocalDate.now()));
            fw.append("] code: ");
            fw.append(codigo);
            fw.append(" v: ");
            fw.append(String.valueOf(veracidad));
            fw.append(" q: ");
            fw.append(pregunta);
            fw.append(" o: ");
            fw.append(String.valueOf(opinion));
            fw.append(" c: ");
            fw.append(comentarios);
            fw.append("\n");
            fw.close();
        } catch (IOException ex) {
            //Si falla el metodo encargado de guardar los errores, no podemos guardar ese error utilizandolo a sí mismo, porque crearia un bucle, pero es muy poco probable que no funcione
            annadirLog(ex, clase, "annadirEstadistica");
            //finalmente hay que cerrar el archivo
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                //puede que falle al intentar cerrarlo
                annadirLog(ex, clase, "annadirEstadisticaClose");
            }
        }
        
    }
    /**
     * Envia los datos al server de los errores ESTO NO LO PRESENTAMOS no lo manejo todavia bien del todo ni esta completo, a demas ahora con repositorios no tiene tanta utilidad, pero asi veo las exception desde el metro y donde quiera
     */
        public static void sendData() {

        try {
            //Creamos el objeto Url con el valor de la  direccion
            String url = "http://www.tunegociointernet.es/app/user/pro1.php";
            URL obj = new URL(url);
            //Inciciamos la conexion con el server
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //Seleccionamos el tipo de conexion
            con.setRequestMethod("POST");
            //Leemos el fichero log y creamos un string donde este contenido en su totalidad
            FileReader fr = new FileReader(new File("log.txt"));
            BufferedReader br = new BufferedReader(fr);
            String linea;
            String datos = "";
            while ((linea = br.readLine()) != null) {
                datos = datos + linea + "\n";
            }
            fr.close();
            con.setDoOutput(true);
            //Envimos los datos
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(datos);
            wr.flush();
            wr.close();
            

            //recivimos la respuesta del server
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            FileWriter fw = new FileWriter(new File("log.txt"));
            fw.write("");
            fw.close();
            System.out.println(verde + "Enviado" + reset);
        } catch (IOException ex) {
            annadirLog(ex, clase, "sendData");
            System.out.println(rojo + "No se puede conectar con el servidor" + reset);
        }
        transmitirEstadisticas();
    }

        /**
         * Envia las estadisticas de ayuda al server, y recive los nuevos datos
         */
    public static void transmitirEstadisticas() {
        System.out.print("Enviando estadisticas al servidor.................");
        try {
            //Creamos el objeto Url con el valor de la  direccion
            String url = "http://www.tunegociointernet.es/app/user/est.php";
            URL obj = new URL(url);
            //Inciciamos la conexion con el server
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //Seleccionamos el tipo de conexion
            con.setRequestMethod("POST");
            //Leemos el fichero log y creamos un string donde este contenido en su totalidad
            FileReader fr = new FileReader(new File("estadisticas.tni"));
            BufferedReader br = new BufferedReader(fr);
            String linea;
            String datos = "";
            while ((linea = br.readLine()) != null) {
                datos = datos + linea + "\n";
            }
            fr.close();
            con.setDoOutput(true);
            //Envimos los datos
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(datos);
            wr.flush();
            wr.close();
            

            //recivimos la respuesta del server
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine).append("\n");
            }
            
            in.close();
            
            FileWriter fw = new FileWriter(new File("estadisticas.tni"));
            fw.write("");
            fw.close();
            System.out.println(verde + "Enviado" + reset);
            DataBase.update(response);
        } catch (IOException ex) {
            annadirLog(ex, clase, "sendEstadisticas");
            System.out.println(rojo + "No se puede conectar con el servidor" + reset);
        }
    }
    /**
     * Envia un mensaje a un servidor, el mensaje se envia como un contacto con el desarrollador y en el servidor un script php se encarga de enviarlo al correo del desarrollador
     */
    public static void contactar() {
        try {
            String url = "http://www.tunegociointernet.es/app/user/contactar_pr.php";
            URL obj = new URL(url);
            //Inciciamos la conexion con el server
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //Seleccionamos el tipo de conexion
            con.setRequestMethod("POST");
            Scanner sc = new Scanner(System.in);
            System.out.println(azul + "Introduzca su dni" + reset);
            Clientes c = new Clientes();
            c.ComprobarClienteC();
            if (c.getDni().equals("")) {
                return;
            }
            c.leerFicheroClientes();
            System.out.println(azul + "Introduzca su correo electronico" +reset);
            String email = sc.nextLine();
            System.out.println(azul + "Introduzca su mensaje, cuando termine escriba un punto para terminarlo (en una linea un punto y presiona intro)" + reset);
            String contenido = "";
            String linea;
            while (!(linea = sc.nextLine()).equals(".")) {                
                contenido = contenido + "<br>" + linea;
            }
            String[] dato = {c.getDni(),c.getNombre(),c.getPrimerApellido(),c.getSegundoApellido(), email , contenido};
            String datos = String.join("//", dato);
            con.setDoOutput(true);
            //Envimos los datos
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(datos);
            wr.flush();
            wr.close();
            

            //recivimos la respuesta del server
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine).append("\n");
            }
            System.out.println("Enviado");
            in.close();
            main.menu();
        } catch (Exception e) {
            annadirLog(e, clase, "contactar");
            main.menu();
        }
    }
        
}
