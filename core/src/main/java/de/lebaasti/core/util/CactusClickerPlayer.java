package de.lebaasti.core.util;

import net.labymod.api.Laby;
import net.labymod.api.LabyAPI;
import net.labymod.api.client.resources.ResourceLocation;

public class CactusClickerPlayer {

  public static boolean isInFabric() {
    return isInFabric(Laby.labyAPI().minecraft().clientWorld().dimension());
  }

  public static boolean isInFabric(ResourceLocation resourceLocation) {
    return resourceLocation.getPath().equalsIgnoreCase(Laby.labyAPI().getName());
  }

  public static boolean isInAincraft() {
    return isInAincraft(Laby.labyAPI().minecraft().clientWorld().dimension());
  }

  public static boolean isInAincraft(ResourceLocation resourceLocation) {
    return resourceLocation.getPath().startsWith("floor");
  }

}
