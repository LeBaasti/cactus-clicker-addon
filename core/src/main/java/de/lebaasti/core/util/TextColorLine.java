package de.lebaasti.core.util;

import net.labymod.api.Laby;
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
  private boolean customLabelColor = false;

  public TextColorLine(TextHudWidget<?> hudWidget, Component key, Object value) {
    super(hudWidget, key, value);
  }

  @Override
  protected void flushInternal() {
    TextHudWidgetConfig config = this.hudWidget.getConfig();

    TextColor bracketColor = TextColor.color(config.bracketColor().get().get());
    TextColor labelColor = customLabelColor
        ? this.keyComponent.getColor()
        : TextColor.color(config.labelColor().get().get());
    TextColor valueColor = TextColor.color(config.valueColor().get().get());

    Component keyComponent = this.updateColor(this.keyComponent, labelColor);
    Component valueComponent = this.updateColor(this.valueComponent, valueColor);

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
    if(this.keyComponent instanceof TextComponent keyTextComponent) {
      if(key instanceof TextComponent textComponent) {
        keyTextComponent.text(textComponent.getText());
        if(customLabelColor) keyTextComponent.color(textComponent.getColor());
      } else {
        keyTextComponent.text(String.valueOf(key));
      }
    }
    return true;
  }

  public boolean updateAndFlushKey(Object key) {
    if (this.updateKey(key)) {
      this.flushInternal();
      return true;
    }
    return false;
  }

  public void updateAndFlushKeyAndValue(Object key, Object value, boolean customLabelColor) {
    this.customLabelColor = customLabelColor;
    boolean keyUpdated = this.updateKey(key);
    boolean valueUpdated = this.update(value);
    if (keyUpdated || valueUpdated) {
      this.flushInternal();
    }
  }
}
