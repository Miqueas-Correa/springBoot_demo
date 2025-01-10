package ar.edu.utn.frbb.tup.springBoot_demo.controller;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.validator.TransferirValidator;
import ar.edu.utn.frbb.tup.springBoot_demo.model.MsjResponce;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.TransferirDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.CuentaNotFoundException;
import ar.edu.utn.frbb.tup.springBoot_demo.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransferControllerTest {

    @Mock
    private TransferirValidator transferValidator;

    @Mock
    private TransferService transferService;

    @InjectMocks
    private TransferController transferController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void transferir_SuccessfulTransfer_ReturnsSuccessResponse() {
        TransferirDto transferirDto = new TransferirDto();
        MsjResponce expectedResponse = new MsjResponce("EXITOSA", "Transferencia realizada con éxito");

        doNothing().when(transferValidator).validate(any(TransferirDto.class));
        when(transferService.transferir(any(TransferirDto.class))).thenReturn(expectedResponse);

        MsjResponce result = transferController.transferir(transferirDto);

        assertEquals(expectedResponse, result);
        verify(transferValidator).validate(transferirDto);
        verify(transferService).transferir(transferirDto);
    }

    @Test
    void transferir_AccountNotFound_ReturnsErrorResponse() {
        TransferirDto transferirDto = new TransferirDto();

        doThrow(new CuentaNotFoundException(null)).when(transferValidator).validate(any(TransferirDto.class));

        MsjResponce result = transferController.transferir(transferirDto);

        assertEquals("FALLIDA", result.getEstado());
        assertEquals("Error en la transferencia. Verifique los datos.", result.getMensaje());
        verify(transferValidator).validate(transferirDto);
        verify(transferService, never()).transferir(any());
    }

    @Test
    void transferir_ValidationSuccess_ServiceError_ReturnsErrorResponse() {
        TransferirDto transferirDto = new TransferirDto();

        doNothing().when(transferValidator).validate(any(TransferirDto.class));
        when(transferService.transferir(any(TransferirDto.class))).thenThrow(new CuentaNotFoundException(null));

        MsjResponce result = transferController.transferir(transferirDto);

        assertEquals("FALLIDA", result.getEstado());
        assertEquals("Error en la transferencia. Verifique los datos.", result.getMensaje());
        verify(transferValidator).validate(transferirDto);
        verify(transferService).transferir(transferirDto);
    }
}
