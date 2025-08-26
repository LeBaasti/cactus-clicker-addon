package de.lebaasti.core.feature;

import de.lebaasti.core.CCAddon;
import de.lebaasti.core.event.InventoryUpdateEvent;
import de.lebaasti.core.util.EssenceRegistry;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.event.Subscribe;
import java.util.ArrayList;
import java.util.List;

public class EssenzeWaveFeature {

  private final CCAddon addon;

  public EssenzeWaveFeature(CCAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onInventoryUpdate(InventoryUpdateEvent event) {
    if(!event.getTitle().contains("Machtkristalle Verbessern")) return;
    event.getItems().forEach(item -> {
      if(item == null) return;

      List<TextComponent> lore = new ArrayList<>();
      for (TextComponent line : item.getLore()) {
        String fullText = getFullText(line);

        if(!fullText.contains("[Essenz]")) {
          lore.add(line);
          continue;
        }

        // "- Spinnen [Essenz] Tier 5: (339400/64000)"
        String mobName = null;
        String tier = null;

        int dashIndex = fullText.indexOf("-");
        int bracketIndex = fullText.indexOf("[");
        if (dashIndex != -1 && bracketIndex != -1) {
          mobName = fullText.substring(dashIndex + 1, bracketIndex).trim();
        }

        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("Tier (\\d+)").matcher(fullText);
        if (matcher.find()) {
          tier = matcher.group(1);
        }

        lore.add(line.append(Component.text(" Wave: " + EssenceRegistry.getValue(mobName, tier))));
      }

      item.setLore(lore);
    });
  }

  private static String getFullText(TextComponent component) {
    StringBuilder sb = new StringBuilder(component.getText());
    component.getChildren().forEach(child -> sb.append(getFullText((TextComponent) child)));
    return sb.toString();
  }
}
