package de.lebaasti.core.widgets.ingame.tablist.util;

import de.lebaasti.core.CactusClickerAddon;
import de.lebaasti.core.util.CactusClickerPlayer;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoAddEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoUpdateEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoUpdateEvent.UpdateType;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TablistEventDispatcher {

  private final CactusClickerAddon addon;
  private final List<Consumer<PlayerInfoUpdateEvent>> listeners = new ArrayList<>();
  private final List<NetworkPlayerInfo> playerList = new ArrayList<>();

  public TablistEventDispatcher(CactusClickerAddon addon) {
    this.addon = addon;
  }

  public void register(Consumer<PlayerInfoUpdateEvent> listener) {
    listeners.add(listener);
  }

  public void unregister(Consumer<PlayerInfoUpdateEvent> listener) {
    listeners.remove(listener);
  }

  @Subscribe
  public void onPlayerInfoAdd(PlayerInfoAddEvent event) {
    NetworkPlayerInfo networkPlayerInfo = event.playerInfo();
    if ((CactusClickerPlayer.isInAincraft() || CactusClickerPlayer.isInFabric()) && networkPlayerInfo.isListed()) {
      playerList.add(event.playerInfo());
    }
  }

  @Subscribe
  public void onSubServerSwitch(SubServerSwitchEvent event) {
    playerList.clear();
  }

  @Subscribe
  public void onTablistUpdate(PlayerInfoUpdateEvent event) {
    if(event.type() == UpdateType.DISPLAY_NAME) {
      for (Consumer<PlayerInfoUpdateEvent> listener : listeners) {
        listener.accept(event);
      }
    } else if (event.type() == UpdateType.UPDATE_LISTED) {
      if(event.playerInfo().isListed()) {
        playerList.add(event.playerInfo());
      }
    }
  }

  public CactusClickerAddon addon() {
    return addon;
  }

  public List<NetworkPlayerInfo> playerList() {
    return playerList;
  }
}

