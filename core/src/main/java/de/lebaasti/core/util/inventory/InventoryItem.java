package de.lebaasti.core.util.inventory;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import java.util.List;

public interface InventoryItem {

  Object getItemStack();
  String getName();
  int getCount();

  List<TextComponent> getLore();
  void setLore(List<TextComponent> lore);
  void addLoreLine(TextComponent line);
}
