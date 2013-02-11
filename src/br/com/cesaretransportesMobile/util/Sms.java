package br.com.cesaretransportesMobile.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class Sms {
	
	private static final String CATEGORIA = "livro";

	public void enviarSms(Context context, String destino, String mensagem) {
		try {
			SmsManager smsManager = SmsManager.getDefault();
			PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
			smsManager.sendTextMessage(destino, null, mensagem, pIntent, null);
		} catch (Exception e) {
			Log.e(CATEGORIA, "Erro ao enviar Sms: " + e.getMessage(), e);
		}	
	}

	public SmsMessage receberMsg(Intent intent) {
		/*
		 * Lê uma mensagem da Intent, recebida por um intent filter
		 * configurado para a ação "android.provider.Telephony.SMS_RECEIVED".
		 */
		SmsMessage[] mensagens = getMessagesFromIntent(intent);
		if (mensagens != null){
			return mensagens[0];
		}
		return null;
	}

	@SuppressWarnings("unused")
	private SmsMessage[] getMessagesFromIntent(Intent intent) {
		Log.d(CATEGORIA, "Sms.getMessagesFromIntent: " + intent.getAction());
		Object messages[] = (Object[])intent.getSerializableExtra("pdus");
		byte pduObjs[][] = new byte[messages.length][];
		
		for (int i = 0; i < messages.length; i++) {
			pduObjs[i] = (byte[]) messages[i];
		}
		byte pdus[][] = new byte[pduObjs.length][];

		if (pdus == null) {
			return null;
		}

		int pduCount = pdus.length;
		if (pduCount == 0) {
			return null;
		}

		SmsMessage msgs[] = new SmsMessage[pduCount];
		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);
			//String celular = msgs[0].getDisplayOriginatingAddress();
			//String mensagem = msgs[0].getDisplayMessageBody();
		}
		
		return msgs;
	}
}
