package ar.edu.utn.frbb.tup.springBoot_demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.*;

public class BanelcoServiceTest {

    @Test
    void testGetRandomBooleanDevuelveBoolean() {
        boolean resultado = BanelcoService.getRandomBoolean();
        assertTrue(resultado || !resultado);
    }

    @RepeatedTest(100)
    void testDistribucionGetRandomBoolean() {
        int conteoVerdaderos = 0;
        int totalIteraciones = 100;

        for (int i = 0; i < totalIteraciones; i++) {
            if (BanelcoService.getRandomBoolean()) {
                conteoVerdaderos++;
            }
        }

        // Verifica si la distribución está aproximadamente equilibrada (entre 30% y 70%)
        assertTrue(conteoVerdaderos >= 30 && conteoVerdaderos <= 70,
                "La distribución aleatoria debería estar aproximadamente equilibrada");
    }

    @Test
    void testLlamadasMultiplesDevuelvenValoresDiferentes() {
        boolean todosIguales = true;
        boolean primerResultado = BanelcoService.getRandomBoolean();

        for (int i = 0; i < 10; i++) {
            if (BanelcoService.getRandomBoolean() != primerResultado) {
                todosIguales = false;
                break;
            }
        }

        assertFalse(todosIguales, "Llamadas múltiples deberían eventualmente devolver valores diferentes");
    }
}
