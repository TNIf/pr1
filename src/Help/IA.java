/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Help;

import AGCH.Panel;
import AGCH.main;
import static AGCH.main.menu;
import static AGCH.main.menuAlquilar;
import static AGCH.main.menuClientes;
import static AGCH.main.menuEdicion;
import static AGCH.main.menuVerDatos;
import administracionDeClientes.Clientes;
import administracionDeProductos.Motos;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import library.Basicos;
import library.Comunes;
import library.LogEdit;
import sun.applet.Main;

/**
 *
 * @author Miguel Pazos
 */
public class IA {
    private static final String reset = "\u001B[0m";
    private static final String rojo = "\u001B[31m";
    private static final String verde = "\u001B[32m";
    private static final String azul = "\u001B[34m";
    private static final String clase = "IA";
    private static int dbError = 0;
    private static int dbOld = 0;
    private static boolean updated = false;
    private static String pregunta;
    private static boolean reiniciarMenu = true;
    
    /**
     * Metodo encargado de soluccionar dudas
     */
    public static void help(){
        reiniciarMenu = true;
        System.out.println(azul + "Sistema de Ayuda rapida, porfavor realice una pregunta (Ejemplo: Como añado un cliente) (VERSION BETA)" + reset);
        try {
            InputStreamReader i = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(i);
            if (dbError > 1 && DataBase.validos == true) {
                System.out.println(rojo + "Hemos detectado que esta teniendo problemas para obtener respuestas, puede que los datos de ayuda esten dañados, quiere probar con una copia local de los datos? (s/n)" + reset);
                String respuesta = br.readLine();
                if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("y") || respuesta.equals("yes") || respuesta.equals("S") || respuesta.equals("Si") || respuesta.equals("Y") || respuesta.equals("Yes") || respuesta.equals("YES") || respuesta.equals("SI") || respuesta.equals("ok") || respuesta.equals("OK") || respuesta.equals("Ok")) {
                    DataBase.obtenerParametrosLocales();
                    if (!DataBase.isReady()) {
                        System.out.println(rojo + "Los datos locales no se han podido cargar correctamente, el menu de ayuda permanecera bloqueado hasta que reinicie el programa" + reset);
                        Comunes.espera();
                        return;
                    } else {
                        System.out.println(verde + "Sistema de ayuda restablecido, haga su pregunta:" + reset);
                    }
                } else {
                    dbError = 0;
                    System.out.println(azul + "No se cambiaran los datos, haga su pregunta:" + reset);
                }
            }
            if (dbOld > 2 && dbOld >= 0) {
                System.out.println(rojo + "Hemos detectado que esta teniendo problemas para obtener respuestas, puede que los datos esten anticuados, quiere probar a actualizar las bases de datos? (s/n)" + reset);
                String res = br.readLine();
                if (res.equals("s") || res.equals("si") || res.equals("y") || res.equals("yes") || res.equals("S") || res.equals("Si") || res.equals("Y") || res.equals("Yes") || res.equals("YES") || res.equals("SI") || res.equals("ok") || res.equals("OK") || res.equals("Ok")) {
                    LogEdit.transmitirEstadisticas();
                    DataBase.obtenerParametros();
                    if (!DataBase.isReady()) {
                        System.out.println(rojo + "Los datos no se han podido cargar correctamente y la backup local esta dañada, el sistema de ayuda quedara bloqueado hasta que se reinicie el programa" + reset);
                        dbOld = -20;
                        Comunes.espera();
                        return;
                    } else {
                        dbOld = -20;
                        System.out.println(verde + "Sistema de ayuda actualizado, haga su pregunta:" + reset);
                    }
                } else {
                    dbOld = 0;
                    System.out.println(azul + "No se actualizaran los datos, haga su pregunta:" + reset);
                }
            }
            pregunta = br.readLine();
            if (pregunta.equals("")) {
                return;
            }
            
            pregunta = pregunta.toUpperCase();
            String[] splited = pregunta.split(" ");
            obtenerCodigo(splited);
        } catch (Exception e) {
            System.out.println(rojo + "Pregunta no valida" + reset);
            help();
        }
    }

    /**
     * Deduce el codigo de la respuesta de una pregunta
     * @param splited 
     */
    private static void obtenerCodigo(String[] splited) {
        ArrayList<String[]> codigo = DataBase.getParametros();
        String code = "";
        for (int i = 1; i < codigo.size(); i++) {
            String[] seccion = codigo.get(i);
            boolean f = false;
            for (int j = 1; j < seccion.length; j++) {
                String[] patrones = seccion[j].split("//");
                
                for (String patron : patrones) {
                    boolean v = false;
                    for (String palabra : splited) {
                        if (palabra.startsWith(patron)) {
                            code = code + String.valueOf(j) + ",";
                            v = true;
                            break;
                        }
                    }
                    if (v) {
                        f = true;
                        break;
                    }
                }
                
            }
            if (f) {
                    
                } else{
                    code = code + "0" + ",";
                }
        }
        respuesta(code, codigo.get(0));
    }
    
    /**
     * Genera la respuesta asociada al codigo
     * @param code codigo de la pregunta
     * @param codigos codigos de respuestas
     */
    private static void respuesta(String code, String[] codigos){
        try{
            int res = 0;
            int accesDir = 0;
            boolean enlazar = false;
            String[] c = code.split(",");
            ArrayList<String> r = DataBase.getRespuestas();
            for (int i = 1; i < codigos.length; i++) {
                String[] splited = codigos[i].split(":");
                String[] codes = splited[0].split(",");
                if(c[0].equals(codes[0]) && c[1].equals(codes[1]) && c[2].equals(codes[2]) && c[3].equals(codes[3])){
                    System.out.println(verde + "Solucción:"+ reset + r.get(Integer.valueOf(splited[1])));
                    if (Integer.valueOf(splited[2]) != 0) {
                        enlazar = preguntaDir(DataBase.getEnlaces().get(Integer.valueOf(splited[2])));
                        accesDir = Integer.valueOf(splited[2]);
                    }
                    res = 1;
                    break;
                } else if(!c[0].equals(codes[0]) && c[1].equals(codes[1]) && c[2].equals(codes[2]) && c[3].equals(codes[3])){
                    System.out.println(verde + "Solucción:"+ reset + r.get(Integer.valueOf(splited[1])));
                    if (Integer.valueOf(splited[2]) != 0) {
                        enlazar = preguntaDir(DataBase.getEnlaces().get(Integer.valueOf(splited[2])));
                        accesDir = Integer.valueOf(splited[2]);
                    }
                    res = 2;
                    break;
                }
            }
            formulario(code, pregunta, res);
            if (res == 0) {
                throw new Error("respuesta invalida");
            }
            if (enlazar) {
                accesoDirecto(accesDir);
            } else{
                help();
            }
            
        } catch(Error e){
            System.out.println(rojo + "No es posible responder a su pregunta, intentela formular de otra forma" + reset);
            dbOld++;
            help();
        } catch(NumberFormatException e){
            System.out.println(rojo + "Se ha producido un error con los datos, por favor intentelo de nuevo" + reset);
            LogEdit.annadirLog(e, clase, "respuesta");
            dbError++;
            help();
        } catch(Exception e){
            System.out.println(rojo + "Error desconocido" + reset);
            LogEdit.annadirLog(e, clase, "respuesta");
            dbError++;
            help();
        }
    }
    
    /**
     * Crea un formulario para saber la opinion sobre una respuesta
     * @param codigo codigo de la respuesta deducido de la pregunta
     * @param pregunta pregunta realizada
     * @param veracidad validez de la respuesta segun sus concordancias
     */
    private static void formulario(String codigo, String pregunta, int veracidad){
        if (veracidad == 0) {
            LogEdit.annadirEstadistica(codigo, pregunta, 0, veracidad, "");
        } else {
            System.out.println("\n¿Podría valorar la respuesta? (nota del 0 al 10) (INTRO para saltar)");
            Scanner sc = new Scanner(System.in);
            String valor = sc.nextLine();
            int valoracion;
            if (valor.equals("")) {
                valoracion = -1;
            } else{
                try {
                    valoracion = Integer.valueOf(valor);
                } catch (Exception e) {
                    valoracion = -1;
                }
            }
            System.out.println("Para terminar, quiere dejar algún comentario? (En caso contrario presione INTRO para volver al menu de ayuda)");
            String comentario = sc.nextLine();
            LogEdit.annadirEstadistica(codigo, pregunta, valoracion, veracidad, comentario);
            System.out.println("Gracias por su opinión, volviendo al menu de ayuda");
        }
    }
    private static boolean preguntaDir(String lugar){
        Scanner sc = new Scanner(System.in);
        System.out.println(azul + "¿Quieres acceder directamente a " + lugar + " (s/n)"+reset);
        String respuesta = sc.nextLine();
        if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("y") || respuesta.equals("yes") || respuesta.equals("S") || respuesta.equals("Si") || respuesta.equals("Y") || respuesta.equals("Yes") || respuesta.equals("YES") || respuesta.equals("SI") || respuesta.equals("ok") || respuesta.equals("OK") || respuesta.equals("Ok")) {
            return true;
        } else{
            return false;
        }
    }

    private static void accesoDirecto(int accesDir) {
        switch(accesDir){
            case 0://Nulo
                help();
                break;
            case 1://Alquilar vehiculo
            case 2://registrar clientes    
                menuAlquilar();//inicia el metodo que se encarga de mostrar las matriculas de los vehiculos alquilados y permite alquilar uno
                break;
            case 3:
                Motos motos = new Motos();
                    //Iniciamos el metodo annadirMoto() para obtener los datos del teclado
                    motos.annadirMoto();
                    Comunes.espera(); //creamos una espera antes de volver al menu
                    //Una vez añadida la moto se vuelve al menu principal
                    menu();
                break;
            case 4:
            case 6:
            case 9:
            case 11:
            case 12:
            case 7:
                menuClientes();
                Comunes.espera();
                menu();
                break;
            case 5:
                main.verAlquileres();
                break;
            case 8:
            case 10:
                menuEdicion();
                Comunes.espera();
                menu();
                break;
            case 13:
                LogEdit.contactar();
                menu();
                break;
            case 14:
                //Comprobamos que existe alguna matricula
                if(Basicos.getCuenta() == 0){
                    //No hay matriculas que visionar, asi que no se ejecuta la accion
                    System.out.println(rojo + "No hay ningun dato para mostrar" + reset);
                    menu();
                } else{
                    //inicia el metodo que lee el archivo con las matriculas y las imprime en pantalla, y posteriormente leer la matricula seleccionada
                    menuVerDatos();
                }
                break;
            case 15:
                Clientes c = new Clientes();
                c.ComprobarClienteC();
                if (c.getDni().equals("")) {
                    return;
                }
                c.leerFicheroClientes();
                Panel.contactoAdmin(c);
                help();
                menu();
                break;
            default:
                help();
                break;
                
        }
        if (accesDir != 0) {
            reiniciarMenu = false;
        }
    }

    /**
     * @return the reiniciarMenu
     */
    public static boolean isReiniciarMenu() {
        return reiniciarMenu;
    }
}