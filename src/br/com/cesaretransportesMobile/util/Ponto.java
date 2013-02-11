package br.com.cesaretransportesMobile.util;

import com.google.android.maps.GeoPoint;

import android.location.Location;

/**
 * <p>Classe utilit�ria na gera��o de mapas.</p>
 * <p>Utilizada para facilitar a convers�o de graus para microdegrees. No construtor, receber� 
 * as coordenadas, caso estas sejam informadas em graus (decimal), ser�o convertidas
 * para graus*1E6 (inteiro), caso estas sejam fornecidas via Gps um obj {@link Location}
 * sera recebido automaticamente pelo Android, seu valor decimal ser� obtidos atrav�s de 
 * chamadas aos m�todos {@link Location#getLatitude()} e {@link Location#getLongitude()}.</p>
 * 
 * @author cesar sousa
 *
 */
public class Ponto extends GeoPoint{

   /**
    * constr�i um GeoPoint a partir de valores inteiros.
    * @param latitudeE6 latitude
    * @param longitudeE6 longetude
    */
    public Ponto(int latitudeE6, int longitudeE6) {
        super(latitudeE6, longitudeE6);
    }
    
    /**
     * constr�i um GeoPoint a partir de valores doubles.
     * @param latitudeE6 latitude
     * @param longitudeE6 longetude
     */
    public Ponto(double latitude, double longitude) {
        this((int) (latitude * 1E6), (int) (longitude * 1E6));
    }
    
    /**
     * constr�i um GeoPoint a partir de uma localiza��o.
     * @param location localiza��o do ponto.
     */
    public Ponto(Location location){
        this(location.getLatitude(), location.getLongitude());
    }
}

