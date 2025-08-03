package de.lebaasti.core.feature;

import de.lebaasti.core.CactusClickerAddon;
import de.lebaasti.core.event.TitleReceiveEvent;
import de.lebaasti.core.util.FontGlyphRegistry;
import net.labymod.api.client.chat.Title;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.event.Subscribe;

public class DisableDropAnimationFeature {

  private final CactusClickerAddon addon;

  public DisableDropAnimationFeature(CactusClickerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onTitleReceive(TitleReceiveEvent event) {
    if(addon.configuration().legendaryAndEpicAnimationEnabled().get()) {
      return;
    }
    Title title = event.getTitle();
    if(title != null && title.getTitle() != null) {
      String titleText = ((TextComponent) title.getTitle()).getText();
      //Legendary and epic animation symbols
      boolean isAnimation = FontGlyphRegistry.containsCharInGroup("cactusclicker_drop_animation", titleText);
      if(isAnimation) event.setCancelled(true);
    }
  }

}
