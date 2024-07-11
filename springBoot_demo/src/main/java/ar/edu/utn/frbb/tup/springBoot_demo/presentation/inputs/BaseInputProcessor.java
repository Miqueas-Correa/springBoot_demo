package ar.edu.utn.frbb.tup.springBoot_demo.presentation.inputs;

import java.util.Scanner;
// ! ESTE CODIGO SE PUEDE REUTILIZAR PARA PROYECTOS FUTUROS.
public class BaseInputProcessor {
    protected Scanner scanner = new Scanner(System.in);


    protected static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // metodo con double
    protected double obtenerOpcionValidaDouble() {
        while (!scanner.hasNextDouble()) {
            scanner.next(); // Consume la entrada no válida
            System.out.println("Error: Debes ingresar un número válido.");
        }
        return scanner.nextDouble();
    }
    // metodo con String
    protected String obtenerCadenaValida() {
        String entrada;
        while (true) {
            entrada = scanner.nextLine().trim(); // Elimina espacios en blanco al inicio y al final
            if (!entrada.isEmpty() && entrada.matches("[a-zA-Z\\s]+")) {
                // La entrada no está vacía y solo contiene letras y espacios
                return entrada;
            } else {
                System.out.println("Error: Debes ingresar una cadena que solo contenga letras y espacios y no esté vacía.");
            }
        }
    }
    // metodo con Long
    protected long obtenerOpcionValidaLong() {
        while (!scanner.hasNextLong()) {
            scanner.next(); // Consume la entrada no válida
            System.out.println("Error: Debes ingresar un número entero largo válido.");
        }
        return scanner.nextLong();
    }
    // motodo con int
    protected int obtenerOpcionValida() {
        // mientras no haya una entrada entera disponible
        while (!scanner.hasNextInt()) {
            scanner.next(); // Consume la entrada no válida
            System.out.println("Error: Debes ingresar un número entero.");
        }
        return scanner.nextInt();
    }
}
