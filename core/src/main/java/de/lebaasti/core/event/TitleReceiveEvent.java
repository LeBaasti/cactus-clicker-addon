package de.lebaasti.core.event;

import net.labymod.api.client.chat.Title;
import net.labymod.api.event.DefaultCancellable;
import net.labymod.api.event.Event;

public class TitleReceiveEvent extends DefaultCancellable implements Event {

  private final Title title;

  public TitleReceiveEvent(Title title) {
    this.title = title;
  }

  public Title getTitle() {
    return title;
  }
}
