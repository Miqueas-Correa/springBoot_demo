package ar.edu.utn.frbb.tup.springBoot_demo.model;

import org.springframework.http.HttpStatus;

public class Respuesta<T> {
    private MsjResponce mensaje;
    private T datos;
    private HttpStatus httpStatus;

    public Respuesta(MsjResponce mensaje, T datos, HttpStatus httpStatus) {
        this.mensaje = mensaje;
        this.datos = datos;
        this.httpStatus = httpStatus;
    }

    public MsjResponce getMensaje() {
        return mensaje;
    }
    public T getDatos() {
        return datos;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    public void setMensaje(MsjResponce mensaje) {
        this.mensaje = mensaje;
    }
    public void setDatos(T datos) {
        this.datos = datos;
    }
    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
