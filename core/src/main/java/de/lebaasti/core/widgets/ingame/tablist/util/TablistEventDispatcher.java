package de.lebaasti.core.widgets.ingame.tablist.util;

import de.lebaasti.core.CCAddon;
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

  private final CCAddon addon;
  private final List<Consumer<NetworkPlayerInfo>> listeners = new ArrayList<>();
  private final List<NetworkPlayerInfo> playerList = new ArrayList<>();

  public TablistEventDispatcher(CCAddon addon) {
    this.addon = addon;
  }

  public void register(Consumer<NetworkPlayerInfo> listener) {
    listeners.add(listener);
  }

  public void unregister(Consumer<NetworkPlayerInfo> listener) {
    listeners.remove(listener);
  }

  @Subscribe
  public void onPlayerInfoAdd(PlayerInfoAddEvent event) {
    if(!addon.server().isConnected()) return;
    NetworkPlayerInfo networkPlayerInfo = event.playerInfo();
    if ((CactusClickerPlayer.isInAincraft() || CactusClickerPlayer.isInFabric()) && networkPlayerInfo.isListed()) {
      if(!playerList.contains(networkPlayerInfo)) playerList.add(networkPlayerInfo);
    }
  }

  @Subscribe
  public void onSubServerSwitch(SubServerSwitchEvent event) {
    if(!addon.server().isConnected()) return;
    playerList.clear();
    for (Consumer<NetworkPlayerInfo> listener : listeners) {
      listener.accept(null);
    }
  }

  @Subscribe
  public void onTablistUpdate(PlayerInfoUpdateEvent event) {
    UpdateType type = event.type();
    if (type != UpdateType.DISPLAY_NAME && type != UpdateType.UPDATE_LISTED) {
      return;
    }

    NetworkPlayerInfo networkPlayerInfo = event.playerInfo();
    if (type == UpdateType.UPDATE_LISTED) {
      if (networkPlayerInfo.isListed() && !playerList.contains(networkPlayerInfo)) {
        playerList.add(networkPlayerInfo);
      }
    }

    for (Consumer<NetworkPlayerInfo> listener : listeners) {
      listener.accept(networkPlayerInfo);
    }
  }

  public CCAddon addon() {
    return addon;
  }

  public List<NetworkPlayerInfo> playerList() {
    return playerList;
  }
}

