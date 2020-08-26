package lilypuree.forest_tree.world.feature.features;

import com.mojang.datafixers.Dynamic;
import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.shrubs.block.MultipleFlowerTile;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;
import java.util.function.Function;

public class DenseFlowerPatchFeature extends Feature<BlockClusterFeatureConfig> {
    public DenseFlowerPatchFeature(Function<Dynamic<?>, ? extends BlockClusterFeatureConfig> dynamicFunction) {
        super(dynamicFunction);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, BlockClusterFeatureConfig config) {
        Random posRand = new Random(rand.nextInt());
        BlockPos flowerPos;
        if (config.field_227298_k_) {
            flowerPos = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos);
        } else {
            flowerPos = pos;
        }
        int count = 0;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int i = 0; i < config.tryCount; i++) {
            posRand.setSeed(rand.nextLong());

            BlockState state = config.stateProvider.getBlockState(posRand, flowerPos);
            mutable.setPos(flowerPos).move(8, 0, 8).move(posRand.nextInt(config.xSpread + 1) - posRand.nextInt(config.xSpread + 1), posRand.nextInt(config.ySpread + 1) - posRand.nextInt(config.ySpread + 1), posRand.nextInt(config.zSpread + 1) - posRand.nextInt(config.zSpread + 1));
            if (worldIn.getChunk(mutable.getX() >> 4, mutable.getZ() >> 4, ChunkStatus.EMPTY, false) == null) continue;
            BlockPos support = mutable.down();
            BlockState supportBlock = worldIn.getBlockState(support);

            boolean isMultipleFlowerBlock = worldIn.getBlockState(mutable).getBlock() == Registration.MULTIPLE_FLOWER_BLOCK.get();
            boolean isReplaceable = config.isReplaceable && worldIn.getBlockState(mutable).getMaterial().isReplaceable();
            boolean isWhiteListed = (config.whitelist.isEmpty() || config.whitelist.contains(supportBlock.getBlock()));
            boolean isBlackListed = config.blacklist.contains(supportBlock);
            boolean isWaterAvailable = !config.requiresWater || worldIn.getFluidState(support.west()).isTagged(FluidTags.WATER) || worldIn.getFluidState(support.east()).isTagged(FluidTags.WATER) || worldIn.getFluidState(support.north()).isTagged(FluidTags.WATER) || worldIn.getFluidState(support.south()).isTagged(FluidTags.WATER);

            if (!isMultipleFlowerBlock) {
                if ((worldIn.isAirBlock(mutable) || isReplaceable) && state.isValidPosition(worldIn, mutable) && isWhiteListed && !isBlackListed && isWaterAvailable) {
                    worldIn.setBlockState(mutable, Registration.MULTIPLE_FLOWER_BLOCK.get().getDefaultState(), 2);
                } else {
                    continue;
                }
            }
            TileEntity tileEntity = worldIn.getTileEntity(mutable);
            if (tileEntity instanceof MultipleFlowerTile) {
                ((MultipleFlowerTile) tileEntity).insertFlower(state);
                ++count;
            } else {
//                worldIn.setBlockState(mutable, Blocks.AIR.getDefaultState(), 19);
            }
        }
        return count > 0;
    }
}
