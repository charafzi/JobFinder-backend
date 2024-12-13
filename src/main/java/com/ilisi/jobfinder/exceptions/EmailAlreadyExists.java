package com.ilisi.jobfinder.exceptions;

public class EmailAlreadyExists extends RuntimeException {
    public EmailAlreadyExists(String message) {
      super(message);
    }
}
