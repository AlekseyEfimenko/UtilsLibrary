package com.utils;

import static io.restassured.RestAssured.oauth2;
import aquality.selenium.core.logging.Logger;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import java.util.Map;

/**
 * Class for managing API requests
 */
public class ApiManager {
    private static final String BASE_PATH = FileManager.getInstance().getProperties("ApiUrl");
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String AUTHORISATION = "Authorization";
    private static ApiManager instance;
    private final RequestSpecification specification = new RequestSpecBuilder()
            .setBaseUri(BASE_PATH)
            .addQueryParam("key", "value")
            .setAuth(oauth2(FileManager.getInstance().getProperties("")))
            .build();
    private Response response;

    private ApiManager() {}

    /**
     * Create an instance to get access to class methods
     * @return Class instance
     */
    public static ApiManager getInstance() {
        if (instance == null) {
            instance = new ApiManager();
            RestAssured.filters(new MyRequestFilter());
        }
        return instance;
    }

    /**
     * Send GET request to API
     * @param target URL of request
     */
    public void getRequest(String target) {
        response = RestAssured
                .given()
                .spec(specification)
                .get(target);
    }

    /**
     * Send POST request to API
     * @param target URL of request
     * @param key The key of parameter
     * @param value The value of parameter to send
     */
    public void postRequest(String target, String key, String value) {
        response = RestAssured
                .given()
                .spec(specification)
                .queryParam(key, value)
                .post(target);
    }

    /**
     * Send POST request to API
     * @param target URL of request
     * @param param Parameters given to request in format key-value through Map<String, String>
     */
    public void postRequest(String target, Map<String, String> param) {
        response = RestAssured
                .given()
                .spec(specification)
                .params(param)
                .post(target);
    }

    /**
     * Send data to the API server to create new resource
     * @param target URL to post request
     * @param obj T object to be sent
     */
    public <T> void postRequest(String target, T obj) {
        response = RestAssured.given().header(CONTENT_TYPE, ContentType.JSON).body(obj).post(BASE_PATH + target);
    }

    /**
     * Send data to the API server to create new resource
     *
     * @param target URL to post request
     * @param obj    T object to be sent
     * @param header The value of header in the request
     * @param <T>    The type of object
     */
    public <T> void postRequest(String target, T obj, String header) {
        response = RestAssured.given()
                .header(CONTENT_TYPE, ContentType.JSON)
                .header(AUTHORISATION, header)
                .body(obj)
                .post(BASE_PATH + target);
    }

    /**
     * Get the status code of request:
     * 1xx - information;
     * 2xx - success;
     * 3xx - redirect;
     * 4xx - client error;
     * 5xx - server error
     * @return Status code
     */
    public int getStatusCode () {
        return response.statusCode();
    }

    /**
     * Get the Content type of the request
     * @return String representation of Content type
     */
    public String getContentType() {
        return response.contentType().split(";")[0];
    }

    /**
     * Get the request body from the API server
     * @return The String, that represents the request body
     */
    public String getBody() {
        return response.getBody().asString();
    }

    /**
     * Converting request from API server to T class object
     * @return T object
     */
    public <T> T convertRequestToPojo(Class<T> cl) {
        return response.as(cl);
    }

    /**
     * Get the list of some value from API request
     * @param target The key to get list of values
     * @param <T> The type of value
     * @return List of values by key "target"
     */
    public <T extends Comparable<T>> List<T> getList(String target) {
        return response.jsonPath().getList(target);
    }

    /**
     * Getting value from the request by the key
     * @param key The key to get value of
     * @param <T> The type of the key and return value
     * @return The value by the key
     */
    public <T> T getValue(T key) {
        return response.jsonPath().get(key.toString());
    }

    /**
     * Converting request from API server to list of T objects
     * @return List of T objects
     */
    public <T> List<T> getListOfObjects(Class<T> cl) {
        return response.jsonPath().getList("$", cl);
    }

    /**
     * Filter request command and add logging
     */
    static class MyRequestFilter implements Filter {
        private final Logger logger = Logger.getInstance();

        @Override
        public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
            Response response = ctx.next(requestSpec, responseSpec);
            if (requestSpec.getMethod().equals("GET")) {
                logger.info(String.format("Getting request from %1$s", requestSpec.getURI()));
            } else if (requestSpec.getMethod().equals("POST")) {
                logger.info(String.format("Post request to %1$s", requestSpec.getURI()));
            }
            Logger.getInstance().info(String.format("Status code of request is: %1$s", response.statusCode()));
            if (response.statusCode() >= 400) {
                logger.error(String.format("%1$s => %2$s", requestSpec.getURI(), response.getStatusLine()));
            }
            return response;
        }
    }
}
