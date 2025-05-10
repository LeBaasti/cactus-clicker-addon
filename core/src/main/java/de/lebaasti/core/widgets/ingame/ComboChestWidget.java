package de.lebaasti.core.widgets.ingame;

import de.lebaasti.core.CactusClickerAddon;
import de.lebaasti.core.event.BossbarRenderEvent;
import de.lebaasti.core.util.CactusClickerPlayer;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.world.DimensionChangeEvent;

public class ComboChestWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TextLine textLine;
  private CactusClickerAddon addon;

  public ComboChestWidget(CactusClickerAddon addon) {
    super("combo_chest_widget");
    this.addon = addon;
    this.bindCategory(addon.widgetCategory());
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    textLine = createLine(Component.translatable("cactusclicker.hudWidget." + id + ".name"), "");
    textLine.setState(State.HIDDEN);
  }

  @Subscribe
  public void onBossbarRender(BossbarRenderEvent event) {
    String componentText = event.getComponent().toString();
    if(componentText.matches(".*[㞕-㞞].*")) {
      try {
        textLine.updateAndFlush(decodeNumber(componentText));
      } catch (NumberFormatException exception) {
      }
    }
  }

  @Subscribe
  public void onDimensionChange(DimensionChangeEvent event) {
    if(CactusClickerPlayer.isInAincraft(event.toDimension())) {
      textLine.setState(State.VISIBLE);
    } else {
      textLine.setState(State.HIDDEN);
    }
  }

  private int decodeNumber(String input) throws NumberFormatException {
    StringBuilder result = new StringBuilder();
    for (char c : input.toCharArray()) {
      if (c >= '㞕' && c <= '㞞') {
        result.append(c - '㞕');
      }
    }
    return Integer.parseInt(result.toString());
  }
}
