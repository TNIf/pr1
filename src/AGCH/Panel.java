/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AGCH;

import static AGCH.main.menu;
import static AGCH.main.menuEdicion;
import administracionDeClientes.Clientes;
import administracionDeProductos.Alquiler;
import administracionDeProductos.Motos;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import library.Basicos;
import library.Comunes;
import library.LogEdit;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author Miguel Pazos
 */
public class Panel {

    //Valores de los codigos paea modificar el color del texto
    private static final String reset = "\u001B[0m";
    private static final String rojo = "\u001B[31m";
    private static final String verde = "\u001B[32m";
    private static final String azul = "\u001B[34m";
    private static final String bold = "\033[0;1m";
    private static final String endBold = "\033[0;0m";
    private static final String clase = "Panel";
    
    private Clientes c;
    private Alquiler a;
    /**
     * Constructor encargado de iniciar el panel, de cliente o de admin.
     * @param id 
     */
    public Panel(String id) {
        if(id.equals("ADMIN")){
           menuAdmin();
        } else{
            c = new Clientes();
            c.setDni(id);
            c.leerFicheroClientes();
            System.out.println(verde + "Iniciado sesion como " + c.getNombre() + " " + c.getPrimerApellido() + " " + c.getSegundoApellido() + reset);
            menuCliente();
        }
    }

    
    /**
     * Crea el menu del cliente
     */
    private void menuCliente() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println(azul + "Seleccione una opción:\n" + reset + "1. Editar datos personales \n2. Editar Alquileres\n3. Alquilar Vehiculo\n4. Ver Alquileres\n5. Contactar con el Administrador\n0. Cerrar Sesión");
            int opcion = sc.nextInt();
            switch(opcion){
                case 1:
                    c.editarCliente();
                    Comunes.espera();
                    menuCliente();
                    break;
                case 2:
                    c.panelAlquiler();
                    Comunes.espera();
                    menuCliente();
                    break;
                case 3:
                    a = new Alquiler();
                    a.setCliente(c);
                    a.iniciarAlquiler();
                    Comunes.espera();
                    menuCliente();
                    break;
                case 4:
                    a = new Alquiler();
                    a.setCliente(c);
                    a.imprimirAlquileresPropios(c.getDni());
                    Comunes.espera();
                    menuCliente();
                    break;
                    
                case 5:
                    contactoAdmin(c);
                    menuCliente();
                    break;
                case 0:
                    System.out.println(azul + "Saliendo del panel" + reset);
                    break;
                default:
                    throw new Exception("Int no valido");
            }
        } catch (Exception e) {
            System.out.println(rojo + "Seleccione una opcion valida" + reset);
            Comunes.espera();
            menuCliente();
        }
    }
    
    /**
     * Crea el menu de admin
     */
    private void menuAdmin() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println(azul + "Seleccione una opción:\n" + reset + "1. Ver Clientes y alquileres \n2. Generar tablas\n3. Ver mensajes\n4. Editar vehiculos\n0. Salir");
            int opcion = sc.nextInt();
            sc.nextLine();
            switch(opcion){
                case 1:
                    verClientes();
                    Comunes.espera();
                    menuAdmin();
                    break;
                case 2:
                    generarTablas();
                    Comunes.espera();
                    menuAdmin();
                    break;
                case 3:
                    menuMensajes();
                    menuAdmin();
                    break;
                case 4:
                    menuEdicion();
                    Comunes.espera();
                    menuAdmin();
                    break;
                case 0:
                    System.out.println(azul + "Saliendo del panel" + reset);
                    break;
                default:
                    throw new Exception("Int no valido");
            }
        } catch (Exception e) {
            System.out.println(rojo + "Seleccione una opcion valida" + reset);
            Comunes.espera();
            menuAdmin();
        }
    }
    /**
     * Muestra una lista de clientes junto con sus alquileres
     */
    private void verClientes() {
        System.out.println(azul + "Listado de clientes actuales con sus alquileres:" + reset);
        String[] dnis = Clientes.getDNIs();
        c = new Clientes();
        a = new Alquiler();
        for (String dni : dnis) {
            c.setDni(dni);
            c.leerFicheroClientes();
            System.out.print("\033[36m" + c + " : ");
            a.imprimirAlquileresPropios(dni);
            
        }
    }
    
    /**
     * Lee por teclado que tablas generar
     */
    private void generarTablas() {
        try {
            System.out.println(azul + "¿Que tablas desea generar? (todas (all), clientes (c), vehiculos (v) o alquileres(a). Para varias escriba las letras de cada una(Ejemplo: vehiculos y alquileres (va)))" + reset);
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            boolean ejecutar = false;
            if (input.equals("all") || input.equals("ALL") || input.equals("All") || input.startsWith("TOD") || input.startsWith("tod") || input.startsWith("Tod")) {
                workbook = new HSSFWorkbook();
                tablaVehiculos();
                tablaAlquileres();
                tablaClientes();
                crearFichero();
            } else {
                workbook = new HSSFWorkbook();
                if (input.contains("v") || input.contains("V")) {
                    ejecutar = true;
                    tablaVehiculos();
                }
                if (input.contains("a") || input.contains("A")) {
                    ejecutar = true;
                    tablaAlquileres();
                }
                if (input.contains("c") || input.contains("C")) {
                    ejecutar = true;
                    tablaClientes();
                }
                if (ejecutar) {
                    crearFichero();
                } else {
                    System.out.println(rojo + "No se creara ningun archivo" + reset);
                }
            }
        } catch (Exception e) {
            LogEdit.annadirLog(e, clase, "generarTablas");
        }
    }
    private HSSFWorkbook workbook;
    /**
     * Genera una hoja con los vehiculos
     */
    private void tablaVehiculos() {
        try {
            System.out.println(azul + "¿Que nombre desea darle a la tabla de Vehiculos" + reset);
            Scanner sc = new Scanner(System.in);
            String name = sc.nextLine();
            Map<Integer, String[]> datos = new HashMap<>();
            Motos m = new Motos();
            int key = 1;
            String[] matriculas = Motos.getMatriculas();
            for (String matricula : matriculas) {
                m.setMatricula(matricula);
                m.getDatosFichero();
                datos.put(key++, m.getDatosArray());
            }
            HSSFSheet sheet = workbook.createSheet(name);
            HSSFRow cabezera = sheet.createRow(0);
            cabezera.createCell(0).setCellValue("Nº");
            cabezera.createCell(1).setCellValue("Matricula");
            cabezera.createCell(2).setCellValue("Marca");
            cabezera.createCell(3).setCellValue("Modelo");
            cabezera.createCell(4).setCellValue("Color");
            cabezera.createCell(5).setCellValue("Precio");
            cabezera.createCell(6).setCellValue("Fecha de añadido");
            Set keys = datos.keySet();
            int fila = 1;
            for (Object kei : keys) {
                HSSFRow row = sheet.createRow(fila++);
                row.createCell(0).setCellValue(String.valueOf(kei));
                String[] data = datos.get(kei);
                int cell = 1;
                for (String dato : data) {
                    HSSFCell casilla = row.createCell(cell++);
                    casilla.setCellValue(dato);
                }
            }
        }catch (Exception e) {
            System.out.println(rojo + "Error desconocido" + reset);
            LogEdit.annadirLog(e, clase, "tablaVehiculo");
        }
    }
    /**
     * Genera uuna tabla con los alquileres
     */
    private void tablaAlquileres() {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            System.out.println(azul + "¿Que nombre desea darle a la tabla de Alquileres" + reset);
            Scanner sc = new Scanner(System.in);
            String name = sc.nextLine();
            Map<Integer, String[]> datos = new HashMap<>();
            int key = 1;
            String[] alquileres = Basicos.getOcupados();
            Clientes c = new Clientes();
            Motos m = new Motos();
            for (String alquiler : alquileres) {
                String[] alquilerArray = alquiler.split("::");
                m.setMatricula(alquilerArray[0]);
                m.getDatosFichero();
                c.setDni(alquilerArray[3]);
                c.leerFicheroClientes();
                String[] dato = {alquilerArray[0], dtf.format(LocalDate.parse(alquilerArray[1])), dtf.format(LocalDate.parse(alquilerArray[2])), alquilerArray[3], c.getNombre() +" "+ c.getPrimerApellido(), String.valueOf(m.getPrecio()), String.valueOf(LocalDate.parse(alquilerArray[1]).until(LocalDate.parse(alquilerArray[2]), ChronoUnit.DAYS)), String.valueOf(m.getPrecioTotal(new BigDecimal(LocalDate.parse(alquilerArray[1]).until(LocalDate.parse(alquilerArray[2]), ChronoUnit.DAYS))))};
                datos.put(key++, dato);
            }
            HSSFSheet sheet = workbook.createSheet(name);
            HSSFRow cabezera = sheet.createRow(0);
            cabezera.createCell(0).setCellValue("Nº");
            cabezera.createCell(1).setCellValue("Matricula");
            cabezera.createCell(2).setCellValue("Fecha de alquiler");
            cabezera.createCell(3).setCellValue("Fecha de devolucion");
            cabezera.createCell(4).setCellValue("DNI del cliente");
            cabezera.createCell(5).setCellValue("Nombre");
            cabezera.createCell(6).setCellValue("precio por dia");
            cabezera.createCell(7).setCellValue("duracion del alquiler (días)");
            cabezera.createCell(8).setCellValue("precio total");
            Set keys = datos.keySet();
            int fila = 1;
            for (Object kei : keys) {
                HSSFRow row = sheet.createRow(fila++);
                row.createCell(0).setCellValue(String.valueOf(kei));
                String[] data = datos.get(kei);
                int cell = 1;
                for (String dato : data) {
                    HSSFCell casilla = row.createCell(cell++);
                    casilla.setCellValue(dato);
                }
            }
        }catch (Exception e) {
            System.out.println(rojo + "Error desconocido" + reset);
            LogEdit.annadirLog(e, clase, "tablaAlquileres");
        }
    }

    
    /**
     * Genera una tabla con los alquileres
     */
    private void tablaClientes() {
       try {
           DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            System.out.println(azul + "¿Que nombre desea darle a la tabla de Clientes" + reset);
            Scanner sc = new Scanner(System.in);
            String name = sc.nextLine();
            Map<Integer, String[]> datos = new HashMap<>();
            Clientes c = new Clientes();
            int key = 1;
            String[] clientes = Clientes.getDNIs();
            for (String cliente : clientes) {
                c.setDni(cliente);
                c.leerFicheroClientes();
                String[] dato = {c.getDni(), c.getNombre(), c.getPrimerApellido(), c.getSegundoApellido(), dtf.format(c.getFechaNacimiento()), dtf.format(c.getFechaDeCreacion())};
                datos.put(key++, dato);
            }
            HSSFSheet sheet = workbook.createSheet(name);
            HSSFRow cabezera = sheet.createRow(0);
            cabezera.createCell(0).setCellValue("Nº");
            cabezera.createCell(1).setCellValue("DNI");
            cabezera.createCell(2).setCellValue("Nombre");
            cabezera.createCell(3).setCellValue("Primer apellido");
            cabezera.createCell(4).setCellValue("Segundo apellido");
            cabezera.createCell(5).setCellValue("Fecha de nacimiento");
            cabezera.createCell(6).setCellValue("Fecha de creación");
            Set keys = datos.keySet();
            int fila = 1;
            for (Object kei : keys) {
                HSSFRow row = sheet.createRow(fila++);
                row.createCell(0).setCellValue(String.valueOf(kei));
                String[] data = datos.get(kei);
                int cell = 1;
                for (String dato : data) {
                    HSSFCell casilla = row.createCell(cell++);
                    casilla.setCellValue(dato);
                }
            }
        }catch (Exception e) {
            System.out.println(rojo + "Error desconocido" + reset);
            LogEdit.annadirLog(e, clase, "tablaClientes");
        }
    }

    /**
     * Crea un archivo con las tablas creadas
     */
    private void crearFichero() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println(azul + "¿Que nombre desearia darle al archivo?" + reset);
            String name = sc.nextLine() + ".xls";
        FileOutputStream fos = new FileOutputStream(new File(name));
            workbook.write(fos);
            fos.close();
            System.out.println(verde + "Archivo " +name+" generado correctamente" + reset);
        } catch (FileNotFoundException e) {
            System.out.println(rojo + "Error con el fichero, puede que el nombre dado no sea valido" + reset);
            LogEdit.annadirLog(e, clase, "crearFicheros");
        } catch (IOException e) {
            System.out.println(rojo + "Error del sistema" + reset);
            LogEdit.annadirLog(e, clase, "crearFicheros");
        }
    }
    /**
     * Guarda un mensaje para el admin
     * @param c 
     */
      public static void contactoAdmin(Clientes c) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println(azul + "Introduzca su mensaje, cuando termine escriba un punto para terminarlo (en una linea un punto y presiona intro)" + reset);
            String contenido = "";
            String linea;
            while (!(linea = sc.nextLine()).equals(".")) {                
                contenido = contenido + "&&sn&&" + linea;
            }
            String[] dato = {c.getDni(),c.getNombre(),c.getPrimerApellido(),c.getSegundoApellido(), contenido, "true", "false"};
            FileWriter fw = new FileWriter(new File("mensage.agm"), true);
            fw.append(String.join("//--//", dato) + "&&&/-/&&&");
            fw.close();
            System.out.println(verde + "Mensaje enviado correctamente" + reset);
        } catch (Exception e) {
            System.out.println(rojo + "Error al enviar el mensaje" + reset);
            LogEdit.annadirLog(e, clase, "contactarAdmin");
        }
    }
      /**
       * Muestra el menu de mensajes
       */
    private void menuMensajes() {
        try {
            FileReader fr = new FileReader(new File("mensage.agm"));
            BufferedReader br = new BufferedReader(fr);
            String datos = br.readLine();
            fr.close();
            String[] mensajes = datos.split("&&&/-/&&&");
            ArrayList<String[]> mensage = new ArrayList<>();
            for (String mensaje : mensajes) {
                mensage.add(mensaje.split("//--//"));
            }
            mensage = showMessages(mensage);
            FileWriter fw = new FileWriter(new File("mensage.agm"));
            ArrayList<String> res = new ArrayList<>();
            for (String[] mensage1 : mensage) {
                res.add(String.join("//--//", mensage1));
            }
            String data = String.join("&&&/-/&&&", res);
            fw.write(data + "&&&/-/&&&");
            fw.close();
            System.out.println(verde + "Bandeja de mensajes cerrada correctamente" + reset);
            Comunes.espera();
        } catch (IOException | NumberFormatException e) {
            System.out.println(rojo + "Introduzca un valor valido" + reset);
            LogEdit.annadirLog(e, clase, "menuMensajes");
        } catch(Exception e){
            System.out.println(rojo + "No hay mensajes disponibles" + reset);
            LogEdit.annadirLog(e, clase, "menuMensajes");
        }
    }
    
    private ArrayList<String[]> showMessages(ArrayList<String[]> message) throws Exception{
        while (true) {            
        System.out.println(azul + "Actualmente dispone de los siguientes mensajes: " + reset + "(codigo de colores: " + verde + "nuevo" + reset + ", " + rojo + "importante" + reset + ", "+ " ya leido" + ")");
            int id = 1;
            for (String[] mens : message) {
                String[] content = mens[4].split("&&sn&&");
                if (mens[5].equals("true")) {
                    
                    System.out.println(verde + "-> " + String.valueOf(id) + " De: " + mens[1] + " " + mens[2] + " Contenido: " + content[1] + "..." + reset);
                } else if(mens[6].equals("true")){
                    System.out.println(rojo + "-> " + String.valueOf(id) + " De: " + mens[1] + " " + mens[2] + " Contenido: " + content[1] + "..." + reset);
                }else {
                    System.out.println("-> " + String.valueOf(id) + " De: " + mens[1] + " " + mens[2] + " Contenido: " + content[1] + "...");
                }
                id++;
            }
            System.out.println(azul + "Indique el codigo del mensaje para abrirlo (INTRO para salir)"+ reset);
            Scanner sc = new Scanner(System.in);
            String sel = sc.nextLine();
            if (sel.equals("")) {
                break;
            }
            int seleccion = Integer.valueOf(sel);
            if ((seleccion - 1) <= message.size() && (seleccion - 1) >= 0) {
                while (true) {
                    String [] mostrar = message.get(seleccion - 1);
                    int res = showMessage(mostrar, seleccion, ((seleccion - 2) >= 0), (seleccion < message.size()), Boolean.valueOf(mostrar[6]), sc); // 0 volveral menu, 1 borrar, 2 marcar, 3 anterior 4 siguiente
                    mostrar[5] = "false";
                    message.set((seleccion -1), mostrar);
                    switch(res){
                        case 1:
                            message.remove(seleccion - 1);
                            break;
                        case 2:
                            if("false".equals(mostrar[6])){
                                mostrar[6] = "true";
                            } else{
                                mostrar[6] = "false";
                            }
                            message.set((seleccion -1), mostrar);
                            break;
                        case 3:
                            seleccion--;
                            break;
                        case 4:
                            seleccion++;
                            break;
                    }
                    if (res ==0 || res == 1) {
                        break;
                    }
                }   
            } else {
                System.out.println(rojo + "Seleccion no valida" + reset);
            }
        }
        return message;
    }
    private int showMessage(String[] mostrar, int seleccion, boolean ini, boolean fin, boolean importante, Scanner sc){
        System.out.println(bold + "Mensaje numero: " + endBold + String.valueOf(seleccion));
        System.out.println(bold + "De: " + endBold + mostrar[1] + " "+ mostrar[2] + " " + mostrar[3]);
        System.out.println(bold + "DNI cliente: " + endBold + mostrar[0]);
        String contenido = mostrar[4].replace("&&sn&&", "\n");
        System.out.println(bold + "Contenido: " + endBold + contenido);
        if (ini) {
            System.out.print(azul + "[a] Anterior | " + reset);
        }
        System.out.print(azul + "[b] Borrar | " + reset);
        if (importante) {
            System.out.print(azul + "[i] Desmarcar como importante | "+ reset);
        } else{
            System.out.print(azul + "[i] Marcar como importante | "+ reset);
        }
        System.out.print(azul + "[INTRO] Salir" + reset);
        if (fin) {
            System.out.print(azul + " | [s] Siguiente" + reset);
        }
        System.out.println("");
        int res = 5;
        while(true){
            String respuesta = sc.nextLine();
            switch(respuesta){
                case "a":
                case "A":
                    if (ini) {
                        res = 3;
                    }
                    break;
                case "s":
                case "S":
                    if (fin) {
                        res = 4;
                    }
                    break;
                case "b":
                case "B":
                    res = 1;
                    break;
                case "i":
                case "I":
                    res = 2;
                    break;
                case "":
                    res = 0;
                    break;
                default:
                    res = 5;
                    break;
            }
            if (res != 5) {
                break;
            }
        }
        return res;
    }
         
}
