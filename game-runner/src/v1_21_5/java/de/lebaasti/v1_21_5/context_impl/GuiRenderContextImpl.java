package de.lebaasti.v1_21_5.context_impl;

import de.lebaasti.context.GuiRenderContext;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.gui.GuiGraphics;

public class GuiRenderContextImpl implements GuiRenderContext {
  private final GuiGraphics graphics;

  public GuiRenderContextImpl(GuiGraphics graphics) {
    this.graphics = graphics;
  }

  @Override
  public void fill(int x1, int y1, int x2, int y2, int z, int color) {
    graphics.fill(RenderType.gui(), x1, y1, x2, y2, z, color);
  }
}
