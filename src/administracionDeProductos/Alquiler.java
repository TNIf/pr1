/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administracionDeProductos;

import administracionDeClientes.Clientes;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import library.Basicos;
import library.Comunes;
import library.LogEdit;

/**
 *
 * @author Miguel Pazos
 */
public class Alquiler extends Motos{
    
    
    //Valores de los codigos paea modificar el color del texto
    private static final String reset = "\u001B[0m";
    private static final String rojo = "\u001B[31m";
    private static final String verde = "\u001B[32m";
    private static final String clase = "Alquiler";
    private static final String azul = "\u001B[34m";

    private LocalDate fechaDeAlquiler;
    private LocalDate fechaDevolucion;
    private Clientes cliente;
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    /**
     * Contructor de la clase Alquiler, crea el objeto cliente
     */
    public Alquiler(){
        
       cliente = new Clientes();
    }
    
    /**
     * Se ejecuta al iniciar un alquiler, reciviendo el DNI y si tiene algun valor ejecuta el metodo iniciarAlquiler
     */
    public void nuevoAlquiler(){
        cliente.ComprobarClienteC();
        if (!cliente.getDni().equals("")) {
            iniciarAlquiler();
        } else {
            System.out.println("Volviendo al menu principal");
        }
    }
        
    /**
     * Crea un alquiler a conociendo el cliente
     */
    public void iniciarAlquiler() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Bienvenido " + cliente.getNombre() + " " + cliente.getPrimerApellido() + " ¿Desea alquilar un vehiculo para poder usarlo desde hoy? responda(s/n) (salir para volver al menu)");
            String respuesta = sc.nextLine();
            switch (respuesta) {
                case "s":
                case "si":
                case "y":
                case "yes":
                case "S":
                case "Si":
                case "Y":
                case "Yes":
                case "YES":
                case "SI":
                case "ok":
                case "OK":
                case "Ok":
                    setFechaDeAlquiler(LocalDate.now());
                    //     id = cliente.getDni() + String.valueOf(cliente.getCuentaAlquileres());
                    alquilarMoto();
                    break;
                case "salir":
                case "Salir":
                case "SALIR":
                case "exit":
                case "Exit":
                case "EXIT":
                    break;
                default:
                    while(true) {
                        System.out.println("Indique la fecha en la que desea alquilar en formato DD/MM/AAA (Ejemplo: 01/01/2000)");
                        try{
                            setFechaDeAlquiler(LocalDate.parse(sc.nextLine(), dtf));
                            if (getFechaDeAlquiler().isAfter(LocalDate.now()) || getFechaDeAlquiler().isEqual(LocalDate.now())) {
                                break;
                            } else {
                                System.out.println(rojo + "No puedes alquilar en el pasado, como muy pronto puedes alquilar para hoy que estamos a: " + LocalDate.now() + reset);
                            }
                        } catch(Exception e) {
                            System.out.println(rojo + "La fecha introducida no concuerda con el formato requerido" + reset);
                        }
                    }   //id = cliente.getDni() + String.valueOf(cliente.getCuentaAlquileres());
                alquilarMoto();
                    break;                
            }
        } catch (Exception e) {
            library.LogEdit.annadirLog(e, clase, "nuevoAlquiler");
        }
        
    }
    
    /**
     * Actualiza los datos de un alquiler
     */
    public void updateAlquiler(){
        System.out.println(azul + "Editando alquiler" + reset);
        Scanner sc = new Scanner(System.in);
        while(true) {
                        System.out.println("Indique la fecha en la que desea alquilar en formato DD/MM/AAA (Ejemplo: 01/01/2000)");
                        try{
                            setFechaDeAlquiler(LocalDate.parse(sc.nextLine(), dtf));
                            if (getFechaDeAlquiler().isAfter(LocalDate.now()) || getFechaDeAlquiler().isEqual(LocalDate.now())) {
                                break;
                            } else {
                                System.out.println(rojo + "No puedes alquilar en el pasado, como muy pronto puedes alquilar para hoy que estamos a: " + LocalDate.now() + reset);
                            }
                        } catch(Exception e) {
                            System.out.println(rojo + "La fecha introducida no concuerda con el formato requerido" + reset);
                        }
                    }   //id = cliente.getDni() + String.valueOf(cliente.getCuentaAlquileres());
            Locale espanna = new Locale("es", "ES");
            NumberFormat nf = NumberFormat.getCurrencyInstance(espanna);
            while(true) {
                System.out.println("Indiquenos cuando desea devolver el vehiculo en formato DD/MM/AAA (Ejemplo: 01/01/2000)");
                try {
                    setFechaDevolucion(LocalDate.parse(sc.nextLine(), dtf));
                    if(getFechaDeAlquiler().isAfter(getFechaDevolucion())) {
                    System.out.println(rojo + "La fecha de devolucion debe ser posterior a la de la entrega" + reset);
                    } else if(getFechaDevolucion().isEqual(getFechaDeAlquiler())) {
                        System.out.println(rojo + "El tiempo minimo de alquiler es de un dia" + reset);
                    } else{
                        break;
                    }
                } catch (Exception e) {
                    System.out.println(rojo + "Formato de fecha no valido" + reset);
                }
            }
            
            System.out.println(azul + "Vista previa del alquiler: " + reset);
            System.out.println("Cliente: " + cliente);
            System.out.println("Vehiculo a alquilar: ");
            System.out.println(super.toString());
            System.out.println("Fecha de inicio del alquiler: " + dtf.format(getFechaDeAlquiler()));
            System.out.println("Fecha de finalizacion del alquiler: " + dtf.format(getFechaDevolucion()));
            System.out.println("Duracion total en dias del alquiler: " + getFechaDeAlquiler().until(getFechaDevolucion(), ChronoUnit.DAYS));
            System.out.println("Total a pagar: " + ", precio total: " + nf.format(super.getPrecioTotal(new BigDecimal(getFechaDeAlquiler().until(getFechaDevolucion(), ChronoUnit.DAYS)))));
            System.out.println("\n" + azul + "¿Son correctos los datos? responda(s/n)" + reset);
            String respuesta = sc.nextLine();
            if(respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("y") || respuesta.equals("yes") || respuesta.equals("S") || respuesta.equals("Si") || respuesta.equals("Y") || respuesta.equals("Yes") || respuesta.equals("YES") || respuesta.equals("SI") || respuesta.equals("ok") || respuesta.equals("OK") || respuesta.equals("Ok")) {
                   System.out.println("Guardando datos");
                   Basicos.borrarAlquiler(getMatricula(), cliente.getDni(), fechaDeAlquiler, fechaDevolucion);
                   String[] valoresAGuardar = new String[4];
                   valoresAGuardar[0] = super.getMatricula();
                   valoresAGuardar[1] = String.valueOf(getFechaDeAlquiler());
                   valoresAGuardar[2] = String.valueOf(getFechaDevolucion());
                   valoresAGuardar[3] = cliente.getDni();
                   Basicos.addOcupado(valoresAGuardar);
                   Comunes.espera();
            } else{
                System.out.println("Regresando al panel");
            }
            
            
        
    }
    /**
     * Crea el dialogo para alquilar una moto
     */
    public void alquilarMoto() {
        try {
            String[] temp = new String[10];
            Locale espanna = new Locale("es", "ES");
            NumberFormat nf = NumberFormat.getCurrencyInstance(espanna);
            Scanner sc = new Scanner(System.in);
            while(true) {
                System.out.println("Indiquenos cuando desea devolver el vehiculo en formato DD/MM/AAA (Ejemplo: 01/01/2000)");
                try {
                    setFechaDevolucion(LocalDate.parse(sc.nextLine(), dtf));
                    if(getFechaDeAlquiler().isAfter(getFechaDevolucion())) {
                    System.out.println(rojo + "La fecha de devolucion debe ser posterior a la de la entrega" + reset);
                    } else if(getFechaDevolucion().isEqual(getFechaDeAlquiler())) {
                        System.out.println(rojo + "El tiempo minimo de alquiler es de un dia" + reset);
                    } else{
                        break;
                    }
                } catch (Exception e) {
                    System.out.println(rojo + "Formato de fecha no valido" + reset);
                }
            }
            imprimirLibres();
            System.out.println("¿Que moto deseas alquilar? introduzca la matricula");
            temp[0] = library.Comunes.leerMatricula();
            if (temp[0].equals("exit") || temp[0].equals("")) {
                return;
            }
            super.setMatricula(temp[0]);
            super.getDatosFichero();
            System.out.println(azul + "Vista previa del alquiler: " + reset);
            System.out.println("Cliente: " + cliente);
            System.out.println("Vehiculo a alquilar: ");
            System.out.println(super.toString());
            System.out.println("Fecha de inicio del alquiler: " + dtf.format(getFechaDeAlquiler()));
            System.out.println("Fecha de finalizacion del alquiler: " + dtf.format(getFechaDevolucion()));
            System.out.println("Duracion total en dias del alquiler: " + getFechaDeAlquiler().until(getFechaDevolucion(), ChronoUnit.DAYS));
            System.out.println("Total a pagar: " + nf.format(super.getPrecioTotal(new BigDecimal(getFechaDeAlquiler().until(getFechaDevolucion(), ChronoUnit.DAYS)))));
            System.out.println("\n" + azul + "¿Son correctos los datos? responda(s/n)" + reset);
            String respuesta = sc.nextLine();
            if(respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("y") || respuesta.equals("yes") || respuesta.equals("S") || respuesta.equals("Si") || respuesta.equals("Y") || respuesta.equals("Yes") || respuesta.equals("YES") || respuesta.equals("SI") || respuesta.equals("ok") || respuesta.equals("OK") || respuesta.equals("Ok")) {
                   System.out.println("Guardando datos");
                   String[] valoresAGuardar = new String[4];
                   valoresAGuardar[0] = super.getMatricula();
                   valoresAGuardar[1] = String.valueOf(getFechaDeAlquiler());
                   valoresAGuardar[2] = String.valueOf(getFechaDevolucion());
                   valoresAGuardar[3] = cliente.getDni();
                   Basicos.addOcupado(valoresAGuardar);
            } else{
                System.out.println("Reiniciando alquiler");
                iniciarAlquiler();
            }
        } catch (Exception e) {
            //Exception Por modificar
            Logger.getLogger(LogEdit.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    /**
     * Crea un panel para editar el alquiler
     * @param dni
     */
    public void panelAlquiler(String dni){
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
                    updateAlquiler();
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
                    System.out.println(rojo + "se va a eliminar el alquiler\n Estas seguro? (s/n)");
                    String res = sc.nextLine();
                    if(res.equals("s") || res.equals("si") || res.equals("y") || res.equals("yes") || res.equals("S") || res.equals("Si") || res.equals("Y") || res.equals("Yes") || res.equals("YES") || res.equals("SI") || res.equals("ok") || res.equals("OK") || res.equals("Ok")) {
                        System.out.println("Eliminando datos");
                        Basicos.borrarAlquiler(getMatricula(), dni, fechaDeAlquiler, fechaDevolucion);
                    } else {
                        System.out.println("Regresando al panel, no se ha eliminado nada");
                    }
                    break;
            }
        } catch (Exception e) {
            LogEdit.annadirLog(e, clase, "panelAlquiler");
        }
    }
    
    /**
     * Imprime en pantalla las vehiculos libres para la fecha
     */
    private void imprimirLibres() {
        Locale espanna = new Locale("es", "ES");
        NumberFormat nf = NumberFormat.getCurrencyInstance(espanna);
        System.out.println(azul + "En el periodo seleccionado disponemos de los siguientes vehiculos :" + reset);
        String[] matriculasV = Basicos.getLibresFechas(getFechaDeAlquiler(), getFechaDevolucion());
        for (String matriculasV1 : matriculasV) {
            super.setMatricula(matriculasV1);
            super.getDatosFichero();
            System.out.println(super.toString() + ", precio total: " + nf.format(super.getPrecioTotal(new BigDecimal(getFechaDeAlquiler().until(getFechaDevolucion(), ChronoUnit.DAYS)))));
        }
    }
    /**
     * Imrpime en pantalla los alquileres realizados
     */
    public void imprimirAlquileres() {
        Locale espanna = new Locale("es", "ES");
        NumberFormat nf = NumberFormat.getCurrencyInstance(espanna);
        String[] alquileres = Basicos.getOcupados();
        if(alquileres == null){
            System.out.println(rojo + "Actualmente no hay ningun vehiculo alquilado, ni existe ningun alquiler en el futuro" + reset);
        } else{
        Clientes c = new Clientes();
        for (String alquiler : alquileres) {
            String[] datos = alquiler.split("::");
            super.setMatricula(datos[0]);
            super.getDatosFichero();
            c.setDni(datos[3]);
                setFechaDeAlquiler(LocalDate.parse(datos[1]));
                setFechaDevolucion(LocalDate.parse(datos[2]));
            c.leerFicheroClientes();
            System.out.println(super.toString() + ", alqilado por: " + c + ", lo cual ha costado un total de: " + nf.format(super.getPrecioTotal(new BigDecimal(getFechaDeAlquiler().until(getFechaDevolucion(), ChronoUnit.DAYS)))) + " Este alquiler va desde " + getFechaDeAlquiler() + " a " + getFechaDevolucion());
        }
        }
    }
    /**
     * Imrpime en pantalla los alquileres realizados por un cliente
     * @param dni
     */
    public void imprimirAlquileresPropios(String dni) {
        Locale espanna = new Locale("es", "ES");
        NumberFormat nf = NumberFormat.getCurrencyInstance(espanna);
        String[] alquileres = Basicos.getOcupados();
        if(alquileres == null){
            System.out.println(azul + "Actualmente no tiene ningun vehiculo alquilado a su nombre" + reset);
        } else if(String.join(" ", alquileres).contains(dni)){
            System.out.println(azul + "Actualmente dispone de los siguientes alquileres" + reset);
        for (String alquiler : alquileres) {
            String[] datos = alquiler.split("::");
            if(dni.equals(datos[3])){
                super.setMatricula(datos[0]);
                super.getDatosFichero();
                    setFechaDeAlquiler(LocalDate.parse(datos[1]));
                    setFechaDevolucion(LocalDate.parse(datos[2]));
                System.out.println(super.toString() + ", lo cual ha costado un total de: " + nf.format(super.getPrecioTotal(new BigDecimal(getFechaDeAlquiler().until(getFechaDevolucion(), ChronoUnit.DAYS)))) + " Este alquiler va desde " + getFechaDeAlquiler() + " a " + getFechaDevolucion());
                }
            }
        } else {
            System.out.println(azul + "Actualmente no tiene ningun vehiculo alquilado a su nombre" + reset);
        }
    }
    
    /**
     * Separa los alquileres para un cliente
     * @param dni Codigo del cliente para comprobar sus alquileres
     * @return Alquileres del cliente
     */
    public String getAlquileresPropios(String dni) {
        Locale espanna = new Locale("es", "ES");
        
        NumberFormat nf = NumberFormat.getCurrencyInstance(espanna);
        String[] alquileres = Basicos.getOcupados();
        String dev = "";
        if(alquileres == null){
            dev = "";
        } else if(String.join(" ", alquileres).contains(dni)){
        for (String alquiler : alquileres) {
            String[] datos = alquiler.split("::");
            if(dni.equals(datos[3])){
                dev = dev + alquiler + "//";
                }
            }
        } else {
            dev = "";
        }
        return dev;
    }

    /**
     * @param fechaDeAlquiler the fechaDeAlquiler to set
     */
    public void setFechaDeAlquiler(LocalDate fechaDeAlquiler) {
        this.fechaDeAlquiler = fechaDeAlquiler;
    }

    /**
     * @param fechaDevolucion the fechaDevolucion to set
     */
    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    /**
     * @return the fechaDeAlquiler
     */
    public LocalDate getFechaDeAlquiler() {
        return fechaDeAlquiler;
    }

    /**
     * @return the fechaDevolucion
     */
    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }
}
