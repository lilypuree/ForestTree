package lilypuree.forest_tree.trees.client.gui;

import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.client.gui.GuiExtended;
import lilypuree.forest_tree.client.gui.GuiTextFieldWidget;
import lilypuree.forest_tree.world.trees.gen.feature.parametric.Parameter;
import lilypuree.forest_tree.world.trees.gen.feature.parametric.TreeGenParamData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TranslationTextComponent;
import se.mickelus.mgui.gui.GuiString;

import java.util.Collections;
import java.util.List;

public class GuiParameterElement extends GuiExtended {

    private Runnable onClickHandler;
    private Parameter parameter;
    private TreeGenParamData paramData;
    private GuiTextFieldWidget textFieldWidgetFocused;
    private boolean showToolTip;

    private static final int TEXT_LENGTH =130;
    private static final int HELP_WIDTH = 10;

    public GuiParameterElement(int x, int y, int width, int height, Parameter parameter, TreeGenParamData paramData, Runnable onClickHandler) {
        super(x, y, width, height);

        this.parameter = parameter;
        this.paramData = paramData;
        this.onClickHandler = onClickHandler;

        String paramName = new TranslationTextComponent(ForestTree.MODID + ".treedesignergui.parameters."+parameter.name).getFormattedText();
        this.addChild(new GuiString(1, 0, paramName).setShadow(true));
        this.addChild(new GuiString(width-HELP_WIDTH, 0, HELP_WIDTH, "?"){
            @Override
            public List<String> getTooltipLines() {
                return showToolTip ? Collections.singletonList(new TranslationTextComponent(ForestTree.MODID+".treedesignergui.descriptions."+parameter.name).getFormattedText()) : null;
            }
        });
        this.initTextWidgets();

    }

    private void initTextWidgets() {
        if (parameter.type == 2) {
            float widgetWidth = (width-TEXT_LENGTH - HELP_WIDTH)/4.0f;
            for (int i = 0; i < 16; i++) {
                GuiTextFieldWidget textFieldWidget = new GuiTextFieldWidget(Minecraft.getInstance().fontRenderer, (int)(TEXT_LENGTH + (i % 4) * widgetWidth), (i / 4) * 10, (int)widgetWidth, 10);
                initTextFieldWidgetArray(textFieldWidget, i);
                if (i == 0) textFieldWidgetFocused = textFieldWidget;
                addChild(textFieldWidget);
            }
        } else {
            GuiTextFieldWidget textFieldWidget = new GuiTextFieldWidget(Minecraft.getInstance().fontRenderer, TEXT_LENGTH, 0, Math.max(0, width - TEXT_LENGTH), 10);
            initTextFieldWidgetSimple(textFieldWidget);
            textFieldWidgetFocused = textFieldWidget;
            addChild(textFieldWidget);
        }
    }

    private void initTextFieldWidgetSimple(GuiTextFieldWidget textFieldWidget) {
        switch (parameter.type) {
            case 2:
                return;
            case 0:
                textFieldWidget.setValidator(string -> string.matches("\\d*"));
                textFieldWidget.setResponder(s -> {
                            try {
                                paramData.setIntegerValue(parameter, Integer.parseInt(s));
                            } catch (NumberFormatException ignored) {

                            }
                        }
                );
                textFieldWidget.setText(Integer.toString(paramData.getIntParameter(parameter)));
                break;
            case 1:
                textFieldWidget.setValidator(string -> string.matches("-?\\d*+(\\.\\d*)?"));
                textFieldWidget.setResponder(s -> {
                    try {
                        paramData.setFloatValue(parameter, Float.parseFloat(s));
                    } catch (NumberFormatException ignored) {

                    }
                });
                textFieldWidget.setText(Float.toString(paramData.getFloatParameter(parameter)));
        }
//        initTextFieldWidgetClickHandler(textFieldWidget);
    }

    private void initTextFieldWidgetArray(GuiTextFieldWidget textFieldWidget, int index) {
        if (parameter.type != 2) return;
        textFieldWidget.setValidator(string -> string.matches("-?\\d?+(\\.\\d*)?"));
        textFieldWidget.setResponder(s -> {
            try {
                paramData.setFloatArrayEntry(parameter, index, Float.parseFloat(s));
            } catch (NumberFormatException ignored) {

            }
        });
        textFieldWidget.setText(Float.toString(paramData.getFloatArrayEntry(parameter, index)));
        initTextFieldWidgetClickHandler(textFieldWidget);
    }

    private void initTextFieldWidgetClickHandler(GuiTextFieldWidget textFieldWidget) {
        textFieldWidget.setOnClickHandler(() -> {
            if (textFieldWidget != textFieldWidgetFocused || !textFieldWidgetFocused.isSelected()) {
                textFieldWidgetFocused.setSelected(false);
                textFieldWidget.setSelected(true);
                textFieldWidgetFocused = textFieldWidget;
            }
        });
    }


    @Override
    public boolean onClick(int x, int y) {
        super.onClick(x, y);
        if (this.hasFocus()) {
//            this.textFieldWidget.setHasFocus(true);
            this.onClickHandler.run();
            return true;
        } else {
            removeSelection();
            return false;
        }
    }

    @Override
    protected void calculateFocusState(int refX, int refY, int mouseX, int mouseY) {
        super.calculateFocusState(refX, refY, mouseX, mouseY);

        boolean gainFocus = mouseX >= this.getX()+refX+width-HELP_WIDTH && mouseX < this.getX() + refX + this.getWidth() && mouseY >= this.getY() + refY && mouseY < this.getY() + refY + this.getHeight();
        if (gainFocus != this.showToolTip) {
            this.showToolTip = gainFocus;
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean flag = textFieldWidgetFocused.keyPressed(keyCode, scanCode, modifiers);
        if (keyCode == 13) {
            return true;
        }
        return flag;
    }

    public boolean charTyped(char typedChar) {
        return textFieldWidgetFocused.charTyped(typedChar);
    }


    public void tick() {
        if (textFieldWidgetFocused.isSelected())
            textFieldWidgetFocused.tick();
    }

    public void removeSelection() {
        this.textFieldWidgetFocused.setSelected(false);
    }
}
