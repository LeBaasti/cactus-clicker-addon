package de.lebaasti.core;

import de.lebaasti.core.feature.DisableComboChestFeature;
import de.lebaasti.core.feature.DisableDropAnimationFeature;
import de.lebaasti.core.generated.DefaultReferenceStorage;
import de.lebaasti.core.listener.GameTickListener;
import de.lebaasti.core.widgets.ingame.ComboChestWidget;
import de.lebaasti.core.widgets.ingame.MaterialsWidget;
import de.lebaasti.core.widgets.ingame.tablist.AnvilWidet;
import de.lebaasti.core.widgets.ingame.tablist.PowerCrystalWidget;
import de.lebaasti.core.widgets.ingame.tablist.util.TablistEventDispatcher;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.HudWidget;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class CactusClickerAddon extends LabyAddon<AddonConfiguration> {

  private HudWidgetCategory widgetCategory;

  private InventoryReader inventoryReader;

  @Override
  protected void enable() {
    this.registerSettingCategory();

    labyAPI().hudWidgetRegistry().categoryRegistry().register(this.widgetCategory = new HudWidgetCategory("cactus_clicker_category"));
    registerHudWidgetWithListener(new MaterialsWidget(this));
    registerHudWidgetWithListener(new ComboChestWidget(this));

    inventoryReader = ((DefaultReferenceStorage) this.referenceStorageAccessor()).inventoryReader();

    this.registerListener(new GameTickListener(this));
    this.registerListener(new DisableDropAnimationFeature(this));
    this.registerListener(new DisableComboChestFeature(this));

    TablistEventDispatcher dispatcher = new TablistEventDispatcher(this);
    this.registerListener(dispatcher);

    labyAPI().hudWidgetRegistry().register(new PowerCrystalWidget(dispatcher));
    labyAPI().hudWidgetRegistry().register(new AnvilWidet(dispatcher));
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
