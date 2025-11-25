package com.bio.clinic.dtos;

public class DadosCheckin {
    
    private Long idConsulta;
    private Double latitude;
    private Double longitude;

    // Construtor Vazio
    public DadosCheckin() {}

    // Getters e Setters
    public Long getIdConsulta() { return idConsulta; }
    public void setIdConsulta(Long idConsulta) { this.idConsulta = idConsulta; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}