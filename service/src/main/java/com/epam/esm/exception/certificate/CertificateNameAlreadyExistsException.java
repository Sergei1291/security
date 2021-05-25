package com.epam.esm.exception.certificate;

public class CertificateNameAlreadyExistsException extends RuntimeException {

    public CertificateNameAlreadyExistsException(String message) {
        super(message);
    }

}