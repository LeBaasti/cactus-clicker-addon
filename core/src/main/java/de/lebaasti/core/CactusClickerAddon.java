package de.lebaasti.core;

import de.lebaasti.core.generated.DefaultReferenceStorage;
import de.lebaasti.core.listener.GameTickListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class CactusClickerAddon extends LabyAddon<AddonConfiguration> {

  private InventoryReader inventoryReader;

  @Override
  protected void enable() {
    this.registerSettingCategory();

    inventoryReader = ((DefaultReferenceStorage) this.referenceStorageAccessor()).inventoryReader();

    this.registerListener(new GameTickListener(this));
  }

  @Override
  protected Class<AddonConfiguration> configurationClass() {
    return AddonConfiguration.class;
  }

  public InventoryReader inventoryReader() {
    return inventoryReader;
  }
}
