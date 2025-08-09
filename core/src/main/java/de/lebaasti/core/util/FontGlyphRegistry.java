package de.lebaasti.core.util;

import com.google.gson.*;
import net.labymod.api.client.resources.ResourceLocation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FontGlyphRegistry {

  private static final Map<String, Map<String, LinkedHashSet<String>>> groups = new HashMap<>();

  private static final Set<String> LOADED_GROUPS = Set.of(
      "cactusclicker_drop_animation",
      "chat",
      "combo_chest",
      "cactusclicker_classes_icons"
  );

  public static void reload() {
    groups.clear();

    ResourceLocation fontJson = ResourceLocation.create("minecraft", "font/default.json");

    try (Reader reader = new InputStreamReader(fontJson.openStream(), StandardCharsets.UTF_8)) {

      JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
      JsonArray providers = root.getAsJsonArray("providers");

      for (JsonElement element : providers) {
        JsonObject provider = element.getAsJsonObject();

        if (!provider.has("group") || !provider.has("chars") || !provider.has("name")) continue;

        String group = provider.get("group").getAsString();
        String name = provider.get("name").getAsString();
        JsonArray charsArray = provider.getAsJsonArray("chars");

        if (!LOADED_GROUPS.contains(group)) continue;

        // Get group map or create it
        Map<String, LinkedHashSet<String>> groupEntries = groups.computeIfAbsent(group, g -> new HashMap<>());
        LinkedHashSet<String> chars = groupEntries.computeIfAbsent(name, n -> new LinkedHashSet<>());

        for (JsonElement charsElement : charsArray) {
          String line = charsElement.getAsString();
          line.codePoints()
              .mapToObj(cp -> new String(Character.toChars(cp)))
              .forEach(chars::add);
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Get full group map by name (e.g. "combo_chest")
  public static Map<String, LinkedHashSet<String>> getGroup(String group) {
    return groups.getOrDefault(group, Collections.emptyMap());
  }

  // Get all chars in a group
  public static Collection<LinkedHashSet<String>> getAllCharsForGroup(String group) {
    return getGroup(group).values();
  }

  // Get all chars in a group, optionally excluding certain subgroups
  public static Collection<LinkedHashSet<String>> getAllCharsForGroupExcluding(String group, String... excludedSubgroups) {
    Map<String, LinkedHashSet<String>> groupMap = getGroup(group);

    if (excludedSubgroups == null || excludedSubgroups.length == 0) {
      return groupMap.values();
    }

    Set<String> excludedSet = Set.of(excludedSubgroups);
    return groupMap.entrySet().stream()
        .filter(entry -> !excludedSet.contains(entry.getKey()))
        .map(Map.Entry::getValue)
        .toList();
  }


  // Check if any char from any subgroup in group exists in text
  public static boolean containsCharInGroup(String group, String text) {
    return getAllCharsForGroup(group).stream()
        .flatMap(Set::stream)
        .anyMatch(text::contains);
  }

  // Get chars from a specific subgroup (e.g. "numbers")
  public static LinkedHashSet<String> getCharsForGroupName(String group, String name) {
    Set<String> result = getGroup(group).get(name);
    if (result instanceof LinkedHashSet<String> linked) {
      return linked;
    }
    return new LinkedHashSet<>(Collections.emptySet());
  }

  // Check if text contains any char from a specific group+name
  public static boolean containsCharInGroupName(String group, String name, String text) {
    return getCharsForGroupName(group, name).stream()
        .anyMatch(text::contains);
  }
}
