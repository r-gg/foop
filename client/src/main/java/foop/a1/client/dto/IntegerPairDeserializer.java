package foop.a1.client.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParser;
import org.springframework.data.util.Pair;

import java.io.IOException;


public class IntegerPairDeserializer extends JsonDeserializer<Pair<Integer, Integer>> {

    static private ObjectMapper objectMapper = null;

    @Override
    public Pair<Integer, Integer> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        JsonNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        Integer first = objectMapper.treeToValue(treeNode.get("first"), Integer.class);
        Integer second = objectMapper.treeToValue(treeNode.get("second"), Integer.class);
        return Pair.of(first, second);
    }

}