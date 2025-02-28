package com.pentryyy.task_manager_api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pentryyy.task_manager_api.model.Task;

import java.io.IOException;

public class TaskIdSerializer extends JsonSerializer<Task> {
    @Override
    public void serialize(Task task, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", task.getId());
        gen.writeEndObject();
    }
}

