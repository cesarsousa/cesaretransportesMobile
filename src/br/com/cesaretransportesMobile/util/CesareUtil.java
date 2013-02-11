package br.com.cesaretransportesMobile.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.cesaretransportesMobile.domain.Cliente.TipoDoDocumento;

public class CesareUtil {	

	/**
	 * 
	 * @return a string que representa a data atual do sistema no formato.<br/> "dd/MM/yyyy hh:mm:ss".
	 */
	public static String getDataDoSistema() {

		String data;
		String hora;

		java.util.Date dataDoSistema = new java.util.Date();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		data = simpleDateFormat.format(dataDoSistema);

		simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
		hora = simpleDateFormat.format(dataDoSistema);

		return data + " " + hora;
	}

	/*public static String enviarEmail(String cetrans, String senha,
			String destino, String assunto, String mensagem) {

		try {
			java.util.Properties properties = new java.util.Properties();

			properties.put("mail.smtp.host", "smtp.gmail.com");
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.debug", "true");
			properties.put("mail.smtp.debug", "true");
			properties.put("mail.mime.charset", "ISO-8859-1");
			properties.put("mail.smtp.port", "465");
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.socketFactory.port", "465");
			properties.put("mail.smtp.socketFactory.fallback", "false");
			properties.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			properties.put("mail.smtp.quitwait", "false");
			properties.setProperty("mail.transport.protocol", "smtp");

			Session session = Session.getInstance(properties, new Autenticacao(cetrans, senha));

			MimeMessage msg = new MimeMessage(session);
			MimeBodyPart mbp1 = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();

			msg.setFrom(new InternetAddress(cetrans));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(
							destino));
			msg.setSubject(assunto);
			
			mbp1.setDataHandler(new DataHandler(mensagem, "text/html"));

			multipart.addBodyPart(mbp1);
			msg.setHeader("X-Mailer", "smtpsend");
			msg.setSentDate(new Date());
			msg.setContent(multipart);
			Transport.send(msg);			
			
		} catch (AddressException e) {
			e.printStackTrace();
			return "AE01";
		} catch (MessagingException e) {			
			e.printStackTrace();
			return "ME01";
		}
		
		return String.valueOf(true);
	}*/
	
	/**
	 * <p>Formata a data de acordo com o padrão especificado.</p>
	 * <p>Exemplo de padrões</p>
	 * <ul>
	 * <li>"dd/MM/yyyy HH:mm:ss" 10/09/2011 15:54:47
	 * <li>"dd/MM/yyyy" 10/09/2011<br/>
	 * <li>"yyyy/MM/dd" 2011/09/10<br/>
	 * <li>"HH:mm:ss" 15:54:47</li>
	 * </ul> 
	 * 
	 * @param data
	 *            data a ser formatada.
	 * @param padrao
	 *            padrão a ser utilizado (dia = dd, mês = MM, ano = yyyy).
	 * @return data formatada.
	 */
	public static String formatarData(Calendar data, String padrao) {
		return new SimpleDateFormat(padrao).format(data.getTime()).toString();
	}

	/**
	 * <p>Formata uma sequência numérica inserindo uma máscara de acordo com o tipo
	 * de documento.</p>
	 * <ul>
	 * <li>CPF: formato da máscara 999.999.999-99
	 * <li>CNPJ: formato da máscara 99.999.999/9999-99</li>
	 * </ul>
	 *  
	 * @param tipo o tipo do documento (CPF ou CNPJ).
	 * @param numero sequência numérica que será inserida a máscara.
	 * @return a sequência numérica devidamente mascarada.
	 */
	public static String formatarDocumento(TipoDoDocumento tipo, String numero) {		
		if(TipoDoDocumento.CNPJ == tipo){
			return numero.substring(0, 2) + "." + numero.substring(2, 5) + "." + numero.substring(5, 8) 
			+ "/" + numero.substring(8, 12) + "-" + numero.substring(12);			
		}else{
			return numero.substring(0, 3) + "." + numero.substring(3, 6) + "." + numero.substring(6, 9) 
			+ "-" + numero.substring(9);
		}		
	}

	/**
	 * <p>Formata uma sequência numérica inserindo uma máscara de telefone no 
	 * formato (99) 9999-9999.</p>
	 * 
	 * @param telefone a sequência numérica a ser mascarada.
	 * @return o número do telefone mascarado.
	 */
	public static String formatarTelefone(String telefone) {
		return "(" + telefone.charAt(0) + telefone.charAt(1) + ") "
				+ telefone.charAt(2) + telefone.charAt(3) + telefone.charAt(4) + telefone.charAt(5) + "-" 
				+ telefone.charAt(6) + telefone.charAt(7) + telefone.charAt(8) + telefone.charAt(9);
	}

	public static String getDDD(String telefone) {
		return telefone.substring(0, 2);
	}

	public static String getTelefoneSemDdd(String telefone) {
		return telefone.substring(2);
	}

	/**
	 * <p>Formata uma String inserindo uma máscara de placa no 
	 * formato AAA-9999.</p>
	 * 
	 * @param placa a placa do veículo.
	 * @return a placa no formato.
	 */
	public static String formatarPlaca(String placa) {
		return placa.substring(0, 3).toUpperCase() + "-" + placa.substring(3);
	}
	
	
}
