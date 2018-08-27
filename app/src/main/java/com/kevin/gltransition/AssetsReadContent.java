package com.kevin.gltransition;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhaoliangtai on 2018/8/15.
 */

public class AssetsReadContent {
    public static String readAssetsContent(Context context, String path) {
        AssetManager manager = context.getAssets();
        String result = null;
        InputStream is = null;
        try {
            is = manager.open(path);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            result = new String(buffer, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
