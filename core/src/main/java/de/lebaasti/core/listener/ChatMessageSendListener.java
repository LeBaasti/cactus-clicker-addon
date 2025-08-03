package de.lebaasti.core.listener;

import de.lebaasti.core.feature.emoji.EmojiRegistry;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatMessageSendEvent;

public class ChatMessageSendListener {

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
