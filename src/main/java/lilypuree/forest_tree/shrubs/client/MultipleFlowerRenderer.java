//package lilypuree.forest_tree.shrubs.client;
//
//import com.mojang.blaze3d.matrix.MatrixStack;
//import com.mojang.blaze3d.vertex.IVertexBuilder;
//import lilypuree.forest_tree.Registration;
//import lilypuree.forest_tree.shrubs.block.MultipleFlowerTile;
//import lilypuree.forest_tree.trees.client.render.TreeDesignerRenderer;
//import net.minecraft.block.BlockState;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.BlockRendererDispatcher;
//import net.minecraft.client.renderer.IRenderTypeBuffer;
//import net.minecraft.client.renderer.LightTexture;
//import net.minecraft.client.renderer.RenderTypeLookup;
//import net.minecraft.client.renderer.model.BakedQuad;
//import net.minecraft.client.renderer.model.IBakedModel;
//import net.minecraft.client.renderer.model.ItemOverrideList;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
//import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
//import net.minecraft.item.BlockItem;
//import net.minecraft.item.ItemStack;
//import net.minecraft.state.properties.BlockStateProperties;
//import net.minecraft.state.properties.DoubleBlockHalf;
//import net.minecraft.util.Direction;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.math.Vec2f;
//import net.minecraft.util.math.Vec3i;
//import net.minecraft.world.ILightReader;
//import net.minecraft.world.LightType;
//import net.minecraft.world.World;
//import net.minecraftforge.client.model.ModelDataManager;
//import net.minecraftforge.client.model.data.EmptyModelData;
//import net.minecraftforge.client.model.data.IDynamicBakedModel;
//import net.minecraftforge.client.model.data.IModelData;
//import net.minecraftforge.fml.client.registry.ClientRegistry;
//import net.minecraftforge.items.ItemStackHandler;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//import java.util.*;
//
//public class MultipleFlowerRenderer extends TileEntityRenderer<MultipleFlowerTile> {
//
//    private Vec2f centerPoint;
//    private Vec2f[] cornerpoints;
//    private Vec2f[] centralpoints;
//
//    public MultipleFlowerRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
//        super(rendererDispatcherIn);
//
//        centerPoint = new Vec2f(0.0f, 0.0f);
//        cornerpoints = new Vec2f[8];
//        centralpoints = new Vec2f[4];
//        for (int i = 0; i < 2; i++) {
//            for (int j = 0; j < 2; j++) {
//                centralpoints[i * 2 + j] = new Vec2f(-0.25f + 0.5f * i, -0.25f + 0.5f * j);
//            }
//        }
//        int k = 0;
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                if (i == 1 && j == 1) continue;
//                cornerpoints[k++] = new Vec2f(-1 / 3f + 1 / 3f * i, -1 / 3f + 1 / 3f * j);
//            }
//        }
//    }
//
//    @Override
//    public void render(MultipleFlowerTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
//        List<BlockState> flowerList = tileEntityIn.flowerList;
//        int flowers = flowerList.size();
//        Random rand = tileEntityIn.getWorld().getRandom();
//        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
//        World world = tileEntityIn.getWorld();
//        BlockPos pos = tileEntityIn.getPos();
//        if (tileEntityIn.offsets.isEmpty() && flowers > 0) {
//            if (flowers > 1 && flowers < 5) {
//                Vec2f[] newPositions = pickNRandom(centralpoints, flowers);
//                for (int i = 0; i < flowers; i++) {
//                    tileEntityIn.offsets.add(getRandomOffset(newPositions[i], 0.2f, rand));
//                }
//            } else {
//                Vec2f center = getRandomOffset(centerPoint, 0.3f, rand);
//                tileEntityIn.offsets.add(center);
//                if (flowers == 5) {
//                    Vec2f[] newPositions = pickNRandom(centralpoints, flowers - 1);
//                    for (int i = 0; i < 4; i++) {
//                        tileEntityIn.offsets.add(getRandomOffset(newPositions[i], 0.2f, rand));
//                    }
//                } else {
//                    Vec2f[] newPositions = pickNRandom(cornerpoints, flowers - 1);
//                    for (int i = 0; i < flowers - 1; i++) {
//                        tileEntityIn.offsets.add(getRandomOffset(newPositions[i], 1 / 6f, rand));
//                    }
//                }
//            }
//        }
//        boolean flag = Minecraft.isAmbientOcclusionEnabled() && tileEntityIn.getBlockState().getLightValue(world, pos) == 0;
//        for (int i = 0; i < flowers; i++) {
//            matrixStackIn.push();
//            Vec2f offset = tileEntityIn.offsets.get(i);
//            matrixStackIn.translate(offset.x, 0, offset.y);
//            BlockState state = flowerList.get(i);
//            IBakedModel model = dispatcher.getBlockModelShapes().getModel(state);
//            IModelData data = model.getModelData(world, pos, state, EmptyModelData.INSTANCE);
//            IVertexBuilder vertexBuilder = bufferIn.getBuffer(RenderTypeLookup.getChunkRenderType(state));
////            dispatcher.getBlockModelRenderer().renderModel(world, model, state, pos, matrixStackIn, vertexBuilder, true, world.rand, 0, OverlayTexture.NO_OVERLAY, data);
//
//            int c = Minecraft.getInstance().getBlockColors().getColor(state, null, null, 0);
//            float f = (float) (c >> 16 & 255) / 255.0F;
//            float f1 = (float) (c >> 8 & 255) / 255.0F;
//            float f2 = (float) (c & 255) / 255.0F;
//            dispatcher.getBlockModelRenderer().renderModel(matrixStackIn.getLast(), vertexBuilder, state, model, f, f1, f2, combinedLightIn, combinedOverlayIn, data);
//            if (state.has(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
//                matrixStackIn.translate(0, 1.0f, 0);
//                BlockPos up = pos.up();
//                BlockState upperState = state.with(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER);
//                IBakedModel upperModel = dispatcher.getBlockModelShapes().getModel(upperState);
//                IModelData upperData = model.getModelData(world, up, upperState, EmptyModelData.INSTANCE);
//                dispatcher.getBlockModelRenderer().renderModel(matrixStackIn.getLast(), vertexBuilder, upperState, upperModel, f, f1, f2, combinedLightIn, combinedOverlayIn, upperData);
//            }
//            matrixStackIn.pop();
//        }
//    }
//
//    public static Vec2f[] pickNRandom(Vec2f[] array, int n) {
//
//        List<Vec2f> list = new ArrayList<Vec2f>(array.length);
//        list.addAll(Arrays.asList(array));
//        Collections.shuffle(list);
//
//        Vec2f[] answer = new Vec2f[n];
//        for (int i = 0; i < n; i++)
//            answer[i] = list.get(i);
////        Arrays.sort(answer);
//
//        return answer;
//
//    }
//
//    private static Vec2f getRandomOffset(Vec2f cen, float dispersion, Random rand) {
//        float xVar = MathHelper.clamp(rand.nextFloat() * dispersion * 2 - dispersion, -dispersion, +dispersion);
//        float yVar = MathHelper.clamp(rand.nextFloat() * dispersion * 2 - dispersion, -dispersion, +dispersion);
//        return new Vec2f(cen.x + xVar, cen.y + yVar);
//    }
//
//    public static void register() {
//        ClientRegistry.bindTileEntityRenderer(Registration.MULTIPLE_FLOWER_TILE.get(), MultipleFlowerRenderer::new);
//    }
//}
