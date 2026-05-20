package br.com.nogueira.servicedesk.service;

import br.com.nogueira.servicedesk.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationServiceTest {
    private final ValidationService validationService = new ValidationService();

    @Test
    void shouldAcceptValidEmail() {
        assertDoesNotThrow(() -> validationService.validateEmail("usuario@empresa.com"));
    }

    @Test
    void shouldRejectInvalidEmail() {
        assertThrows(BusinessException.class, () -> validationService.validateEmail("email-invalido"));
    }

    @Test
    void shouldRejectShortTitle() {
        assertThrows(BusinessException.class, () -> validationService.requireText("Titulo", "abc", 5));
    }
}
