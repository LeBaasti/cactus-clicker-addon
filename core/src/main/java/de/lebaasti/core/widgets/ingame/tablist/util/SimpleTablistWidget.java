package de.lebaasti.core.widgets.ingame.tablist.util;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.client.network.NetworkPlayerInfo;
import java.util.List;

public abstract class SimpleTablistWidget extends TablistHudWidget {

  private TextLine textLine;

  public SimpleTablistWidget(String id, TablistEventDispatcher dispatcher) {
    super(id, dispatcher);
  }

  @Override
  protected abstract String germanNameText();

  @Override
  protected abstract String englishNameText();

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    textLine = createLine(Component.translatable("cactusclicker.hudWidget." + id + ".name"), "");
    textLine.setState(State.HIDDEN);
    values.clear();
  }

  @Override
  protected void onWidgetKeyUpdate(NetworkPlayerInfo playerInfo) {
    List<NetworkPlayerInfo> playerList = dispatcher.playerList();
    int keyIndex = playerList.indexOf(playerInfo);
    int valueIndex = keyIndex + 1;
    if(valueIndex >= playerList.size()) return;

    NetworkPlayerInfo value = playerList.get(valueIndex);
    values.clear();
    values.add(value);
    textLine.setState(State.VISIBLE);
  }

  @Override
  protected void onWidgetValueUpdate(NetworkPlayerInfo playerInfo) {
    TextComponent value = (TextComponent) playerInfo.displayName();
    textLine.updateAndFlush(value);
  }
}
