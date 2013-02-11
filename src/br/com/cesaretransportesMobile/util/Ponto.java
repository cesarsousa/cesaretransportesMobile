package br.com.cesaretransportesMobile.util;

import com.google.android.maps.GeoPoint;

import android.location.Location;

/**
 * <p>Classe utilitária na geração de mapas.</p>
 * <p>Utilizada para facilitar a conversão de graus para microdegrees. No construtor, receberá 
 * as coordenadas, caso estas sejam informadas em graus (decimal), serão convertidas
 * para graus*1E6 (inteiro), caso estas sejam fornecidas via Gps um obj {@link Location}
 * sera recebido automaticamente pelo Android, seu valor decimal será obtidos através de 
 * chamadas aos métodos {@link Location#getLatitude()} e {@link Location#getLongitude()}.</p>
 * 
 * @author cesar sousa
 *
 */
public class Ponto extends GeoPoint{

   /**
    * constrói um GeoPoint a partir de valores inteiros.
    * @param latitudeE6 latitude
    * @param longitudeE6 longetude
    */
    public Ponto(int latitudeE6, int longitudeE6) {
        super(latitudeE6, longitudeE6);
    }
    
    /**
     * constrói um GeoPoint a partir de valores doubles.
     * @param latitudeE6 latitude
     * @param longitudeE6 longetude
     */
    public Ponto(double latitude, double longitude) {
        this((int) (latitude * 1E6), (int) (longitude * 1E6));
    }
    
    /**
     * constrói um GeoPoint a partir de uma localização.
     * @param location localização do ponto.
     */
    public Ponto(Location location){
        this(location.getLatitude(), location.getLongitude());
    }
}

