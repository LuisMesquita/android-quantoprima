package br.com.mobiclub.quantoprima.core.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import br.com.mobiclub.quantoprima.core.service.util.Util;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedByteArray;

@SuppressLint("DefaultLocale")
public class RetrofitClientMock implements Client {
    private Context context;

    private String scenario = null;

    public RetrofitClientMock(Context ctx) {
        this.context = ctx;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    @Override
    public Response execute(Request request) throws IOException {
        URL requestedUrl = new URL(request.getUrl());
        String requestedMethod = request.getMethod();

        String prefix = "";
        if (this.scenario != null) {
            prefix = scenario + "_";
        }

        String path = requestedUrl.getPath().replace("/api/Service", "");
        String fileName = (prefix + requestedMethod + path).replace("/", "_");
        fileName = fileName.toLowerCase();

        int resourceId = context.getResources().getIdentifier(fileName, "raw",
                context.getPackageName());

        if (resourceId == 0) {
            Log.wtf("YourTag", "Could not find res/raw/" + fileName + ".json");
            throw new IOException("Could not find res/raw/" + fileName + ".json");
        }

        InputStream inputStream = context.getResources().openRawResource(resourceId);

        String mimeType = URLConnection.guessContentTypeFromStream(inputStream);
        if (mimeType == null) {
            mimeType = "application/json";
        }

        byte[] bytes = Util.streamToBytes(inputStream);

        String bodyCharset = MimeUtil.parseCharset(mimeType);
        String responseString = new String(bytes, bodyCharset);

        int httpStatus = getHttpStatus(responseString);
        httpStatus = 200;
        Response response = new Response(path, httpStatus, "Content from res/raw/" + fileName,
                new ArrayList<Header>(), new TypedByteArray(mimeType, bytes));
        return response;
    }

    private int getHttpStatus(String response) {
        JsonElement element = new JsonParser().parse(response);
        JsonObject object = element.getAsJsonObject();
        int httpMessageStatus = object.get("HttpStatus").getAsInt();
        return httpMessageStatus;
    }
}