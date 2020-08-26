package lilypuree.forest_tree.world.trees.gen_old.feature;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.datafixers.Dynamic;
import lilypuree.forest_tree.trees.TreeBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.shapes.BitSetVoxelShapePart;
import net.minecraft.util.math.shapes.VoxelShapePart;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraftforge.common.IPlantable;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public abstract class AdvancedAbstractTreeFeature<T extends AdvancedBaseTreeFeatureConfig> extends Feature<T> {
   public AdvancedAbstractTreeFeature(Function<Dynamic<?>, ? extends T> configFactoryIn) {
        super(configFactoryIn);
    }

    protected static boolean canBeReplacedByLogs(IWorldGenerationBaseReader worldIn, BlockPos pos) {
        if (worldIn instanceof IWorldReader) {
            return worldIn.hasBlockState(pos, state -> state.canBeReplacedByLogs((IWorldReader) worldIn, pos));
        }
        return worldIn.hasBlockState(pos, (state) -> {
            Block block = state.getBlock();
            return state.isAir() || state.isIn(BlockTags.LEAVES) || isDirt(block) || block.isIn(BlockTags.LOGS) || block.isIn(BlockTags.SAPLINGS) || block == Blocks.VINE;
        });
    }

    public static boolean isAir(IWorldGenerationBaseReader worldIn, BlockPos pos) {
        if (worldIn instanceof IBlockReader) // FORGE: Redirect to state method when possible
            return worldIn.hasBlockState(pos, state -> state.isAir((IBlockReader) worldIn, pos));
        return worldIn.hasBlockState(pos, BlockState::isAir);
    }

    protected static boolean isDirt(IWorldGenerationBaseReader worldIn, BlockPos pos) {
        return worldIn.hasBlockState(pos, (state) -> {
            Block block = state.getBlock();
            return isDirt(block) && block != Blocks.GRASS_BLOCK && block != Blocks.MYCELIUM;
        });
    }

    protected static boolean isVine(IWorldGenerationBaseReader worldIn, BlockPos posIn) {
        return worldIn.hasBlockState(posIn, (state) -> {
            return state.getBlock() == Blocks.VINE;
        });
    }

    public static boolean isWater(IWorldGenerationBaseReader worldIn, BlockPos pos) {
        return worldIn.hasBlockState(pos, (state) -> {
            return state.getBlock() == Blocks.WATER;
        });
    }

    public static boolean isAirOrLeaves(IWorldGenerationBaseReader worldIn, BlockPos pos) {
        if (worldIn instanceof IWorldReader) // FORGE: Redirect to state method when possible
            return worldIn.hasBlockState(pos, state -> state.canBeReplacedByLeaves((IWorldReader) worldIn, pos));
        return worldIn.hasBlockState(pos, (state) -> {
            return state.isAir() || state.isIn(BlockTags.LEAVES);
        });
    }

    @Deprecated //Forge: moved to isSoil
    public static boolean isDirtOrGrassBlock(IWorldGenerationBaseReader worldIn, BlockPos pos) {
        return worldIn.hasBlockState(pos, (state) -> {
            return isDirt(state.getBlock());
        });
    }

    protected static boolean isSoil(IWorldGenerationBaseReader reader, BlockPos pos, IPlantable sapling) {
        if (!(reader instanceof IBlockReader) || sapling == null)
            return isDirtOrGrassBlock(reader, pos);
        return reader.hasBlockState(pos, state -> state.canSustainPlant((IBlockReader) reader, pos, Direction.UP, sapling));
    }

    @Deprecated //Forge: moved to isSoilOrFarm
    protected static boolean isDirtOrGrassBlockOrFarmland(IWorldGenerationBaseReader worldIn, BlockPos pos) {
        return worldIn.hasBlockState(pos, (state) -> {
            Block block = state.getBlock();
            return isDirt(block) || block == Blocks.FARMLAND;
        });
    }

    protected static boolean isSoilOrFarm(IWorldGenerationBaseReader reader, BlockPos pos, IPlantable sapling) {
        if (!(reader instanceof IBlockReader) || sapling == null)
            return isDirtOrGrassBlockOrFarmland(reader, pos);
        return reader.hasBlockState(pos, state -> state.canSustainPlant((IBlockReader) reader, pos, Direction.UP, sapling));
    }

    public static boolean isTallPlants(IWorldGenerationBaseReader reader, BlockPos pos) {
        return reader.hasBlockState(pos, (state) -> {
            Material material = state.getMaterial();
            return material == Material.TALL_PLANTS;
        });
    }

    @Deprecated //Forge: moved to setDirtAt
    protected void setDirt(IWorldGenerationReader reader, BlockPos pos) {
        if (!isDirt(reader, pos)) {
            this.setBlockState(reader, pos, Blocks.DIRT.getDefaultState());
        }
    }

    protected void setDirtAt(IWorldGenerationReader reader, BlockPos pos, BlockPos origin) {
        if (!(reader instanceof IWorld)) {
            setDirt(reader, pos);
            return;
        }
        ((IWorld) reader).getBlockState(pos).onPlantGrow((IWorld) reader, pos, origin);
    }

    protected boolean addLog(IWorldGenerationReader reader, Random rand, BlockPos pos, Set<BlockPos> blockPosSet, MutableBoundingBox mBB, AdvancedBaseTreeFeatureConfig config) {
        if (!isAirOrLeaves(reader, pos) && !isTallPlants(reader, pos) && !isWater(reader, pos)) {
            return false;
        } else {
            this.setBlockStateAndExpandBounds(reader, pos, config.species.getLog().getDefaultState(), mBB);
            blockPosSet.add(pos.toImmutable());
            return true;
        }
    }

    protected boolean addLeaves(IWorldGenerationReader reader, Random rand, BlockPos pos, Set<BlockPos> blockPosSet, MutableBoundingBox mBB, AdvancedBaseTreeFeatureConfig config) {
        if (!isAirOrLeaves(reader, pos) && !isTallPlants(reader, pos) && !isWater(reader, pos)) {
            return false;
        } else {
            this.setBlockStateAndExpandBounds(reader, pos, config.species.getLeaves().getDefaultState(), mBB);
            blockPosSet.add(pos.toImmutable());
            return true;
        }
    }

    protected boolean addBranch(IWorldGenerationReader reader, Random rand, BlockPos pos, Vec3i source, Set<BlockPos> branches, MutableBoundingBox mBB, AdvancedBaseTreeFeatureConfig config) {
        if (!isAirOrLeaves(reader, pos) && !isTallPlants(reader, pos) && !isWater(reader, pos)) {
            return false;
        } else {
            //set branch based on source and config's species
            this.setBlockStateAndExpandBounds(reader, pos, TreeBlocks.getBranchBlock(source, config.species).getDefaultState(), mBB);
            branches.add(pos.toImmutable());
            return true;
        }
    }

    protected boolean addBranchEnd(IWorldGenerationReader reader, Random rand, BlockPos pos, Vec3i source, Set<BlockPos> branches, MutableBoundingBox mBB, AdvancedBaseTreeFeatureConfig config) {
        if (!isAirOrLeaves(reader, pos) && !isTallPlants(reader, pos) && !isWater(reader, pos)) {
            return false;
        } else {
            //set branch end based on source and config's species
            this.setBlockStateAndExpandBounds(reader, pos, TreeBlocks.getBranchBlock(source,config.species).getDefaultState(), mBB);
            branches.add(pos.toImmutable());
            return true;
        }
    }

    protected void setBlockState(IWorldWriter worldIn, BlockPos pos, BlockState state) {
        this.setBlockStateWithNoNeighborReaction(worldIn, pos, state);
    }

    protected final void setBlockStateAndExpandBounds(IWorldWriter writer, BlockPos pos, BlockState state, MutableBoundingBox mBB) {
        this.setBlockStateWithNoNeighborReaction(writer, pos, state);
        mBB.expandTo(new MutableBoundingBox(pos, pos));
    }

    private void setBlockStateWithNoNeighborReaction(IWorldWriter writer, BlockPos pos, BlockState state) {
        writer.setBlockState(pos, state, 19);
    }

    @Override
    public final boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, T config) {
        Set<BlockPos> set = Sets.newHashSet();
        Set<BlockPos> set1 = Sets.newHashSet();
        Set<BlockPos> set2 = Sets.newHashSet();
        MutableBoundingBox mBB = MutableBoundingBox.getNewBoundingBox();
        boolean flag = this.generate(worldIn, rand, pos, set, set1, mBB, config);
        if (mBB.minX <= mBB.maxX && flag && !set.isEmpty()) {
            if (!config.decorators.isEmpty()) {
                List<BlockPos> list = Lists.newArrayList(set);
                List<BlockPos> list1 = Lists.newArrayList(set1);
                list.sort(Comparator.comparingInt(Vec3i::getY));
                list1.sort(Comparator.comparingInt(Vec3i::getY));
                config.decorators.forEach((treeDecorator) -> {
                    treeDecorator.func_225576_a_(worldIn, rand, list, list1, set2, mBB);
                });
            }

            VoxelShapePart voxelshapepart = this.getTreeBlocksVoxelShapePart(worldIn, mBB, set, set2);
            Template.func_222857_a(worldIn, 3, voxelshapepart, mBB.minX, mBB.minY, mBB.minZ);
            return true;
        } else {
            return false;
        }
    }

    private VoxelShapePart getTreeBlocksVoxelShapePart(IWorld world, MutableBoundingBox mBB, Set<BlockPos> logs, Set<BlockPos> leaves) {
        List<Set<BlockPos>> list = Lists.newArrayList();
        VoxelShapePart voxelshapepart = new BitSetVoxelShapePart(mBB.getXSize(), mBB.getYSize(), mBB.getZSize());
        int i = 6;

        for (int j = 0; j < 6; ++j) {
            list.add(Sets.newHashSet());
        }

        try (BlockPos.PooledMutable mutablePos = BlockPos.PooledMutable.retain()) {
            for (BlockPos leavesPos : Lists.newArrayList(leaves)) {
                if (mBB.isVecInside(leavesPos)) {
                    voxelshapepart.setFilled(leavesPos.getX() - mBB.minX, leavesPos.getY() - mBB.minY, leavesPos.getZ() - mBB.minZ, true, true);
                }
            }

            for (BlockPos logPos : Lists.newArrayList(logs)) {
                if (mBB.isVecInside(logPos)) {
                    voxelshapepart.setFilled(logPos.getX() - mBB.minX, logPos.getY() - mBB.minY, logPos.getZ() - mBB.minZ, true, true);
                }

                for (Direction direction : Direction.values()) {
                    mutablePos.setPos(logPos).move(direction);
                    if (!logs.contains(mutablePos)) {
                        BlockState blockstate = world.getBlockState(mutablePos);
                        if (blockstate.has(BlockStateProperties.DISTANCE_1_7)) {
                            list.get(0).add(mutablePos.toImmutable());
                            this.setBlockStateWithNoNeighborReaction(world, mutablePos, blockstate.with(BlockStateProperties.DISTANCE_1_7, Integer.valueOf(1)));
                            if (mBB.isVecInside(mutablePos)) {
                                voxelshapepart.setFilled(mutablePos.getX() - mBB.minX, mutablePos.getY() - mBB.minY, mutablePos.getZ() - mBB.minZ, true, true);
                            }
                        }
                    }
                }
            }

            for (int l = 1; l < 6; ++l) {
                Set<BlockPos> set = list.get(l - 1);
                Set<BlockPos> set1 = list.get(l);

                for (BlockPos leavesPos : set) {
                    if (mBB.isVecInside(leavesPos)) {
                        voxelshapepart.setFilled(leavesPos.getX() - mBB.minX, leavesPos.getY() - mBB.minY, leavesPos.getZ() - mBB.minZ, true, true);
                    }

                    for (Direction direction1 : Direction.values()) {
                        mutablePos.setPos(leavesPos).move(direction1);
                        if (!set.contains(mutablePos) && !set1.contains(mutablePos)) {
                            BlockState state = world.getBlockState(mutablePos);
                            if (state.has(BlockStateProperties.DISTANCE_1_7)) {
                                int k = state.get(BlockStateProperties.DISTANCE_1_7);
                                if (k > l + 1) {
                                    BlockState newState = state.with(BlockStateProperties.DISTANCE_1_7, l + 1);
                                    this.setBlockStateWithNoNeighborReaction(world, mutablePos, newState);
                                    if (mBB.isVecInside(mutablePos)) {
                                        voxelshapepart.setFilled(mutablePos.getX() - mBB.minX, mutablePos.getY() - mBB.minY, mutablePos.getZ() - mBB.minZ, true, true);
                                    }

                                    set1.add(mutablePos.toImmutable());
                                }
                            }
                        }
                    }
                }
            }
        }

        return voxelshapepart;
    }

    protected abstract boolean generate(IWorldGenerationReader reader, Random rand, BlockPos pos, Set<BlockPos> logs, Set<BlockPos> leaves, MutableBoundingBox mBB, T config);
}
