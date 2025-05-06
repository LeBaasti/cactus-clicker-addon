package de.lebaasti.core;

import de.lebaasti.core.generated.DefaultReferenceStorage;
import de.lebaasti.core.listener.GameTickListener;
import de.lebaasti.core.widgets.ingame.MaterialsWidget;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.HudWidget;
import net.labymod.api.models.addon.annotation.AddonMain;
import org.jetbrains.annotations.NotNull;

@AddonMain
public class CactusClickerAddon extends LabyAddon<AddonConfiguration> {

  private HudWidgetCategory widgetCategory;

  private InventoryReader inventoryReader;

  @Override
  protected void enable() {
    this.registerSettingCategory();

    labyAPI().hudWidgetRegistry().categoryRegistry().register(this.widgetCategory = new HudWidgetCategory("cactus_clicker_category"));
    registerHudWidgetWithListener(new MaterialsWidget(this));

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

  public HudWidgetCategory widgetCategory() {
    return widgetCategory;
  }

  public void registerHudWidgetWithListener(HudWidget<?> widget) {
    labyAPI().hudWidgetRegistry().register(widget);
    registerListener(widget);
  }
}
