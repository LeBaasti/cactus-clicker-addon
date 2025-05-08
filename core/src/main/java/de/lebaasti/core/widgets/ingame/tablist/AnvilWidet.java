package de.lebaasti.core.widgets.ingame.tablist;

import de.lebaasti.core.widgets.ingame.tablist.util.SimpleTablistWidget;
import de.lebaasti.core.widgets.ingame.tablist.util.TablistEventDispatcher;

public class AnvilWidet extends SimpleTablistWidget {

  public AnvilWidet(TablistEventDispatcher dispatcher) {
    super("anvil_widget", dispatcher);
  }

  @Override
  protected String germanNameText() {
    return "Amboss Kapazität";
  }

  @Override
  protected String englishNameText() {
    return "Anvil capacity";
  }
}
