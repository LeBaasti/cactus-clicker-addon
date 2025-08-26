package de.lebaasti.core.event;

import de.lebaasti.core.context.GuiRenderContext;
import net.labymod.api.event.Event;

public class RenderItemDecorationsEvent implements Event {

  private final GuiRenderContext context;
  private final String itemName;
  private final int x;
  private final int y;

  public RenderItemDecorationsEvent(GuiRenderContext context, String itemName, int x, int y) {
    this.context = context;
    this.itemName = itemName;
    this.x = x;
    this.y = y;
  }

  public GuiRenderContext context() { return context; }

  public String getItemName() { return itemName; }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}
