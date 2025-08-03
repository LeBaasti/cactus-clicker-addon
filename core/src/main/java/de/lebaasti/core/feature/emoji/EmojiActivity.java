package de.lebaasti.core.feature.emoji;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.chatinput.ChatInputTabSettingActivity;
import net.labymod.api.client.gui.screen.widget.AbstractWidget;
import net.labymod.api.client.gui.screen.widget.action.ListSession;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.TilesGridWidget;

import java.util.*;

@Link("chatinput/emojis.lss")
@AutoActivity
public class EmojiActivity extends ChatInputTabSettingActivity<FlexibleContentWidget> {

  private final List<EmojiWidget> emojiWidgets = new ArrayList<>();
  private final TilesGridWidget<EmojiWidget> gridWidget = new TilesGridWidget<>();
  private final ListSession<?> listSession = new ListSession<>();

  public EmojiActivity() {
    gridWidget.addId("gridWidget");

    for (String name : EmojiRegistry.getAllNames()) {
      String symbol = EmojiRegistry.getSymbol(name);
      if (symbol != null) {
        EmojiWidget widget = new EmojiWidget(name, symbol);
        emojiWidgets.add(widget);
      }
    }
  }

  @Override
  public void initialize(Parent parent) {
    document.addChild(createWindow());
  }

  private AbstractWidget<?> createWindow() {
    this.contentWidget = new FlexibleContentWidget();
    contentWidget.addId("window");

    TextFieldWidget textFieldWidget = new TextFieldWidget();
    textFieldWidget.addId("title-bar");
    textFieldWidget.placeholder(Component.text("Suchen..."));

    textFieldWidget.updateListener(search -> {
      String lowerSearch = search.toLowerCase(Locale.ROOT);
      gridWidget.getChildren().clear();

      emojiWidgets.stream()
          .filter(widget -> lowerSearch.isEmpty() || widget.getTitle().toLowerCase(Locale.ROOT).contains(lowerSearch))
          .forEach(gridWidget::addTile);

      gridWidget.updateTiles();
    });

    contentWidget.addContent(textFieldWidget);
    emojiWidgets.forEach(gridWidget::addTile);

    ScrollWidget scrollWidget = new ScrollWidget(gridWidget, listSession);
    contentWidget.addFlexibleContent(scrollWidget);

    return contentWidget;
  }
}
