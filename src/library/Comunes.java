/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import administracionDeProductos.Motos;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.System.console;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 *
 * @author Miguel Pazos
 */
public class Comunes {
    private static final String reset = "\u001B[0m";
    private static final String rojo = "\u001B[31m";
    /**
     * 
     */
    public static boolean console;
    private static final Console con = System.console();
    
    /**
     * Devuelve el resulado de un OR exclusivo (XOR)
     * @param x valor 1
     * @param y valor 2
     * @return resulatado XOR
     */
    public static boolean logicalXOR(boolean x, boolean y) {
        return !( ( x || y ) && ! ( x && y ) );
    }
    
    /**
     * Mediante los metodos de la clase matricula, lee esta en pantalla, la verifica y la devuelve con letras mayusculas
     * @return la matricula valida o el codigo exit
     */
    public static String leerMatricula() {
        String matricula = "";
        try{
            while(true){
        Scanner sc = new Scanner(System.in);
        matricula = sc.nextLine();
        if (!matricula.equals(""))
        {  
            Motos m = new Motos();
            int validezM = m.validarMatricula(matricula);
            matricula = m.getMatricula();
            if(validezM == 0) {
                    System.out.println(rojo + "Matricula no valida, porfavor introducala de nuevo" + reset);
            } else {
                if(!m.validarMatricula()) {
                        System.out.println(rojo + "Matricula no valida, porfavor introducala de nuevo" + reset);
                    } else {
                        break;
                    }
            }
        } else{
            matricula = "exit";
            break;
        }
            }
        return matricula;
        } catch(Exception e){
            return "";
        }
    }
    //PENDIENTE DE REPARACION DEBIDO A QUE CADA PC CREA UN CODIGO DIFERENTE
    /**
     * Verifica la contraseña con MD5 NO FUNCIONAL
     * @param pass
     * @return 
     */
    public static boolean verificarPass(String pass){
        
//        try {
//            FileReader fr = new FileReader(new File(".javapr"));
//            BufferedReader br = new BufferedReader(fr);
//            String pass1 = br.readLine();
//            fr.close();
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            if (pass == null || pass.length() == 0) {
//                return false;
//            } else{
//                md.update(pass.getBytes());
//                byte[] hash = md.digest();
//                StringBuilder hexString = new StringBuilder();
//                for (int i = 0; i < hash.length; i++) {
//                    if ((0xff & hash[i]) < 0x10) {
//                        hexString.append("0").append(Integer.toHexString((0xFF & hash[i])));
//                    } else {
//                        hexString.append(Integer.toHexString(0xFF & hash[i]));
//                    }
//                }
//                return (pass1.equals(hexString.toString()));
//            }
//        } catch (IOException | NoSuchAlgorithmException e) {
//            return false;
//        }
        return ("admin".equals(pass));
    }
    /**
     * Imprime en pantalla, objetivo para implementacion de contraseña (No implementado aun)
     * @param datos
     * @param color 
     */
    public static void output(String datos, String color){
        if (console) {
            System.out.println(datos);
        } else {
            System.out.println(color + datos + reset);
        }
        
    }
    /**
     * Lee una contraseña, objetivo no Implementado
     * @return 
     */
    public static String inputpass(){
        if (console) {
            return String.valueOf(console().readPassword("Contraseña:"));
        } else{
            System.out.println(rojo + "Ejecute en consola para iniciar sesion" + reset);
        return null;
    }
    }
    /**
     * Fuerza una espera hasta que se presiona la tecla INTRO
     */
    public static void espera(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Presione INTRO para continuar...");
        sc.nextLine();
    }
    
}
