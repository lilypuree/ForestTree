package lilypuree.forest_tree.trees.client.gui;

import lilypuree.forest_tree.trees.world.gen.feature.parametric.Module;
import net.minecraft.util.text.TranslationTextComponent;
import se.mickelus.mgui.gui.GuiClickable;
import se.mickelus.mgui.gui.GuiRect;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class DesignModuleButton extends GuiClickable {

    Module module;

    private boolean enabled;
    private GuiRect selSquare;

    public DesignModuleButton(int x, int y, Module module, Consumer<Module> clickHandler) {
        super(x, y, 20, 20, () -> clickHandler.accept(module));
        this.module = module;
        this.enabled = true;
        selSquare = new GuiRect(0, 0, width, height, 16777215);
        selSquare.setOpacity(0.2f);
        selSquare.setVisible(false);
        addChild(selSquare);
    }

    private void updateColor() {
        if (!this.enabled) {
            this.selSquare.setColor(8355711);
        } else if (this.hasFocus()) {
            this.selSquare.setColor(0xFFFF55);
            this.selSquare.setVisible(true);
        } else {
            this.selSquare.setColor(16777215);
            this.selSquare.setVisible(false);
        }

    }

    @Override
    protected void onFocus() {
        updateColor();
    }

    @Override
    protected void onBlur() {
        updateColor();
    }

    @Override
    public List<String> getTooltipLines() {
        return this.hasFocus() ? Collections.singletonList(new TranslationTextComponent("forest_tree.treedesignergui.modules."+module.name()).getFormattedText()) : null;
    }
}
