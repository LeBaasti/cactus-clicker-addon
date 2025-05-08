package de.lebaasti.core.util;

import net.labymod.api.Laby;
import net.labymod.api.LabyAPI;

public class CactusClickerPlayer {

  public static boolean isInFabric() {
    LabyAPI labyAPI = Laby.labyAPI();
    return labyAPI.minecraft().clientWorld().dimension().getPath().equalsIgnoreCase(labyAPI.getName());
  }

  public static boolean isInAincraft() {
    return Laby.labyAPI().minecraft().clientWorld().dimension().getPath().startsWith("floor");
  }

}
