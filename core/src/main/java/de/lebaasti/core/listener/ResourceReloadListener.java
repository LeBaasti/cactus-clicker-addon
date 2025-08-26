package de.lebaasti.core.listener;

import de.lebaasti.core.CCAddon;
import de.lebaasti.core.feature.emoji.EmojiRegistry;
import de.lebaasti.core.util.FontGlyphRegistry;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.resources.pack.ResourceReloadEvent;

public class ResourceReloadListener {

  private final CCAddon addon;

  public ResourceReloadListener(CCAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onResourceReload(ResourceReloadEvent event) {
    FontGlyphRegistry.reload();
    EmojiRegistry.reload();
  }

}
