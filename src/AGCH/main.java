package AGCH;

import Help.DataBase;
import Help.IA;
import administracionDeClientes.Clientes;
import administracionDeProductos.Alquiler;
import administracionDeProductos.Motos;
import java.io.Console;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;
import library.Basicos;
import library.Comunes;
import library.LogEdit;

/**
 * 
 * @author Miguel Pazos y Emiliano Franco
 */
public class main {
    private static final String reset = "\u001B[0m";
    private static final String rojo = "\u001B[31m";
    private static final String verde = "\u001B[32m";
    private static final String azul = "\u001B[34m";
    private static final String clase = "Main";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Console console = System.console();
        Comunes.console = console != null;
        //Iniciamos Basicos para obteer valores como la cuenta de vehiculos
        Basicos b = new Basicos();
        DataBase db = new DataBase();
        //Se imprime un saludo inicial en pantalla
        Comunes.output("            " + "Bienvenido al programa de administracion de vehiculos de alquiler\n", azul); 
        Comunes.output("Porfavor seleccione una opcion en el menu:", azul);
        //inicia el menu principal
        menu();  
    }

    /**
     * Crea un menu en consola, el menu principal del programa desde el que se
     * accedera a todas las opciones disponibles
     */
    public static void menu() {
        try {
            //Iniciamos el escaner para recivir los datos de la consola
            Scanner sc = new Scanner(System.in);
            //indicamos las opciones del menu
            System.out.println("0. Cerrar programa\n1. Añadir vehiculo\n2. Obtener datos de los vehiculos\n3. Alquilar vehiculo\n4. Panel de clientes\n5. Ayuda\n"+ azul + "especifique la opcion porfavor"+ reset);
            //leemos los datos introduccidos, un entero, en caso de que no se introduzca un entero saltara una exception
            int seleccion = sc.nextInt();
            //obtenemos la siguiente linea para evitar problamas con el \n
            sc.nextLine();
            //Creamos un switch para indicar lo que tiene que hacer el programa para cada valor introduccido, mucho mas comodo que usar if, else if y else
            switch (seleccion) {
                //Opcion Añadir vehiculo
                case 1:
                    //Creamos el objeto motos
                    Motos motos = new Motos();
                    //Iniciamos el metodo annadirMoto() para obtener los datos del teclado
                    motos.annadirMoto();
                    Comunes.espera(); //creamos una espera antes de volver al menu
                    //Una vez añadida la moto se vuelve al menu principal
                    menu();
                    break;
                //Opcion Obtener datos del vehiculo
                case 2:
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
                case 3:
                    //Comprobamos que existe alguna matricula
                    if((Basicos.getCuenta() == 0)){
                        //No hay matriculas que visionar, asi que no se ejecuta la accion
                        System.out.println(rojo + "No hay ningun para alquilar" + reset);
                        menu();
                    } else{
                    //inicia el metodo que se encarga de mostrar las matriculas de los vehiculos alquilados y permite alquilar uno
                    menuAlquilar();
                    }
                    break;
             //   case 4:
               //     verAlquileres();
                 //   break;
                case 4:
                    menuClientes();
                    Comunes.espera();
                    menu();
                    break;
                case 5:
                    if (DataBase.isReady()) {
                        IA.help();
                    } else{
                        System.out.println(rojo + "Sentimos informarme de que el sistema de ayuda no esta disponible actualmente" + reset);
                        Comunes.espera();
                    }
                    if (IA.isReiniciarMenu()) {
                        menu();
                    }
                    break;
                //Opción cerrar programa y enviar el archivo log al server
                case 0:
                    System.out.print("Enviando errores al servidor......................");
                    LogEdit.sendData();
                    //No ejecuta nada ni vuelve a iniciar el programa
                    System.out.println("Cerrando programa");
                    break;
                default:
                    //En caso de que se introduzca un entero que no este asociado a ningun caso entonces se ejecuta de nuevo el menu
                    System.out.println(rojo + "Opcion no valida, por favor, seleccione una opcionn dentro de la lista:" + reset);
                    menu();
                    break;

            }
        } catch (Exception e) {
            //Reinicia el menu en caso de que se produzca un error al introducir el entero
            System.out.println(rojo + "Tiene que indicar el codigo de la opción" + reset);
            //Guarda la exception
            LogEdit.annadirLog(e, clase, "Menu principal");
            menu();
        }
    }

    /**
     * Crea un menu para mostrar las matriculas y escoger una para ver sus datos
     * especificos
     */
    public static void menuVerDatos() {
        try {
            //Imprimimos en pantalla una cabezera
            System.out.println(azul +"Visionado de Motos:" + reset);
            //Imprimimos el numero de matriculas en el sisteme
            System.out.println("Numero de matriculas almacenadas: " + String.valueOf(Basicos.getCuenta()));
            //Creamos un escaner para leer la entrada por pantalla
            Scanner sc = new Scanner(System.in);
            //Ejecutamos getMatriculas() para obtener el array con las matriculas
            String[] matriculas = Motos.getMatriculas();
            //Creamos uun loop en el que imprimimos cada matricula en una linea
            for (String matricula : matriculas) {
                //Imrpimimos la matricula
                System.out.println(matricula);
            }
            //Inicializamos el valor de loop para el bucle
            Boolean loop = true;
            //creamos un bucle q se repita mientras loop sea true, para que siga ejecutando el menu mientras no se cierre
            while (loop) {
                if (Basicos.getCuenta() == 0) {
                    loop = false;
                }
                else{
                    //Imrpimimos un mensaje indicando que hacer
                    System.out.println("Indique una matricula para ver mas información sobre esta, escriba 0 para volver  al menu principal");
                    //Leemos la entrada en pantalla
                    String matriculaIn = sc.nextLine();
                    //comprobmos si se ha introducido un 0
                    if (matriculaIn.equals("0")) {
                        //En este caso se  cierra el loop
                        loop = false;
                    }
                    //Si el valor no es 0 imprimimos los datos
                    else{
                        Motos m = new Motos();
                        m.setMatricula(matriculaIn);
                        //Ejecutamos el metodo que imprime los datos
                        m.imprimirDatos();
                    }
                }
            }
            //Una vez terminado el loop regresamos al menu
            menu();
        } catch (Exception e) {
            //Guardamos los improbables fallos para poder soluccionarlos posteriormente
            LogEdit.annadirLog(e, clase, "MenuVerDatos");
            Comunes.espera(); //espera
            //volvemos al menu principal
            menu();
        }
    }
    /**
     * Metodo  que muestra los vehiculos no alquilados y da la posibilidad de alquilar uno
     */
    public static void menuAlquilar() {
        try {
            //Imprimimos en pantalla una cabezera
            System.out.println(azul +"Alquiler de Motos:" + reset);
            //Imprimimos el numero de matriculas en el sisteme
            //System.out.println("Vehiculos disponibles en este momento: " + String.valueOf(Basicos.getLibres()));
            Alquiler a = new Alquiler();
            a.nuevoAlquiler();
            //Una vez terminado el loop regresamos al menu
            menu();
        } catch (Exception e) {
            //Guardamos los improbables fallos para poder soluccionarlos posteriormente
            LogEdit.annadirLog(e, clase, "menuAlquilar");
            //volvemos al menu principal
            menu();
        }
    }
    /**
     * Metodo encargado de mostrar los alquileres
     */
    public static void verAlquileres(){
        //Imprimimos un titulo
        System.out.println(azul + "Actualmente hay los siguientes alquileres: " + reset);
        Alquiler a = new Alquiler(); //Creamos un nuevo alquiler
        a.imprimirAlquileres();  //Imprimimos los alquileres actuales
        Comunes.espera();
        menu();  //Volvemos al menu
    }
    
    /**
     * Crea el menu de edicion y eliminacion
     */
    public static void menuEdicion() {
        try {
        System.out.println(azul + "Actualmente disponemos de los siguientes vehiculos: " + reset);
        Motos m = new Motos();
        //Creamoos la localidad españa para dar el formato euro a el precio
        Locale espanna = new Locale("es", "ES");
        //Creamos el formato de numero para españa
        NumberFormat nf = NumberFormat.getCurrencyInstance(espanna);
        String[] matriculas = Motos.getMatriculas();  //Creamos un array con todas las matriculas
        String[] datos = Basicos.getOcupados();  //Obtenemos todas las matriculas ocupadas
        String editables = ""; //Creamos un String donde se guardaran las matriculas libres
        for (String matricula : matriculas) { //bucle que comprueba si la matricula esta ocupada
            m.setMatricula(matricula); //Indica la matricula del objeto m
            m.getDatosFichero(); //lee los datos para esta matricula
            boolean avisar =false; //Declaramos un booleano con valor falso por defecto para saber si esta ocupada o no
            if(datos != null) { //Evita una Exception en caso de no haber nada ocupado
                for(String ocupada : datos){ //Crea un bucle para cada matricula ocupada
                    String[] ocupadaArray = ocupada.split("::"); //Separa la matricula del resto de los dato
                    if(ocupadaArray[0].equals(matricula)){ //Comprueba si las matriculas son iguales
                        avisar = true; //En caso afirmativo se da valor true a avisar
                        break; //Se sale del bucle actual
                    }
                }
            }
            if (avisar) { //En caso de que la matricula este ocupada se imprime en rojo
                System.out.println(rojo + m + ", precio: " + nf.format(m.getPrecio()) + reset);
            } else { //En caso contrario se imprime en verde y se añade a las opciones disponibles
                editables = editables + matricula + "//";
                System.out.println(verde + m + ", precio: " + nf.format(m.getPrecio()) + reset);
            }
            
    }
            while (true) { //Creamos un bucle infinito y iniciamos un aviso indicando las opciones
            System.out.println(azul + "Seleccione la matricula a editar, solo sera posible editar las matriculas que no esten alquiladas marcadas con color verde: (Intro para volver)" + reset);
            try {
                Scanner sc = new Scanner(System.in); //Creamos un scanner para la entrada de texto
                String matriculaInput = sc.nextLine();  //Lee la siguiente linea de texto
                m.setMatricula(matriculaInput);  //Indicamos la matricula de m introducida por consola
                m.validarMatricula(matriculaInput); // validamos la matricula
                matriculaInput = m.getMatricula();  //Obtenemos la matricula definitiva (mayusculas)
                String[] matriculasValidas = editables.split("//"); //Creamos un array con las matriculas editables
                boolean v = false; //Iniciamos un booleano con valor false por defecto
                for (String matriculaValida : matriculasValidas) { //Creamos un bucle para todas las matriculas validas
                    if (matriculaInput.equals(matriculaValida)) { //Comprueba si es valida la matricula introducida
                        v = true; //En caso afirmativo se da valor a v de true
                        break;    //y se cierra el bucle
                    }
                }
                if (v) { //En caso de ser valida se inicia el editor
                    System.out.println("Iniciando editor");
                    m.editorMotos(); //Iniciamos el metodo de edicion
                    break; //Cerramos el bucle infinito
                } else if(matriculaInput.equals("")){ //En caso de indicar el deseo de salir (intro) se cierra el bucle
                   break;
                } else { //Para otros casos la matricula no es valida
                    System.out.println("Matricula no valida");
                }
                
            } catch (Exception e) { //Guardamos las exceptions para posterior revision
                LogEdit.annadirLog(e, clase, "menuEdicionScanner");
            }
            }
        } catch (Exception e) { //Guardamos las exceptions para posterior revision
            LogEdit.annadirLog(e, clase, "menuEdicion");
        }
        
}
    /**
     * Comprueba el login del panel y lo inicia en caso afirmativo
     */
    public static void menuClientes() {
        System.out.println(azul + "Porfavor Indique su DNI, para poder modificar sus datos (Intro para volver)" + reset);
        try{
            Scanner sc = new Scanner(System.in);
            String lectura = sc.nextLine();
            if (Comunes.verificarPass(lectura)) {
                System.out.println(verde + "Se ha iniciado sesion como Administrador" + reset);
                Panel p = new Panel("ADMIN");
            } else if(lectura.equals("")){
                return;
            } else{
                Clientes c = new Clientes();
                c.setDni(lectura);
                if(c.validar()){
                   if(c.comprobarExitencia()){
                        Panel p = new Panel(c.getDni());
                   }
                   else{
                       System.out.println(rojo + "Cliente inexistente" + reset);
                       menuClientes();
                       return;
                   }
                } else{
                    System.out.println(rojo + "reiniciando panel" + reset);
                    menuClientes();
                    return;
                }
            }
        } catch(Exception e){
            LogEdit.annadirLog(e, clase, "menuClientes");
        }
    }
}
