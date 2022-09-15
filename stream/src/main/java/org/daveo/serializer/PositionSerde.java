package org.daveo.serializer;

import com.avroGenerator.NamedPosition;
import com.avroGenerator.Position;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class PositionSerde {
    private PositionSerde() {}

    public static Serde<Position> Position() {
        JsonSerializer<Position> serializer = new JsonSerializer<>();
        JsonDeserializer<Position> deserializer = new JsonDeserializer<>(Position.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<GenericRecord> GenericRecord() {
        SchemaRegistryClient schemaRegistry = new CachedSchemaRegistryClient("http://127.0.0.1:8081", 30);
        return new GenericAvroSerde(schemaRegistry);
    }

    public static Serde<NamedPosition> NamedPosition() {
        JsonSerializer<NamedPosition> serializer = new JsonSerializer<>();
        JsonDeserializer<NamedPosition> deserializer = new JsonDeserializer<>(NamedPosition.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
}
