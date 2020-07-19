package lilypuree.forest_tree.trees.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.client.HologramRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ILightReader;
import net.minecraft.world.World;
import net.minecraftforge.client.extensions.IForgeVertexBuilder;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.lwjgl.system.MemoryStack;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import static org.lwjgl.opengl.GL14C.GL_FUNC_ADD;

public class BlockHologramRenderer extends BlockModelRenderer {

    private static BlockHologramRenderer INSTANCE;

    public BlockHologramRenderer(BlockModelRenderer baseRenderer) {
        super(baseRenderer.blockColors);
    }

    public static BlockHologramRenderer getInstance(BlockModelRenderer baseRenderer) {
        if (INSTANCE == null || INSTANCE.blockColors != baseRenderer.blockColors) {
            INSTANCE = new BlockHologramRenderer(baseRenderer);
        }

        return INSTANCE;
    }

    @Override
    public boolean renderModel(ILightReader worldIn, IBakedModel modelIn, BlockState stateIn, BlockPos posIn, MatrixStack matrixIn, IVertexBuilder buffer, boolean checkSides, Random randomIn, long rand, int combinedOverlayIn, IModelData modelData) {
        return super.renderModel(worldIn, modelIn, stateIn, posIn, matrixIn, buffer, checkSides, randomIn, rand, combinedOverlayIn, modelData);
    }

    public static void renderBlockHologram(BlockPos pos, BlockState state, World world, MatrixStack matrix, IRenderTypeBuffer renderTypeBuffer, int combinedLight, int combinedOverlay) {
        BlockRendererDispatcher blockDispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        BlockModelRenderer renderer = getInstance(blockDispatcher.getBlockModelRenderer());
        IVertexBuilder hologramBuffer = renderTypeBuffer.getBuffer(HologramRenderType.HOLOGRAM);

        renderer.renderModelFlat(
                world,
                blockDispatcher.getModelForState(state),
                state,
                pos,
                matrix,
                hologramBuffer,
                false,
                world.rand,
                state.getPositionRandom(pos),
                OverlayTexture.NO_OVERLAY,
                net.minecraftforge.client.model.data.EmptyModelData.INSTANCE);

    }


}
