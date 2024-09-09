package ar.edu.utn.frbb.tup.springBoot_demo.controller;

public class ClienteDto extends PersonaDto{
    private String banco;

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }
}