package de.lebaasti.core.widgets.ingame.tablist;

import de.lebaasti.core.util.TextColorLine;
import de.lebaasti.core.widgets.ingame.tablist.util.TablistEventDispatcher;
import de.lebaasti.core.widgets.ingame.tablist.util.TablistHudWidget;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.resources.ResourceLocation;
import java.util.ArrayList;
import java.util.List;

public class PowerCrystalWidget extends TablistHudWidget {

  private final List<TextColorLine> textLines = new ArrayList<>();

  public PowerCrystalWidget(TablistEventDispatcher dispatcher) {
    super("power_crystal_widget", dispatcher);
    this.setIcon(Icon.texture(ResourceLocation.create("minecraft", "textures/item/diamond.png")));
  }

  @Override
  protected String germanNameText() {
    return "Machtkristall";
  }

  @Override
  protected String englishNameText() {
    return "Power Crystal";
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    textLines.clear();
    values.clear();
    for (int i = 0; i < 3; i++) {
      TextColorLine line = createLine(
          Component.text("Powercrystal " + i), Component.text(""),TextColorLine::new);
      line.setState(State.HIDDEN);
      textLines.add(line);
      values.add(null);
    }
  }

  private int powerCrystalNumber = 0;

  @Override
  protected void onWidgetKeyUpdate(NetworkPlayerInfo playerInfo) {
    List<NetworkPlayerInfo> playerList = dispatcher.playerList();
    int keyIndex = playerList.indexOf(playerInfo);
    int valueIndex = keyIndex + 1;
    if(valueIndex >= playerList.size()) return;

    TextColorLine powerCrystalLine = textLines.get(powerCrystalNumber);

    TextComponent key = (TextComponent) playerInfo.displayName();
    powerCrystalLine.updateAndFlushKey(clearText(key.getText()));
    powerCrystalLine.setState(State.VISIBLE);

    NetworkPlayerInfo value = playerList.get(valueIndex);
    values.set(powerCrystalNumber, value);
    onWidgetValueUpdate(value);

    powerCrystalNumber = (powerCrystalNumber + 1) % 3;
  }

  @Override
  protected void onWidgetValueUpdate(NetworkPlayerInfo playerInfo) {
    TextColorLine powerCrystalLine = textLines.get(values.indexOf(playerInfo));
    TextComponent value = (TextComponent) playerInfo.displayName();
    powerCrystalLine.updateAndFlush(value);
  }
}
