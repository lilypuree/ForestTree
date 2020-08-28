package lilypuree.forest_tree.common.world.trees.gen.feature;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import lilypuree.forest_tree.api.genera.Species;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.common.IPlantable;

public class TreeConfig implements IFeatureConfig {

    public final Species species;
    public final int age;
    public transient boolean forcePlacement;

    protected TreeConfig(Species speciesIn, int ageIn) {
        this.species = speciesIn;
        this.age = ageIn;
    }

    public void forcePlacement() {
        this.forcePlacement = true;
    }

    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();
        builder.put(ops.createString("species"), this.species.serialize(ops)).put(ops.createString("age"), ops.createInt(age));
        return new Dynamic<>(ops, ops.createMap(builder.build()));
    }

    public static TreeConfig deserialize(Dynamic<?> p_214639_0_) {
//        return new TreeConfig(ModSpecies.PINE, 7);
        return null;
    }


    public static class Builder {
        public final Species species;
        private int age = 0;
        protected IPlantable sapling = (IPlantable) Blocks.OAK_SAPLING;

        public Builder(Species speciesIn) {
            this.species = speciesIn;
        }

        public TreeConfig.Builder age(int ageIn) {
            this.age = ageIn;
            return this;
        }

        public TreeConfig.Builder setSapling(IPlantable value) {
            this.sapling = value;
            return this;
        }

        public TreeConfig build() {
            return new TreeConfig(this.species, this.age);
        }
    }
}
