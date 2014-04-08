package br.com.varjaosite.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.topsys.util.TSCryptoUtil;
import br.com.topsys.util.TSDateUtil;
import br.com.topsys.util.TSUtil;

public final class Utilitarios {

	private Utilitarios() {

	}

	public static String tratarString(String valor) {
		if (!TSUtil.isEmpty(valor)) {
			valor = valor.trim().replaceAll("  ", " ");
		}

		return valor;
	}

	public static Long tratarLong(Long valor) {
		if ((!TSUtil.isEmpty(valor)) && (valor.equals(Long.valueOf(0L)))) {
			valor = null;
		}

		return valor;
	}

	public static String removerAcentos(String palavra) {
		if (TSUtil.isEmpty(palavra)) {
			return null;
		}

		return Normalizer.normalize(new StringBuilder(palavra), Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

	public static String gerarHash(String valor) {
		if (!TSUtil.isEmpty(valor)) {
			valor = TSCryptoUtil.gerarHash(valor, "md5");
		}

		return valor;
	}

	public static String gerarSenha() {
		Calendar rightNow = Calendar.getInstance();

		int diaAtual = rightNow.get(5);
		int mesAtual = rightNow.get(2) + 1;
		int anoAtual = rightNow.get(1);
		int horaAtual = rightNow.get(11);
		int minutoAtual = rightNow.get(12);
		int segAtual = rightNow.get(13);
		int miliAtual = rightNow.get(14);

		String senha = anoAtual + mesAtual + diaAtual + horaAtual + minutoAtual + segAtual + miliAtual + "";

		return senha;
	}

	@SuppressWarnings("resource")
	public static byte[] getBytes(File file) {

		int len = (int) file.length();

		byte[] sendBuf = new byte[len];

		FileInputStream inFile = null;

		try {

			inFile = new FileInputStream(file);

			inFile.read(sendBuf, 0, len);

		} catch (FileNotFoundException fnfex) {

			fnfex.printStackTrace();

		} catch (IOException ioex) {

			ioex.printStackTrace();
		}

		return sendBuf;
	}

	public static boolean isValidDate(String data) {

		if (!TSUtil.isEmpty(data)) {

			SimpleDateFormat dateFormat = new SimpleDateFormat(TSDateUtil.DD_MM_YYYY);

			if (data.trim().length() != dateFormat.toPattern().length()) {
				
				return false;
			}

			dateFormat.setLenient(false);

			try {

				dateFormat.parse(data.trim());

			} catch (ParseException pe) {

				return false;
			}

		} else {

			return false;
		}

		return true;
	}
}