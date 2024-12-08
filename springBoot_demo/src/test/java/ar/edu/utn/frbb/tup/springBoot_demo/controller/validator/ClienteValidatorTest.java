package ar.edu.utn.frbb.tup.springBoot_demo.controller.validator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.ClienteDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.InvalidAgeException;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;

public class ClienteValidatorTest {

    @Mock
    private ServiceCliente serviceCliente;

    @InjectMocks
    private ClienteValidator clienteValidator;

    private ClienteDto clienteDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clienteDto = new ClienteDto();
        clienteDto.setDni(12345678L);
        clienteDto.setNombre_y_apellido("John Doe");
        clienteDto.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        clienteDto.setBanco("nacion");
        clienteDto.setTelefono(12345678L);
        clienteDto.setEmail("test@example.com");
    }

    @Test
    void validateValidClienteDto() throws ClienteAlreadyExistsException, InvalidAgeException {
        // Mockea el servicio para que devuelva null, indicando que el cliente no existe
        when(serviceCliente.buscarClientePorDni(anyLong())).thenReturn(null);

        // Verifica que no se lanza ninguna excepción
        assertDoesNotThrow(() -> clienteValidator.validate(clienteDto));
    }

    @Test
    void validateInvalidTelefono() {
        clienteDto.setTelefono(123L);
        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
    }

    @Test
    void validateInvalidEmail() {
        clienteDto.setEmail("invalid-email");
        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
    }

    @Test
    void validateInvalidBanco() {
        clienteDto.setBanco("santander");
        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
    }

    @Test
    void validateDuplicateDni() throws ClienteAlreadyExistsException {
        // Crea una instancia válida de Cliente
        Cliente clienteMock = new Cliente();
        clienteMock.setDni(12345678L); // Configura un DNI válido para el caso de prueba
        // Configura el mock para devolver un objeto Cliente válido
        when(serviceCliente.buscarClientePorDni(anyLong())).thenReturn(clienteMock);
        // Ejecuta la prueba
        assertThrows(ClienteAlreadyExistsException.class, () -> clienteValidator.validate(clienteDto));
    }

    @Test
    void validateInvalidDniStartingWithZero() {
        clienteDto.setDni(01234567L);
        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
    }

    @Test
    void validateEmptyNombre() {
        clienteDto.setNombre_y_apellido("");
        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
    }

    @Test
    void validateNullFechaNacimiento() {
        clienteDto.setFechaNacimiento(null);
        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
    }

    @Test
    void validateEmailMaxLength() {
        clienteDto.setEmail("verylongemailaddressthatshouldnotbevalid@example.com");
        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
    }

    @Test
    void validateValidEmail() {
        clienteDto.setEmail("valid.email@domain.com");
        assertDoesNotThrow(() -> clienteValidator.validate(clienteDto));
    }
}
