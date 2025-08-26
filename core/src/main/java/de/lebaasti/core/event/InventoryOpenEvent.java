package de.lebaasti.core.event;

import net.labymod.api.event.Event;

public class InventoryOpenEvent implements Event {

  private final String title;

  public InventoryOpenEvent(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
