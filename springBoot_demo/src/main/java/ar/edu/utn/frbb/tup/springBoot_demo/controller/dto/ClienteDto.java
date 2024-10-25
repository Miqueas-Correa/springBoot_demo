package ar.edu.utn.frbb.tup.springBoot_demo.controller.dto;

public class ClienteDto extends PersonaDto {
    private String banco;

    public String getBanco() {
        return this.banco;
    }
    public void setBanco(String banco) {
        this.banco = banco;
    }
}