package lilypuree.forest_tree.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import se.mickelus.mgui.gui.GuiTexture;

public class GuiResizableTexture extends GuiTexture {

    private int textureWidth;
    private int textureHeight;

    public GuiResizableTexture(int x, int y, int width, int height, ResourceLocation textureLocation) {
        this(x, y, width, height, 0, 0, textureLocation);
    }

    public GuiResizableTexture(int x, int y, int width, int height, int textureX, int textureY, ResourceLocation textureLocation) {
        this(x, y, width, height, textureX, textureY, 256, 256, textureLocation);
    }

    public GuiResizableTexture(int x, int y, int width, int height, int textureX, int textureY, int textureWidth, int textureHeight, ResourceLocation textureLocation) {
        super(x, y, width, height, textureX, textureY, textureLocation);
        this.textureHeight = textureHeight;
        this.textureWidth = textureWidth;
    }

    public void setTextureSize(int textureWidth, int textureHeight){
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    public void draw(MatrixStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        this.calculateFocusState(refX, refY, mouseX, mouseY);
        this.drawChildren(matrixStack, refX + this.x, refY + this.y, screenWidth, screenHeight, mouseX, mouseY, opacity * this.opacity);
        drawResizableTexture(matrixStack, this.textureLocation, refX + this.x, refY + this.y, this.width, this.height, this.textureX, this.textureY, this.textureWidth, this.textureHeight, this.color, this.getOpacity() * opacity);
    }

    protected static void drawResizableTexture(MatrixStack matrixStack, ResourceLocation textureLocation, int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, int color, float opacity) {
        RenderSystem.pushMatrix();
        Minecraft.getInstance().getTextureManager().bindTexture(textureLocation);
        RenderSystem.color4f((float)(color >> 16 & 255) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F, opacity);
        RenderSystem.enableBlend();
        RenderSystem.enableAlphaTest();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(matrixStack.getLast().getMatrix(), (float)x, (float)(y + height), 0.0F).tex((float)u / textureWidth, (float)(v + height) / textureHeight).endVertex();
        buffer.pos(matrixStack.getLast().getMatrix(), (float)(x + width), (float)(y + height), 0.0F).tex((float)(u + width) / textureWidth, (float)(v + height) / textureHeight).endVertex();
        buffer.pos(matrixStack.getLast().getMatrix(), (float)(x + width), (float)y, 0.0F).tex((float)(u + width) /textureWidth, (float)v / textureHeight).endVertex();
        buffer.pos(matrixStack.getLast().getMatrix(), (float)x, (float)y, 0.0F).tex((float)u / textureWidth, (float)v / textureHeight).endVertex();
        tessellator.draw();
        RenderSystem.popMatrix();
    }



}
