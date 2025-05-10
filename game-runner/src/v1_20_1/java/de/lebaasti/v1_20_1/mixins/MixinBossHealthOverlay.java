package de.lebaasti.v1_20_1.mixins;


import de.lebaasti.core.event.BossbarRenderEvent;
import net.labymod.api.Laby;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Mixin(BossHealthOverlay.class)
public abstract class MixinBossHealthOverlay {

  @Shadow
  @Final
  private Map<UUID, LerpingBossEvent> events;

  @Shadow
  @Final
  private Minecraft minecraft;

  @Shadow
  protected abstract void drawBar(GuiGraphics $$0, int $$1, int $$2, BossEvent $$3);

  @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
  private void onRender(GuiGraphics $$0, CallbackInfo callbackInfo) {
    if (!this.events.isEmpty()) {
      int $$1 = $$0.guiWidth();
      int $$2 = 12;
      Iterator var4 = this.events.values().iterator();

      while(var4.hasNext()) {
        LerpingBossEvent $$3 = (LerpingBossEvent)var4.next();

        Component bossBarText = $$3.getName();
        BossbarRenderEvent event = new BossbarRenderEvent(net.labymod.api.client.component.Component.text(bossBarText));
        Laby.fireEvent(event);
        if(event.isCancelled()) {
          continue;
        }

        int $$4 = $$1 / 2 - 91;
        int $$5 = $$2;
        this.drawBar($$0, $$4, $$5, $$3);

        int $$7 = this.minecraft.font.width(bossBarText);
        int $$8 = $$1 / 2 - $$7 / 2;
        int $$9 = $$5 - 9;
        $$0.drawString(this.minecraft.font, bossBarText, $$8, $$9, 16777215);
        Objects.requireNonNull(this.minecraft.font);
        $$2 += 10 + 9;
        if ($$2 >= $$0.guiHeight() / 3) {
          break;
        }
      }
    }
    callbackInfo.cancel();
  }

}
