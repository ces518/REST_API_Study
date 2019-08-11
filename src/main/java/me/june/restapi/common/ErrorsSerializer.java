package me.june.restapi.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2019-08-11
 * Time: 22:19
 **/
@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {

    @Override
    public void serialize(Errors errors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // 배열로 존재하기때문에 startArray, endArray를 사용
        jsonGenerator.writeStartArray();

        // Field Errors
        errors.getFieldErrors().stream().forEach(e -> {
                try {
                    jsonGenerator.writeStartObject();
                    jsonGenerator.writeStringField("field", e.getField());
                    jsonGenerator.writeStringField("objectName", e.getObjectName());
                    jsonGenerator.writeStringField("code", e.getCode());
                    jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());
                    Object rejectedValue = e.getRejectedValue();
                    if (rejectedValue != null) {
                        jsonGenerator.writeStringField("rejectedValue", rejectedValue.toString());
                    }
                    jsonGenerator.writeEndObject();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        });

        // GlobalErrors
        errors.getGlobalErrors().stream().forEach(e -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("objectName", e.getObjectName());
                jsonGenerator.writeStringField("code", e.getCode());
                jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());
                jsonGenerator.writeEndObject();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        jsonGenerator.writeEndArray();
    }
}
