package br.com.android.check.library;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import br.com.android.check.domain.Vendedor;

/**
 * Created by marcelo on 02/02/2017.
 */
public class VendedorDeserializer implements JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement vendedor = json.getAsJsonObject();

        return new Gson().fromJson(vendedor, Vendedor.class);
    }
}