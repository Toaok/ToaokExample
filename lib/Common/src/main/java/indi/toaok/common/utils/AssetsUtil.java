package indi.toaok.common.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author hpp
 * @version 1.0  2019/6/5.
 */
public class AssetsUtil {

    public static String getFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";

            while ((line = bufReader.readLine()) != null)
                result += line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
