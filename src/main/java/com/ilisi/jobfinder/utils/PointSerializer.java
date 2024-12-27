package com.ilisi.jobfinder.utils;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.locationtech.jts.geom.Point;

import java.io.IOException;

public class PointSerializer extends StdSerializer<Point> {

    public PointSerializer() {
        super(Point.class);
    }

    @Override
    public void serialize(Point point, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("type");
        jsonGenerator.writeString("Point");
        jsonGenerator.writeFieldName("coordinates");
        jsonGenerator.writeStartArray();
        jsonGenerator.writeNumber(point.getX());
        jsonGenerator.writeNumber(point.getY());
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}