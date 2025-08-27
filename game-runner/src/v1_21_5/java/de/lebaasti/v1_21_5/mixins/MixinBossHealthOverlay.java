package de.lebaasti.v1_21_5.mixins;


import de.lebaasti.core.CCAddon;
import de.lebaasti.core.event.BossbarRenderEvent;
import net.labymod.api.Laby;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
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
  Map<UUID, LerpingBossEvent> events;

  @Shadow
  @Final
  private Minecraft minecraft;

  @Shadow
  protected abstract void drawBar(GuiGraphics $$0, int $$1, int $$2, BossEvent $$3);

  @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
  private void onRender(GuiGraphics $$0, CallbackInfo callbackInfo) {
    if(!CCAddon.getInstance().isEnabledAndConnected()) return;

    if (!this.events.isEmpty()) {
      ProfilerFiller $$1 = Profiler.get();
      $$1.push("bossHealth");
      int $$2 = $$0.guiWidth();
      int $$3 = 12;
      Iterator var5 = this.events.values().iterator();

      while(var5.hasNext()) {
        LerpingBossEvent $$4 = (LerpingBossEvent)var5.next();

        //INJECTION CODE - START
        Component bossBarText = $$4.getName();
        BossbarRenderEvent event = new BossbarRenderEvent(net.labymod.api.client.component.Component.text(bossBarText));
        Laby.fireEvent(event);
        if(event.isCancelled()) {
          continue;
        }
        //INJECTION CODE - END

        int $$5 = $$2 / 2 - 91;
        int $$6 = $$3;
        this.drawBar($$0, $$5, $$6, $$4);
        Component $$7 = $$4.getName();
        int $$8 = this.minecraft.font.width($$7);
        int $$9 = $$2 / 2 - $$8 / 2;
        int $$10 = $$6 - 9;
        $$0.drawString(this.minecraft.font, $$7, $$9, $$10, 16777215);
        Objects.requireNonNull(this.minecraft.font);
        $$3 += 10 + 9;
        if ($$3 >= $$0.guiHeight() / 3) {
          break;
        }
      }

      $$1.pop();

      //INJECTION CODE (return function) - START
      callbackInfo.cancel();
      //INJECTION CODE - END
    }
  }

}
