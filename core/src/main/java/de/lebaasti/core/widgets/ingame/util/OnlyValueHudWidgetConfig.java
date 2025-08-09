package de.lebaasti.core.widgets.ingame.util;

import net.labymod.api.client.gui.hud.hudwidget.text.Formatting;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;

public class OnlyValueHudWidgetConfig extends TextHudWidgetConfig {

  public OnlyValueHudWidgetConfig() {
    this.useGlobal().set(false);
    this.formatting().set(Formatting.VALUE_ONLY);
  }

  @Override
  public void reset() {
    super.reset();
    this.useGlobal().set(false);
    this.formatting().set(Formatting.VALUE_ONLY);
  }
}
