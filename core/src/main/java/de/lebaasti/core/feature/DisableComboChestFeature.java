package de.lebaasti.core.feature;

import de.lebaasti.core.CCAddon;
import de.lebaasti.core.event.BossbarRenderEvent;
import de.lebaasti.core.util.FontGlyphRegistry;
import net.labymod.api.event.Subscribe;

public class DisableComboChestFeature {

  private final CCAddon addon;

  public DisableComboChestFeature(CCAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onBossbarRenderEvent(BossbarRenderEvent event) {
    if(!addon.server().isConnected() || addon.configuration().comboChestEnabled().get()) {
      return;
    }

    String componentText = event.getComponent().toString();
    if(FontGlyphRegistry.containsCharInGroup("combo_chest", componentText)) {
      event.setCancelled(true);
    }
  }

}
