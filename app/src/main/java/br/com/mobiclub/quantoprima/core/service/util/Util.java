package br.com.mobiclub.quantoprima.core.service.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit.RetrofitError;

public class Util {
    private static final int BUFFER_SIZE = 0x1000;

    public static byte[] streamToBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (stream != null) {
            byte[] buf = new byte[BUFFER_SIZE];
            int r;
            while ((r = stream.read(buf)) != -1) {
                baos.write(buf, 0, r);
            }
        }
        return baos.toByteArray();
    }

    public static boolean urlContains(RetrofitError cause, String url) {
        return cause.getUrl().contains(url);
    }


    private Util() {
        // No instances.
    }
}
