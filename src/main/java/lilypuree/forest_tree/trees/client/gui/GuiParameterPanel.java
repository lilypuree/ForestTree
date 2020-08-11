package lilypuree.forest_tree.trees.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import lilypuree.forest_tree.client.gui.GuiExtended;
import lilypuree.forest_tree.client.gui.GuiScrollBar;
import lilypuree.forest_tree.trees.world.gen.feature.parametric.Module;
import lilypuree.forest_tree.trees.world.gen.feature.parametric.Parameter;
import lilypuree.forest_tree.trees.world.gen.feature.parametric.TreeGenParamData;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import se.mickelus.mgui.gui.GuiElement;

public class GuiParameterPanel extends GuiExtended {

    private TreeGenParamData paramData;

    private GuiElement background;
    private GuiScrollBar scrollBar;
    private GuiExtended parameterList;
    private GuiParameterElement selected = null;

    private final int barWidth = 6;

    private int elementHeight = 18;

    public GuiParameterPanel(int x, int y, int width, int height, TreeGenParamData paramData) {
        super(x, y, width, height);

        this.paramData = paramData;

        this.scrollBar = new GuiScrollBar(width - barWidth , 0, barWidth, height, getContentHeight(0));
        scrollBar.setScrollAmount(elementHeight / 2);
        addChild(scrollBar);

        parameterList = new GuiExtended(0,0 , width - barWidth, height);
        addChild(parameterList);
    }

    private int getContentHeight(int size) {
        int tempHeight = 10;
        tempHeight += size;
        if (tempHeight < height) {
            tempHeight = height;
        }
        return tempHeight;
    }

    public void setModule(Module module) {
        parameterList.clearChildren();
        if (module == null) {
            scrollBar.setContentHeight(getContentHeight(0));
            scrollBar.setScrollAmount(elementHeight/2);
            return;
        }
        int index = 0;
        int height = 0;
        for (Parameter parameter : Parameter.parameters[module.index]) {
            int finalIndex = index;
            int elementHeight = parameter.type==2 ? (int)(this.elementHeight * 2.5f) : this.elementHeight;
            GuiParameterElement element = new  GuiParameterElement(0, height, parameterList.getWidth(), elementHeight, parameter, paramData, () -> setSelected(finalIndex));
            parameterList.addChild(element);

            height += elementHeight;
            index++;
        }
        scrollBar.setContentHeight(getContentHeight(height));
        scrollBar.setScrollAmount(elementHeight/2);
        parameterList.setY(-scrollBar.getScrollAmount());
    }

    public void setSelected(int index) {
        GuiParameterElement newSelected = (GuiParameterElement) parameterList.getChild(index);
        if (this.selected != newSelected) {
            if (this.selected != null) {
                this.selected.removeSelection();
            }
            this.selected = newSelected;
//            selectionBox.clearChildren();
//            GuiRect selection = new GuiRect(0, 0, 3, 3, 0xFBAC76);
//            selection.setAttachmentAnchor(GuiAttachment.middleCenter);
//            selection.setAttachment(GuiAttachment.middleRight);
//            selection.setY(selected.getY());
//            selectionBox.addChild(selection);
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (selected != null) {
            return selected.keyPressed(keyCode, scanCode, modifiers);
        }
        return false;
    }

    public boolean charTyped(char typedChar) {
        if (selected != null) {
            return selected.charTyped(typedChar);
        }
        return false;
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double scroll) {
        scrollBar.scroll(scroll);
    }

    @Override
    public void draw(MatrixStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        int baseY = -(int) scrollBar.getScrollDistance();
        parameterList.setY(baseY);
        MainWindow mainWindow = Minecraft.getInstance().getMainWindow();
        double scale = mainWindow.getGuiScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) ((refX + this.x) * scale), (int) (mainWindow.getFramebufferHeight() - ((refY + this.y + height) * scale)),
                (int) ((width) * scale), (int) (height * scale));

        super.draw(matrixStack, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public void tick() {
        if (selected != null) {
            selected.tick();
        }
    }
}
