package de.lebaasti.v1_21_5.util_impl;

import de.lebaasti.core.util.inventory.InventoryItem;
import net.labymod.api.Laby;
import net.labymod.api.client.component.TextComponent;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InventoryItemImpl implements InventoryItem {

  private static final HolderLookup.Provider DUMMY_PROVIDER = HolderLookup.Provider.create(Stream.empty());

  private final ItemStack itemStack;
  private final String name;
  private final int count;

  public InventoryItemImpl(ItemStack itemStack, String name, int count) {
    this.itemStack = itemStack;
    this.name = name;
    this.count = count;
  }

  @Override
  public Object getItemStack() {
    return itemStack;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getCount() {
    return count;
  }

  // ---- Converter ----
  private static TextComponent toLaby(Component mcComponent) {
    return (TextComponent) Laby.labyAPI().minecraft()
        .componentMapper().fromMinecraftComponent(mcComponent);
  }

  private static Component toMc(TextComponent labyComponent) {
    return (Component) Laby.labyAPI().minecraft().componentMapper().toMinecraftComponent(labyComponent);
  }

  // ---- Lore handling ----
  @Override
  public List<TextComponent> getLore() {
    ItemLore lore = itemStack.get(DataComponents.LORE);
    if (lore == null) return List.of();

    return lore.lines().stream()
        .map(InventoryItemImpl::toLaby)
        .collect(Collectors.toList());
  }

  @Override
  public void setLore(List<TextComponent> loreLines) {
    ItemLore lore = loreLines.isEmpty()
        ? ItemLore.EMPTY
        : new ItemLore(
            loreLines.stream()
                .map(InventoryItemImpl::toMc)
                .collect(Collectors.toUnmodifiableList())
        );
    itemStack.set(DataComponents.LORE, lore);
  }

  @Override
  public void addLoreLine(TextComponent line) {
    List<TextComponent> lore = getLore();
    lore.add(line);
    setLore(lore);
  }
}
