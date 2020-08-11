package lilypuree.forest_tree.trees.block;

import com.electronwill.nightconfig.core.utils.ObservedMap;
import lilypuree.forest_tree.trees.species.Species;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import java.util.HashMap;
import java.util.Map;

public class StumpBlock extends Block {
    public static final IntegerProperty AGE = ModBlockProperties.TREE_AGE;
    private Species species;
    private Map<Integer, VoxelShape> ageToVoxelShapes = new HashMap<>();

    public StumpBlock(Properties properties, Species speciesIn) {
        super(properties);
        this.species = speciesIn;
        this.setDefaultState(this.getStateContainer().getBaseState().with(AGE, 1));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Entity looker = context.getEntity();
        if (looker instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) looker;
            Vec3d start = player.getEyePosition(0);
            Vec3d look = player.getLook(0);
            Vec3d end = start.add(look.scale(6));
            return BranchVoxelShapes.getClosestShape(start, end, pos, state, worldIn);
        }
        return ageToVoxelShapes.computeIfAbsent(state.get(AGE), age -> BranchVoxelShapes.getVoxelShapeForStump(this, state.get(AGE)));
    }

    public Species getSpecies() {
        return species;
    }


}
