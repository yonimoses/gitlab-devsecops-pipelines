package com.bnhp.gitlabci.devsecops.pipeline;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import java.io.*;
import java.util.*;

public class JsonUtils {

    private final static JsonFactory jsonFactory = new JsonFactory();

    public static ObjectMapper objectMapper = new ObjectMapper();

    private final static ObjectMapper simpleMapper = new ObjectMapper(jsonFactory);

    private final static ObjectMapper prettyMapper = new ObjectMapper(jsonFactory);

    private static ObjectMapper objectMapperMessagePack = new ObjectMapper();

    static {
        simpleMapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapperMessagePack.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        prettyMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        prettyMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        prettyMapper.findAndRegisterModules();
        objectMapperMessagePack.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private JsonUtils() {
    }

//    private final static JsonFactory jsonFactory = new JsonFactory();
//
//    public final static ObjectMapper objectMapper = new ObjectMapper( jsonFactory );
//
//    private final static ObjectMapper prettyMapper = new ObjectMapper( jsonFactory );
//
//    static
//    {
//
//        simpleMapper.configure( JsonGenerator.Feature.QUOTE_FIELD_NAMES, false );
//
//        objectMapper.findAndRegisterModules();
//        objectMapper.configure( JsonGenerator.Feature.QUOTE_FIELD_NAMES, true );
//        objectMapper.configure( SerializationFeature.FAIL_ON_EMPTY_BEANS, false );
//        objectMapper.getSerializationConfig().with( SerializationFeature.INDENT_OUTPUT );
//        objectMapper.setSerializationInclusion( JsonInclude.Include.NON_NULL );
//        objectMapper.enableDefaultTyping();
//
//        objectMapper.enable( DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY );
//
//        prettyMapper.configure( JsonParser.Feature.ALLOW_COMMENTS, true );
//        prettyMapper.configure( SerializationFeature.INDENT_OUTPUT, true );
//        prettyMapper.findAndRegisterModules();
//
//    }

    public static <T> T convert(final Map<String, Object> propertyMap, final Class<T> typeRef) {
        Assert.notNull(propertyMap, "propertyMap is required");
        Assert.notNull(typeRef, "typeRef is required");
        return objectMapper.convertValue(propertyMap, typeRef);
    }

    @SuppressWarnings("unchecked")
    public static <T> T convert(final Map<String, Object> propertyMap, final TypeReference<T> typeRef) {
        Assert.notNull(propertyMap, "propertyMap is required");
        Assert.notNull(typeRef, "typeRef is required");
        return (T) objectMapper.convertValue(propertyMap, typeRef);
    }

    public static <T> T parse(final InputStream is, final Class<T> clazz) throws IOException {
        return objectMapper.readValue(is, clazz);
    }

    public static <T> TypeReference<ArrayList<T>> getArrayType(final Class<T> clazz) throws IOException {
        return new TypeReference<ArrayList<T>>() {

        };
    }

    public static <T> TypeReference<T> getType(final Class<T> clazz) throws IOException {
        return new TypeReference<T>() {

        };
    }

    public static <T> T parse(final InputStream is, final TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(is, typeReference);
    }

    public static <T> T parse(final JsonParser is, final Class<T> clazz) throws IOException {
        return objectMapper.readValue(is, clazz);
    }

    public static <T> T parse(final String json, final Class<T> clazz) {

        Assert.hasText(json, "json is required");
        Assert.notNull(clazz, "clazz is required");

        try {
            return parse(new ByteArrayInputStream(json.getBytes("UTF-8")), clazz);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseSimple(final InputStream is, final Class<T> clazz) {

        try {
            return simpleMapper.readValue(is, clazz);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseSimple(final String json, final Class<T> clazz) {

        Assert.hasText(json, "json is required");
        Assert.notNull(clazz, "clazz is required");

        try {
            return simpleMapper.readValue(new ByteArrayInputStream(json.getBytes("UTF-8")), clazz);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(final String json, final TypeReference<T> typeReference) {
        Assert.hasText(json, "json is required");
        Assert.notNull(typeReference, "typeReference is required");
        try {
            return parse(new ByteArrayInputStream(json.getBytes("UTF-8")), typeReference);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String serialize(final Object obj) {
        Assert.notNull(obj, "obj is required");
        final ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
        try {
            objectMapper.writeValue(bos, obj);
            return bos.toString("UTF-8");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> toMap(final Object obj) {
        return JsonUtils.parse(serialize(obj), Map.class);
    }

    public static String serializeSimple(final Object obj) {
        Assert.notNull(obj, "obj is required");
        final ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
        try {
            simpleMapper.writeValue(bos, obj);
            return bos.toString("UTF-8");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static String serializePretty(final Object obj) {
        Assert.notNull(obj, "obj is required");
        final ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
        try {
            prettyMapper.writeValue(bos, obj);
            return bos.toString("UTF-8");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void serialize(final OutputStream os, final Object obj) throws IOException {
        objectMapper.writeValue(os, obj);
    }

    public static String getJsonStringFromObject(Object obj) throws JsonGenerationException,
            JsonMappingException, IOException {
        StringWriter writer = new StringWriter();
        objectMapper.writeValue(writer, obj);
        return writer.toString();
    }

    public static <T> T getObjectFromJsonString(Class<T> clazz, String requestJson) throws IOException {
        T t = null;
        InputStream is = new ByteArrayInputStream(requestJson.getBytes("UTF-8"));
        t = (T) objectMapper.readValue(is, clazz);
        return t;
    }

    public static <T> Object getObjectFromJsonString(String json, TypeReference<T> typeRef)
            throws IOException {
        InputStream is = new ByteArrayInputStream(json.getBytes("UTF-8"));
        return objectMapper.readValue(is, typeRef);
    }


    public static JsonNode convertObjectToJsonNode(Object object) {
        JsonNode jsonAdObject = objectMapper.valueToTree(object);
        return jsonAdObject;
    }

    public static <T> Object getObjectFromFile(String fileName, Class<T> className)
            throws JsonParseException, JsonMappingException, IOException {
        T obj = null;
        File file = new File(fileName);
        obj = objectMapper.readValue(file, className);
        return obj;
    }

    public static JsonNode getJsonNodeFromQueryParamsMap(Map<String, String[]> queryParams) {
        if (queryParams == null) {
            return null;
        }
        Map<String, String> queryParamsMap = new HashMap<String, String>();

        for (Map.Entry<String, String[]> entry : queryParams.entrySet()) {

            queryParamsMap.put(entry.getKey(), entry.getValue()[0]);

        }
        JsonNode jsonObject = objectMapper.valueToTree(queryParamsMap);

        return jsonObject;

    }

    public static JsonNode getFirstParamNodeFromQueryParams(Map<String, String[]> queryParams) {
        if (queryParams == null) {
            return null;
        }
        ObjectNode node = objectMapper.createObjectNode();
        for (String key : queryParams.keySet()) {
            node.put(key, queryParams.get(key)[0]);
        }
        return node;
    }

    public static <T> T getObjectFromJsonNode(JsonNode node, Class<T> className)
            throws JsonProcessingException {
        return objectMapper.treeToValue(node, className);
    }

    public static JsonNode getJsonNodeFromObject(Object object) {
        return objectMapper.valueToTree(object);
    }

    public static JsonNode getJsonNodeFromJsonString(String jsonString) throws JsonParseException,
            JsonMappingException, IOException {
        JsonNode retval = null;
        if (!StringUtils.isEmpty(jsonString)) {
            retval = objectMapper.readValue(jsonString, JsonNode.class);
        }
        return retval;
    }

    public static ArrayNode createArrayNode() {
        return objectMapper.createArrayNode();
    }

    public static ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }

    public static byte[] messagePackSerialize(Object obj) throws JsonProcessingException {
        return objectMapperMessagePack.writeValueAsBytes(obj);
    }

    public static <T> Object messagePackDeserialize(byte[] b, Class<T> className)
            throws JsonParseException, JsonMappingException, IOException {
        return objectMapperMessagePack.readValue(b, className);
    }

    public static List<String> getSortedArrayNode(ArrayNode arrayNode) throws IOException {
        List<String> list = new ArrayList<String>();
        Iterator<JsonNode> it = arrayNode.iterator();
        while (it.hasNext()) {
            list.add(it.next().asText());
        }
        Collections.sort(list);
        return list;
    }

}