/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administracionDeProductos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.Scanner;
import library.Basicos;
import library.LogEdit;

/**
 *
 * @author Miguel Pazos y Emiliano Franco
 */
public class Motos {

    private static final String reset = "\u001B[0m";
    private static final String rojo = "\u001B[31m";
    private static final String verde = "\u001B[32m";
    private static final String azul = "\u001B[34m";
    
    private String modelo;
    private String color;
    private String marca;
    private BigDecimal precio;
    private String matricula;
    private String archivo;
    private LocalDate fechaAnnadido;
    private Boolean estado = false;
    private static final String clase = "Motos";
    private final BigDecimal cambioDolar = new BigDecimal("1.134945");


    /**
     * Metodo para añadir los datos que se introdzcan por teclado y se guardan
     * en archivos de texto
     */
    public void annadirMoto() {
        try {
            //Lee los datos de la consola
            Scanner sc = new Scanner(System.in);
            System.out.println("Introduzcca la marca de la moto");
            marca = sc.nextLine();
            System.out.println("Introduzca el modelo de la moto");
            modelo = sc.nextLine();
            System.out.println("Introduzca el color de la moto");
            color = sc.nextLine();
            System.out.println("Introduzca el precio de alquiler de la moto");
            precio = new BigDecimal(sc.nextLine());
            System.out.println("Introduzca la matricula con las letras");
            setMatricula(sc.nextLine());
            //valida la matricula
            int validez = validarMatricula(getMatricula());
            if (validez == 0) {
                System.err.println("Matricula no valida");
            } else {
                guardarArchvo();
            }
        } catch (Exception e) {
            LogEdit.annadirLog(e, clase, "annadirMoto");
            System.err.println("Error al añadir la Moto");
        }
    }

    /**
     * Metodo para guardar los datos anteriormente obtenidos en annadirMoto
     */
    public void guardarArchvo() {
        //crea el nombre del archivo
        archivo = getMatricula() + ".txt";
        try {
            //Intenta guardar los datos especificos en el archivo
            FileWriter fw = new FileWriter(new File(archivo));
            fw.write(marca + "\n");
            fw.write(modelo + "\n");
            fw.write(color + "\n");
            fw.write(String.valueOf(getPrecio()) + "\n");
            fw.write(String.valueOf(LocalDate.now()) + "\n");
            fw.close();
            Basicos.addMoto();
        } catch (Exception e) {
            LogEdit.annadirLog(e, clase, "guardarArchivo1");
            System.err.println("Error al guardar los datos");

        }
        try {
            //Añade la matricula en un archivo donde se almacenan todos las matriculas registradas
            FileWriter fw = new FileWriter(new File("matriculas.txt"), true);
            fw.append(getMatricula() + "//");
            fw.close();
            System.out.println("Datos de la moto guardados correctamente, volviendo al menu principal");
        } catch (Exception e) {
            System.err.println("Error al guardar los datos");
            LogEdit.annadirLog(e, clase, "guardarArchivo2");
        }
    }
    
    /**
     * Actualiza los datos de la moto
     */
    public void actualizarDatos() {
        //crea el nombre del archivo
        archivo = getMatricula() + ".txt";
        try {
            //Intenta guardar los datos especificos en el archivo
            FileWriter fw = new FileWriter(new File(archivo));
            fw.write("");
            fw.write(this.marca + "\n");
            fw.write(modelo + "\n");
            fw.write(color + "\n");
            fw.write(String.valueOf(getPrecio()) + "\n");
            fw.write(String.valueOf(fechaAnnadido) + "\n");
            fw.close();
            System.out.println("Datos de la moto guardados correctamente, volviendo al menu principal");
        } catch (Exception e) {
            LogEdit.annadirLog(e, clase, "actualizarMoto");
            System.err.println(rojo + "Error al guardar los datos" + reset);

        }
            
    }
    /**
     * Lee el archivo matriculas.txt y las devuelve en un array
     * @return matriculas
     */
    public static String[] getMatriculas() {
        try {
            //Creamos eu lector y abrimos el archivo
            FileReader fr = new FileReader(new File("matriculas.txt"));
            //Creamos un BufferedReader para facilitar la lectura del fichero
            BufferedReader br = new BufferedReader(fr);
            //Guardamoslos los datos de la primera y unica linea del archivo en el string datos
            String datos = br.readLine();
            //Cerramos el fichero
            fr.close();
            //Creamos un  array mediante la diivision de el string por los caracteres "//"
            String[] matriculas = datos.split("//");
            //Una vez que tenemos el array devolvemos el valor
            return matriculas;
            
        
        } catch(Exception e) {
            //En caso de producirse una excepcion la guardamos para posterior soluccion
            LogEdit.annadirLog(e, clase, "getMatriculas");
            //Devolvemos un valor nulo porq es neccesarrio devolver algun valor
            return null;
        }
    }
    /**
     * Imprime en pantalla los datos especificos de una matricula
     */
    public void imprimirDatos() {
        
            int validez = validarMatricula(getMatricula());
            if (validez == 0) {
                System.err.println("Matricula no valida");
            } else {
               getDatosFichero();
            //Imprimimos la primera linea, la marca
            System.out.println("Marca: " + marca);
            //Imprimimos la siguiente linea, el modelo
            System.out.println("Modelo: " + modelo);
            //Imprimimos la siguiente linea, el Color
            System.out.println("Color: " + color);
            //Creamoos la localidad españa para dar el formato euro a el precio
            Locale espanna = new Locale("es", "ES");
            //Creamos el formato de numero para españa
            NumberFormat nf = NumberFormat.getCurrencyInstance(espanna);
            //Imprime en pantalla el precio en euros
            System.out.println("Precio en Euros: " + nf.format(round(getPrecio(), 2)));
            //Creamos el numberFormat para imprimir el precio en dolares
            NumberFormat eeuu = NumberFormat.getCurrencyInstance(Locale.US);
            //Imprimimos el precio en dolares
            System.out.println("Precio en Dolares: " + eeuu.format(round(getPrecio().multiply(cambioDolar), 2)));
            //Imprimimos la siguiente linea, la fecha en la que se añadio 
            System.out.println("Añadido en: " + fechaAnnadido);
            //Imprimimos la siguiente linea, el estado(ocupada o libre)
            System.out.println("Estado actual: " + getEstado());
    }
    }
    
    /**
     * Metodo encargado de obtener los datos de una matricula
    */
    public void getDatosFichero() {
        try {
            //Construimos el lector y abrimos el archivo
            FileReader fr = new FileReader(new File(matricula + ".txt"));
            //Construimos un BufferedReader para leer mas facilmente el fichero
            BufferedReader br = new BufferedReader(fr);
            //Leemos la primera linea, la marca
            marca = br.readLine();
            //Leemos la siguiente linea, el modelo
            modelo = br.readLine();
            //Leemos la siguiente linea, el Color
            color = br.readLine();
            //Leemos la siguiente linea, el precio
            precio = new BigDecimal(br.readLine());
            //Leemos la siguiente linea, la fecha en la que se añadio 
            fechaAnnadido = LocalDate.parse(br.readLine());
            //Cerramos el archivo
            fr.close();
        } catch (Exception e) {
            //En caso de producirse este error, lo mas probable es que no exista el archivo, por tanto vamos a ejecutar unas comprobaciones para soluccionar estos fallos
            Basicos b = new Basicos(2);
        }
    }
    /**
     * Metodo encargado de generar un Array con los datos del vehiculo
     * @return array con los datos
     */
    public String[] getDatosArray() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String[] re = {matricula, marca, modelo, color, String.valueOf(precio), dtf.format(fechaAnnadido)};
        return re;
    }

    /**
     * @return el modelo
     */
    public String getModelo() {
        return modelo;
    }
    /**
     * 
     * @return estado en castellano
     */
    public String getEstado() {
        //si el estado es true devueleve si, en caso contrario devuelve no
        if(estado){
            return rojo + "Ocupado" + reset; 
        } else{
            return verde + "Libre" + reset;
        }
    }

    /**
     * @param modelo el nuevo modelo
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * @return el color
     */
    public String getColor() {
        return color;
    }
    /**
     * Comprueba la matricula si ya existe
     * @return validez
     */
    public boolean validarMatricula() {
        String[] matriculas = getMatriculas();
        boolean valido = false;
        for (String mat : matriculas) {
            if (matricula.equals(mat)) {
                valido = true;
                break;
            }
        }
        return valido;
    }
    /**
     * Reduce la cantidad de decimales de un BigDecimal mediante el Floor
     * @param d el BigDecimal a redondear
     * @param scale La escala, los decimales a dejar
     * @return El valor redondeado
     */
    public static BigDecimal round(BigDecimal d, int scale) {
  return d.setScale(scale, BigDecimal.ROUND_FLOOR);
}

    /**
     * @param color el nuevo color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return la marca
     */
    public String getMarca() {
        return marca;
    }

    /**
     * @param marca la nueva marca
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * Valida la matricula segun los formatos principales
     * @param matricula la matricula a validar
     * @return validez de la matricula
     */
   public int validarMatricula(String matricula) {
       //inicia el String donde se guardara el formato
       String formato = "";
       //Pasamos la matricula a un array de caracteres para poder comprobar el tipo de caracter
       char[] valores = matricula.toCharArray();
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
       int res;
       //creamos un switch con el formato para indicar el resultado en funcion del tipo de matricula
       switch(formato){
            case "0000XXX":
                res = 1;
                break;
            case "X0000XX":
                res= 2;
                break;
            case "XX0000XX":
                res= 3;
                break;
            default:
                res= 0;
                break;
       }
       //cambiamos el valor de matricula por el de esta con letras mayusculas
       this.setMatricula(String.valueOf(valores));
       //devolvemos el valor resultado
       return res;
   }

    /**
     * @param matricula la nueva matricula
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    
    /**
     * Metodo encargado de calcular el precio durante un tiempo determinado
     * @param tiempo dias de duracion para el precio
     * @return Precio total
     * 
     */
    public BigDecimal getPrecioTotal(BigDecimal tiempo) {
        return tiempo.multiply(getPrecio());
    }
    
    /**
     * Metodo encargado de Crear un String de los datos de una matricula
     * @return 
     */
    @Override
    public String toString() {
        return "+ " + getMatricula() + " : " + marca + " " + modelo + " " + color;
    }

    /**
     * @return the matricula
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * @return the precio
     */
    public BigDecimal getPrecio() {
        return precio;
    }
    
    /**
     * Edita los datos de las matriculas
     */
    public void editorMotos() {
        getDatosFichero();
        try {
            System.out.println(azul + "Que desea hacer, Editar (e) o borrar (b) Seleccione una de las opciones(e/b): " + reset);
            Scanner sc = new Scanner(System.in);
            String respuesta = sc.nextLine();
            switch (respuesta) {
                case "e":
                case "E":
                case "editar":
                case "Editar":
                case "EDITAR":
                case "edit":
                case "Edit":
                case "EDIT":
                    editorDatos();
                    break;
                case "b":
                case "B":
                case "borrar":
                case "Borrar":
                case "BORRAR":
                case "delete":
                case "Delete":
                case "DELETE":
                case "del":
                case "Del":
                case "DEL":
                    System.out.println(rojo + "se va a eliminar el siguiente vehiculo \n" + toString() + "\n Estas seguro? (s/n)");
                    String res = sc.nextLine();
                    if(res.equals("s") || res.equals("si") || res.equals("y") || res.equals("yes") || res.equals("S") || res.equals("Si") || res.equals("Y") || res.equals("Yes") || res.equals("YES") || res.equals("SI") || res.equals("ok") || res.equals("OK") || res.equals("Ok")) {
                        System.out.println("Eliminando datos");
                        boolean b = Basicos.borrarMoto(matricula);
                    } else {
                        System.out.println("Regresando al menu principal, no se ha eliminado nada");
                    }
                    break;
            }
        } catch (Exception e) {
            LogEdit.annadirLog(e, clase, "editorMotos");
        }
    }

    private void editorDatos() {
        System.out.println(azul + "Para editar datos iremos indicandole el valor a editar, en caso de querer el valor ya exitente presione la tecla INTRO. La matricula no se permite modificar." + reset);
        Locale espanna = new Locale("es", "ES");
        NumberFormat nf = NumberFormat.getCurrencyInstance(espanna);
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Editando la marca de la moto (actualmente: " + this.marca + ")");
            String marca = sc.nextLine();
            System.out.println("Editando el modelo de la moto (actualmente: " + this.modelo + ")");
            String modelo = sc.nextLine();
            System.out.println("Editando el color de la moto (actualmente: " + this.color + ")");
            String color = sc.nextLine();
            System.out.println("Editando el precio de alquiler de la moto (actualmente: " + nf.format(precio) + ")");
            String precioS = (sc.nextLine());
            
            System.out.println(azul + "Proceso de edicion terminado, porfavor confirme los siguientes cambios: " + reset);
            System.out.println("Matricula del vehiculo: " + matricula);
            if (marca.equals("")) {
                System.out.println("Marca: " + verde + this.marca + reset);
                marca = this.marca;
            } else {
                System.out.println("Marca: " + rojo + this.marca + reset + "---->" + verde + marca + reset);
            }
            if (modelo.equals("")) {
                System.out.println("Modelo: " + verde + this.modelo + reset);
                modelo = this.modelo;
            } else {
                System.out.println("Modelo: " + rojo + this.modelo + reset + "---->" + verde + modelo + reset);
            }
            if (color.equals("")) {
                System.out.println("Color: " + verde + this.color + reset);
                color = this.color;
            } else {
                System.out.println("Color: " + rojo + this.color + reset + "---->" + verde + color + reset);
            }
            if (precioS.equals("")) {
                System.out.println("Precio: " + verde + nf.format(this.precio) + reset);
                precioS = String.valueOf(getPrecio());
            } else {
                System.out.println("Precio: " + rojo + nf.format(this.precio) + reset + "---->" + verde + nf.format(new BigDecimal(precioS)) + reset);
            }
            System.out.println(azul + "Esta todo correcto? (s/n)" + reset);
            String respuesta = sc.nextLine();
            if(respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("y") || respuesta.equals("yes") || respuesta.equals("S") || respuesta.equals("Si") || respuesta.equals("Y") || respuesta.equals("Yes") || respuesta.equals("YES") || respuesta.equals("SI") || respuesta.equals("ok") || respuesta.equals("OK") || respuesta.equals("Ok")) {
                System.out.println("Guardando Datos");
                this.marca = marca;
                this.modelo = modelo;
                this.color = color;
                this.precio = new BigDecimal(precioS);
                actualizarDatos();
            } else {
                System.out.println("Se han restablecido los datos anteriores");
            }
        } catch (Exception e) {
            LogEdit.annadirLog(e, clase, "editorDatos");
        }
    }
}
