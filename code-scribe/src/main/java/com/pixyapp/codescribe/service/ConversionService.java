package com.pixyapp.codescribe.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ConversionService {

    public JsonNode convertJsonId(JsonNode inputJsonNode) {
        if (inputJsonNode.isArray()) {
            // If the input is an array, process each element
            ArrayNode arrayNode = (ArrayNode) inputJsonNode;
            for (JsonNode object : arrayNode) {
                processJsonObject(object);
            }
        } else {
            // If the input is a single object, process it
            processJsonObject(inputJsonNode);
        }
        // Return the modified JSON node
        return inputJsonNode;
    }

    private void processJsonObject(JsonNode object) {
        JsonNode idObject = object.get("_id");
        JsonNode createdOnObject = object.get("createdOn");
        JsonNode updatedOnObject = object.get("updatedOn");

        if (idObject != null && idObject.has("$oid")) {
            String id = idObject.get("$oid").asText();
            ((ObjectNode) object).remove("_id");
            ((ObjectNode) object).put("_id", id);
        }

        if (createdOnObject != null && createdOnObject.has("$numberLong")) {
            long createdOn = Long.parseLong(createdOnObject.get("$numberLong").asText());
            ((ObjectNode) object).remove("createdOn");
            ((ObjectNode) object).put("createdOn", createdOn);
        }

        if (updatedOnObject != null && updatedOnObject.has("$numberLong")) {
            long updatedOn = Long.parseLong(updatedOnObject.get("$numberLong").asText());
            ((ObjectNode) object).remove("updatedOn");
            ((ObjectNode) object).put("updatedOn", updatedOn);
        }
    }

}
