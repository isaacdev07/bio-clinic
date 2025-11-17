package com.bio.clinic.dtos;

public class FaceRegistrationRequest {
    
    // O descritor facial que virá do frontend
    private String descriptor;

    // Construtor, Getter e Setter
    public FaceRegistrationRequest() {
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }
}