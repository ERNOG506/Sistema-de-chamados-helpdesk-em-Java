package br.com.nogueira.servicedesk.service;

import br.com.nogueira.servicedesk.exception.BusinessException;

import java.util.regex.Pattern;

public class ValidationService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$");

    public void requireText(String fieldName, String value, int minimumLength) {
        if (value == null || value.trim().length() < minimumLength) {
            throw new BusinessException(fieldName + " deve ter pelo menos " + minimumLength + " caracteres.");
        }
    }

    public void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new BusinessException("E-mail invalido.");
        }
    }

    public void requirePositiveId(int id, String fieldName) {
        if (id <= 0) {
            throw new BusinessException(fieldName + " deve ser maior que zero.");
        }
    }
}
