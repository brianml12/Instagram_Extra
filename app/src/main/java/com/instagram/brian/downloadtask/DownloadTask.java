package com.instagram.brian.downloadtask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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
        HttpURLConnection conexion = null;
        try{
            URL urlObj = new URL(url);
            conexion = (HttpURLConnection) urlObj.openConnection();
            conexion.connect();
            if(conexion.getResponseCode() == HttpURLConnection.HTTP_OK){
                int longitudArchivo = conexion.getContentLength();
                FileOutputStream outputStream = new FileOutputStream(archivoSalida);
                InputStream inputStream = conexion.getInputStream();
                byte[] buffer = new byte[4096];
                int bytesleidos;
                long totalBytesleidos = 0;
                while ((bytesleidos = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, bytesleidos);
                    totalBytesleidos += bytesleidos;
                    int progress = (int) (totalBytesleidos * 100 / longitudArchivo);
                    publishProgress(progress);
                }
                outputStream.close();
                inputStream.close();
                Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                intent.setData(Uri.fromFile(archivoSalida));
                this.context.sendBroadcast(intent);
                return archivoSalida.getPath();
            }else {
                return "error server returned HTTP " + conexion.getResponseCode() + " " + conexion.getResponseMessage();
            }
        }catch (Exception e) {
            return null;
        } finally {
            if(conexion!=null){
                conexion.disconnect();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        dialogoProgreso.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String textoToast) {
        super.onPostExecute(textoToast);
        dialogoProgreso.dismiss();
        if (textoToast != null) {
            if(textoToast.contains("error server returned HTTP")){
                Toast.makeText(context, textoToast, Toast.LENGTH_SHORT);
            } else{
                Toast.makeText(context, "Guardado en " + textoToast, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "Error al descargar el archivo", Toast.LENGTH_SHORT).show();
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
