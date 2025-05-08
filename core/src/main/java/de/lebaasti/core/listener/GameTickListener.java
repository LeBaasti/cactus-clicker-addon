package de.lebaasti.core.listener;

import de.lebaasti.core.InventoryReader;
import net.labymod.api.Laby;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import de.lebaasti.core.CactusClickerAddon;

public class GameTickListener {

  private final CactusClickerAddon addon;
  private final InventoryReader inventoryReader;

  public GameTickListener(CactusClickerAddon addon) {
    this.addon = addon;
    this.inventoryReader = addon.inventoryReader();
  }

  @Subscribe
  public void onGameTick(GameTickEvent event) {
    if (event.phase() != Phase.PRE) {
      return;
    }
    if(addon.configuration().starForgedSoundEnabled().get() && inventoryReader.wasDisassembleMenuOpenOrUpdated()) {
      if(inventoryReader.openInventoryContainsStarForged()) {
        Laby.labyAPI().minecraft().sounds().playSound(
            ResourceLocation.create("cactusclicker", "misc.starforged"),
            1000f,
            1f
        );
      }
    }
  }
}
