package de.lebaasti.v1_21_5.mixins;

import de.lebaasti.core.CCAddon;
import de.lebaasti.core.util.inventory.InventoryTracker;
import de.lebaasti.core.util.inventory.InventoryItem;
import de.lebaasti.v1_21_5.util_impl.InventoryItemImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

  // Reference to the global tracker
  private final InventoryTracker tracker = InventoryTracker.get();

  @Inject(method = "handleOpenScreen", at = @At("TAIL"))
  private void onOpenScreen(ClientboundOpenScreenPacket packet, CallbackInfo callbackInfo) {
    if(!CCAddon.getInstance().isEnabledAndConnected()) return;
    tracker.openContainer(packet.getTitle().getString());
  }

  @Inject(method = "handleContainerContent", at = @At("TAIL"))
  private void onContainerContent(ClientboundContainerSetContentPacket packet, CallbackInfo callbackInfo) {
    if(!CCAddon.getInstance().isEnabledAndConnected()) return;
    List<InventoryItem> items = packet.items().stream()
        .map(this::toInventoryItem) // convert ItemStack to InventoryItem
        .toList();
    tracker.setContent(items);
  }

  @Inject(method = "handleContainerSetSlot", at = @At("TAIL"))
  private void onSlotUpdate(ClientboundContainerSetSlotPacket packet, CallbackInfo callbackInfo) {
    if(!CCAddon.getInstance().isEnabledAndConnected()) return;
    tracker.updateSlot(packet.getSlot(), toInventoryItem(packet.getItem()));
  }

  @Inject(method = "handleContainerClose", at = @At("TAIL"))
  private void onContainerClose(ClientboundContainerClosePacket packet, CallbackInfo callbackInfo) {
    if(!CCAddon.getInstance().isEnabledAndConnected()) return;
    tracker.closeContainer();
  }

  // Private helper method to convert ItemStack → InventoryItem
  private InventoryItem toInventoryItem(ItemStack stack) {
    if (stack.isEmpty()) return null;
    return new InventoryItemImpl(stack, stack.getHoverName().getString(), stack.getCount());
  }

}
