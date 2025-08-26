package de.lebaasti.core.context;

import java.util.List;

public interface ItemLoreContext {
  List<String> getLore(Object itemStack);
  void setLore(Object itemStack, List<String> lore);
  void addLine(Object itemStack, String line);
}
