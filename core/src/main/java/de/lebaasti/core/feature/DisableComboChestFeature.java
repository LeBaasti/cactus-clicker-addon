package de.lebaasti.core.feature;

import de.lebaasti.core.CactusClickerAddon;
import de.lebaasti.core.event.BossbarRenderEvent;
import de.lebaasti.core.util.FontGlyphRegistry;
import net.labymod.api.event.Subscribe;

public class DisableComboChestFeature {

  private final CactusClickerAddon addon;

  public DisableComboChestFeature(CactusClickerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onBossbarRenderEvent(BossbarRenderEvent event) {
    if(addon.configuration().comboChestEnabled().get()) {
      return;
    }
    String componentText = event.getComponent().toString();
    if(FontGlyphRegistry.containsCharInGroup("combo_chest", componentText)) {
      event.setCancelled(true);
    }
  }

}
