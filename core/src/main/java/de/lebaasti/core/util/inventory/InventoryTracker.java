package de.lebaasti.core.util.inventory;

import de.lebaasti.core.context.ItemLoreContext;
import de.lebaasti.core.event.InventoryOpenEvent;
import de.lebaasti.core.event.InventoryUpdateEvent;
import net.labymod.api.Laby;
import java.util.ArrayList;
import java.util.List;

public class InventoryTracker {

  private static final InventoryTracker INSTANCE = new InventoryTracker();

  public static InventoryTracker get() {
    return INSTANCE;
  }

  private InventoryTracker() {

  }

  private String currentContainerName = null;
  private final List<InventoryItem> currentItems = new ArrayList<>();

  // Called when a container is opened
  public void openContainer(String name) {
    this.currentContainerName = name;
    this.currentItems.clear(); // reset items

    Laby.fireEvent(new InventoryOpenEvent(currentContainerName));
  }

  // Called when a container is closed
  public void closeContainer() {
    this.currentContainerName = null;
    this.currentItems.clear();
  }

  // Initialize all slots (replace the current list)
  public void setContent(List<InventoryItem> items) {
    if(currentContainerName == null) return;

    this.currentItems.clear();
    this.currentItems.addAll(items);

    Laby.fireEvent(new InventoryUpdateEvent(currentContainerName, this.currentItems));
  }

  // Update a single slot
  public void updateSlot(int slotIndex, InventoryItem item) {
    if(currentContainerName == null) return;
    // Expand the list if needed
    while (currentItems.size() <= slotIndex) currentItems.add(null);
    currentItems.set(slotIndex, item);
  }

  // Get the name of the currently opened container
  public String getCurrentContainerName() {
    return currentContainerName;
  }

  // Get all items of the currently opened container
  public List<InventoryItem> getCurrentItems() {
    return currentItems;
  }

  // Check if a container is currently open
  public boolean isContainerOpen() {
    return currentContainerName != null;
  }
}
