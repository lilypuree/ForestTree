package lilypuree.forest_tree.api.gen;

import lilypuree.forest_tree.api.genera.FoliageCategory;
import lilypuree.forest_tree.api.genera.Species;
import lilypuree.forest_tree.api.genera.WoodCategory;
import lilypuree.forest_tree.api.registration.TreeBlockRegistry;
import lilypuree.forest_tree.common.trees.block.BranchBlock;
import lilypuree.forest_tree.common.trees.block.ModBlockProperties;
import lilypuree.forest_tree.common.world.trees.gen.feature.PlacementHelper;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraftforge.fml.RegistryObject;

import java.util.Map;

public class SpeciesPlacer implements ITreePlacer {
    IWorldGenerationReader world;

    public SpeciesPlacer(IWorldGenerationReader world) {
        this.world = world;
    }

    private int getThickness(float thickness) {
        return MathHelper.clamp((int) (thickness * ModBlockProperties.MAX_DIVISIONS), 1, ModBlockProperties.MAX_DIVISIONS);
    }

    @Override
    public boolean placeStump(BlockPos pos, WoodCategory category, float thickness) {
        return addBlock(pos, TreeBlockRegistry.getStumpBlock(category).getDefaultState().with(ModBlockProperties.THICKNESS, getThickness(thickness)));
    }

    @Override
    public boolean placeBranch(BlockPos pos, WoodCategory category, Vec3i dir, float thickness) {
        return addBlock(pos, TreeBlockRegistry.getBranchBlock(dir, category).getDefaultState().with(ModBlockProperties.THICKNESS, getThickness(thickness)));
    }

    @Override
    public boolean placeBranchEnd(BlockPos pos, WoodCategory category, Vec3i dir, float thickness) {
        return addBlock(pos, TreeBlockRegistry.getBranchEndBlock(dir, category).getDefaultState().with(ModBlockProperties.THICKNESS, getThickness(thickness)));
    }

    @Override
    public boolean placeFoliage(BlockPos pos, FoliageCategory foliage) {
        return addBlock(pos, foliage.getDefaultFoliage().getDefaultState());
    }

    @Override
    public boolean addBlock(BlockPos pos, BlockState state) {
        if (!PlacementHelper.isAirOrLeaves(world, pos) && !PlacementHelper.isTallPlants(world, pos) && !PlacementHelper.isWater(world, pos)) {
            return false;
        } else {
            setBlockStateWithNoNeighborReaction(world, pos, state);
            return true;
        }
    }

    private static void setBlockStateWithNoNeighborReaction(IWorldWriter writer, BlockPos pos, BlockState state) {
        writer.setBlockState(pos, state, 19);
    }
}
