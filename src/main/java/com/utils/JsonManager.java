package com.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Class for working with JSON files and Objects
 */
public class JsonManager {
    private static final Gson g = new Gson();

    private JsonManager() {}

    /**
     * Read JSON file and convert it to JsonElement
     * @param file File to be read
     * @return JsonElement representation of JSON file
     */
    public static JsonElement readJSON(String file) {
        try(FileReader reader = new FileReader(FileManager.getInstance().getResourcePath(file), Charset.forName("windows-1251"))) {
            return JsonParser.parseReader(reader);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Deserialize JSON file to Object
     * @param file JSON file to be deserialized
     * @param type The specific genericized type of src
     * @param <T> The desired type of Object to be obtained
     * @return Object of type T
     */
    public static <T> T convertToObject(String file, Type type) {
        return g.fromJson(readJSON(file), type);
    }

    /**
     * Deserialize JSON file to Object
     * @param file JSON file to be deserialized
     * @param tClass The class of T
     * @param <T> The desired type of Object to be obtained
     * @return Object of type T
     */
    public static <T> T convertToObject(String file, Class<T> tClass) {
        return g.fromJson(readJSON(file), tClass);
    }

    /**
     * Partial deserialization of JSON file to Object by the given key
     * @param file JSON file to be deserialized
     * @param tClass The class of T
     * @param key The key to get the partial deserialization of JSON file
     * @param <T> The desired type of Object to be obtained
     * @return Object of type T
     */
    public static <T> T partialConvertToObject(String file, Class<T> tClass, String key) {
        JsonElement element = Objects.requireNonNull(readJSON(file)).getAsJsonObject().get(key);
        return g.fromJson(element, tClass);
    }
}
