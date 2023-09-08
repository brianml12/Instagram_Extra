package com.instagram.brian.main;

import android.app.Activity;
import android.os.Build;

public class Brian {
    private static int posicion = 0;

    public static int getPosicion() {
        return posicion;
    }

    public static void setPosicion(int posicion) {
        Brian.posicion = posicion;
    }

    public static void descargar(Object json_media, Activity actividad){
        if(isPermisoEscrituraAlmacenamientoExt()){
            
        }
    }

    private static boolean isPermisoEscrituraAlmacenamientoExt(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean permisoConcedido;
            try {
                permisoConcedido = Integer.parseInt(Activity.class.getMethod("checkSelfPermission", new Class[]{String.class}).invoke(InstagramApplication.getCtx(), new Object[]{"android.permission.WRITE_EXTERNAL_STORAGE"}).toString()) == 0;
            } catch (Exception e) {
                permisoConcedido = false;
            }
            if (!permisoConcedido) {
                return true;
            }
        }
        return false;

    }
}
