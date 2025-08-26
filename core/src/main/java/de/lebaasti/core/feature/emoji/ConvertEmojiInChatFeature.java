package de.lebaasti.core.feature.emoji;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatMessageSendEvent;

public class ConvertEmojiInChatFeature {

  @Subscribe
  public void onChatMessageSend(ChatMessageSendEvent event) {
    String newMessage = event.getMessage();

    for (String symbol : EmojiRegistry.getAllSymbols()) {
      String name = EmojiRegistry.getName(symbol);
      if (name != null) {
        newMessage = newMessage.replace(symbol, ":" + name + ":");
      }
    }

    event.changeMessage(newMessage);
  }

}
