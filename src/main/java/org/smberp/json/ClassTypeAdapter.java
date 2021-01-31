/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smberp.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 *
 * @author Burhani152
 */
public class ClassTypeAdapter extends TypeAdapter<Class<?>> {

    @Override
    public void write(JsonWriter jsonWriter, Class<?> clazz) throws IOException {
        if (clazz == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(clazz.getName());
    }

    @Override
    public Class<?> read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        Class<?> clazz = null;
        try {
            clazz = Class.forName(jsonReader.nextString());
        } catch (ClassNotFoundException exception) {
            throw new IOException(exception);
        }
        return clazz;

    }
}
