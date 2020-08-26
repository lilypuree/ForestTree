package lilypuree.forest_tree.world.trees.gen_old.feature;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import lilypuree.forest_tree.trees.species.Species;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraftforge.common.IPlantable;

import java.util.List;

public class AdvancedBaseTreeFeatureConfig implements IFeatureConfig {

    public final List<TreeDecorator> decorators;
    public final Species species;
    public final int age;
    public transient boolean forcePlacement;
    protected IPlantable sapling = (IPlantable) Blocks.OAK_SAPLING;

    protected AdvancedBaseTreeFeatureConfig(Species speciesIn, List<TreeDecorator> decoratorsIn, int ageIn){

        this.species = speciesIn;
        this.decorators = decoratorsIn;
        this.age = ageIn;
    }

    public void forcePlacement(){this.forcePlacement = true;}



    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();
        builder.put(ops.createString("species"), this.species.serialize(ops)).put(ops.createString("age"), ops.createInt(age)).put(ops.createString("decorators"), ops.createList(this.decorators.stream().map((decorator)->{
            return decorator.serialize(ops);
        })));
        return new Dynamic<>(ops, ops.createMap(builder.build()));
    }

    protected AdvancedBaseTreeFeatureConfig setSapling(IPlantable value){
        this.sapling = value;
        return this;
    }

    public IPlantable getSapling() {return  this.sapling;}
//
//    public static <T> AdvancedTreeFeatureConfig deserialize(Dynamic<T> ops){
//        ops.get("species").ge
//    }

    public static class Builder{
        public final Species species;
        private List<TreeDecorator> decorators = Lists.newArrayList();
        private int age = 0;
        protected IPlantable sapling = (IPlantable)Blocks.OAK_SAPLING;

        public Builder(Species speciesIn){
            this.species = speciesIn;
        }
        public AdvancedBaseTreeFeatureConfig.Builder age(int ageIn){
            this.age = ageIn;
            return this;
        }
        public AdvancedBaseTreeFeatureConfig.Builder setSapling(IPlantable value) {
            this.sapling = value;
            return this;
        }
        public AdvancedBaseTreeFeatureConfig build(){
            return new AdvancedBaseTreeFeatureConfig(this.species, this.decorators, this.age).setSapling(sapling);
        }
    }
}
