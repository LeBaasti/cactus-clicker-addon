package de.lebaasti.v1_21_5.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import de.lebaasti.core.event.RenderItemDecorationsEvent;
import de.lebaasti.v1_21_5.context_impl.GuiRenderContextImpl;
import net.labymod.api.Laby;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.awt.*;
import java.util.List;
import java.util.Optional;

@Mixin(GuiGraphics.class)
public abstract class MixinGuiGraphics {

  @Shadow
  private PoseStack pose;

  @Shadow
  public abstract void fill(RenderType renderType, int x1, int y1, int x2, int y2, int z, int color);


  @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
      at = @At("TAIL"))
  public void onRenderItemDecorations(Font font, ItemStack itemStack, int x, int y, @Nullable String countLabel, CallbackInfo callbackInfo) {
    if (!itemStack.isEmpty() && font != null) {
      this.pose.pushPose();

      GuiRenderContextImpl context = new GuiRenderContextImpl((GuiGraphics)(Object)this);
      RenderItemDecorationsEvent event = new RenderItemDecorationsEvent(context, itemStack.getHoverName().getString(), x, y);
      Laby.fireEvent(event);

      this.pose.popPose();
    }
  }
}

