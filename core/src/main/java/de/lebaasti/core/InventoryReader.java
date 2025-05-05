package de.lebaasti.core;

import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
public interface InventoryReader {
  boolean openInventoryContainsStarForged();
  boolean wasDisassembleMenuOpenOrUpdated();
}
