package com.bobomee.android.updatechecker.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 模拟数据工具类
 */
public class ImitateUtil {

    public static String convertToString(Context context, String name) {

        try {
            InputStream is = context.getAssets().open(name);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }

            br.close();
            String data = sb.toString();
            sb = null;
            return data;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("抛异常啦");
        }

        return null;
    }

}
