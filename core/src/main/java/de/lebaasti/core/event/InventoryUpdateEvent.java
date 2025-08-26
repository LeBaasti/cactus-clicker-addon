package de.lebaasti.core.event;

import de.lebaasti.core.context.ItemLoreContext;
import de.lebaasti.core.util.inventory.InventoryItem;
import net.labymod.api.event.Event;
import java.util.List;

public class InventoryUpdateEvent implements Event {

  private final String title;
  private final List<InventoryItem> items;

  public InventoryUpdateEvent(String title, List<InventoryItem> items) {
    this.title = title;
    this.items = items;
  }

  public String getTitle() {
    return title;
  }

  public List<InventoryItem> getItems() {
    return items;
  }
}
