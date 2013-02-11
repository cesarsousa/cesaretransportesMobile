package br.com.cesaretransportesMobile.activity;

import java.io.IOException;
import java.util.List;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import br.com.cesaretransportesMobile.util.ImagemOverlay;
import br.com.cesaretransportesMobile.util.Ponto;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

/**
 * <p>Classe Geradora de mapas. Gera uma visualização Google Maps 
 * a partir de um endereço no formato rua numero cidade estado.</p>
 * 
 * @author cesar sousa
 *
 */
public class Mapa extends MapActivity {
	
	private static final int MODO_SATELITE = 1;
	private static final int MODO_RUA = 2;
	private static final int MODO_TRANSITO = 3;

	@Override
	protected void onCreate(Bundle icicle) {		
		super.onCreate(icicle);
		setContentView(R.layout.mapview);
		
		Address endereco = (Address) getIntent().getParcelableExtra("endereco");
		
		Toast.makeText(this, getEndereco(endereco), Toast.LENGTH_LONG).show();
		
		MapView mapa = (MapView) findViewById(R.id.mapa);
		MapController controller = mapa.getController();
		mapa.setClickable(true);
		mapa.setStreetView(true);
		mapa.setBuiltInZoomControls(true);
		controller.setZoom(18);
		
		Ponto ponto = new Ponto(endereco.getLatitude(), endereco.getLongitude());
		mapa.getOverlays().add(new ImagemOverlay(ponto, R.drawable.image_overlay));			
		controller.setCenter(ponto);
		
		/*try {
			Geocoder geocoder = new Geocoder(this, Locale.getDefault());
			List<Address> enderecos = geocoder.getFromLocationName(endereco, 1);
			Ponto ponto = new Ponto(enderecos.get(0).getLatitude(), enderecos.get(0).getLongitude());
			mapa.getOverlays().add(new ImagemOverlay(ponto, R.drawable.image_overlay));			
			controller.setCenter(ponto);
		}catch (IOException e) {
			Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
		}*/
			
		/*
		 * coordenadas
		 * latitude -25.518141
		 * longetude -49.178678
		 * 
		 */
		
	}

	private String getEndereco(Address endereco) {
		return endereco.getAddressLine(0) + ", " + endereco.getAddressLine(1) + ", " + endereco.getAddressLine(2)  + ", " + endereco.getAddressLine(3);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
    	MenuItem item = menu.add(0, MODO_SATELITE, 0, "Satélite");
    	item.setIcon(R.drawable.maps_icone); 
    	item = menu.add(0,MODO_RUA, 0, "Ruas");
    	item.setIcon(R.drawable.maps_icone);
    	item = menu.add(0, MODO_TRANSITO, 0, "Tránsito");
    	item.setIcon(R.drawable.maps_icone);
    	return true;
    }
	
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		MapView mapa = (MapView) findViewById(R.id.mapa);
    	switch(item.getItemId()){
    	case MODO_SATELITE:
    		mapa.setSatellite(true);
    		mapa.setStreetView(false);
    		mapa.setTraffic(false);
    		return true;
    	case MODO_RUA:
    		mapa.setSatellite(false);
    		mapa.setStreetView(true);
    		mapa.setTraffic(false);
    		return true;
    	case MODO_TRANSITO:
    		mapa.setSatellite(false);
    		mapa.setStreetView(false);
    		mapa.setTraffic(true);
    		return true;
    	}
    	return false;    	
    }
	
	public Address getEndereco(String endereco, Geocoder geocoder) throws IOException{		
		List<Address> enderecos = geocoder.getFromLocationName(endereco, 1);
		return enderecos.get(0);
		
	}
}