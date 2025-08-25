package de.lebaasti.core;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@ConfigName("settings")
public class AddonConfiguration extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
  @SwitchSetting
  private final ConfigProperty<Boolean> starForgedSoundEnabled = new ConfigProperty<>(true);
  @SwitchSetting
  private final ConfigProperty<Boolean> legendaryAndEpicAnimationEnabled = new ConfigProperty<>(true);
  @SwitchSetting
  private final ConfigProperty<Boolean> comboChestEnabled = new ConfigProperty<>(true);
  @SwitchSetting
  private final ConfigProperty<Boolean> itemFrameEnabled = new ConfigProperty<>(true);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> starForgedSoundEnabled() {
    return this.starForgedSoundEnabled;
  }

  public ConfigProperty<Boolean> legendaryAndEpicAnimationEnabled() {
    return this.legendaryAndEpicAnimationEnabled;
  }

  public ConfigProperty<Boolean> comboChestEnabled() {
    return comboChestEnabled;
  }

  public ConfigProperty<Boolean> itemFrameEnabled() {
    return itemFrameEnabled;
  }

}
