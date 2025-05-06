package de.lebaasti.core.util;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.hud.hudwidget.text.Formatting;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.render.font.RenderableComponent;
import java.util.Objects;

/*
* Allows you to give key and value component to have a custom color
* */
public class TextColorLine extends TextLine {

  protected Object lastKey;

  public TextColorLine(TextHudWidget<?> hudWidget, Component key, Object value) {
    super(hudWidget, key, value);
  }

  @Override
  protected void flushInternal() {
    TextHudWidgetConfig config = this.hudWidget.getConfig();

    TextColor bracketColor = TextColor.color(config.bracketColor().get().get());

    Formatting formatting = config.formatting().get();
    Component componentLine = formatting.build(
        keyComponent,
        valueComponent,
        this.isLeftAligned(),
        bracketColor
    );
    this.renderableComponent = RenderableComponent.builder()
        .disableCache()
        .format(componentLine)
        .disableWidthCaching();
  }

  public boolean updateKey(Object key) {
    if (Objects.equals(this.lastKey, key)) {
      return false;
    }

    this.lastKey = key;
    this.valueComponent = key instanceof Component
        ? (Component) key
        : this.keyComponent instanceof TextComponent keyTextComponent
        ? keyTextComponent.text(String.valueOf(key))
        : Component.text(String.valueOf(key));
    return true;
  }

  public void updateAndFlushKeyAndValue(Object key, Object value) {
    if (this.updateKey(key) || this.update(value)) {
      this.flushInternal();
    }
  }
}
