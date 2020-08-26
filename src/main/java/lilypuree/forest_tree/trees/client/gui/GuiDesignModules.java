package lilypuree.forest_tree.trees.client.gui;

import lilypuree.forest_tree.world.trees.gen.feature.parametric.Module;
import se.mickelus.mgui.gui.GuiAttachment;
import se.mickelus.mgui.gui.GuiElement;

import java.util.function.Consumer;

public class GuiDesignModules extends GuiElement {

    private static final int DESIGN_MODULE_COUNT = 4;

    private DesignModuleButton[] designModuleButtons;

    public GuiDesignModules(int x, int y, Consumer<Module> clickHandler) {
        super(x, y, 0, DESIGN_MODULE_COUNT * 25);

        designModuleButtons = new DesignModuleButton[DESIGN_MODULE_COUNT];
        for (int i = 0; i < DESIGN_MODULE_COUNT; i++) {
            designModuleButtons[i] = new DesignModuleButton(0, i * 25, Module.values()[i], clickHandler);
            designModuleButtons[i].setAttachment(GuiAttachment.topLeft);
            addChild(designModuleButtons[i]);
        }
    }

}
