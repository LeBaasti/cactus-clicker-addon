package de.lebaasti.core;

import de.lebaasti.core.feature.DisableComboChestFeature;
import de.lebaasti.core.feature.DisableDropAnimationFeature;
import de.lebaasti.core.feature.EssenzeWaveFeature;
import de.lebaasti.core.feature.ItemFrameFeature;
import de.lebaasti.core.feature.StarForgedSoundFeature;
import de.lebaasti.core.feature.emoji.EmojiActivity;
import de.lebaasti.core.feature.emoji.ConvertEmojiInChatFeature;
import de.lebaasti.core.listener.ResourceReloadListener;
import de.lebaasti.core.widgets.ingame.AbilitiesWidget;
import de.lebaasti.core.widgets.ingame.ComboChestWidget;
import de.lebaasti.core.widgets.ingame.MaterialsWidget;
import de.lebaasti.core.widgets.ingame.tablist.AnvilWidet;
import de.lebaasti.core.widgets.ingame.tablist.PowerCrystalWidget;
import de.lebaasti.core.widgets.ingame.tablist.util.TablistEventDispatcher;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.HudWidget;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.activity.chat.ChatButtonWidget;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.revision.SimpleRevision;
import net.labymod.api.util.version.SemanticVersion;

@AddonMain
public class CCAddon extends LabyAddon<AddonConfiguration> {

  private HudWidgetCategory widgetCategory;
  private CCServer server;

  @Override
  protected void preConfigurationLoad() {
    Laby.references().revisionRegistry().register(new SimpleRevision("ccaddon", new SemanticVersion("1.0.5"), "2025-06-26"));
  }

  @Override
  protected void enable() {
    this.registerSettingCategory();

    labyAPI().chatProvider().chatInputService().register(getEmojiWidget());

    labyAPI().hudWidgetRegistry().categoryRegistry().register(this.widgetCategory = new HudWidgetCategory("ccaddon_category"));
    registerHudWidgetWithListener(new MaterialsWidget(this));
    registerHudWidgetWithListener(new ComboChestWidget(this));
    registerHudWidgetWithListener(new AbilitiesWidget(this));

    this.registerListener(new ConvertEmojiInChatFeature());
    this.registerListener(new ResourceReloadListener(this));
    this.registerListener(new DisableDropAnimationFeature(this));
    this.registerListener(new DisableComboChestFeature(this));
    this.registerListener(new ItemFrameFeature(this));
    this.registerListener(new StarForgedSoundFeature(this));
    this.registerListener(new EssenzeWaveFeature(this));

    TablistEventDispatcher dispatcher = new TablistEventDispatcher(this);
    this.registerListener(dispatcher);

    labyAPI().hudWidgetRegistry().register(new PowerCrystalWidget(dispatcher));
    labyAPI().hudWidgetRegistry().register(new AnvilWidet(dispatcher));

    labyAPI().serverController().registerServer(server = new CCServer(this));
  }

  @Override
  protected Class<AddonConfiguration> configurationClass() {
    return AddonConfiguration.class;
  }

  public CCServer server() {
    return server;
  }

  public HudWidgetCategory widgetCategory() {
    return widgetCategory;
  }

  public void registerHudWidgetWithListener(HudWidget<?> widget) {
    labyAPI().hudWidgetRegistry().register(widget);
    registerListener(widget);
  }

  public static ChatButtonWidget getEmojiWidget() {
    ResourceLocation resourceLocation = ResourceLocation.create("ccaddon", "buttons/emoji_button.png");
    return ChatButtonWidget.icon("emojis", Icon.texture(resourceLocation), EmojiActivity::new);
  }
}
