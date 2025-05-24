package com.speedlaundryapp.userapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Locale;

public class GeneralUtil {
    public static String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

    public static void errorToast(Context context, JSONObject object) throws JSONException {
        JSONObject obj = new JSONObject(object.getString("data"));
        Iterator<String> iter = obj.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            Object value = obj.get(key);
            String str = ""+value;
            if(str.charAt(0) == '[' && str.charAt(str.length()-1) == ']')
            {
                Toast.makeText(context, "ERRERRTOAST: " +  str.substring(1, str.length() - 1), Toast.LENGTH_SHORT).show();
            }

        }
    }
    public static String convertRupiah(int intPrice) {
        Locale localId = new Locale("in", "ID");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(localId);
        String strFormat = formatter.format(intPrice);
        return strFormat;
    }
}
