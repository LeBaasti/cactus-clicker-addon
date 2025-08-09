package de.lebaasti.core.widgets.ingame;

import de.lebaasti.core.CactusClickerAddon;
import de.lebaasti.core.event.BossbarRenderEvent;
import de.lebaasti.core.util.CactusClickerPlayer;
import de.lebaasti.core.util.FontGlyphRegistry;
import de.lebaasti.core.widgets.ingame.util.OnlyValueHudWidgetConfig;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.Formatting;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.world.DimensionChangeEvent;
import java.util.Set;
import java.util.stream.Collectors;

public class AbilitiesWidget extends TextHudWidget<OnlyValueHudWidgetConfig> {

  private TextLine textLine;
  private CactusClickerAddon addon;

  public AbilitiesWidget(CactusClickerAddon addon) {
    super("abilities_widget", OnlyValueHudWidgetConfig.class);
    this.addon = addon;
    this.bindCategory(addon.widgetCategory());
  }

  @Override
  public void load(OnlyValueHudWidgetConfig config) {
    super.load(config);
    textLine = createLine(Component.translatable("cactusclicker.hudWidget." + id + ".name"),
        Component.translatable("cactusclicker.hudWidget." + id + ".name"));
  }

  @Subscribe
  public void onBossbarRender(BossbarRenderEvent event) {
    if(!isEnabled()) return;
    String componentText = event.getComponent().toString();
    String value = extractAbilitySymbols(componentText);
    if(!value.isEmpty()) {
      textLine.updateAndFlush(value);
      event.setCancelled(true);
    }
  }

  @Subscribe
  public void onDimensionChange(DimensionChangeEvent event) {
    if(!isEnabled()) return;
    if(CactusClickerPlayer.isInAincraft(event.toDimension()) || CactusClickerPlayer.isInFabric(event.toDimension())) {
      textLine.setState(State.VISIBLE);
    } else {
      textLine.setState(State.HIDDEN);
    }
  }

  public String extractAbilitySymbols(String text) {
    Set<String> validSymbols = FontGlyphRegistry.getAllCharsForGroup("cactusclicker_classes_icons")
        .stream()
        .flatMap(Set::stream)
        .collect(Collectors.toSet());

    return text.codePoints()
        .mapToObj(cp -> new String(Character.toChars(cp)))
        .filter(validSymbols::contains)
        .collect(Collectors.joining());
  }

}
