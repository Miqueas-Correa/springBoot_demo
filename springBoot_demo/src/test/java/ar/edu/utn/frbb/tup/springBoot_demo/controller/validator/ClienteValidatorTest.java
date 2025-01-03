package ar.edu.utn.frbb.tup.springBoot_demo.controller.validator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
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
        clienteDto.setTelefono(1234567890L);
        clienteDto.setEmail("test@example.com");
    }

    @Test
    void validateValidClienteDto() throws ClienteAlreadyExistsException, InvalidAgeException {
        when(serviceCliente.buscarClientePorDni(anyLong())).thenReturn(null);
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
    void validateInvalidDniStartingWithZero() {
        clienteDto.setDni(12345678L);
        assertDoesNotThrow(() -> clienteValidator.validate(clienteDto));
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

    @Test
    void validateFechaNacimientoFutura() {
        clienteDto.setFechaNacimiento(LocalDate.now().plusDays(1));
        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
    }

    @Test
    void validateEmailWithMultipleDots() {
        clienteDto.setEmail("test.user.name@gmail.com");
        assertDoesNotThrow(() -> clienteValidator.validate(clienteDto));
    }

    @Test
    void validateTelefonoStartingWithZero() {
        clienteDto.setTelefono(1234567890L);
        assertDoesNotThrow(() -> clienteValidator.validate(clienteDto));
    }

    @Test
    void validateDniWithAllSameDigits() {
        clienteDto.setDni(11111111L);
        assertDoesNotThrow(() -> clienteValidator.validate(clienteDto));
    }

    @Test
    void validateNombreWithNumbers() {
        clienteDto.setNombre_y_apellido("John Doe 3rd");
        assertDoesNotThrow(() -> clienteValidator.validate(clienteDto));
    }

    @Test
    void validateTelefonoWithAllZeros() {
        clienteDto.setTelefono(0L);
        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
    }
}
