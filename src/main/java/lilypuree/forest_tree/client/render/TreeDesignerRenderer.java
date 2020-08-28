package lilypuree.forest_tree.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.common.trees.customization.TreeDesignerTile;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class TreeDesignerRenderer extends TileEntityRenderer<TreeDesignerTile> {

    public static final ResourceLocation HOLOGRAM_RAY = new ResourceLocation(ForestTree.MODID, "block/hologram_ray_3");
    public static final ResourceLocation HOLOGRAM = new ResourceLocation(ForestTree.MODID, "block/hologram");
    private static TextureAtlasSprite sprite = null;

    public TreeDesignerRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    private static void add(IVertexBuilder renderer, MatrixStack stack, float x, float y, float z, float u, float v) {
        renderer.pos(stack.getLast().getMatrix(), x, y, z)
                .color(1.0f, 1.0f, 1.0f, 1.0f)
                .tex(u, v)
                .lightmap(0, 240)
                .normal(0, 1, 0)
                .endVertex();
    }

    @Override
    public void render(TreeDesignerTile tileEntityIn, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack sapling = tileEntityIn.saplingHandler.getStackInSlot(0);
        if (sapling.isEmpty()) {

        } else {
            long time = System.currentTimeMillis();
            float angle = (time / 50f) % 360;
            Quaternion rotation = Vector3f.YP.rotationDegrees(angle);

            matrixStack.push();
            float scale = 0.6f;
            matrixStack.translate(0.5f * (1 - scale), 0.7f, 0.5f * (1 - scale));
            matrixStack.scale(scale, scale, scale);
            matrixStack.translate(0.5f, 0, 0.5f);
            matrixStack.rotate(rotation);
            matrixStack.translate(-0.5f, 0.0f, -0.5f);

            renderBlockHologram(tileEntityIn.getPos(), ((BlockItem) sapling.getItem()).getBlock().getDefaultState(), tileEntityIn.getWorld(), matrixStack, bufferIn, combinedLightIn, combinedOverlayIn);

            matrixStack.pop();

            addHologramEye(bufferIn, matrixStack);

            int columns = Math.max((int) ((100 - tileEntityIn.getPos().distanceSq(Minecraft.getInstance().player.getPosition())) / 10), 1);

            renderDissectedHologramRays(bufferIn, matrixStack, columns);
        }
    }

    private static float lowerWidthHalf = 2.5f / 16.0f;
    private static float upperWidthHalf = 6 / 16.0f;
    private static float height = 7 / 16.0f;
    private static float actualheight = (float) Math.sqrt((upperWidthHalf - lowerWidthHalf) * (upperWidthHalf - lowerWidthHalf) + height * height);

    private void renderBlockHologram(BlockPos pos, BlockState state, World world, MatrixStack matrix, IRenderTypeBuffer renderTypeBuffer, int combinedLight, int combinedOverlay) {
        BlockRendererDispatcher blockDispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        BlockModelRenderer renderer = blockDispatcher.getBlockModelRenderer();
        IVertexBuilder hologramBuffer = renderTypeBuffer.getBuffer(HologramRenderType.HOLOGRAM);

        renderer.renderModelFlat(world,
                blockDispatcher.getModelForState(state),
                state, pos, matrix, hologramBuffer, false, world.rand, state.getPositionRandom(pos),
                OverlayTexture.NO_OVERLAY,
                net.minecraftforge.client.model.data.EmptyModelData.INSTANCE);

    }

    private void addHologramEye(IRenderTypeBuffer buffer, MatrixStack matrixStack) {
        IVertexBuilder builder = buffer.getBuffer(HologramRenderType.HOLOGRAM);
        sprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(HOLOGRAM);
        float x1 = 0.5f - lowerWidthHalf;
        float x2 = 0.5f + lowerWidthHalf;
        float uwidth = sprite.getMaxU() - sprite.getMinU();
        float vwidth = sprite.getMaxV() - sprite.getMinV();
        float u1 = sprite.getMinU() + uwidth * 5 / 16.0f;
        float u2 = sprite.getMinU() + uwidth * 11 / 16.0f;
        float v1 = sprite.getMinV() + vwidth * 5 / 16.0f;
        float v2 = sprite.getMinV() + vwidth * 11 / 16.0f;
        add(builder, matrixStack, x1, 0.26f, x1, u1, v1);
        add(builder, matrixStack, x1, 0.26f, x2, u1, v2);
        add(builder, matrixStack, x2, 0.26f, x2, u2, v2);
        add(builder, matrixStack, x2, 0.26f, x1, u2, v1);
    }

    private void renderDissectedHologramRays(IRenderTypeBuffer buffer, MatrixStack matrixStack, int columns) {
        IVertexBuilder builder = buffer.getBuffer(RenderType.getTranslucent());
        sprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(HOLOGRAM_RAY);

        float x1 = 0.5f - lowerWidthHalf;
        float x2 = 0.5f + lowerWidthHalf;
        float x3 = 0.5f - upperWidthHalf;
        float x4 = 0.5f + upperWidthHalf;
        float y1 = 0.25f;
        float y2 = 0.25f + height;
        Vector3f vl1 = new Vector3f(x1, y1, x1);
        Vector3f vl2 = new Vector3f(x1, y1, x2);
        Vector3f vl3 = new Vector3f(x2, y1, x2);
        Vector3f vl4 = new Vector3f(x2, y1, x1);
        Vector3f vu1 = new Vector3f(x3, y2, x3);
        Vector3f vu2 = new Vector3f(x3, y2, x4);
        Vector3f vu3 = new Vector3f(x4, y2, x4);
        Vector3f vu4 = new Vector3f(x4, y2, x3);

        float minU = sprite.getMinU();
        float texWidth = sprite.getMaxU() - sprite.getMinU();
        float texHeight = sprite.getMaxV() - sprite.getMinV();
        float u1 = minU + (0.5f - lowerWidthHalf) * texWidth;
        float u2 = minU + (0.5f + lowerWidthHalf) * texWidth;
        float u3 = minU + (0.5f - upperWidthHalf) * texWidth;
        float u4 = minU + (0.5f + upperWidthHalf) * texWidth;
        float minV = Math.min(sprite.getMinV() + actualheight * texHeight, sprite.getMaxV());
        float maxV = sprite.getMinV();


        addDissectedPlanes(builder, matrixStack, vl1, vl2, vu2, vu1, u1, u2, u4, u3, minV, maxV, columns);
        addDissectedPlanes(builder, matrixStack, vl2, vl3, vu3, vu2, u1, u2, u4, u3, minV, maxV, columns);
        addDissectedPlanes(builder, matrixStack, vl3, vl4, vu4, vu3, u1, u2, u4, u3, minV, maxV, columns);
        addDissectedPlanes(builder, matrixStack, vl4, vl1, vu1, vu4, u1, u2, u4, u3, minV, maxV, columns);
    }

    public static void addDissectedPlanes(IVertexBuilder builder, MatrixStack matrixStack, Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, float u1, float u2, float u3, float u4, float minV, float maxV, int columns) {
        float textureWidthLower = u2 - u1;
        float textureWidthUpper = u3 - u4;

        float columnWidthMultiplier = 0.4f;
        float textureColumnWidthLower = textureWidthLower * columnWidthMultiplier;
        float textureColumnWidthUpper = textureWidthUpper * columnWidthMultiplier;
        float x1 = v1.getX();
        float x2 = v2.getX();
        float xWidthLower = x2 - x1;
        float x3 = v3.getX();
        float x4 = v4.getX();
        float xWidthUpper = x3 - x4;
        float z1 = v1.getZ();
        float z2 = v2.getZ();
        float zWidthLower = z2 - z1;
        float z3 = v3.getZ();
        float z4 = v4.getZ();
        float zWidthUpper = z3 - z4;
        float y1 = v1.getY();
        float y2 = v3.getY();
        float columnWidthLowerX = xWidthLower / columns * columnWidthMultiplier;
        float columnWidthUpperX = xWidthUpper / columns * columnWidthMultiplier;
        float columnWidthLowerZ = zWidthLower / columns * columnWidthMultiplier;
        float columnWidthUpperZ = zWidthUpper / columns * columnWidthMultiplier;
        for (int i = 0; i < columns; i++) {
            float k = i / (float) columns;
            Vector3f vc1 = new Vector3f(x1 + xWidthLower * k, y1, z1 + zWidthLower * k);
            Vector3f vc2 = new Vector3f(x1 + xWidthLower * k + columnWidthLowerX, y1, z1 + zWidthLower * k + columnWidthLowerZ);
            Vector3f vc3 = new Vector3f(x4 + xWidthUpper * k + columnWidthUpperX, y2, z4 + zWidthUpper * k + columnWidthUpperZ);
            Vector3f vc4 = new Vector3f(x4 + xWidthUpper * k, y2, z4 + zWidthUpper * k);
            float uc1 = u1 + k * textureWidthLower;
            float uc2 = u1 + k * textureWidthLower + textureColumnWidthLower;
            float uc3 = u4 + k * textureWidthUpper + textureColumnWidthUpper;
            float uc4 = u4 + k * textureWidthUpper;
            addFullPlane(builder, matrixStack, vc1, vc2, vc3, vc4, uc1, uc2, uc3, uc4, minV, maxV);
        }
    }

    public static void addFullPlane(IVertexBuilder builder, MatrixStack matrixStack, Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, float u1, float u2, float u3, float u4, float minV, float maxV) {
        add(builder, matrixStack, v1.getX(), v1.getY(), v1.getZ(), u1, minV);
        add(builder, matrixStack, v2.getX(), v2.getY(), v2.getZ(), u2, minV);
        add(builder, matrixStack, v3.getX(), v3.getY(), v3.getZ(), u3, maxV);
        add(builder, matrixStack, v4.getX(), v4.getY(), v4.getZ(), u4, maxV);

        add(builder, matrixStack, v1.getX(), v1.getY(), v1.getZ(), u1, minV);
        add(builder, matrixStack, v4.getX(), v4.getY(), v4.getZ(), u4, maxV);
        add(builder, matrixStack, v3.getX(), v3.getY(), v3.getZ(), u3, maxV);
        add(builder, matrixStack, v2.getX(), v2.getY(), v2.getZ(), u2, minV);
    }

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(Registration.TREE_DESIGNER_TILE.get(), TreeDesignerRenderer::new);
    }

    @Override
    public boolean isGlobalRenderer(TreeDesignerTile te) {
        return true;
    }
}
