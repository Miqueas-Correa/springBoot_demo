// package ar.edu.utn.frbb.tup.springBoot_demo.presentation.inputs;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;

// import ar.edu.utn.frbb.tup.springBoot_demo.model.Banco;
// import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
// import ar.edu.utn.frbb.tup.springBoot_demo.presentation.shows.PrintsMenues;
// import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceCliente;

// @Component
// public class InputMenu extends BaseInputProcessor {
//     // instancio un menu
//     PrintsMenues p_menu;
//     // instancio un input cliente
//     InputCliente inputCliente;
//     // instancio un cliente
//     Cliente cliente;
//     @Autowired
//     ServiceCliente serviceCliente;

//     public InputMenu(PrintsMenues p_menu, InputCliente inputCliente, Cliente cliente) {
//         this.p_menu = p_menu;
//         this.inputCliente = inputCliente;
//         this.cliente = cliente;
//     }

//     public void menuInput(Banco banco){
//         // muestro el menu de inicio del banco
//         while (true) {
//             p_menu.menuInicio();
//             int opcion = obtenerOpcionValida();
//             scanner.nextLine();
//             switch (opcion) {
//                 case 0:
//                     return;
//                 case 1:
//                 // iniciar sesion
//                     iniciarSesion(banco);
//                     break;
//                 case 2:
//                 // registrar cliente
//                     inputCliente.registrarCliente(banco);
//                     break;
//                 default:
//                     System.out.println("Opción inválida. Por favor, elige una opción válida.");
//                     break;
//             }
//         }
//     }
//     // inicio sesion
//     private void iniciarSesion(Banco banco){
//         clearScreen();
//         int intentos = 3;
//         // simula intentos de un banco
//         while (intentos > 0) {
//             System.out.println("Ingrese DNI del usuario, '0' para salir:");
//             Long dni = Long.valueOf(obtenerOpcionValidaLong());
//             if (dni == 0) return;
//             try {
//                 Cliente cliente = serviceCliente.buscarClientePorDni(dni);
//                 clearScreen();
//                 System.out.println("Bienvenido \n" + cliente.getNombre_y_apellido());
//                 inputCliente.menuSesionCliente(cliente, banco);
//             } catch (IllegalArgumentException e) {
//                 System.out.println(e.getMessage());
//                 intentos--;
//                 if (intentos == 0) {
//                     System.out.println("Supero el limite de intentos");
//                 }
//             }
//         }
//     }
// }