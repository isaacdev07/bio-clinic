package com.bio.clinic.utils;

public class GeoUtils {

    // Raio da Terra em Metros (aproximado)
    private static final int RAIO_TERRA = 6371000;

    // Calcula a distância em METROS entre dois pontos
    public static double calcularDistanciaEmMetros(double lat1, double lon1, double lat2, double lon2) {
        
        // Converte graus para radianos
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        // Retorna a distância em metros
        return RAIO_TERRA * c;
    }
}