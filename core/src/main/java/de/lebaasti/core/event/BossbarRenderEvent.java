package de.lebaasti.core.event;

import net.labymod.api.client.component.Component;
import net.labymod.api.event.DefaultCancellable;
import net.labymod.api.event.Event;

public class BossbarRenderEvent extends DefaultCancellable implements Event {

  private final Component component;

  public BossbarRenderEvent(Component component) {
    this.component = component;
  }

  public Component getComponent() {
    return component;
  }
}
