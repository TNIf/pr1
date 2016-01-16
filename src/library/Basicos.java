/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import administracionDeProductos.Motos;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Miguel Pazos y Emiliano Franco
 */
public class Basicos {
    //Valores de los codigos paea modificar el color del texto
    private static final String reset = "\u001B[0m";
    private static final String rojo = "\u001B[31m";
    private static final String verde = "\u001B[32m";
    
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static int cuenta;
    private static int cuentaClientes;
    private static String[] ocupados;
    private static final String linea1 = "Archivo de datos basicos para el funcionamiento rapido del programa \n";
    private static final String ficheroBase = "bases.txt";
    private static final String clase = "Basicos";

    /**
     * Elimina un alquiler
     * @param matricula
     * @param dni
     * @param fechaDeAlquiler
     * @param fechaDevolucion 
     */
    public static void borrarAlquiler(String matricula, String dni, LocalDate fechaDeAlquiler, LocalDate fechaDevolucion) {
        String nuevo = "";
        for (String ocupado : ocupados) {
            String[] alquiler = ocupado.split("::");
            if(((!matricula.equals(alquiler[0]) || !fechaDeAlquiler.isEqual(LocalDate.parse(alquiler[1], dtf))) || !fechaDevolucion.isEqual(LocalDate.parse(alquiler[2], dtf))) || !dni.equals(alquiler[3])){
                nuevo = nuevo + String.join("::", alquiler) + "//";
            }
        }
        try {
            //Abrimos el archivo base
            FileWriter fw = new FileWriter(new File(ficheroBase));
            //Escribimos la primera linea, la introduccion del archivo
            fw.write(linea1);
            //Escribimos la  cuenta de motos
            fw.write(String.valueOf(cuenta) + "\n");
            //Escribimos las matriculas ocupadas con sus fechas y cliente asociado
                fw.write(nuevo + "\n");
                ocupados = nuevo.split("//");
                //Escribimos la cuenta de clientes
                fw.write(String.valueOf(cuentaClientes));
            //cerramos el archivo
            fw.close();
        } catch (IOException e) {
            //Mandamos guardar los posibles fallos
            System.out.println(e);
            LogEdit.annadirLog(e, clase, "borrarAlquiler");
        }
        
    }


    /**
     * Inicia las comprobaciones basicas
     */
    public Basicos() {
        //Indicamos el inicio de las pruebas
        System.out.println("Iniciando Pruebas iniciales:");
        //Indicamos que vamos a obtener las configuraciones iniciales
        System.out.print("Obteniendo configuraciones iniciales..............");
        //Ejecutamos el metodo para Obtener los valores iniciales
        getValoresIniciales();
        //Indicamos que vamos a realizar unas pruebas a los datos
        System.out.print("Realizando pruebas de los datos(1/2)..............");
        //iniciamos el metodo que realizará las pruebas
        testDeErrores();
        //Indicamos que vamos a realizar unas pruebas a los datos 2
        System.out.print("Realizando pruebas de los datos(2/2)..............");
        //iniciamos el metodo que realizará las pruebas
        testDeErrores2();
        //Indicamos el inicio de la limpieza de los archivos antiguos
        System.out.print("Realizando limpieza de los datos antiguos.........");
        //Iniciamos la limpieza
        limpiarAntiguos();

    }
    /**
     * Ejecuta varios metodos de la clase en funcion de lo que se necesita
     * @param i tipo de ejecucion
     */
    public Basicos(int i) {
        switch(i){
            case 1:
                //En este caso es necesario revisar el archivo matriculas.txt
                System.out.print("Realizando pruebas de los datos...................");
                //iniciamos el metodo que realizará las pruebas
                testDeErrores();
                break;
            case 2:
                //En este caso se revisan los posibles errores con los archivos especificos de las matriculas
                System.out.print("Realizando verificiones de los datos..............");
                verificarMatriculas();
                break;
        }
    }
            
    /**
     * incrementa el numero de motos en el almacén
     */
    public static void addMoto() {
        try {
            //Abrimos el archivo base
            FileWriter fw = new FileWriter(new File(ficheroBase));
            //Escribimos la primera linea, la introduccion del archivo
            fw.write(linea1);
            //Escribimos la  cuenta de motos incrementada
            fw.write(String.valueOf(++cuenta) + "\n");
            //Escribimos la cuenta de matriculas ocupados
            String linea4 = String.join("//", ocupados);
            fw.write(linea4);
            //cerramos el archivo
            fw.close();
        } catch (IOException e) {
            //Mandamos guardar los posibles fallos
            LogEdit.annadirLog(e, clase, "addMoto");
        }
    }
    
    /**
     * decrementa el numero de motos en el almacén
     */
    public static void delMoto() {
        try {
            //Abrimos el archivo base
            FileWriter fw = new FileWriter(new File(ficheroBase));
            //Escribimos la primera linea, la introduccion del archivo
            fw.write(linea1);
            //Escribimos la  cuenta de motos decrementada
            fw.write(String.valueOf(--cuenta) + "\n");
            //Escribimos las matriculas ocupadas
            String linea4 = String.join("//", ocupados);
            fw.write(linea4 + "\n");
            //Escribimos la cuenta de clientes
            fw.write(String.valueOf(cuentaClientes));
            //cerramos el archivo
            fw.close();
        } catch (IOException e) {
            //Mandamos guardar los posibles fallos
            LogEdit.annadirLog(e, clase, "delMoto");
        }
    }
    
    /**
     * Añade una nuevvva matricula ocupada
     * @param datos 
     */
    public static void addOcupado(String[] datos) {
        try {
            if (ocupados[0].equals("")) {
                ocupados = null;
            }
            //Abrimos el archivo base
            FileWriter fw = new FileWriter(new File(ficheroBase));
            //Escribimos la primera linea, la introduccion del archivo
            fw.write(linea1);
            //Escribimos la  cuenta de motos
            fw.write(String.valueOf(cuenta) + "\n");
            //Escribimos las matriculas ocupadas con sus fechas y cliente asociado
            if (ocupados == null) {
                fw.write(String.join("::", datos) + "//");
                ocupados = (String.join("::", datos) + "//").split("//");
            } else {
                String linea4 = String.join("//", ocupados);
                String oses = (linea4 + "//" + String.join("::", datos) + "//");
                fw.write(oses);
                ocupados = oses.split("//");
                //Escribimos la cuenta de clientes
            }
            fw.write(String.valueOf("\n" + cuentaClientes));
            //cerramos el archivo
            fw.close();
        } catch (IOException e) {
            //Mandamos guardar los posibles fallos
            System.out.println(e);
            LogEdit.annadirLog(e, clase, "addMoto");
        }
    }
    /**
     * Lee el archivo base y guarda sus valores en atributos
     */
    private void getValoresIniciales() {
        try {
            //Abre el archivo base
            FileReader fr = new FileReader(new File(ficheroBase));
            //Crea un bufferedReader para facilitar la lectura
            BufferedReader br = new BufferedReader(fr);
            //leemos la primera linea sin guardarla, porque no contiene ningun dato util
            br.readLine();
            //leemos la segunda linea y la pasamos a int
            cuenta = Integer.valueOf(br.readLine());
            //leemos la tercera linea, vehiculos ocupados
            String linea4 = br.readLine();
            if(linea4 == null || linea4.equals("")){
                ocupados = null;
            } else{
            ocupados = linea4.split("//");
            }
            //Leemos la cuenta de clientes
            cuentaClientes = Integer.valueOf(br.readLine());
            //cerramos el archivo
            fr.close();
            //Imprimimos un aviso de que se ha completado la obtencion de los valores iniciales
            System.out.println(verde +"Completado" + reset);
        } catch (IOException | NumberFormatException e) {
            //Imprimimos un aviso indicando el fracaso
            System.out.println(rojo +"Archivo no encontrado" + reset);
            //Imprimimos un aviso indicando que vamos a generar un archivo base, debido a que no es posible leerlo, lo que nos lleva a pensar que aun no existe
            System.out.print("Generando archivo base............................");
            //iniciamos el metodo que genera el archivo
            generarArhivo();
        }
    }
    /**
     * Genera un archivo base para guardar datos como las configuraciones y las cuentas ya calculadas
     */
    private void generarArhivo() {
        try {
            //creamos el archivo base
             FileWriter fw = new FileWriter(new File(ficheroBase));
             //escribimos la primera linea de explicacion
             fw.write(linea1);
             //Escribimos la cantidad de motos, en este caso 0;
             fw.write("0"+ "\n");
             // indicamos el valor del atributo de la cuenta de motos
             cuenta = 0;
             //Guardamos la cuenta de matriculas ocupados
             fw.write("\n");
             ocupados = null;
             //Guardamos la cuenta de clientes
             fw.write("0");
             //cerramos el archivo base
             fw.close();
             //Imprimimos un aviso indicando el final exitoso
             System.out.println(verde +"Completado" + reset);
        } catch (Exception e) {
            //guardamos los errores que puedan suceder
            LogEdit.annadirLog(e, clase, "generarArchivo");
            //Imprimimos un aviso indicando el fracaso de la accion
            System.out.println(rojo +"Error al generar el archivo" + reset);
        }
    }
    
    /**
     * Realiza una comprobacion de los valores guardados y los reales
     */
    private void testDeErrores() {
        //Comprueba si el valor de la cuenta de motos es igual al real
        if (cuenta != getCuentaMatriculas()) {
            //Al saber que no es correctov el valor, indica en pantalla que ha detectado un fallo
            System.out.println(rojo +"Fallo detectado" + reset);
            //Indica por pantalla que va a iniciar la reparacion de los errores
            System.out.print("Reparando los errores detectados..................");
            //modifica el valor de la cuenta de motos por el real
            cuenta = getCuentaMatriculas();
            //Intenta guardarlo en el archivo base
            try {
                //abre el archivo base
             FileWriter fw = new FileWriter(new File(ficheroBase));
             //escribe la primera linea
             fw.write(linea1);
             //escribe el nuevo valor
             fw.write(String.valueOf(cuenta)+ "\n");
             String linea4;
             if(ocupados != null){
             //Escriibe el valor de la cuenta de ocupados
             linea4 = String.join("//", ocupados);
             } else{
                linea4 = "";
             }
             fw.write(linea4 + "\n");
             //Cuenta de clientes
             fw.write(String.valueOf(cuentaClientes));
             //cierra el archivo
             fw.close();
             //imprime en pantalla un aviso de que la accion a sido completada
                System.out.println(verde +"Completado" + reset);
        } catch (Exception e) {
            //logea el posible error
            LogEdit.annadirLog(e, clase,  "testDeErrores");
            //Imprime en pantalla un aviso de que no se pudo soluccionar
                System.out.println(rojo +"Error sin soluccion" + reset);
        }
            //limitamos la comprobacion de fallos a que al menos tenga una matricula guardada
            if (cuenta != 0) {
                //Se indica en pantalla el inicio de la comprobacion de otros fallos, motivada por la existencia del fallo anterior
                System.out.print("Comprobando otros posibles daños..................");
                //Se ejecuta el metodo encargado de verificar las Matriculas
                verificarMatriculas();
                }
        } else {
            //Se imprime en pantalla un aviso diciendo que el proceso se ha completado correctamente
            System.out.println(verde +"Completado" + reset);
        }
    }
    
    /**
     * Realizamos el segundo test de errores para los clientes
     */
    private void testDeErrores2() {
        //Comprueba si el valor de la cuenta de motos es igual al real
        if (cuentaClientes != getCuentaClientes()) {
            //Al saber que no es correctov el valor, indica en pantalla que ha detectado un fallo
            System.out.println(rojo +"Fallo detectado" + reset);
            //Indica por pantalla que va a iniciar la reparacion de los errores
            System.out.print("Reparando los errores detectados..................");
            //modifica el valor de la cuenta de motos por el real
            cuentaClientes = getCuentaClientes();
            //Intenta guardarlo en el archivo base
            try {
                //abre el archivo base
             FileWriter fw = new FileWriter(new File(ficheroBase));
             //escribe la primera linea
             fw.write(linea1);
             //escribe el nuevo valor
             fw.write(String.valueOf(cuenta)+ "\n");
             String linea4;
             if(ocupados != null){
             //Escriibe el valor de la cuenta de ocupados
             linea4 = String.join("//", ocupados);
             } else{
                linea4 = "";
             }
             fw.write(linea4 + "\n");
             //Escribimos la cuenta de clientes
             fw.write(String.valueOf(cuentaClientes));
             //cierra el archivo
             fw.close();
             //imprime en pantalla un aviso de que la accion a sido completada
                System.out.println(verde +"Completado" + reset);
        } catch (Exception e) {
            //logea el posible error
            LogEdit.annadirLog(e, clase,  "testDeErrores2");
            //Imprime en pantalla un aviso de que no se pudo soluccionar
                System.out.println(rojo +"Error sin soluccion" + reset);
        }
            //limitamos la comprobacion de fallos a que al menos tenga una matricula guardada
            if (cuentaClientes != 0) {
                //Se indica en pantalla el inicio de la comprobacion de otros fallos, motivada por la existencia del fallo anterior
                System.out.print("Comprobando otros posibles daños..................");
                //Se ejecuta el metodo encargado de verificar las Matriculas
                verificarClientes();
                }
        } else {
            //Se imprime en pantalla un aviso diciendo que el proceso se ha completado correctamente
            System.out.println(verde +"Completado" + reset);
        }
    }
    
    
    /**
     * Lee el fichero donde se guardan las matriculas y las cuenta
     * @return Numero de matriculas almacenadas
     */
    private int getCuentaMatriculas() {
        //Iniciamos la variable cuentaM, para que no se produzcan errores si el programa falla al leer el archivo
        int cuentaM;
        try {
            //abrimos el archivo
            FileReader fr = new FileReader(new File("matriculas.txt"));
            //Creamos un BufferedReader para falicitar su lectura
            BufferedReader br = new BufferedReader(fr);
            //leemos una linea, ya que el fichero no contiene ningun salto de linea
            String datos = br.readLine();
            //Construimos un Array de strings dividiendo los datos del fichero con los separadores '//'
            String[] lista = datos.split("//");
            //Guardamos el valor del tamaño del array en cuentaM
            cuentaM = lista.length;
            //cerramos el fichero
            fr.close();
        } catch (Exception e) {
            //le damos un valor 0 porque el metodo tiene que devolver un valor, y si no ha sido posible leer el fichero lo mas probable es que no exista, y por tanto no exista ninguna matricula todavia
             cuentaM = 0;
        }
        //devolvemos la cuenta de matriculas
        return cuentaM;
    }
    /**
     * Lee el fichero donde se guardan los clientes y los cuenta
     * @return Numero de clientes almacenados
     */
    private int getCuentaClientes() {
        //Iniciamos la variable cuentaM, para que no se produzcan errores si el programa falla al leer el archivo
        int cuentaM;
        try {
            //abrimos el archivo
            FileReader fr = new FileReader(new File("clientes.txt"));
            //Creamos un BufferedReader para falicitar su lectura
            BufferedReader br = new BufferedReader(fr);
            //leemos una linea, ya que el fichero no contiene ningun salto de linea
            String datos = br.readLine();
            //Construimos un Array de strings dividiendo los datos del fichero con los separadores '//'
            String[] lista = datos.split("//");
            //Guardamos el valor del tamaño del array en cuentaM
            cuentaM = lista.length;
            //cerramos el fichero
            fr.close();
        } catch (Exception e) {
            //le damos un valor 0 porque el metodo tiene que devolver un valor, y si no ha sido posible leer el fichero lo mas probable es que no exista, y por tanto no exista ninguna matricula todavia
             cuentaM = 0;
        }
        //devolvemos la cuenta de matriculas
        return cuentaM;
    }
    /**
     * Comprueba que todas las matriculas tienen un archivo asociado donde se encuentran los datos mas especificos
     */
    private void verificarMatriculas() {
        try {
            //Abrimos el archivo donde se guardan las matriculas
            FileReader fr = new FileReader(new File("matriculas.txt"));
            //Creamos el BufferedReader para simplificar la lectura
            BufferedReader br = new BufferedReader(fr);
            //leemos la primera y unica linea del fichero
            String datos = br.readLine();
            //Creamos un array que contiene las matriculas
            String[] lista = datos.split("//");
            //inicializamos el string bug, donde se almacenaran las matriculas sin un datos especificos asociados
            String bug = "";
            //Creamos un bucle que se repita tantas veces como matriculas hay
            for (int i = 0; i < lista.length; i++) {
                //creamos un try/catch para comprobar si el archivo con los datos especificos se puede leer
                try {
                    //creamos el lector y abrimos el archivo
                FileReader fs = new FileReader(new File(lista[i] + ".txt"));
                //Construimos un BufferedReader para simplificar la lectura, que no es necesario en este caso, pero asi queda mas ordenado el codigo, si usamos siempre el mismo sistema de lectura
                BufferedReader bs = new BufferedReader(fs);
                //leemos una linea
                bs.readLine();
                //cerramos el archivo
                fs.close();
                //en caso de llegar hasta aqui significa que el archivo donde se encuentran los valores especificos es accesible y existe, por tanto no hay ningun fallo, en caso de no llegar se ejecutaria la accion contenida en el catch, almacenar la matricula
                }catch(Exception er) {
                    //le añadimos la matricula afectada a la variable bug, junto con el resto de matriculas ya almacenadas y un separador
                    bug = bug + lista[i] + ":";
                }
            }
            //cerramos el archivo matriculas.txt
            fr.close();
            //revisamos si bug es nulo, lo que significa que no hay ningun archivo dañado
            if (bug.equals("")) {
                //al estar todo bien imprimimos un aviso en pantalla indicando que esta todo correcto
                System.out.println(verde + "Completado" + reset);
            } else {
                //en caso de que bug no sea nulo significa que hay datos dañados, por tanto lo avisamos en pantalla
                System.out.println(rojo+ "Datos no validos detectados" + reset);
                //Iniciamos el reparador que eliminara las matriculas que tengan el archivo asociado dañado
                repararExceso(bug);
            }
        } catch (Exception e) {
            //guarda la exception para mas tarde poder soluccionar los problemas que desconozcamos
            LogEdit.annadirLog(e, clase, "verificarMatriculas");
            //se avisa en pantalla del error
            System.out.println(rojo + "Error al intentar acceder a los datos, reparacion no posible" + reset);
        }
    }
    
    /**
     * Comprueba que todos los clientes tienen su archivo asociado
     */
    private void verificarClientes() {
        try {
            //Abrimos el archivo donde se guardan los clientes
            FileReader fr = new FileReader(new File("clientes.txt"));
            //Creamos el BufferedReader para simplificar la lectura
            BufferedReader br = new BufferedReader(fr);
            //leemos la primera y unica linea del fichero
            String datos = br.readLine();
            //Creamos un array que contiene las matriculas
            String[] lista = datos.split("//");
            //inicializamos el string bug, donde se almacenaran los DNIs sin un datos especificos asociados
            String bug = "";
            //Creamos un bucle que se repita tantas veces como DNIs hay
            for (int i = 0; i < lista.length; i++) {
                //creamos un try/catch para comprobar si el archivo con los datos especificos se puede leer
                try {
                    //creamos el lector y abrimos el archivo
                FileReader fs = new FileReader(new File(lista[i] + ".txt"));
                //Construimos un BufferedReader para simplificar la lectura, que no es necesario en este caso, pero asi queda mas ordenado el codigo, si usamos siempre el mismo sistema de lectura
                BufferedReader bs = new BufferedReader(fs);
                //leemos una linea
                bs.readLine();
                //cerramos el archivo
                fs.close();
                //en caso de llegar hasta aqui significa que el archivo donde se encuentran los valores especificos es accesible y existe, por tanto no hay ningun fallo, en caso de no llegar se ejecutaria la accion contenida en el catch, almacenar la matricula
                }catch(Exception er) {
                    //le añadimos la matricula afectada a la variable bug, junto con el resto de matriculas ya almacenadas y un separador
                    bug = bug + lista[i] + ":";
                }
            }
            //cerramos el archivo clientes.txt
            fr.close();
            //revisamos si bug es nulo, lo que significa que no hay ningun archivo dañado
            if (bug.equals("")) {
                //al estar todo bien imprimimos un aviso en pantalla indicando que esta todo correcto
                System.out.println(verde + "Completado" + reset);
            } else {
                //en caso de que bug no sea nulo significa que hay datos dañados, por tanto lo avisamos en pantalla
                System.out.println(rojo+ "Datos no validos detectados" + reset);
                //Iniciamos el reparador que eliminara las matriculas que tengan el archivo asociado dañado
                repararClientes(bug);
            }
        } catch (Exception e) {
            //guarda la exception para mas tarde poder soluccionar los problemas que desconozcamos
            LogEdit.annadirLog(e, clase, "verificarClientes");
            //se avisa en pantalla del error
            System.out.println(rojo + "Error al intentar acceder a los datos, reparacion no posible" + reset);
        }
    }
    
    /**
     * Elimina las matriculas que no tienen ningun archivo asociado
     * @param bug Las matriculas afectadas
     */
    private void repararExceso(String bug) {
        //Imprime en pantalla un aviso de que va a iniciar la reparacion
        System.out.print("Eliminando datos no validos.......................");
        //crea unn array con las matriculas afectadas
        String[] matriculas = bug.split(":");
        try {
            //Creamos un lector y abrimos el fichero
            FileReader fr = new FileReader(new File("matriculas.txt"));
            //Creamos un bufferedReader para facilitar la lectura del fichero
            BufferedReader br = new BufferedReader(fr);
            //leemos la primera linea del archivo, unica existente
            String datos = br.readLine();
            //Creampos un array con las matriculas almacena
            String[] lista = datos.split("//");
            //Cerramos el fichero
            fr.close();
            //iniciamos el variable nuevaCuenta con un valor ya definido, 0
            int nuevaCuenta = 0;
            //Creamos un escritor y abrimos el fichero matriculas.txt
            FileWriter fw = new FileWriter(new File("matriculas.txt"));
            //creamos un loop para ejecutar una accion tantas veces como matriculas existan en el fichero matriculas.txt
            for (int i = 0; i < lista.length; i++) {
                //Inicializamos el booleano valido con valor true
                Boolean valido  = true;
                //Creamos un loop para repetir una accion tantas veces como matriculas afectadas existan
                for (int j = 0; j < matriculas.length; j++) {
                    //comprobamos si la matricula afectada concuerda con la matricula del fichero matriculas.txt
                    if (matriculas[j].equals(lista[i])) {
                        //le damos el valor false a valido, debido a que la matricula esta afectada
                        valido = false;
                    }
                }
                //si valido es verdadero, quiere decir que no hay ninguna matricula afectada
                if (valido == true) {
                    //guardamos en matriculas.txt el valor de la matricula y añadimos un separador
                   fw.write(lista[i] + "//");
                   //Incrementamos el valor de la nueva cuuenta de matriculas
                   nuevaCuenta++;
                }
            }
            //cerramos el archivo matriculas.txt
            fw.close();
            //damos a cuenta el nuevo valor tras modificar los archivos
            cuenta = nuevaCuenta;
            //Imprimimos en pantalla un aviso indicando el exito de la accion
            System.out.println(verde + "Completado" + reset);
            //iniciamos el metodo encargado de guardar los nuevo datos
            guardarNuevosValores();
        } catch (Exception e) {
            //guardamos la excepcion para posterior soluccion
            LogEdit.annadirLog(e, clase, "repararExceso");
            //avisamos de la imposibilidad de soluccionar los problemas
            System.out.println(rojo + "No ha sido posible completar la reparación" + reset);
        }
    }
    
    /**
     * Repara el exceso de clientes comparado con los archivos asociados
     * @param bug Clientes en exceso
     */
    private void repararClientes(String bug) {
        //Imprime en pantalla un aviso de que va a iniciar la reparacion
        System.out.print("Eliminando datos no validos.......................");
        //crea unn array con los DNIs afectados
        String[] dnis = bug.split(":");
        try {
            //Creamos un lector y abrimos el fichero
            FileReader fr = new FileReader(new File("clientes.txt"));
            //Creamos un bufferedReader para facilitar la lectura del fichero
            BufferedReader br = new BufferedReader(fr);
            //leemos la primera linea del archivo, unica existente
            String datos = br.readLine();
            //Creampos un array con los DNIs almacenados
            String[] lista = datos.split("//");
            //Cerramos el fichero
            fr.close();
            //iniciamos el variable nuevaCuenta con un valor ya definido, 0
            int nuevaCuenta = 0;
            //Creamos un escritor y abrimos el fichero clientes.txt
            FileWriter fw = new FileWriter(new File("clientes.txt"));
            //creamos un loop para ejecutar una accion tantas veces como clientes existan en el fichero clientes.txt
            for (int i = 0; i < lista.length; i++) {
                //Inicializamos el booleano valido con valor true
                Boolean valido  = true;
                //Creamos un loop para repetir una accion tantas veces como clientes afectados existan
                for (int j = 0; j < dnis.length; j++) {
                    //comprobamos si el DNI afectado concuerda con el DNI del fichero clientes.txt
                    if (dnis[j].equals(lista[i])) {
                        //le damos el valor false a valido, debido a que el DNI esta afectado
                        valido = false;
                    }
                }
                //si valido es verdadero, quiere decir que no hay ningun cliente afectado
                if (valido == true) {
                    //guardamos en clientes.txt el valor del cliente y añadimos un separador
                   fw.write(lista[i] + "//");
                   //Incrementamos el valor de la nueva cuenta de clientes
                   nuevaCuenta++;
                }
            }
            //cerramos el archivo matriculas.txt
            fw.close();
            //damos a cuenta el nuevo valor tras modificar los archivos
            cuenta = nuevaCuenta;
            //Imprimimos en pantalla un aviso indicando el exito de la accion
            System.out.println(verde + "Completado" + reset);
            //iniciamos el metodo encargado de guardar los nuevo datos
            guardarNuevosValores();
        } catch (Exception e) {
            //guardamos la excepcion para posterior soluccion
            LogEdit.annadirLog(e, clase, "repararExceso");
            //avisamos de la imposibilidad de soluccionar los problemas
            System.out.println(rojo + "No ha sido posible completar la reparación" + reset);
        }
    }
   
    /**
     * Elimina los alquileres antiguos
     */
    public static void limpiarAntiguos() {
        try {
            try ( //Abrimos el fichero
                    FileWriter fw = new FileWriter(new File(ficheroBase))) {
                fw.write(linea1);
                //Escribimos la  cuenta de motos
                fw.write(String.valueOf(cuenta) + "\n");
                if (ocupados != null){
                    for (String ocupado : ocupados) {
                        String[] datos = ocupado.split("::");
                        if(LocalDate.now().isBefore(LocalDate.parse(datos[2])) || LocalDate.now().isEqual(LocalDate.parse(datos[2]))) {
                            fw.write(ocupado +  "//");
                        }
                    }
                }
                fw.write("\n" + String.valueOf(cuentaClientes));
                fw.close();
            }
            try (FileReader fr = new FileReader(new File(ficheroBase))) {
                BufferedReader br = new BufferedReader(fr);
                br.readLine();
                br.readLine();
                String ocupado = br.readLine();
                if (ocupado != null || !"".equals(ocupado)) {
                    try {
                        ocupados = ocupado.split("//");
                    } catch (Exception e) {
                        ocupados = null;
                        
                    }
                    
                } else {
                    ocupados = null;
                }
                fr.close();
                //Cerramos el archivo
            }
            System.out.println(verde + "Completado" + reset);
        } catch (Exception e) {
            LogEdit.annadirLog(e, clase, "limpiarAntiguos");
            System.out.println(rojo + "No ha sido posible realizar la limpieza" + reset);
        }
    }
    
   /**
    * Guarda los nuevos valores en el archivo base
    */
    private void guardarNuevosValores() {
        //Imprimimos en pantalla un aviso indicando que vamos a guardar los nuevos datos
        System.out.print("Guardando las nuevas configuraciones..............");
        FileWriter fw = null;
        try {
            //Crea un escritor y abre el archivo
            fw = new FileWriter(new File(ficheroBase));
            //Escribe la primera linea explicatoria
            fw.write(linea1);
            //Escribe el valor de la cuenta de matriculas
            fw.write(String.valueOf(cuenta)+ "\n");
            //Escribe la cuenta de ocupados
            fw.write(String.valueOf(ocupados) + "\n");
            //Escribe la cuenta de Clientes
            fw.write(String.valueOf(cuentaClientes));
            //Cierra el archivo
            fw.close();
            //Imprime en pantalla un aviso de que se ha completado la accion
            System.out.println(verde + "Completado" + reset);
        } catch (Exception e) {
            //se guarda la exception para poder soluccionarla Posteriormente
            LogEdit.annadirLog(e, clase, "guardarNuevosValores");
            //Se imprime en pantalla un aviso indicando que a sido imposible guardar el archivo
            System.out.println(rojo + "Guardado imposible" + reset);
        } finally {
            try {
                fw.close();
            } catch (Exception e) {
                LogEdit.annadirLog(e, clase, "guardarNuevosDatosCierre");
            }
        }
    }
    /**
     * Devuelve la cuenta de matriculas
     * @return cuenta la cuenta total de matriculas
     */
    public static int getCuenta() {
        return cuenta;
    }
    /**
     * Devuelve un array con las Motos ocupadas
     * @return Ocupadas
     */
    public static String[] getOcupados() {
        return ocupados;
    }
    /**
     * Devuelve las matriculas libres para un periodo de tiempo determinado
     * @param inicio
     * @param fin
     * @return 
     */
    public static String[] getLibresFechas(LocalDate inicio, LocalDate fin) {
        String[] res = null;
        if(ocupados == null){
            try {
                FileReader fr = new FileReader(new File("matriculas.txt"));
                BufferedReader br = new BufferedReader(fr);
                res = br.readLine().split("//");
                fr.close();
            } catch (Exception e) {
                LogEdit.annadirLog(e, clase, "getLibresFechas1");
                res = null;
            }
        } else {
            String[] matriculas;
            String[] datosOcupadas;
            try {
                FileReader fr = new FileReader(new File("matriculas.txt"));
                BufferedReader br = new BufferedReader(fr);
                matriculas = br.readLine().split("//");
                fr.close();
            } catch (Exception e) {
                LogEdit.annadirLog(e, clase, "getLibresFechas2");
                matriculas = null;
            }
            String matriculasV = "";
            for (int j = 0; j < matriculas.length; j++){
                boolean valido = true;
                for (int i = 0; i < ocupados.length; i++) {
                    datosOcupadas = ocupados[i].split("::");
                    if(matriculas[j].equals(datosOcupadas[0])){                          
                        if (Comunes.logicalXOR(inicio.isAfter(LocalDate.parse(datosOcupadas[2], dtf)), fin.isBefore(LocalDate.parse(datosOcupadas[1], dtf)))) {
                            valido = false;
                            break;
                        }
                    }                    
                }
                if(valido){
                    matriculasV = matriculasV + matriculas[j] + "//";
                }
            }
            res = matriculasV.split("//");
        }
        return res;
    }
    
    /**
     * Borra la matricula indicada
     * @param matricula
     * @return 
     */
    public static boolean borrarMoto(String matricula) {
        File f = new File(matricula + ".txt");
        boolean s = false;
        try {
            String[] matriculas = Motos.getMatriculas();
            String matriculasNuevas = "";
            for (String matriculaInd : matriculas) {
                if (!matricula.equals(matriculaInd)) {
                    matriculasNuevas = matriculasNuevas + matriculaInd + "//";
                }
            }
            FileWriter fw = new FileWriter(new File("matriculas.txt"));
            fw.write(matriculasNuevas);
            fw.close();
            s = f.delete();
            delMoto();
        } catch (Exception e) {
            LogEdit.annadirLog(e, clase, "borrarMoto");
        }
        
        return s;
    }
    /**
     * Comprueba posibles fallos con los Alquileres y los clientes PENDIENTE DE CONSTRUCCION
     */
    private static void testDeAlquileres() {
        
    }
}