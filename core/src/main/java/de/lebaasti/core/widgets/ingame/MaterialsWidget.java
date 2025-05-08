package de.lebaasti.core.widgets.ingame;

import de.lebaasti.core.CactusClickerAddon;
import de.lebaasti.core.util.CactusClickerPlayer;
import de.lebaasti.core.util.TextColorLine;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ActionBarReceiveEvent;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MaterialsWidget extends TextHudWidget<TextHudWidgetConfig> {

  private final EnumMap<MaterialRarity, TextColorLine> textLines = new EnumMap<>(MaterialRarity.class);

  public MaterialsWidget(CactusClickerAddon addon) {
    super("materials_widget");
    this.bindCategory(addon.widgetCategory());
    this.setIcon(Icon.texture(ResourceLocation.create("minecraft", "textures/item/name_tag.png")));
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    textLines.clear();
    for (MaterialRarity value : MaterialRarity.values()) {
      TextColorLine line = createLine(
          Component.text(value.name()).color(TextColor.color(value.colorCode)), Component.text(""),
          TextColorLine::new);
      line.setState(State.HIDDEN);
      textLines.put(value, line);
    }
  }

  @Subscribe
  public void onActionBarReceive(ActionBarReceiveEvent event) {
    if (event.phase() != Phase.PRE) {
      return;
    }
    Component message = event.getMessage();
    if(message instanceof TextComponent textMessage) {
      List<Component> children = textMessage.getChildren();
      if(children.size() == 3) {
        TextComponent materialName = (TextComponent) children.get(1);
        TextComponent materialAmountComponent = (TextComponent) children.get(2);
        String materialAmount = materialAmountComponent.getText().replaceAll("[^0-9]+", "");
        MaterialRarity rarity = MaterialRarity.findByColorCode(materialName.getColor().getValue());
        if(rarity != null) {
          TextColorLine textColorLine = textLines.get(rarity);
          textColorLine.updateAndFlushKeyAndValue(materialName, materialAmount, true);
          textColorLine.setState(State.VISIBLE);
        }
      }
    }
  }

  @Subscribe
  public void onSubServerSwitch(SubServerSwitchEvent event) {
    if(!CactusClickerPlayer.isInAincraft()) {
      for (Map.Entry<MaterialRarity, TextColorLine> entry : textLines.entrySet()) {
        entry.getValue().setState(State.HIDDEN);
      }
    }
  }

  enum MaterialRarity {
    COMMON(16777215),
    UNCOMMON(2031360),
    RARE(28893),
    EPIC(10696174),
    LEGENDARY(16744448);

    private final int colorCode;

    MaterialRarity(int colorCode) {
      this.colorCode = colorCode;
    }

    public static MaterialRarity findByColorCode(int code) {
      for (MaterialRarity rarity : MaterialRarity.values()) {
        if (rarity.colorCode == code) return rarity;
      }
      return null;
    }
  }
}
