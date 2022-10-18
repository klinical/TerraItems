package net.terramc.terraitems.shared;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Type;

public class PotionEffectTypeDeserializer implements JsonDeserializer<PotionEffectType> {
    @Override
    public PotionEffectType deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String stringValue = jsonElement.getAsJsonObject().get("key").getAsJsonObject().get("key").getAsString();

        return PotionEffectType.getByName(stringValue.toUpperCase());
    }
}
