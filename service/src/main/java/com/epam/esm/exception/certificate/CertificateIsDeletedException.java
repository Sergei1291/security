package com.epam.esm.exception.certificate;

public class CertificateIsDeletedException extends RuntimeException {

    public CertificateIsDeletedException(String message) {
        super(message);
    }

}