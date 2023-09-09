package com.instagram.brian.downloadtask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DownloadTask extends AsyncTask<String, Integer, String> {

    private Context context;
    private ProgressDialog dialogoProgreso;

    public DownloadTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialogoProgreso = new ProgressDialog(context);
        dialogoProgreso.setMessage("Downloading...");
        dialogoProgreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialogoProgreso.setIndeterminate(false);
        dialogoProgreso.setMax(100);
        dialogoProgreso.setProgress(0);
        dialogoProgreso.show();
    }

    @Override
    protected String doInBackground(String... urls) {
        String url = urls[0];
        File archivoSalida = getArchivo(url);
        try{
            URL urlObj = new URL(url);
            HttpURLConnection conexion = (HttpURLConnection) urlObj.openConnection();
            conexion.connect();
            if(conexion.getResponseCode() != HttpURLConnection.)


        }catch (Exception e) {
            return null;
        }
    }

    public File getArchivo(String url) {
        File directorio = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Descargas Instagram" + File.separator);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        String urlCortada = url.split("\\?")[0];
        File archivoSalida;
        int i = 0;
        do {
            i++;
            String nombreArchivo = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + "-" + getNumero(i) + urlCortada.substring(urlCortada.lastIndexOf(".") + 1);
            archivoSalida = new File(directorio, reemplazarFormatoImagen(nombreArchivo));
        } while (archivoSalida.exists());
        return archivoSalida;
    }

    public String getNumero(int numero) {
        if(numero>0 & numero<=9){
            return "000" + numero;
        }else if(numero>=10 && numero<=99){
            return "00" + numero;
        }else if(numero>=100 && numero<=999){
            return "0" + numero;
        }else{
            return ""+ numero;
        }
    }

    public String reemplazarFormatoImagen(String nombreArchivo) {
        if (nombreArchivo.contains(".webp")) {
            return nombreArchivo.replaceAll(".webp", ".jpg");
        } else if (nombreArchivo.contains(".heic")) {
            return nombreArchivo.replaceAll(".heic", ".jpg");
        } else {
            return nombreArchivo;
        }
    }
}
