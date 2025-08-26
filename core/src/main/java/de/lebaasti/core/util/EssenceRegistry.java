package de.lebaasti.core.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.labymod.api.client.resources.ResourceLocation;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class EssenceRegistry {

  private static final Gson GSON = new Gson();
  private static Map<String, Map<String, Integer>> data;

  private static void load() {
    try {
      ResourceLocation resourceLocation = ResourceLocation.create("ccaddon", "data/essence.json");
      try (InputStreamReader reader = new InputStreamReader(resourceLocation.openStream(), StandardCharsets.UTF_8)) {
        data = GSON.fromJson(reader, new TypeToken<Map<String, Map<String, Integer>>>(){}.getType());
      }
    } catch (Exception e) {
      throw new RuntimeException("Konnte essence.json nicht laden!", e);
    }
  }

  private static void ensureLoaded() {
    if (data == null) {
      load();
    }
  }

  public static int getValue(String mob, String tier) {
    ensureLoaded();
    return data.getOrDefault(mob, Map.of()).getOrDefault(tier, -1);
  }

  public static Map<String, Map<String, Integer>> getAll() {
    ensureLoaded();
    return data;
  }
}
