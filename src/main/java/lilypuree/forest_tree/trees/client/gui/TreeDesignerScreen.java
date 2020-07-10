package lilypuree.forest_tree.trees.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.gui.GuiExtended;
import lilypuree.forest_tree.trees.customization.TreeDesignerContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;
import se.mickelus.mgui.gui.GuiElement;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class TreeDesignerScreen extends ContainerScreen<TreeDesignerContainer> {
    private static final ResourceLocation backgroundTexture = new ResourceLocation(ForestTree.MODID, "textures/gui/container/tree_designer.png");

    private GuiExtended defaultGui;
    private final TreeDesignerContainer container;

    private GuiDesignModules designModules;
    private Module selectedModule;

    private GuiParameterPanel parameterPanel;

    private Map<String, Float> parameters;

    public TreeDesignerScreen(TreeDesignerContainer container, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(container, playerInventory, titleIn);

        this.xSize = 320;
        this.ySize = 240;
        this.container = container;
//        defaultGui = new GuiExtended(40, 70, width, height);
        defaultGui = new GuiExtended(40, 60, width, height);
//        defaultGui.addChild(new GuiTextureOffset(134, 40, 179, 150, backgroundTexture));
        designModules = new GuiDesignModules(0, 0, this::changeModule);
        defaultGui.addChild(designModules);

        parameterPanel = new GuiParameterPanel(30, 0,240, 100);
        defaultGui.addChild(parameterPanel);
    }

    private void changeModule(Module module) {
        selectedModule = module;
        parameterPanel.setModule(module);
        parameterPanel.setVisible(module != null);
    }

    @Override
    public void tick() {
        super.tick();
        parameterPanel.tick();
    }

    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
        drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        defaultGui.draw(new MatrixStack(), x, y, width, height, mouseX, mouseY, 1);
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        super.renderHoveredToolTip(mouseX, mouseY);
        List<String> tooltipLines = defaultGui.getTooltipLines();
        if (tooltipLines != null) {
            tooltipLines = tooltipLines.stream()
                    .map(line -> line.replace("\\n", "\n"))
                    .flatMap(line -> Arrays.stream(line.split("\n")))
                    .collect(Collectors.toList());

            GuiUtils.drawHoveringText(tooltipLines, mouseX, mouseY, width, height, -1, minecraft.fontRenderer);
        }
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return !parameterPanel.keyPressed(keyCode, scanCode, modifiers) && super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char typedChar, int p_charTyped_2_) {
        return !parameterPanel.charTyped(typedChar) && super.charTyped(typedChar, p_charTyped_2_);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        return defaultGui.onClick((int) mouseX, (int) mouseY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);

        defaultGui.mouseReleased((int) mouseX, (int) mouseY);

        return true;
    }

    @Override
    public boolean mouseDragged(double x1, double y1, int button, double x2, double y2) {
        if (isValidClickButton(button)) {
            defaultGui.mouseDragged(x1, y1, x2, y2);
        }
        return super.mouseDragged(x1, y1, button, x2, y2);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        defaultGui.mouseScrolled(mouseX, mouseY, scroll);
        return super.mouseScrolled(mouseX, mouseY, scroll);
    }

    protected boolean isValidClickButton(int button) {
        return button == 0;
    }
}
