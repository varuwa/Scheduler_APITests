package com.scheduler.qa;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class ApiTests {
    @Test
    public void createUserTest() throws IOException {
        String response = apiLoginOrRegistration("userQABarbaraAPI@gmail.com", "barbara");
      // System.out.println(response);
        int statusCode = sendPostRequest("/api/records").execute().returnResponse().getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, 500);

//        JsonElement parsed = new JsonParser().parse(response);
//        JsonElement token = parsed.getAsJsonObject().get("token");
//        JsonElement status = parsed.getAsJsonObject().get("status");
//        JsonElement registration = parsed.getAsJsonObject().get("registration");
//
//        System.out.println(token);
//        //System.out.println(status);
//        //System.out.println(registration);
//
//        Assert.assertFalse(registration.getAsBoolean());
//        Assert.assertEquals(status.toString(), "\"Login success\"");
    }

    @Test
    public void getRecordsPeriod() throws IOException {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InZhcnV3YSsxMDBAZ21haWwuY29tIn0.ir3Gixmn8HVasm3GDO8ToFi9ZQlhjmabufnYYg0AGF0";
        String response = Request.Get("https://super-scheduler-app.herokuapp.com/api/recordsPeriod")
                .addHeader("Authorization", token)
                .addHeader("Content-Type", "application/json")
                .execute().returnContent().asString();
        System.out.println(response);

        JsonElement parsed = new JsonParser().parse(response);
        String monthFrom = parsed.getAsJsonObject().get("monthFrom").toString();
        String monthTo = parsed.getAsJsonObject().get("monthTo").toString();
        String yearFrom = parsed.getAsJsonObject().get("yearFrom").toString();
        String yearTo = parsed.getAsJsonObject().get("yearTo").toString();

        String records = sendPostRequest("/api/records")
                .addHeader("Authorization", token)
                .addHeader("Content-Type", "application/json")
                .bodyString("{\"monthFrom\": " + monthFrom + ", \"monthTo\": " + monthTo + ", \"yearFrom\":"
                        + yearFrom + ", \"yearTo\": " + yearTo + "}", ContentType.APPLICATION_JSON)
                .execute().returnContent().asString();

        System.out.println(records);

    }

    private String apiLoginOrRegistration(String email, String password) throws IOException {
        return sendPostRequest("/api/login")
                //.addHeader("Content-Type", "application/json")
                .bodyString("{\"email\": \"" + email +"\", \"password\": \""+ password + "\"}", ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();
    }

    private Request sendPostRequest(String controller) {
        return Request.Post("https://super-scheduler-app.herokuapp.com" + controller);
    }

    public int handleResponse(final HttpResponse response){
        StatusLine statusLine = response.getStatusLine();
       return statusLine.getStatusCode();
    }
}
