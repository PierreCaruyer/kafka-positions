package org.daveo.service;

import com.avroGenerator.NamedPosition;
import com.avroGenerator.Position;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.daveo.serializer.PositionSerde;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class StreamService {

    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final Serde<GenericRecord> AVRO_SERDE = PositionSerde.GenericRecord();

    private static final String dictionnaryPath = "classpath:words";
    private final List<String> words;
    private final Random random;

    public StreamService() throws IOException {
        this.random = new Random();
        this.words = Collections.unmodifiableList(IOUtils.readLines(new BufferedInputStream(Files.newInputStream(Paths.get(dictionnaryPath))), "UTF-8"));
    }

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {
        KStream<String, GenericRecord> messageStream = streamsBuilder.stream("positions", Consumed.with(STRING_SERDE, AVRO_SERDE));
        KStream<String, GenericRecord> namedPositionKStream = messageStream.mapValues(position -> {
            int index = this.random.nextInt(words.size());
            String latitude = position.get("latitude").toString();
            String longitude = position.get("longitude").toString();
            return new NamedPosition(Double.parseDouble(latitude), Double.parseDouble(longitude), words.get(index));
        });

        namedPositionKStream.to("named-positions", Produced.with(STRING_SERDE, AVRO_SERDE));
    }
}
