package com.framework.utils;

import io.cucumber.java.Before;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

public class Base {
    protected static Properties globalProps;
    protected static InputStream isGlobalProps;

    protected static Properties ratingPayloadProps=new Properties();
    protected static InputStream isRatingPayload;

    protected static RequestSpecification req;
    protected String baseEndPoint;

    protected static HashMap<String, String> strings = new HashMap<String, String>();

    public RequestSpecification RequestSpecificationWithSeesionId(String subURI) throws IOException {
        String baseURI = getGlobalProperties("baseURI").concat(subURI);
        String apiKey = getGlobalProperties("api_key");
        String language = getGlobalProperties("language");

        PrintStream ps = new PrintStream(new FileOutputStream("logging.txt"));
        req = new RequestSpecBuilder().setBaseUri(baseURI)
                .addQueryParam("api_key", apiKey)
                .addQueryParam("guest_session_id", getGlobalProperties("session_id"))
                .addFilter(RequestLoggingFilter.logRequestTo(ps))
                .addFilter(ResponseLoggingFilter.logResponseTo(ps))
                .setContentType(ContentType.JSON).build();


        return req;
    }

    public RequestSpecification RequestSpecificationWithoutSeesionId(String subURI) throws IOException {
        String baseURI = getGlobalProperties("baseURI").concat(subURI);
        String apiKey = getGlobalProperties("api_key");
        String language = getGlobalProperties("language");

        PrintStream ps = new PrintStream(new FileOutputStream("logging.txt"));
        req = new RequestSpecBuilder().setBaseUri(baseURI)
                .addQueryParam("api_key", apiKey)
                .addQueryParam("language", language)
                .addFilter(RequestLoggingFilter.logRequestTo(ps))
                .addFilter(ResponseLoggingFilter.logResponseTo(ps))
                .setContentType(ContentType.JSON).build();

        return req;
    }

    public static InputStream getRatingPayloadBodyAsStream() throws IOException {
        File propFile = new File("src/test/resources/ratingPayloadBody.Properties");
        return new FileInputStream(propFile);

    }

    public static InputStream getGlobalPropertiesAsStream() throws IOException {
        File propFile = new File("src/test/resources/app.Properties");
        return new FileInputStream(propFile);
    }

    public static String getGlobalProperties(String key) throws IOException {
        return globalProps.get(key).toString();
    }

    public static void setGlobalProperties(String key, String value) throws IOException {
        globalProps.setProperty(key, value);
    }


    public String getResponseObjectKeyValue(Response response, String key) {

        String responseString = response.asString();
        JsonPath js = new JsonPath(responseString);
        return js.get(key).toString();
    }


    public JSONObject getResponseAsJSONObject(Response response) throws JSONException {
        String responseString = response.asString();
        return new JSONObject(responseString);
    }
}
