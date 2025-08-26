package de.lebaasti.core.widgets.ingame;

import de.lebaasti.core.CCAddon;
import de.lebaasti.core.event.BossbarRenderEvent;
import de.lebaasti.core.util.CactusClickerPlayer;
import de.lebaasti.core.util.FontGlyphRegistry;
import de.lebaasti.core.widgets.ingame.ComboChestWidget.ComboChestHudWidgetConfig;
import de.lebaasti.core.widgets.ingame.util.OnlyValueHudWidgetConfig;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.Formatting;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.position.HudSize;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.event.Subscribe;

import java.util.*;
import java.util.stream.Collectors;

public class ComboChestWidget extends TextHudWidget<ComboChestHudWidgetConfig> {

  // Initial defaults
  private static final Component DEFAULT_SYMBOL = Component.text(
      FontGlyphRegistry.getCharsForGroupName("combo_chest", "tier_e")
  );
  private static final Component EMPTY = Component.empty();
  private static final int[] NUMBER_POSITIONS = {39, 37, 33, 30, 27};

  // Current state
  private Component comboChestSymbols = DEFAULT_SYMBOL;
  private Component comboChestNumbers = EMPTY;
  private long comboChestNumbersPos = NUMBER_POSITIONS[0];

  private TextLine textLine;
  private final CCAddon addon;

  public ComboChestWidget(CCAddon addon) {
    super("combo_chest_widget", ComboChestHudWidgetConfig.class);
    this.addon = addon;
    bindCategory(addon.widgetCategory());
    setIcon(Icon.texture(ResourceLocation.create("minecraft", "textures/item/iron_sword.png")));
  }

  @Override
  public void load(ComboChestHudWidgetConfig config) {
    super.load(config);
    textLine = createLine(Component.translatable("cactusclicker.hudWidget." + id + ".name"), "Combo Chest");
  }


  @Override
  public void render(Stack stack, MutableMouse mouse, float partialTicks, boolean isEditorContext, HudSize size) {
    if (config.showAsText().get()) {
      super.render(stack, mouse, partialTicks, isEditorContext, size);
      return;
    }

    if(stack != null) {
      var renderer = labyAPI.renderPipeline().componentRenderer().builder();
      renderer.text(RenderableComponent.of(comboChestSymbols)).pos(-23, 18).render(stack);
      renderer.text(RenderableComponent.of(comboChestNumbers)).pos(comboChestNumbersPos, 18).render(stack);
    }

    size.setWidth(84L);
    size.setHeight(61L);
  }

  @Override
  public boolean isVisibleInGame() {
    return addon.server().isConnected() && CactusClickerPlayer.isInAincraft();
  }

  @Subscribe
  public void onBossbarRender(BossbarRenderEvent event) {
    if (!addon.server().isConnected() || !isEnabled()) return;

    String componentText = event.getComponent().toString();
    if (!FontGlyphRegistry.containsCharInGroup("combo_chest", componentText)) return;
    event.setCancelled(config.hideDefaultComboChest().get());

    if (config.showAsText().get()) {
      if (!FontGlyphRegistry.containsCharInGroupName("combo_chest", "numbers", componentText)) return;
      try {
        textLine.updateAndFlush(decodeNumber(componentText));
      } catch (NumberFormatException ignored) {}
      return;
    }

    String symbol = extractSymbols(componentText, "numbers");
    if (!symbol.isEmpty()) {
      comboChestSymbols = Component.text(symbol);

      String numbers = extractNumbers(componentText);
      if (!numbers.isEmpty()) {
        comboChestNumbers = Component.text(numbers);
        comboChestNumbersPos = NUMBER_POSITIONS[Math.min(numbers.length() - 1, NUMBER_POSITIONS.length - 1)];
      } else { //combochest levelup animation
        comboChestNumbers = Component.text("");
      }
    }
  }

  private String extractSymbols(String text, String... excludedSubgroups) {
    Set<String> validSymbols = FontGlyphRegistry.getAllCharsForGroupExcluding("combo_chest", excludedSubgroups)
        .stream()
        .flatMap(Set::stream)
        .collect(Collectors.toSet());

    return filterByValidSymbols(text, validSymbols);
  }

  private String extractNumbers(String text) {
    return filterByValidSymbols(text, FontGlyphRegistry.getCharsForGroupName("combo_chest", "numbers"));
  }

  private String filterByValidSymbols(String text, Set<String> validSymbols) {
    return text.codePoints()
        .mapToObj(cp -> new String(Character.toChars(cp)))
        .filter(validSymbols::contains)
        .collect(Collectors.joining());
  }

  private int decodeNumber(String input) {
    StringBuilder result = new StringBuilder();
    LinkedHashSet<String> numbers = FontGlyphRegistry.getCharsForGroupName("combo_chest", "numbers");

    if (numbers.isEmpty()) throw new NumberFormatException("No number glyphs available");

    char first = numbers.iterator().next().charAt(0);
    char last = numbers.stream().reduce((a, b) -> b).orElseThrow().charAt(0);

    for (char c : input.toCharArray()) {
      if (c >= first && c <= last) {
        result.append(c - first);
      }
    }
    return Integer.parseInt(result.toString());
  }

  public static class ComboChestHudWidgetConfig extends OnlyValueHudWidgetConfig {

    public ComboChestHudWidgetConfig() {
      showAsText.addChangeListener((type, oldValue, newValue) ->
          formatting().set(newValue ? Formatting.SQUARE_BRACKETS : Formatting.VALUE_ONLY)
      );
    }

    @SwitchSetting
    private final ConfigProperty<Boolean> showAsText = new ConfigProperty<>(false);

    @SwitchSetting
    private final ConfigProperty<Boolean> hideDefaultComboChest = new ConfigProperty<>(true);

    public ConfigProperty<Boolean> showAsText() {
      return showAsText;
    }

    public ConfigProperty<Boolean> hideDefaultComboChest() {
      return hideDefaultComboChest;
    }
  }
}
