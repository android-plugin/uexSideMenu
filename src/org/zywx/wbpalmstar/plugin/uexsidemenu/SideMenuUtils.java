package org.zywx.wbpalmstar.plugin.uexsidemenu;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;

import org.zywx.wbpalmstar.base.BUtility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class SideMenuUtils {

    public static BitmapDrawable getLocalImgBitmap(Context ctx, String imgUrl) {
        if (imgUrl == null || imgUrl.length() == 0) {
            return null;
        }
        BitmapDrawable bitmap = null;
        InputStream is = null;
        try {
            if (imgUrl.startsWith(BUtility.F_Widget_RES_SCHEMA)) {
                is = BUtility.getInputStreamByResPath(ctx, imgUrl);
            } else if (imgUrl.startsWith(BUtility.F_FILE_SCHEMA)) {
                imgUrl = imgUrl.replace(BUtility.F_FILE_SCHEMA, "");
                is = new FileInputStream(imgUrl);
            } else if (imgUrl.startsWith(BUtility.F_Widget_RES_path)) {
                try {
                    is = ctx.getAssets().open(imgUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (imgUrl.startsWith("/")) {
                is = new FileInputStream(imgUrl);
            }
            bitmap = new BitmapDrawable(ctx.getResources(), is);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
}
