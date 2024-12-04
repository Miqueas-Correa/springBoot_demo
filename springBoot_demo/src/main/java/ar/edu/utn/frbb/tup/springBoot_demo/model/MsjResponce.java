package ar.edu.utn.frbb.tup.springBoot_demo.model;

public class MsjResponce {
    private String estado;
    private String mensaje;

    // Constructor
    public MsjResponce(String estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }

    // Getters y Setters
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
