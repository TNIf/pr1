/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Help;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import library.LogEdit;

/**
 *
 * @author Miguel Pazos
 */
public class DataBase {
    private static final String reset = "\u001B[0m";
    private static final String rojo = "\u001B[31m";
    private static final String verde = "\u001B[32m";
    private static final String azul = "\u001B[34m";
    private static ArrayList<String[]> parametros = new ArrayList<>();
    private static boolean ready;
    
    /**
     *Valor que determina si los dartos son nuevos
     */
    protected static boolean validos;
    private static final String clase = "DataBase";
    private static ArrayList<String> respuestas = new ArrayList<String>();
    private static ArrayList<String> enlaces = new ArrayList<>();

    /**
     * @return the parametros
     */
    public static ArrayList<String[]> getParametros() {
        return parametros;
    }

    /**
     * @return the ready
     */
    public static boolean isReady() {
        return ready;
    }

    /**
     * @return the respuestas
     */
    public static ArrayList<String> getRespuestas() {
        return respuestas;
    }
    /**
     * Actualiza los datos de las respuestas
     * @param response datos del server
     */
    public static void update(StringBuilder response) {
        try {
            String[] files = response.toString().split("&.&");
            FileWriter fw = new FileWriter(new File("words_new.agm"));
            fw.append(files[0]);
            fw.close();
            FileWriter fw1 = new FileWriter(new File("answer_new.agm"));
            fw1.append(files[1]);
            fw1.close();
        } catch (Exception e) {
            LogEdit.annadirLog(e, clase, "update");
        }
    }

    /**
     * @return the enlaces
     */
    public static ArrayList<String> getEnlaces() {
        return enlaces;
    }
    /**
     * Constructor encargado de iniciar las configuraciones de los datos de HELP
     */
    public DataBase(){
            System.out.print("Leyendo datos del programa........................");
        obtenerParametros();
        if (isReady()) {
            System.out.println(verde + "Completado" + reset);
        } else {
            System.out.println(rojo + "Datos no validos" + reset);
            System.out.print("Leyendo backup de los datos del programa..........");
            obtenerParametrosLocales();
            if(ready){
                System.out.println(verde + "Completado" + reset);
            } else{
                System.out.println(rojo + "Imposible leer datos de ayuda" + reset);
            }
        }
    }
    
    /**
     * Obtiene los parametros para leer una pregunta y resolverla
     */
    protected static void obtenerParametros(){
        try {
            FileReader fr = new FileReader(new File("words_new.agm"));
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            String linea;
            int i = 1;
            ArrayList<String> p1 = new ArrayList<>();
            p1.add(0, "CONFIG");
            while(!"::TIPO::".equals(linea = br.readLine())){
                if (linea.equals("")) {
                    throw new Error("Sin datos");
                }
                p1.add(i++, linea);
            }
            String[] f1 = new String[p1.size()];
            f1 = p1.toArray(f1);
            ArrayList<String> p2 = new ArrayList<>();
            p2.add(0, "TIPO");
            i = 1;
            while(!"::ACCION::".equals(linea = br.readLine())){
                if (linea.equals("")) {
                    throw new Error("Sin datos");
                }
                p2.add(i++, linea);
            }
            String[] f2 = new String[p2.size()];
            f2 = p2.toArray(f2);
            ArrayList<String> p3 = new ArrayList<>();
            p3.add(0, "ACCION");
            i = 1;
            while(!"::OBJETIVO::".equals(linea = br.readLine())){
                if (linea.equals("")) {
                    throw new Error("Sin datos");
                }
                p3.add(i++, linea);
            }
            String[] f3 = new String[p3.size()];
            f3 = p3.toArray(f3);
            ArrayList<String> p4 = new ArrayList<>();
            p4.add(0, "OBJETIVO");
            i = 1;
            while(!"::NEGAR::".equals(linea = br.readLine())){
                if (linea.equals("")) {
                    throw new Error("Sin datos");
                }
                p4.add(i++, linea);
            }
            String[] f4 = new String[p4.size()];
            f4 = p4.toArray(f4);
            ArrayList<String> p5 = new ArrayList<>();
            p5.add(0, "NEGAR");
            i = 1;
            while(!"::END::".equals(linea = br.readLine())){
                if (linea.equals("")) {
                    throw new Error("Sin datos");
                }
                p5.add(i++, linea);
            }
            String[] f5 = new String[p5.size()];
            f5 = p5.toArray(f5);
            getParametros().add(0,f1);
            getParametros().add(1,f2);
            getParametros().add(2,f3);
            getParametros().add(3,f4);
            getParametros().add(4,f5);
            
            fr.close();
            
            FileReader fr1 = new FileReader(new File("answer_new.agm"));
            BufferedReader br1 = new BufferedReader(fr1);
            i = 0;
            getRespuestas().add(i++, "RESPUESTAS");
            while(!(linea = br1.readLine()).equals("::END::")){
                if (linea.equals("")) {
                    throw new Error("Sin datos");
                }
                getRespuestas().add(i++, linea);
            }
            fr1.close();
            FileReader fr2 = new FileReader(new File("enlaces.agm"));
            BufferedReader br2 = new BufferedReader(fr2);
            while (!(linea = br2.readLine()).equals("::END::")) {
                if (linea.equals("")) {
                    throw new Error("Sin datos");
                }
                getEnlaces().add(linea);
            }
            fr2.close();
            ready = true;
            validos = true;
        } catch (Exception e) {
            LogEdit.annadirLog(e, clase, "obtenerParametros");
            ready = false;
            validos = false;
        }catch(Error e){
            ready = false;
            validos = false;
        }
    }
    
    /**
     * Obtiene la backup local de los parametros para responder una pregunta
     */
    protected static void obtenerParametrosLocales(){
        try {
            FileReader fr = new FileReader(new File("words.agm"));
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            String linea;
            int i = 1;
            ArrayList<String> p1 = new ArrayList<>();
            p1.add(0, "CONFIG");
            while(!"::TIPO::".equals(linea = br.readLine())){
                if (linea.equals("")) {
                    throw new Error("Sin datos");
                }
                p1.add(i++, linea);
            }
            String[] f1 = new String[p1.size()];
            f1 = p1.toArray(f1);
            ArrayList<String> p2 = new ArrayList<>();
            p2.add(0, "TIPO");
            i = 1;
            while(!"::ACCION::".equals(linea = br.readLine())){
                if (linea.equals("")) {
                    throw new Error("Sin datos");
                }
                p2.add(i++, linea);
            }
            String[] f2 = new String[p2.size()];
            f2 = p2.toArray(f2);
            ArrayList<String> p3 = new ArrayList<>();
            p3.add(0, "ACCION");
            i = 1;
            while(!"::OBJETIVO::".equals(linea = br.readLine())){
                if (linea.equals("")) {
                    throw new Error("Sin datos");
                }
                p3.add(i++, linea);
            }
            String[] f3 = new String[p3.size()];
            f3 = p3.toArray(f3);
            ArrayList<String> p4 = new ArrayList<>();
            p4.add(0, "OBJETIVO");
            i = 1;
            while(!"::NEGAR::".equals(linea = br.readLine())){
                if (linea.equals("")) {
                    throw new Error("Sin datos");
                }
                p4.add(i++, linea);
            }
            String[] f4 = new String[p4.size()];
            f4 = p4.toArray(f4);
            ArrayList<String> p5 = new ArrayList<>();
            p5.add(0, "NEGAR");
            i = 1;
            while(!"::END::".equals(linea = br.readLine())){
                if (linea.equals("")) {
                    throw new Error("Sin datos");
                }
                p5.add(i++, linea);
            }
            String[] f5 = new String[p5.size()];
            f5 = p5.toArray(f5);
            getParametros().add(0,f1);
            getParametros().add(1,f2);
            getParametros().add(2,f3);
            getParametros().add(3,f4);
            getParametros().add(4,f5);
            
            fr.close();
            
            FileReader fr1 = new FileReader(new File("answer.agm"));
            BufferedReader br1 = new BufferedReader(fr1);
            i = 0;
            getRespuestas().add(i++, "RESPUESTAS");
            while(!(linea = br1.readLine()).equals("::END::")){
                if (linea.equals("")) {
                    throw new Error("Sin datos");
                }
                getRespuestas().add(i++, linea);
            }
            fr1.close();
            FileReader fr2 = new FileReader(new File("enlaces.agm"));
            BufferedReader br2 = new BufferedReader(fr2);
            while (!(linea = br2.readLine()).equals("::END::")) {
                if (linea.equals("")) {
                    throw new Error("Sin datos");
                }
                getEnlaces().add(linea);
            }
            fr2.close();
            ready = true;
            validos = false;
        } catch (Exception e) {
            LogEdit.annadirLog(e, clase, "obtenerParametrosLocales");
            ready = false;
            validos = false;
        }catch(Error e){
            ready = false;
            validos = false;
        }
    }
    
}
