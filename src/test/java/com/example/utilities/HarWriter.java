package com.example.utilities;

import com.browserup.harreader.model.HarEntry;
import com.browserup.harreader.model.HarLog;
import com.browserup.harreader.model.HarPage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class HarWriter {

    public static void writeHAR(File harFile, HarLog log) throws IOException {
        String version = log.getVersion();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(harFile, JsonEncoding.UTF8);

        ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jsonGenerator.setCodec(objectMapper);
        jsonGenerator.useDefaultPrettyPrinter();

        // Begin File
        jsonGenerator.writeStartObject();

        // Begin log
        jsonGenerator.writeFieldName("log");

        // Begin log object
        jsonGenerator.writeStartObject();

        jsonGenerator.writeFieldName("version");
        jsonGenerator.writeObject(version);

        jsonGenerator.writeFieldName("creator");
        jsonGenerator.writeObject(log.getCreator());

        jsonGenerator.writeFieldName("pages");
        // pages field contains an array of objects
        jsonGenerator.writeStartArray();

        // create the objects in the pages array
        for (HarPage page : log.getPages()) {
            jsonGenerator.writeStartObject();

            jsonGenerator.writeFieldName("startedDateTime");

            jsonGenerator.writeObject(dateFormat.format(page.getStartedDateTime()));

            jsonGenerator.writeFieldName("id");
            jsonGenerator.writeObject(page.getId());

            jsonGenerator.writeFieldName("title");
            jsonGenerator.writeObject(page.getTitle());

            jsonGenerator.writeFieldName("pageTimings");
            jsonGenerator.writeObject(page.getPageTimings());

            jsonGenerator.writeEndObject();
        }

        // end of pages array
        jsonGenerator.writeEndArray();

        jsonGenerator.writeFieldName("entries");
        // Begin of entris array
        jsonGenerator.writeStartArray();
        // write object for each entry
        for (HarEntry entry : log.getEntries()) {
            jsonGenerator.writeStartObject();

            jsonGenerator.writeFieldName("startedDateTime");
            jsonGenerator.writeObject(dateFormat.format(entry.getStartedDateTime()));

            jsonGenerator.writeFieldName("time");
            jsonGenerator.writeObject(entry.getTime());

            jsonGenerator.writeFieldName("request");
            jsonGenerator.writeObject(entry.getRequest());

            jsonGenerator.writeFieldName("response");
            jsonGenerator.writeObject(entry.getResponse());

            jsonGenerator.writeFieldName("timings");
            // object timings has multiple fields
            jsonGenerator.writeObject(entry.getTimings());

            jsonGenerator.writeFieldName("serverIPAddress");
            jsonGenerator.writeObject(entry.getServerIPAddress());
            jsonGenerator.writeFieldName("connection");
            jsonGenerator.writeObject(entry.getConnection());
            jsonGenerator.writeFieldName("pageref");
            jsonGenerator.writeObject(entry.getPageref());

            // end entry object
            jsonGenerator.writeEndObject();

        }

        // end of entries Array
        jsonGenerator.writeEndArray();

        // end of log object
        jsonGenerator.writeEndObject();

        // close File
        jsonGenerator.close();
    }

}
