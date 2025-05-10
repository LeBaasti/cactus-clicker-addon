package de.lebaasti.core.util;

import net.labymod.api.Laby;
import net.labymod.api.LabyAPI;
import net.labymod.api.client.resources.ResourceLocation;

public class CactusClickerPlayer {

  public static boolean isInFabric() {
    LabyAPI labyAPI = Laby.labyAPI();
    return labyAPI.minecraft().clientWorld().dimension().getPath().equalsIgnoreCase(labyAPI.getName());
  }

  public static boolean isInAincraft() {
    return isInAincraft(Laby.labyAPI().minecraft().clientWorld().dimension());
  }

  public static boolean isInAincraft(ResourceLocation resourceLocation) {
    return resourceLocation.getPath().startsWith("floor");
  }

}
