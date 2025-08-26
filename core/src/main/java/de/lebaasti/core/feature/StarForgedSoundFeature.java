package de.lebaasti.core.feature;

import de.lebaasti.core.CCAddon;
import de.lebaasti.core.event.InventoryUpdateEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;

public class StarForgedSoundFeature {

  private final CCAddon addon;

  public StarForgedSoundFeature(CCAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onInventoryUpdate(InventoryUpdateEvent event) {
    if(!addon.server().isConnected() || !addon.configuration().starForgedSoundEnabled().get()) {
      return;
    }
    if (!event.getTitle().contains("Disassemble") && !event.getTitle().contains("Zerlegen")) {
      return;
    }

    boolean containsStarForgedItem = event.getItems().stream().anyMatch(
        item -> item.getName().contains("Sternengeschmiedet") || item.getName().contains("Starforged"));

    if(containsStarForgedItem) {
      Laby.labyAPI().minecraft().sounds().playSound(
          ResourceLocation.create("ccaddon", "misc.starforged"),
          1000f,
          1f
      );
    }

  }
}
