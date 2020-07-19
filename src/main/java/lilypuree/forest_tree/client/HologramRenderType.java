package lilypuree.forest_tree.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.opengl.GL14C;

import static org.lwjgl.opengl.GL14C.GL_FUNC_ADD;

public class HologramRenderType extends RenderType {

    public HologramRenderType(String nameIn, VertexFormat vertexFormatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, vertexFormatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static final RenderState.TransparencyState HOLOGRAM_TRANSPARENCY = new RenderState.TransparencyState("hologram_transparency", ()->{
        RenderSystem.enableBlend();
        RenderSystem.blendColor(77/256.0f, 154/256.0f,128/256.0f,1.0f);
        RenderSystem.blendEquation(GL_FUNC_ADD);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.CONSTANT_COLOR);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    public static final RenderType HOLOGRAM = makeType("hologram", DefaultVertexFormats.BLOCK, 7, 256, true, false,
            RenderType.State.getBuilder().shadeModel(RenderState.SHADE_DISABLED).lightmap(RenderState.LIGHTMAP_DISABLED).texture(RenderState.BLOCK_SHEET).transparency(HOLOGRAM_TRANSPARENCY).alpha(RenderState.HALF_ALPHA).build(false));
}
