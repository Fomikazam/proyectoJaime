package model;

import com.squareup.okhttp.*;
import java.io.IOException;


public class ApiRequests {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client;

    public ApiRequests(){
        client = new OkHttpClient();
    }

    public String getRequest(String url) throws IOException {
        Request request= new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }
    
    public String getRequest(String url, String json) throws IOException{
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public String postRequest(String url, String json) throws IOException{
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }
    
    
    public String putRequest(String url, String json) throws IOException{
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }
    
    public String deleteRequest(String url, String json) throws IOException{
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }

}
