package lilypuree.forest_tree.trees.client.gui;

import se.mickelus.mgui.gui.GuiClickable;
import se.mickelus.mgui.gui.GuiRect;

import java.util.function.Consumer;

public class DesignModuleButton extends GuiClickable {

    Module module;

    public DesignModuleButton(int x, int y, Module module, Consumer<Module> clickHandler) {
        super(x, y, 20, 20, () -> clickHandler.accept(module));

        addChild(new GuiRect(0, 0, width, height, 0));
    }

    @Override
    protected void onFocus() {
        super.onFocus();
    }

    @Override
    protected void onBlur() {
        super.onBlur();
    }

}
