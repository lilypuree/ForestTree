package lilypuree.forest_tree.trees.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.core.network.ForestTreePacketHandler;
import lilypuree.forest_tree.core.network.PacketSaveParams;
import lilypuree.forest_tree.client.gui.GuiExtended;
import lilypuree.forest_tree.client.gui.GuiResizableTexture;
import lilypuree.forest_tree.trees.customization.TreeDesignerContainer;
import lilypuree.forest_tree.trees.world.gen.feature.parametric.Module;
import lilypuree.forest_tree.trees.world.gen.feature.parametric.TreeGenParamData;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;
import se.mickelus.mgui.gui.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class TreeDesignerScreen extends ContainerScreen<TreeDesignerContainer> {
    private static final ResourceLocation backgroundTexture = new ResourceLocation(ForestTree.MODID, "textures/gui/container/tree_designer.png");

    private GuiExtended defaultGui;

    private GuiDesignModules designModules;
    private Module selectedModule;

    private GuiElement background;
    private GuiParameterPanel parameterPanel;
    private int panelBorder = 2;

    private TreeGenParamData paramData;

    public TreeDesignerScreen(TreeDesignerContainer container, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(container, playerInventory, titleIn);
    }

    @Override
    protected void init() {
        paramData = getParametersFromStack(container.inventorySlots.get(0).getStack());
        this.xSize = 320;
        this.ySize = 180;
        defaultGui = new GuiExtended(0, 0, xSize, ySize);
        defaultGui.addChild(new GuiResizableTexture(0, 0, xSize, ySize, 0, 0, 400, 256, backgroundTexture));

        defaultGui.addChild(new GuiString(15, 10, new TranslationTextComponent(ForestTree.MODID+".treedesignergui.title").getFormattedText()).setShadow(true));
        designModules = new GuiDesignModules(15, 25, this::changeModule);
        defaultGui.addChild(designModules);

        configurePanelBackground();

        parameterPanel = new GuiParameterPanel(50 + panelBorder, 20 + panelBorder, xSize - 50 - 15 - panelBorder *2, ySize - 20*2 - panelBorder *2, paramData);
        defaultGui.addChild(parameterPanel);

        defaultGui.addChild(new GuiButton(15, 160, new TranslationTextComponent("forest_tree.treedesignergui.save").getFormattedText(), this::save));
        super.init();
    }

    private void configurePanelBackground(){
        int backgroundheight = ySize - 20*2;
        int backgroundwidth = xSize - 50 - 15;
        background = new GuiElement(50, 20, backgroundwidth,backgroundheight);
        GuiRect transparentScreen = new GuiRect(0,0, backgroundwidth, backgroundheight, 0x111111);
        transparentScreen.setOpacity(0.7f);
        GuiRect left = new GuiRect(50, 20, 1, backgroundheight, 0x000000);
        GuiRect right = new GuiRect(0, 0, 1, backgroundheight, 0x000000);
        GuiRect top = new GuiRect(50, 20, 1, backgroundheight, 0x000000);
        GuiRect bottom = new GuiRect(0 , 0, backgroundwidth, 1, 0x000000);
        right.setAttachment(GuiAttachment.middleRight);
        bottom.setAttachmentAnchor(GuiAttachment.bottomCenter);
        bottom.setAttachmentPoint(GuiAttachment.bottomCenter);
        background.addChild(transparentScreen);
        background.addChild(bottom);
        background.addChild(right);
        background.setVisible(false);
        defaultGui.addChild(background);
    }

    private TreeGenParamData getParametersFromStack(ItemStack stack) {
        CompoundNBT compound = stack.getOrCreateChildTag("BlockEntityTag");
        return TreeGenParamData.deserializeNbt(compound);
    }

    private CompoundNBT writeNewParameters(CompoundNBT compound) {
        compound.put("BlockEntityTag", paramData.writeToNbt(new CompoundNBT()));
        return compound;
    }

    private void changeModule(Module module) {
        if(paramData == null){
            paramData = getParametersFromStack(container.inventorySlots.get(0).getStack());
            if(paramData == null){
                background.setVisible(false);
                return;
            }
            parameterPanel.remove();
            parameterPanel = new GuiParameterPanel(50 + panelBorder, 20 + panelBorder, xSize - 50 - 15 - panelBorder *2, ySize - 20*2 - panelBorder *2, paramData);
            defaultGui.addChild(parameterPanel);
        }
        selectedModule = module;
        parameterPanel.setModule(module);
        parameterPanel.setVisible(module != null);
        background.setVisible(module != null);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void onClose() {
        save();
        super.onClose();
    }

    public void save(){
        ItemStack sapling = container.getSlot(0).getStack();
        if(sapling.getItem() == Registration.CUSTOM_SAPLING_ITEM.get() && paramData != null){
            CompoundNBT compound = paramData.writeToNbt(new CompoundNBT());
            ForestTreePacketHandler.sendToServer(new PacketSaveParams(this.container.getPos(),compound ));
            ItemStack newStack = new ItemStack(sapling.getItem());
            newStack.setTagInfo("BlockEntityTag", compound);
            container.getSlot(0).putStack(newStack);
        }
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
