package br.com.cesaretransportesMobile.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * <p>Classe utilitária na geração de mapas.</p>
 * <p>Esta classe é utilizada para desenhar um overlay num mapa</p>
 * @author cesar sousa
 *
 */
public class ImagemOverlay extends Overlay{
	
	private Paint paint = new Paint();
	private final int imagemId;
	private final GeoPoint geoPoint;
	
	public ImagemOverlay(GeoPoint geoPoint, int resId){
		this.geoPoint = geoPoint;
		imagemId = resId;
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		
		/*
		 * conversão das coordenadas para pixels
		 */
		Point p = mapView.getProjection().toPixels(geoPoint, null);
		Bitmap bitmap = BitmapFactory.decodeResource(mapView.getResources(), this.imagemId);
		RectF r = new RectF(p.x, p.y, p.x + bitmap.getWidth(), p.y + bitmap.getHeight());
		canvas.drawBitmap(bitmap, null, r, paint);
	}
	
	/*
	 * O codigo do metodo draw converte o recurso da imagem fornecida para um bitmap.
	 * GeoPoint foi convertido para Point para obter os pontos em pixels relativos a tela, 
	 * dessa forma o objeto rectf é criado com  as coordenadas xy deste ponto
	 */
	
}
