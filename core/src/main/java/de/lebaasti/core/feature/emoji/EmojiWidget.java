package de.lebaasti.core.feature.emoji;


import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;

@AutoWidget
public class EmojiWidget extends SimpleWidget {

  private final String title;
  private final String symbol;

  public EmojiWidget(String title, String symbol) {
    this.title = title;
    this.symbol = symbol;
  }

  @Override
  public void initialize(Parent parent) {
    ComponentWidget symbolWidget = ComponentWidget.text(symbol);
    symbolWidget.addId("emojiIcon");
    symbolWidget.setPressable(() -> {
      labyAPI.minecraft().chatExecutor().insertText(symbol);
    });
    addChild(symbolWidget);

    setHoverComponent(Component.text(title));
  }

  public String getTitle() {
    return this.title;
  }
}
