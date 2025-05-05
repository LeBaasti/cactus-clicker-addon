package de.lebaasti.v1_20_1;

import de.lebaasti.core.InventoryReader;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import javax.inject.Singleton;
import java.util.Objects;

@Singleton
@Implements(InventoryReader.class)
public class VersionedInventoryReader implements InventoryReader {

  private AbstractContainerScreen<?> currentInventory;

  @Override
  public boolean openInventoryContainsStarForged() {
    if (currentInventory == null) {
      return false;
    }
    AbstractContainerMenu menu = currentInventory.getMenu();
    for (int i = 0; i < menu.slots.size(); i++) {
      Slot slot = menu.slots.get(i);
      ItemStack stack = slot.getItem();

      if (!stack.isEmpty()) {
        String itemName = stack.getHoverName().getString();
        if (itemName.contains("Sternengeschmiedet") || itemName.contains("Starforged")) {
          return true;
        }
      }
    }
    return false;
  }

  private String currentPage;

  @Override
  public boolean wasDisassembleMenuOpenOrUpdated() {
    boolean firstOpen = wasNewInventoryOpen();
    if(currentInventory == null) return false;
    String titleString = currentInventory.getTitle().getString();
    if (titleString.contains("Disassemble") || titleString.contains("Zerlegen")) {
      if(firstOpen) return true;
      else {
        NonNullList<Slot> slots = currentInventory.getMenu().slots;
        if(slots.size() < 5) return false;
        String newPage = slots.get(4).getItem().getDisplayName().getString();
        if(!Objects.equals(currentPage, newPage)) {
          currentPage = newPage;
          return true;
        }
      }
    }
    return false;
  }

  public boolean wasNewInventoryOpen() {
    Screen newScreen = Minecraft.getInstance().screen;
    if(newScreen == null) return false;
    if(newScreen != currentInventory && (currentInventory == null || currentInventory.getTitle() != newScreen.getTitle()) && newScreen instanceof AbstractContainerScreen<?> containerScreen) {
      currentInventory = containerScreen;
      return true;
    }
    return false;
  }
}
