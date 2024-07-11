package ar.edu.utn.frbb.tup.springBoot_demo.model;
import java.time.LocalDate;

public class Persona {
    // Atributos
    private String nombre_y_apellido;
    private String direccion;
    private Long telefono;
    private String email;
    private LocalDate fecha_de_nacimiento;
    private Long dni;

    // constructor
    public Persona(String nombre_y_apellido, String direccion, Long telefono, String email, LocalDate fecha_de_nacimiento, Long dni){
        this.nombre_y_apellido = nombre_y_apellido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.fecha_de_nacimiento = fecha_de_nacimiento;
        this.dni = dni;
    }
    public Persona(){}

    // Getters y Setters
    public String getNombre_y_apellido(){
        return this.nombre_y_apellido;
    }
    public void setNombre_y_apellido(String nombre_y_apellido){
        this.nombre_y_apellido = nombre_y_apellido;
    }
    public String getDireccion(){
        return this.direccion;
    }
    public void setDireccion(String direccion){
        this.direccion = direccion;
    }
    public Long getTelefono(){
        return this.telefono;
    }
    public void setTelefono(Long telefono){
        this.telefono = telefono;
    }
    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getFecha_de_nacimiento(){
        return this.fecha_de_nacimiento.toString();
    }
    public void setFecha_de_nacimiento(LocalDate fecha_de_nacimiento){
        this.fecha_de_nacimiento = fecha_de_nacimiento;
    }
    public Long getDni(){
        return this.dni;
    }
    public void setDni(Long dni){
        this.dni = dni;
    }

    // Métodos
    public void getDatosCliente(){
        System.out.println("------------------------");
        System.out.println("DATOS DEL CLIENTE");
        System.out.println("Nombre y apellido: " + this.nombre_y_apellido);
        System.out.println("Direccion: " + this.direccion);
        System.out.println("Telefono: " + this.telefono);
        System.out.println("Email: " + this.email);
        System.out.println("Fecha de nacimiento: " + this.fecha_de_nacimiento);
        System.out.println("DNI: "+ this.dni);
        System.out.println("---------------------------");
    }
}