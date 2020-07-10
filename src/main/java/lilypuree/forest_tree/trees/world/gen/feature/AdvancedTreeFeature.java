package lilypuree.forest_tree.trees.world.gen.feature;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.datafixers.Dynamic;
import lilypuree.forest_tree.trees.world.gen_old.feature.AdvancedAbstractBaseTreeFeature;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.shapes.BitSetVoxelShapePart;
import net.minecraft.util.math.shapes.VoxelShapePart;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.Template;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class AdvancedTreeFeature<T extends NoFeatureConfig> extends Feature<T> {

    private TreeGenerator treeGenerator;

    public AdvancedTreeFeature(Function<Dynamic<?>, ? extends T> configFactoryIn) {
        super(configFactoryIn);
        this.treeGenerator = new TreeGenerator();
    }

    public AdvancedTreeFeature<T> withGenerator(TreeGenerator generator){
        this.treeGenerator = generator;
        return this;
    }

    protected boolean generate(IWorldGenerationReader reader, Random rand, BlockPos pos, Set<BlockPos> branches, Set<BlockPos> leaves, MutableBoundingBox mBB, T Config) {
        //don't have to do the chunk boundary test
        treeGenerator.setTreegenParameters(reader, rand, branches, leaves, mBB);
        //TODO
        //how should I determine the age?
        return treeGenerator.generate(pos, 11);
    }

    //Copied from AbstractTreeFeature
    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, T config) {
        Set<BlockPos> branches = Sets.newHashSet();
        Set<BlockPos> leaves = Sets.newHashSet();
        Set<BlockPos> decorators = Sets.newHashSet();
        MutableBoundingBox mBB = MutableBoundingBox.getNewBoundingBox();


        boolean flag = this.generate(worldIn, rand, pos, branches, leaves, mBB, config);
        if (mBB.minX <= mBB.maxX && flag && !branches.isEmpty()) {
//            if (!config.decorators.isEmpty()) {
            List<BlockPos> branchList = Lists.newArrayList(branches);
            List<BlockPos> leafList = Lists.newArrayList(leaves);
            branchList.sort(Comparator.comparingInt(Vec3i::getY));
            leafList.sort(Comparator.comparingInt(Vec3i::getY));
//                config.decorators.forEach((treeDecorator) -> {
//                    treeDecorator.func_225576_a_(worldIn, rand, branchList, leafList, decorators, mBB);
//                });
//            }

            VoxelShapePart voxelshapepart = this.getTreeBlocksVoxelShapePart(worldIn, mBB, branches, decorators);
            Template.func_222857_a(worldIn, 3, voxelshapepart, mBB.minX, mBB.minY, mBB.minZ);
            return true;
        } else {
            return false;
        }
    }

    private VoxelShapePart getTreeBlocksVoxelShapePart(IWorld world, MutableBoundingBox mBB, Set<BlockPos> branches, Set<BlockPos> leaves) {
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

            for (BlockPos logPos : Lists.newArrayList(branches)) {
                if (mBB.isVecInside(logPos)) {
                    voxelshapepart.setFilled(logPos.getX() - mBB.minX, logPos.getY() - mBB.minY, logPos.getZ() - mBB.minZ, true, true);
                }

                for (Direction direction : Direction.values()) {
                    mutablePos.setPos(logPos).move(direction);
                    if (!branches.contains(mutablePos)) {
                        BlockState blockstate = world.getBlockState(mutablePos);
                        if (blockstate.has(BlockStateProperties.DISTANCE_1_7)) {
                            list.get(0).add(mutablePos.toImmutable());
//                            this.setBlockStateWithNoNeighborReaction(world, mutablePos, blockstate.with(BlockStateProperties.DISTANCE_1_7, Integer.valueOf(1)));
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
//                                    this.setBlockStateWithNoNeighborReaction(world, mutablePos, newState);
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
}
