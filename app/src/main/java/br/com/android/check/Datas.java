package br.com.android.check;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class Datas {
	// converte de String para data
	public static Date convertStringEmData(String data, String sdf) {
		if (data == null || data.equals(""))
			return null;
		try {
			SimpleDateFormat formato = new SimpleDateFormat(sdf);
			Date date = (java.util.Date) formato.parse(data);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	// converte de String para data
	public static Date convertStringEmData(String data) {
		if (data == null || data.equals(""))
			return null;
		try {
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = (java.util.Date) formato.parse(data);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String convertDataEmString(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(data);
	}

	public static Boolean cancelamentoDisponivel(Date dataConsulta, Date dataAtual) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(dataConsulta);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(dataAtual);

		long d1 = c1.getTimeInMillis();
		long d2 = c2.getTimeInMillis();

		int diferenca = 0;

		switch (c1.compareTo(c2)) {
		case -1:
			diferenca = (int) ((d2 - d1) / (60 * 60 * 1000));
			break;
		case 1:
			diferenca = (int) ((d1 - d2) / (60 * 60 * 1000));
			break;
		default:
			return false;
		}

		return (diferenca >= 24);
	}

	// retorna se a consulta para essa especialidade pode ser marcada
	public static Boolean marcacaoDisponivel(Date dataMarcada, Date dataPendenteMarcacao) {
		int MILLIS_IN_DAY = 86400000;

		Calendar c1 = Calendar.getInstance();
		c1.setTime(dataMarcada);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(dataPendenteMarcacao);

		long d1 = c1.getTimeInMillis();
		long d2 = c2.getTimeInMillis();

		int diferenca = 0;

		switch (c1.compareTo(c2)) {
		case -1:
			diferenca = (int) ((d2 - d1) / MILLIS_IN_DAY);
			break;
		case 1:
			diferenca = (int) ((d1 - d2) / MILLIS_IN_DAY);
			break;
		default:
			return false;
		}

		return (diferenca > 30);
	}

}