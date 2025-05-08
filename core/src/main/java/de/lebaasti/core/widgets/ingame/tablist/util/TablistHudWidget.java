package de.lebaasti.core.widgets.ingame.tablist.util;

import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.network.NetworkPlayerInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class TablistHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  protected final TablistEventDispatcher dispatcher;

  protected final List<NetworkPlayerInfo> values = new ArrayList<>();

  public TablistHudWidget(String id, TablistEventDispatcher dispatcher) {
    super(id);
    this.bindCategory(dispatcher.addon().widgetCategory());
    this.dispatcher = dispatcher;
    dispatcher.register(event -> {
      NetworkPlayerInfo playerInfo = event.playerInfo();
      String text = ((TextComponent) playerInfo.displayName()).getText();
      if(text.contains(germanNameText()) || text.contains(englishNameText())) {
        onWidgetKeyUpdate(playerInfo);
      } else if(values.contains(playerInfo)) {
        onWidgetValueUpdate(playerInfo);
      }
    });
  }

  protected String translationKey(String key) {
    return String.format(
        Locale.ROOT, "%s.hudWidget.%s.%s",
        this.namespace,
        id,
        key
    );
  }

  public String replaceColorCodes(String text) {
    return text.replaceAll("§[0-9A-Fa-f]", "");
  }

  public String clearText(String text) {
    return replaceColorCodes(text).replaceAll("[^a-zA-Z\\s]", "");
  }

  protected abstract String germanNameText();
  protected abstract String englishNameText();
  protected abstract void onWidgetKeyUpdate(NetworkPlayerInfo playerInfo);
  protected abstract void onWidgetValueUpdate(NetworkPlayerInfo playerInfo);
}
