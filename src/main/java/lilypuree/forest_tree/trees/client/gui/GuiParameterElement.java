package lilypuree.forest_tree.trees.client.gui;

import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.gui.GuiExtended;
import lilypuree.forest_tree.gui.GuiTextFieldWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import se.mickelus.mgui.gui.*;

import java.util.function.Consumer;

public class GuiParameterElement extends GuiExtended {

    private Runnable onClickHandler;
    private GuiTextFieldWidget textFieldWidget;
    private Consumer<String> onEnterPressed;


    public GuiParameterElement(int x, int y, int width, int height, String name, Consumer<String> responder, Runnable onClickHandler, boolean isInt) {
        super(x, y, width, height);
        this.onEnterPressed = responder;
        this.onClickHandler = onClickHandler;


//        String paramName = new TranslationTextComponent(ForestTree.MODID + ".parameters."+name).getKey();
        String paramName = new TranslationTextComponent(name).getKey();
        this.addChild(new GuiString(0, 0, paramName).setShadow(true));

        textFieldWidget = new GuiTextFieldWidget(Minecraft.getInstance().fontRenderer, 100, 0, Math.max(0, width - 100) , 10);
        if(isInt){
            textFieldWidget.setValidator(string -> string.matches("\\d?"));
        }else {
            textFieldWidget.setValidator(string -> string.matches("-?\\d?+(\\.\\d*)?"));
        }
        textFieldWidget.setResponder(responder);
        this.addChild(textFieldWidget);
    }

    public void setValue(String value) {
        textFieldWidget.setText(value);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean flag = textFieldWidget.keyPressed(keyCode, scanCode, modifiers);
        if (keyCode == 13) {
            onEnterPressed.accept(textFieldWidget.getText());
            return true;
        }
        return flag;
    }

    public boolean charTyped(char typedChar) {
        return textFieldWidget.charTyped(typedChar);
    }

    @Override
    public boolean onClick(int x, int y) {
        super.onClick(x, y);
        if (this.hasFocus()) {
            this.textFieldWidget.setHasFocus(true);
            this.onClickHandler.run();
            return true;
        } else {
            return false;
        }
    }

    public void tick() {
        this.textFieldWidget.tick();
    }

    public void removeSelection(){
        this.textFieldWidget.setHasFocus(false);
    }
}
