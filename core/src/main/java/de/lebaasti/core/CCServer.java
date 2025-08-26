package de.lebaasti.core;

import net.labymod.api.client.network.server.AbstractServer;
import net.labymod.api.event.Phase;

public class CCServer extends AbstractServer {

  private final CCAddon addon;
  private boolean connected;

  public CCServer(CCAddon addon) {
    super("playlegend");
    this.addon = addon;
    connected = false;
  }

  @Override
  public void loginOrSwitch(LoginPhase phase) {
    if(phase == LoginPhase.LOGIN) connected = true;
  }

  @Override
  public void disconnect(Phase phase) {
    if(phase == Phase.POST) connected = false;
  }

  public boolean isConnected() {
    return connected;
  }

}
