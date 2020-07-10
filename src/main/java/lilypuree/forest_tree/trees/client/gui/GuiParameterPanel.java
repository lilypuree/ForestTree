package lilypuree.forest_tree.trees.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import lilypuree.forest_tree.gui.GuiExtended;
import lilypuree.forest_tree.gui.GuiScrollBar;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import se.mickelus.mgui.gui.GuiAttachment;
import se.mickelus.mgui.gui.GuiElement;
import se.mickelus.mgui.gui.GuiRect;

import java.util.Map;

public class GuiParameterPanel extends GuiExtended {

    private Map<String, Float> paramMap;

    private GuiScrollBar scrollBar;
    private GuiExtended parameterList;
    private GuiParameterElement selected = null;
    private GuiElement selectionBox;

    private final int barWidth = 6;

    private int elementHeight = 18;

    public GuiParameterPanel(int x, int y, int width, int height, Map<String, Float> paramMap) {
        super(x, y, width, height);

        this.paramMap = paramMap;

        this.scrollBar = new GuiScrollBar(width - barWidth, 0, barWidth, height, getContentHeight(0));
        scrollBar.setScrollAmount(elementHeight / 2);
        addChild(scrollBar);

        parameterList = new GuiExtended(0, 0, width - barWidth, height);
        addChild(parameterList);

        selectionBox = new GuiElement(0, 0, 10, 10);
        addChild(selectionBox);


    }

    private int getContentHeight(int size) {
        int tempHeight = 30;
        tempHeight += size * elementHeight;
        if (tempHeight < height - 8) {
            tempHeight = height - 8;
        }
        return tempHeight;
    }

    public void setModule(Module module) {
        selectionBox.clearChildren();
        parameterList.clearChildren();
        if (module == null) {
            scrollBar.setContentHeight(getContentHeight(0));
            return;
        }
        scrollBar.setContentHeight(getContentHeight(module.getParameterCount()));
        int index = 0;
        for (String name : module.getParameterNames()) {
            int finalIndex = index;
            boolean isInt = name.equals("maxHeight") || name.equals("minimumBranchAge");
            GuiParameterElement element = new GuiParameterElement(0, index * elementHeight, parameterList.getWidth(), elementHeight, name,
                    s -> {
                        try {
                            if (isInt) {
                                int num = Integer.parseInt(s);
                                paramMap.put(name, (float) num);
                            } else {
                                float num = Float.parseFloat(s);
                                paramMap.put(name, num);
                            }
                        } catch (NumberFormatException ignored){

                        }
                    },
                    () -> setSelected(finalIndex), isInt
            );
            parameterList.addChild(element);
            index++;
        }
    }

    public void setSelected(int index) {
        GuiParameterElement newSelected = (GuiParameterElement) parameterList.getChild(index);
        if (this.selected != newSelected) {
            if (this.selected != null) {
                this.selected.removeSelection();
            }
            this.selected = newSelected;
            selectionBox.clearChildren();
            GuiRect selection = new GuiRect(0, 0, 3, 3, 0xFBAC76);
            selection.setAttachmentAnchor(GuiAttachment.middleCenter);
            selection.setAttachment(GuiAttachment.middleRight);
            selection.setY(selected.getY());
            selectionBox.addChild(selection);
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
        selectionBox.setY(baseY);

        MainWindow mainWindow = Minecraft.getInstance().getMainWindow();
        double scale = mainWindow.getGuiScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) ((refX + this.x) * scale), (int) (mainWindow.getFramebufferHeight() - ((refY + height) * scale)),
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
