package de.lebaasti.core.feature.emoji;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import de.lebaasti.core.util.FontGlyphRegistry;

import java.util.*;

public class EmojiRegistry {

  private static final Map<String, String> nameToSymbol = new HashMap<>();
  private static final Map<String, String> symbolToName = new HashMap<>();

  private static final List<String> EMOJI_NAMES = List.of(
      "smiley", "smile", "grin", "laughing", "sweat_smile", "joy", "rofl", "blush", "innocent", "slight_smile",
      "upside_down", "wink", "relieved", "heart_eyes", "kissing_closed_eyes", "yum", "tongue_close_eyes", "money_mouth", "hugging", "nerd",
      "sunglasses", "freezing_clown", "smirk", "unamused", "persevere", "pensive", "confused", "slight_frown", "frowning2", "cold_face",
      "hot_face", "smiling_imp", "weary", "triumph", "angry", "rage", "neutral_face", "expressionless", "hushed", "frowning",
      "anguished", "open_mouth", "astonished", "flushed", "scream", "fearful", "cold_sweat", "disappointed_relieved", "sweat", "sob",
      "snod", "crying", "sweating_pray", "sleeping", "rolling_eyes", "thinking", "lying", "grimacing", "nauseated", "vomiting",
      "sneezing", "mask", "thermometer", "bandage", "raised_eyebrow", "star_struck", "exploding", "monocle", "shushing", "zany",
      "worried", "hand_over_mouth"
  );

  public static void clear() {
    nameToSymbol.clear();
    symbolToName.clear();
  }

  public static void register(String name, String symbol) {
    nameToSymbol.put(name.toLowerCase(), symbol);
    symbolToName.put(symbol, name.toLowerCase());
  }

  public static String getSymbol(String name) {
    return nameToSymbol.get(name.toLowerCase());
  }

  public static String getName(String symbol) {
    return symbolToName.get(symbol);
  }

  public static boolean containsEmoji(String name) {
    return nameToSymbol.containsKey(name.toLowerCase());
  }

  public static boolean containsSymbol(String symbol) {
    return symbolToName.containsKey(symbol);
  }

  public static Set<String> getAllNames() {
    return Collections.unmodifiableSet(nameToSymbol.keySet());
  }

  public static Set<String> getAllSymbols() {
    return Collections.unmodifiableSet(symbolToName.keySet());
  }

  public static void reload() {
    LinkedHashSet<String> emojiGlyphs = FontGlyphRegistry.getCharsForGroupName("chat", "emojies");

    if (emojiGlyphs.size() < EMOJI_NAMES.size()) {
      throw new IllegalStateException("Mismatch between emoji names (" + EMOJI_NAMES.size()
          + ") and glyphs (" + emojiGlyphs.size() + ")");
    }

    clear();

    Iterator<String> glyphIterator = emojiGlyphs.iterator();
    for (String name : EMOJI_NAMES) {
      String glyph = glyphIterator.next();
      register(name, glyph);
    }
  }
}