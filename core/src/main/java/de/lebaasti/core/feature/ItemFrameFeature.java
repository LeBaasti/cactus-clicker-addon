package de.lebaasti.core.feature;

import de.lebaasti.core.context.GuiRenderContext;
import de.lebaasti.core.CCAddon;
import de.lebaasti.core.event.RenderItemDecorationsEvent;
import net.labymod.api.event.Subscribe;
import java.awt.*;
import java.util.Map;

public class ItemFrameFeature {

  private final CCAddon addon;

  public ItemFrameFeature(CCAddon addon) {
    this.addon = addon;
  }

  private static final Map<String, Integer> ITEM_COLORS = Map.of(
      "Frostgeschmiedet", 0xFF00BFFF,
      "Lavageschmiedet",  0xFFFF4500,
      "Titangeschmiedet", 0xFFC0C0C0,
      "Drachengeschmiedet", 0xFF00FF7F,
      "Dämonengeschmiedet", 0xFF8B008B,
      "Blitzgeschmiedet",  0xFFFFFF00,
      "Sternengeschmiedet",  0xFFFFD700
  );

  @Subscribe
  public void onRenderItemDecorations(RenderItemDecorationsEvent event) {
    if(!addon.server().isConnected() || !addon.configuration().itemFrameEnabled().get()) {
      return;
    }

    GuiRenderContext renderContext = event.context();
    String name = event.getItemName();
    int x = event.getX();
    int y = event.getY();

    int zOffset = 250;

    int frameColor = getFrameColor(name);

    if (frameColor == -1) {
      return;
    }

    if (name.contains("Sternengeschmiedet")) {
      drawRainbowFrame(renderContext, x, y, zOffset);
    } else {
      drawSolidFrame(renderContext, x, y, zOffset, frameColor);
    }
  }

  private void drawSolidFrame(GuiRenderContext ctx, int x, int y, int zOffset, int color) {
    ctx.fill(x, y, x + 16, y + 1, zOffset, color);        // oben
    ctx.fill(x, y + 15, x + 16, y + 16, zOffset, color);  // unten
    ctx.fill(x, y, x + 1, y + 16, zOffset, color);        // links
    ctx.fill(x + 15, y, x + 16, y + 16, zOffset, color);  // rechts
  }


  private void drawRainbowFrame(GuiRenderContext ctx, int x, int y, int zOffset) {
    long time = System.currentTimeMillis();
    float cycle = (time % 5000L) / 5000f;
    int perimeter = 64; // 4 * 16
    int p = 0;

    // top
    for (int i = 0; i < 16; i++, p++) {
      ctx.fill(x + i, y, x + i + 1, y + 1, zOffset, rainbowColor(p, perimeter, cycle));
    }
    // right
    for (int i = 0; i < 16; i++, p++) {
      ctx.fill(x + 15, y + i, x + 16, y + i + 1, zOffset, rainbowColor(p, perimeter, cycle));
    }
    // bottom
    for (int i = 0; i < 16; i++, p++) {
      ctx.fill(x + (15 - i), y + 15, x + (16 - i), y + 16, zOffset, rainbowColor(p, perimeter, cycle));
    }
    // left
    for (int i = 0; i < 16; i++, p++) {
      ctx.fill(x, y + (15 - i), x + 1, y + (16 - i), zOffset, rainbowColor(p, perimeter, cycle));
    }
  }

  private int rainbowColor(int pos, int perimeter, float cycle) {
    float hue = (pos / (float) perimeter + cycle) % 1.0f;
    return Color.HSBtoRGB(hue, 1f, 1f) | 0xFF000000;
  }

  private int getFrameColor(String name) {
    for (var entry : ITEM_COLORS.entrySet()) {
      if (name.contains(entry.getKey())) {
        return entry.getValue();
      }
    }
    return -1;
  }
}
