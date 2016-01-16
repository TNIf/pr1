 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administracionDeClientes;

import administracionDeProductos.Alquiler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import library.Comunes;
import library.LogEdit;

/**
 *
 * @author Miguel Pazos
 */
public class Clientes {
    
    //Valores de los codigos paea modificar el color del texto
    private static final String reset = "\u001B[0m";
    private static final String rojo = "\u001B[31m";
    private static final String verde = "\u001B[32m";
    private static final String azul = "\u001B[34m";
    //Variable para exceptions
    private static final String clase = "Clientes";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //Formato fechas para archivos
    private static final DateTimeFormatter dtfin = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final String[] dniNumero = {"T","R","W","A","G","M","Y","F","P","D","X","B","N","J","Z","S","Q","V","H","L","C","K","E"}; //Array con las letras del dni ordenadas
    private final String[] nifNumero = {"J","A","B","C","D","E","F","G","H","I"};  //Array con las letras del nif ordenadas
    
    //Atributos del cliente
    private String dni;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private LocalDate fechaNacimiento;
    private LocalDate fechaDeCreacion;
    
    /**
     * Metodo encargado de leer en la consola el DNI y compobar si este es valido, y en caso afirmativo si esta almacenado en es sistema
     */
    public void ComprobarClienteC(){
        try {
            //Primero Pedimos que introduzca su documento de Identificacion
            System.out.println("Introduzca su numero de identificación (DNI/NIE/NIF) sin espacios ni guiones (Intro para volver)");
            //Creamos el escaner para leer por teclado
            Scanner sc = new Scanner(System.in);
            //Leemos el valor introducccido
            setDni(sc.nextLine());
            //Comprueba la validez del DNI
            int validez = validarDNI();
            //Creamos un string para la respuesta
            String respuesta;
            //Creamos un switch para saber que hacer con cada tipo de DNI
            switch(validez) {
                case 0: //Los datos introduccidos no son un numero  de identificacion
                    System.out.println(rojo + "Los datos introducidos no son validos" + reset); 
                    ComprobarClienteC(); // reiniciamos el metodo
                    break;
                case 1: //El documento introduccido es un DNI y mediante los medios disponibles sabemos que puede ser valido
                    System.out.println("DNI detectado");
                    comprobarBD(); // comprueba si exite ya el cliente
                    break;
                case 2: //El documento introduccido es un NIF para menores de 14 años y por ahora sin 18 NO HAY COCHE
                    System.out.println(rojo + "Debe ser mayor de edad para alquilar un vehiculo" + reset);
                    break;
                case 3: //El documento introduccido es un NIF y mediante los medios dispinibles sabemos que puede ser validos
                    System.out.println("NIF detectado");
                    comprobarBD(); // Comprueba si existe el cliente
                    break;
                case 4: //El documento introduccido es un DNI pero la letra introduccida no concuerda con la real, puede ser que este mal el DNI, o que la letra sea erronea, asi que pregunta si es correcta con otra letra
                    System.out.println(rojo + "El DNI introducido no es correcto ¿puede ser este? " + reset + getDni() + " Compruebelo y responda (s/n)");
                    respuesta = sc.nextLine();
                    if(respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("y") || respuesta.equals("yes") || respuesta.equals("S") || respuesta.equals("Si") || respuesta.equals("Y") || respuesta.equals("Yes") || respuesta.equals("YES") || respuesta.equals("SI") || respuesta.equals("ok") || respuesta.equals("OK") || respuesta.equals("Ok")) {
                        comprobarBD();
                    } else{
                        ComprobarClienteC();
                    }
                    break;
                case 5: //El documento introduccido es un NIF pero la letra introduccida no concuerda con la real, puede ser que este mal el DNI, o que la letra sea erronea, asi que pregunta si es correcta con otra letra
                    System.out.println(rojo + "El NIF introducido no es correcto ¿puede ser este? " + reset + getDni() + " Compruebelo y responda (s/n)");
                    respuesta = sc.nextLine();
                    if(respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("y") || respuesta.equals("yes") || respuesta.equals("S") || respuesta.equals("Si") || respuesta.equals("Y") || respuesta.equals("Yes") || respuesta.equals("YES") || respuesta.equals("SI") || respuesta.equals("ok") || respuesta.equals("OK") || respuesta.equals("Ok")) {
                        comprobarBD();
                    } else{
                        ComprobarClienteC();
                    }
                    break;
                case 6: //El documento introduccido es un DNI, pero no se ha introducido la letra asi que se ha generado automaticamente
                    System.out.println(rojo + "No ha introducido la letra de su DNI, ha sido completado automaticamente" + reset);
                    comprobarBD();
                    break;
                case 7: // El documento introduccido es un NIF, pero no se ha introduccido la letra asi que se ha generado automaticamente
                    System.out.println(rojo + "No ha introducido la letra de su NIF, ha sido completado automaticamente" + reset);
                    comprobarBD();
                    break;
                case 8: //El documento introducido es un NIE y mediante los medios disponibles sabemos que puede ser valido
                    System.out.println(rojo + "NIE detectado" + reset);
                    comprobarBD();
                    break;
                case 9: //El documento introducido es un NIE, pero la letra introduccida no concuerda con la real, puede ser que este mal el DNI, o que la letra sea erronea, asi que pregunta si es correcta con otra letra
                    System.out.println(rojo + "El NIE introducido no es correcto ¿puede ser este? " + reset + getDni() + " Compruebelo y responda (s/n)");
                    respuesta = sc.nextLine();
                    if(respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("y") || respuesta.equals("yes") || respuesta.equals("S") || respuesta.equals("Si") || respuesta.equals("Y") || respuesta.equals("Yes") || respuesta.equals("YES") || respuesta.equals("SI") || respuesta.equals("ok") || respuesta.equals("OK") || respuesta.equals("Ok")) {
                        comprobarBD();
                    } else{
                        ComprobarClienteC();
                    }
                    break;
                case 10: //El documento introducido es un NIE, pero no se ha introducido la letra asi que se ha generado automaticamente
                    System.out.println(rojo + "No ha introducido la letra de su NIE, ha sido completado automaticamente" + reset);
                    comprobarBD();
                    break;
                case 11: // Se ha introducido un INTRO, por tanto se sale al menu
                    setDni("");
                    break;
                default: // en caso de que nos sea ningun valor se Imprime un aviso de error
                    System.out.println(rojo + "Error en la lectura del DNI" + reset);
                
            }
        } catch (Exception e) { //Se ha producido un error en el intento leer en la consola asi que se logea el error y se vuelve al menu
           library.LogEdit.annadirLog(e, clase, "comprobarCliente");
        }
        
    }
    
    /**
     * swich para identificar DNI
     * @return Validez
     */
    public boolean validar(){
        int validez = validarDNI();
            //Creamos un string para la respuesta
            String respuesta;
            //Creamos un switch para saber que hacer con cada tipo de DNI
            boolean res;
            try{
                Scanner sc = new Scanner(System.in);
            
            switch(validez) {
                case 0: //Los datos introduccidos no son un numero  de identificacion
                    System.out.println(rojo + "Los datos introducidos no son validos" + reset); 
                    res = false;
                    break;
                case 1: //El documento introduccido es un DNI y mediante los medios disponibles sabemos que puede ser valido
                    System.out.println("DNI detectado");
                    res = true;
                    break;
                case 2: //El documento introduccido es un NIF para menores de 14 años y por ahora sin 18 NO HAY COCHE
                    System.out.println(rojo + "Debe ser mayor de edad" + reset);
                    res = false;
                    break;
                case 3: //El documento introduccido es un NIF y mediante los medios dispinibles sabemos que puede ser validos
                    System.out.println("NIF detectado");
                    res = true;
                    break;
                case 4: //El documento introduccido es un DNI pero la letra introduccida no concuerda con la real, puede ser que este mal el DNI, o que la letra sea erronea, asi que pregunta si es correcta con otra letra
                    System.out.println(rojo + "El DNI introducido no es correcto ¿puede ser este? " + reset + getDni() + " Compruebelo y responda (s/n)");
                    respuesta = sc.nextLine();
                    if(respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("y") || respuesta.equals("yes") || respuesta.equals("S") || respuesta.equals("Si") || respuesta.equals("Y") || respuesta.equals("Yes") || respuesta.equals("YES") || respuesta.equals("SI") || respuesta.equals("ok") || respuesta.equals("OK") || respuesta.equals("Ok")) {
                        res = true;
                    } else{
                        res = false;
                    }
                    break;
                case 5: //El documento introduccido es un NIF pero la letra introduccida no concuerda con la real, puede ser que este mal el DNI, o que la letra sea erronea, asi que pregunta si es correcta con otra letra
                    System.out.println(rojo + "El NIF introducido no es correcto ¿puede ser este? " + reset + getDni() + " Compruebelo y responda (s/n)");
                    respuesta = sc.nextLine();
                    if(respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("y") || respuesta.equals("yes") || respuesta.equals("S") || respuesta.equals("Si") || respuesta.equals("Y") || respuesta.equals("Yes") || respuesta.equals("YES") || respuesta.equals("SI") || respuesta.equals("ok") || respuesta.equals("OK") || respuesta.equals("Ok")) {
                        res = true;
                    } else{
                        res = false;
                    }
                    break;
                case 6: //El documento introduccido es un DNI, pero no se ha introducido la letra asi que se ha generado automaticamente
                    System.out.println(rojo + "No ha introducido la letra de su DNI, ha sido completado automaticamente" + reset);
                    res = true;
                    break;
                case 7: // El documento introduccido es un NIF, pero no se ha introduccido la letra asi que se ha generado automaticamente
                    System.out.println(rojo + "No ha introducido la letra de su NIF, ha sido completado automaticamente" + reset);
                    res = true;
                    break;
                case 8: //El documento introducido es un NIE y mediante los medios disponibles sabemos que puede ser valido
                    System.out.println(rojo + "NIE detectado" + reset);
                    res = true;
                    break;
                case 9: //El documento introducido es un NIE, pero la letra introduccida no concuerda con la real, puede ser que este mal el DNI, o que la letra sea erronea, asi que pregunta si es correcta con otra letra
                    System.out.println(rojo + "El NIE introducido no es correcto ¿puede ser este? " + reset + getDni() + " Compruebelo y responda (s/n)");
                    respuesta = sc.nextLine();
                    if(respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("y") || respuesta.equals("yes") || respuesta.equals("S") || respuesta.equals("Si") || respuesta.equals("Y") || respuesta.equals("Yes") || respuesta.equals("YES") || respuesta.equals("SI") || respuesta.equals("ok") || respuesta.equals("OK") || respuesta.equals("Ok")) {
                        res = true;
                    } else{
                        res = false;
                    }
                    break;
                case 10: //El documento introducido es un NIE, pero no se ha introducido la letra asi que se ha generado automaticamente
                    System.out.println(rojo + "No ha introducido la letra de su NIE, ha sido completado automaticamente" + reset);
                    res = true;
                    break;
                default: // en caso de que nos sea ningun valor se Imprime un aviso de error
                    System.out.println(rojo + "Error en la lectura del DNI" + reset);
                    res = false;
                
            }
            } catch(Exception e){
                res = false;
            }
            return res;
    }
    /**
     * Comprueba si ya existe el cliente
     * @return validez
     */
    public boolean comprobarExitencia() {
        try {
            FileReader fr = new FileReader(new File("clientes.txt")); //Abrimos el fichero para su lectura
            BufferedReader br = new BufferedReader(fr); //Creamos un BufferedReader para facilitar la lectura
            String[] clientela = br.readLine().split("//"); //transforma la primera linea en un Array de Strings
            fr.close(); //Cerramos el lector
            Boolean dniCliente = false; //Inicializamos el boolenano que se encargara de decir si ya existe el cliente
            for (int i = 0; i < clientela.length; i++) { //Creamos un loop que compruebe para cada DNI almacenado si concuerda con el valor introducido
                if(getDni().equals(clientela[i])) {
                    //En caso afirmativo se da valor true al booleano y se cierra el bucle
                    dniCliente = true;
                    break;
                } 
            }
            if(dniCliente){ //En caslo de que el DNI este registrado se leen los datos del fichero de este cliente
                leerFicheroClientes();
                return true;
            } else { // En caso contrario iniciamos el registro de clientes
                return false;
            }
        } catch (Exception e) { //En caso de que no se puedan leer los ficheros, caso de que no existan pues hay que registrar al primer usuario
            return false;
        }
    }
        /**
     * Comprueba si ya existe el cliente y en caso contrario lo añade
     * @return 
     */
    private void comprobarBD() {
        try {
            FileReader fr = new FileReader(new File("clientes.txt")); //Abrimos el fichero para su lectura
            BufferedReader br = new BufferedReader(fr); //Creamos un BufferedReader para facilitar la lectura
            String[] clientela = br.readLine().split("//"); //transforma la primera linea en un Array de Strings
            fr.close(); //Cerramos el lector
            Boolean dniCliente = false; //Inicializamos el boolenano que se encargara de decir si ya existe el cliente
            for (int i = 0; i < clientela.length; i++) { //Creamos un loop que compruebe para cada DNI almacenado si concuerda con el valor introducido
                if(getDni().equals(clientela[i])) {
                    //En caso afirmativo se da valor true al booleano y se cierra el bucle
                    dniCliente = true;
                    break;
                } 
            }
            if(dniCliente){ //En caslo de que el DNI este registrado se leen los datos del fichero de este cliente
                leerFicheroClientes();
            } else { // En caso contrario iniciamos el registro de clientes
                annadirCliente();
            }
        } catch (Exception e) { //En caso de que no se puedan leer los ficheros, caso de que no existan pues hay que registrar al primer usuario
            annadirCliente();
        }
    }
    /**
     * lee el fichero donde se almacenan los datos del cliente y los guarda en el objeto
     */
    public void leerFicheroClientes() {
        try {
            FileReader fr = new FileReader(new File(getDni() + ".txt")); //Crea un lector para el archivo de los datos especificos de la matricula
            BufferedReader br = new BufferedReader(fr); //Creamos un BufferedReader para facilitar la lectura
            String[] noYAp = br.readLine().split("//");  //Leemos la primera linea del fichero y creamos un Array con los datos
            //Damos los valores del Array a los atributos del objeto
            nombre = noYAp[0];
            primerApellido = noYAp[1];
            segundoApellido = noYAp[2];
            String[] fechas = br.readLine().split("//");
            fechaNacimiento = LocalDate.parse(fechas[0], dtf);
            fechaDeCreacion = LocalDate.parse(fechas[1], dtf);
            fr.close(); //Cerramos el archivo
        } catch (Exception e) { //Error al leer los datos de los clientes
            library.LogEdit.annadirLog(e, clase, "leerFicheroClientes");
        }
    }
    
    /**
     * Lee los clientes existentes y los devuelve en un array
     * @return dnis
     */
    public static String[] getDNIs(){
        String[] res;
        try {
            FileReader fr = new FileReader(new File("clientes.txt"));
            BufferedReader br = new BufferedReader(fr);
            res = br.readLine().split("//");
        } catch (Exception e) {
            res = null;
            LogEdit.annadirLog(e, clase, "getDNIS");
        }
        return res;
    }
    
    
    /**
     * Añade un nuevo cliente
     */
    private void annadirCliente() {
        System.out.println(azul +"Para poder alquilar un vehiculo, es necesario que se registre"+ reset);
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Escriba su nombre (sin apellidos)");
            nombre = sc.nextLine();
            System.out.println("Escriba sus dos apellidos");
            String[] apellidos = sc.nextLine().split(" ");
            primerApellido = apellidos[0];
            segundoApellido = apellidos[1];
            DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            System.out.println("Escriba su fecha de nacimiento en formato DD/MM/AAAA (Ejemplo : 01/01/2000)");
            fechaNacimiento = LocalDate.parse(sc.nextLine(), dtf1);
            fechaDeCreacion = LocalDate.now();
            try {
                FileWriter fw = new FileWriter(new File("clientes.txt"), true);
                fw.append(getDni() + "//");
                fw.close();
                FileWriter fw1 = new FileWriter(new File(getDni() + ".txt"));
                fw1.write(getNombre() + "//" + getPrimerApellido() + "//" + getSegundoApellido() + "\n");
                fw1.write(getFechaNacimiento() + "//" + getFechaDeCreacion() + "\n");
                fw1.close();
            } catch (Exception e) {
                System.out.println(rojo + "Error al guardar los datos" + reset);
                library.LogEdit.annadirLog(e, clase, "annadirCliente2");
            }
            } catch (Exception e) {
                System.out.println(rojo + "Error al introducir los datos" + reset);
                library.LogEdit.annadirLog(e, clase, "annadirCliente1");
                annadirCliente();
        }
        
    }
    /**
     * Comprueba el tipo de Documento y su validez
     * @return validez
     */
    public int validarDNI() {
        String formato = "";
       //Pasamos el documento a un array de caracteres para poder comprobar el tipo de caracter
       char[] valores = getDni().toCharArray();
       //creamos un loop para repetir la accion con cada caracter
       for (int i = 0; i < valores.length; i++) {
           if(Character.getType(valores[i]) == Character.DECIMAL_DIGIT_NUMBER){
               //el caracter es decimal, por tanto lo indicamos como 0
               formato = formato + "0";
           }
           else if(Character.getType(valores[i]) == Character.UPPERCASE_LETTER){
               //el caracter es una letra y lo indicamos como X
               formato = formato + "X";
           }
           else if(Character.getType(valores[i]) == Character.LOWERCASE_LETTER){
               //el caracter es una letra y lo indicamos como X
               formato = formato + "X";
               //En este caso al ser minuscula la pasamos a mayuscula para que quede mas estetico
               valores[i] = Character.toUpperCase(valores[i]);
           }
       }
       //iniciamos la variable res donde se indica el valor a devolver
       int res = 0;
       //Valores res 0 no valida, 1 DNI valido, 2 NIF menor 14 años, 3 NIF valido, 4 DNI letra erronea, 5 NIF letra erronea, 6 DNI sin letra, 7 NIF sin letra, 8 NIE valido, 9 NIE letra erronea, 10 NIE sin letra, 11 DNI = ""
       //creamos un switch con el formato para indicar el resultado en funcion del tipo de matricula
       switch(formato){
            case "00000000X":
                String numeros = String.valueOf(valores, 0, 8);
                String letra = calcularLetraDNI(Integer.valueOf((numeros)));
                if(letra.equals(String.valueOf(valores[8]))) {
                    res = 1;
                } else {
                    valores[8] = letra.charAt(0);
                    res = 4;
                }
                setDni(String.valueOf(valores));
                break;
            case "X0000000X":
                if ("K".equals(String.valueOf(valores[0]))) {
                    res = 2;
                    setDni(String.valueOf(valores));
                }
                else {
                    switch(String.valueOf(valores[0])){
                        case "L":
                        case "M":
                            int[] numeroNIF = null;
                            for (int i = 1; i < 8; i++) {
                                numeroNIF[i] = valores[i];
                            }
                            String letraNIF = calcularLetraNIF(numeroNIF);
                            if(letraNIF.equals(String.valueOf(valores[8]))) {
                                res = 3;
                            } else {
                                valores[8] = letraNIF.charAt(0);
                                res = 5;
                            }
                            setDni(String.valueOf(valores));
                            break;
                        case "X":
                            String numerosNIE0 = "0" + String.valueOf(valores, 1, 7);
                            String letraNIE0 = calcularLetraDNI(Integer.valueOf((numerosNIE0)));
                            if(letraNIE0.equals(String.valueOf(valores[8]))) {
                                res = 8;
                            } else {
                                valores[8] = letraNIE0.charAt(0);
                                res = 9;
                            }
                            setDni(String.valueOf(valores));
                            break;
                        case "Y":
                            String numerosNIE1 = "1" + String.valueOf(valores, 1, 7);
                            String letraNIE1 = calcularLetraDNI(Integer.valueOf((numerosNIE1)));
                            if(letraNIE1.equals(String.valueOf(valores[8]))) {
                                res = 8;
                            } else {
                                valores[8] = letraNIE1.charAt(0);
                                res = 9;
                            }
                            setDni(String.valueOf(valores));
                            break;
                        case "Z":
                            String numerosNIE2 = "2" + String.valueOf(valores, 1, 7);
                            String letraNIE2 = calcularLetraDNI(Integer.valueOf((numerosNIE2)));
                            if(letraNIE2.equals(String.valueOf(valores[8]))) {
                                res = 8;
                            } else {
                                valores[8] = letraNIE2.charAt(0);
                                res = 9;
                            }
                            setDni(String.valueOf(valores));
                            break;
                        default:
                            res = 0;
                            break;
                    }
                }
                break;
            case "00000000":
                String numerosDNI = String.valueOf(valores);
                String letraDNI = calcularLetraDNI(Integer.valueOf((numerosDNI)));
                setDni(String.valueOf(valores) + letraDNI.charAt(0));
                res = 6;
                break;
            case "X0000000":
                if ("K".equals(String.valueOf(valores[0]))) {
                    res = 2;
                }
                else {
                    switch(String.valueOf(valores[0])){
                        case "L":
                        case "M":
                            int[] numeroNIF = null;
                            for (int i = 1; i < 8; i++) {
                                numeroNIF[i] = valores[i];
                            }
                            String letraNIF = calcularLetraNIF(numeroNIF);
                            setDni(String.valueOf(valores) + letraNIF.charAt(0));
                            res = 7;
                            break;
                        case "X":
                            String numerosNIE0 = "0" + String.valueOf(valores, 1, 7);
                            String letraNIE0 = calcularLetraDNI(Integer.valueOf((numerosNIE0)));
                            res = 10;
                            setDni(String.valueOf(valores) + letraNIE0.charAt(0));
                            break;
                        case "Y":
                            String numerosNIE1 = "1" + String.valueOf(valores, 1, 7);
                            String letraNIE1 = calcularLetraDNI(Integer.valueOf((numerosNIE1)));
                            res = 10;
                            setDni(String.valueOf(valores) + letraNIE1.charAt(0));
                            break;
                        case "Z":
                            String numerosNIE2 = "2" + String.valueOf(valores, 1, 7);
                            String letraNIE2 = calcularLetraDNI(Integer.valueOf((numerosNIE2)));
                            res = 10;
                            setDni(String.valueOf(valores) + letraNIE2.charAt(0));
                            break;
                        default:
                            res = 0;
                            break;
                    }
                }
                break;
            case "":
                res = 11;
                break;
                
            default:
                res= 0;
                break;
       }
       //cambiamos el valor de matricula por el de esta con letras mayusculas
       
       //devolvemos el valor resultado
       return res;
   }
    
    /**
     * Calcula la letra de un DNI/NIE
     * @param doc numero DNI/NIE
     * @return Letra DNI/NIE
     */
    public String calcularLetraDNI(int doc){
        int numeroLetra = doc%23;
        return dniNumero[numeroLetra];
    }
    
    /**
     * Calcula la letra del NIF
     * @param doc numero NIF
     * @return Letra NIF
     */
    public String calcularLetraNIF(int[] doc) {
        //Sumamos los digitos pares
        int sumaPares = doc[1] + doc[3] + doc[5];
        //Multiplicamos cada digito por dos y sumamos los digitos de los resultados. sumamos todos los resultados
        int calculoImpares = 0;
        for (int i = 0; i < 7; i += 2) {
            int calculo;
            if (doc[i] < 5) {
                calculo = doc[i] * 2;
            } else {
                calculo = 1 + 2*(doc[i] - 5);
            }
            calculoImpares = calculoImpares + calculo; 
        }
        int valorSuma = sumaPares + calculoImpares;
        char[] digitos = ("" + valorSuma).toCharArray();
        int numeroLetra = (int) digitos[digitos.length - 1];
        return nifNumero[numeroLetra];
    }

    /**
     * @return the dni
     */
    public String getDni() {
        return dni;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return the primerApellido
     */
    public String getPrimerApellido() {
        return primerApellido;
    }

    /**
     * @return the segundoApellido
     */
    public String getSegundoApellido() {
        return segundoApellido;
    }

    /**
     * @return the fechaNacimiento
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * @return the fechaDeCreacion
     */
    public LocalDate getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    /**
     * Devuelve los datos del cliente
     * @return String con los datos
     */
    @Override
    public String toString() {
        int tipoDoc = validarDNI();
        String doc;
        switch(tipoDoc){
            case 1:
                doc = "DNI";
                break;
            case 3:
                doc = "NIF";
                break;
            case 8:
                doc = "NIE";
                break;
            default:
                doc = "numero de identificacion";
                break;
        }
        return nombre + " " + primerApellido + " " + segundoApellido + ", cuyo " + doc + " es: " + dni;
    }
    /**
     * Devuelve un String con indicando el DNI/NIF/NIE
     * @return String
     */
        public String toStringHelp() {
        int tipoDoc = validarDNI();
        comprobarBD();
        String doc;
        switch(tipoDoc){
            case 1:
                doc = "DNI";
                break;
            case 3:
                doc = "NIF";
                break;
            case 8:
                doc = "NIE";
                break;
            default:
                doc = "numero de identificacion";
                break;
        }
        return nombre + " " + primerApellido + " " + segundoApellido + ", cuyo " + doc + " es: " + dni;
    }

    /**
     * @param dni the dni to set
     */
    public void setDni(String dni) {
        this.dni = dni;
    }
    /**
     * Sistema para editar los datos personales de un usuario
     */
    public void editarCliente() {
        System.out.println(azul + "Para editar datos iremos indicandole el valor a editar, en caso de querer el valor ya exitente presione la tecla INTRO. El dni no se permite modificar." + reset);
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Editando Nombre (acutalmente: " + this.nombre + ")");
            String nombre = sc.nextLine();
            System.out.println("Editando primer apellido (acutalmente: " + this.primerApellido + ")");
            String primerApellido = sc.nextLine();
            System.out.println("Editando segundo apellido (acutalmente: " + this.segundoApellido + ")");
            String segundoApellido = sc.nextLine();
            System.out.println("Editando fecha de nacimiento (acutalmente: " + dtfin.format(fechaNacimiento) + ")");
            String fechaNacimiento = sc.nextLine();
            LocalDate fechaDeNacimiento;
            
            
            System.out.println(azul + "Proceso de edicion terminado, porfavor confirme los siguientes cambios: " + reset);
            System.out.println("Numero ID: " + dni);
            if (nombre.equals("")) {
                System.out.println("Nombre: " + verde + this.nombre + reset);
                nombre = this.nombre;
            } else {
                System.out.println("Nombre: " + rojo + this.nombre + reset + "---->" + verde + nombre + reset);
            }
            if(primerApellido.equals("")) {
                System.out.println("Primer apellido: " + verde + this.primerApellido + reset);
                primerApellido = this.primerApellido;
            } else{
                System.out.println("Primer apellido: " + rojo + this.primerApellido + reset + "---->" + verde + primerApellido + reset);
            }
            if(segundoApellido.equals("")) {
                System.out.println("Segundo apellido: " + verde + this.segundoApellido + reset);
                segundoApellido = this.segundoApellido;
            } else{
                System.out.println("Segundo apellido: " + rojo + this.segundoApellido + reset + "---->" + verde + segundoApellido + reset);
            }
            if(fechaNacimiento.equals("")) {
                System.out.println("Fecha de nacimiento: " + verde + dtfin.format(this.fechaNacimiento) + reset);
                fechaDeNacimiento = this.fechaNacimiento;
            } else{
                System.out.println("Fecha de nacimiento: " + rojo + dtfin.format(this.fechaNacimiento) + reset + "---->" + verde + fechaNacimiento + reset);
                fechaDeNacimiento = LocalDate.parse(fechaNacimiento, dtfin);
            }
            System.out.println(azul + "Esta todo correcto? (s/n)" + reset);
            String respuesta = sc.nextLine();
            if(respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("y") || respuesta.equals("yes") || respuesta.equals("S") || respuesta.equals("Si") || respuesta.equals("Y") || respuesta.equals("Yes") || respuesta.equals("YES") || respuesta.equals("SI") || respuesta.equals("ok") || respuesta.equals("OK") || respuesta.equals("Ok")) {
                System.out.println("Guardando Datos");
                this.nombre = nombre;
                this.primerApellido = primerApellido;
                this.segundoApellido = segundoApellido;
                this.fechaNacimiento = fechaDeNacimiento;
                actualizarDatos();
                
            } else {
                System.out.println("Se han restablecido los datos anteriores");
            }
        } catch (Exception e) {
            LogEdit.annadirLog(e, clase, "EditorClientes");
        }
    }

    private void actualizarDatos() {
        try {
            FileWriter fw = new FileWriter(new File(getDni() + ".txt"));
                fw.write(getNombre() + "//" + getPrimerApellido() + "//" + getSegundoApellido() + "\n");
                fw.write(getFechaNacimiento() + "//" + getFechaDeCreacion() + "\n");
                fw.close();
        } catch (Exception e) {
            Comunes.output("Error al guardar datos", rojo);
            LogEdit.annadirLog(e, clase, "actualizarDatos");
        }
    }
    /**
     * Crea un menu donde editar y eliminar los alquileres de un usuario
     */
    public void panelAlquiler() {
        Locale espanna = new Locale("es", "ES");
        NumberFormat nf = NumberFormat.getCurrencyInstance(espanna);
        System.out.println("¿Que alquiler desea modificar?");
        Alquiler a = new Alquiler();
        ArrayList<String> alquileresEditables = new ArrayList<>();
        String[] alquileres = a.getAlquileresPropios(dni).split("//");
        for (int i = 0; i < alquileres.length; i++) {
            String[] datos = alquileres[i].split("::");
            a.setMatricula(datos[0]);
            a.getDatosFichero();
            a.setFechaDeAlquiler(LocalDate.parse(datos[1]));
            a.setFechaDevolucion(LocalDate.parse(datos[2]));
            if(a.getFechaDeAlquiler().isBefore(LocalDate.now()) || a.getFechaDeAlquiler().isEqual(LocalDate.now())){
            System.out.println(rojo + String.valueOf(i) + ". " + a + ", lo cual ha costado un total de: " + nf.format(a.getPrecioTotal(new BigDecimal(a.getFechaDeAlquiler().until(a.getFechaDevolucion(), ChronoUnit.DAYS)))) + " Este alquiler va desde " + a.getFechaDeAlquiler() + " a " + a.getFechaDevolucion() + reset);
            alquileresEditables.add(i, null);
            } else{
            System.out.println(verde + String.valueOf(i) + ". " + a + ", lo cual ha costado un total de: " + nf.format(a.getPrecioTotal(new BigDecimal(a.getFechaDeAlquiler().until(a.getFechaDevolucion(), ChronoUnit.DAYS)))) + " Este alquiler va desde " + a.getFechaDeAlquiler() + " a " + a.getFechaDevolucion() + reset);
            alquileresEditables.add(i, alquileres[i]);
            }
        }
        try{
        Scanner sc = new Scanner(System.in);
        int respuesta = sc.nextInt();
        sc.nextLine();
        if(respuesta < alquileres.length){
            String[] datos;
            try{
                datos = alquileresEditables.get(respuesta).split("::");
            } catch(Exception e){
                System.out.println(rojo + "Alquiler no editable"+ reset);
                return;
            }
            a.setMatricula(datos[0]);
            a.getDatosFichero();
            a.setFechaDeAlquiler(LocalDate.parse(datos[1]));
            a.setFechaDevolucion(LocalDate.parse(datos[2]));
            System.out.println("Editando alquiler: " + a + ", lo cual ha costado un total de: " + nf.format(a.getPrecioTotal(new BigDecimal(a.getFechaDeAlquiler().until(a.getFechaDevolucion(), ChronoUnit.DAYS)))) + " Este alquiler va desde " + a.getFechaDeAlquiler() + " a " + a.getFechaDevolucion());
            a.setCliente(this);
            a.panelAlquiler(dni);
        } else{
            System.out.println(rojo + "Indique un alquiler de la lista" + reset);
        }
        } catch(Exception e){
            System.out.println(rojo + "Indique un valor valido"+ reset);
        }
    }
}

