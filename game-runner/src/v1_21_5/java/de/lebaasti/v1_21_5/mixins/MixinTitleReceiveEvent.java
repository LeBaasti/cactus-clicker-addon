package de.lebaasti.v1_21_5.mixins;

import de.lebaasti.core.event.TitleReceiveEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.Title;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class MixinTitleReceiveEvent {

  @Shadow private Component title;
  @Shadow private Component subtitle;
  @Shadow private int titleFadeInTime;
  @Shadow private int titleStayTime;
  @Shadow private int titleFadeOutTime;

  @Inject(method = "setTitle", at = @At("HEAD"), cancellable = true)
  private void fireTitleReceiveEvent(Component tag, CallbackInfo callbackInfo) {
    TitleReceiveEvent event = new TitleReceiveEvent(
        new Title(net.labymod.api.client.component.Component.text(tag),
            net.labymod.api.client.component.Component.text(subtitle), titleFadeInTime,
            titleStayTime, titleFadeOutTime));

    Laby.fireEvent(event);

    if(event.isCancelled()) {
      callbackInfo.cancel();
    }
  }
}
