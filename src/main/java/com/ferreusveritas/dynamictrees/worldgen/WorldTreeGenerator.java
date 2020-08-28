package com.ferreusveritas.dynamictrees.worldgen;

import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.EnumChance;
import com.ferreusveritas.dynamictrees.api.worldgen.IGroundFinder;
import com.ferreusveritas.dynamictrees.systems.poissondisc.PoissonDisc;
import com.ferreusveritas.dynamictrees.systems.poissondisc.PoissonDiscProviderUniversal;
import com.ferreusveritas.dynamictrees.util.RandomXOR;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.google.common.collect.Maps;
import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.ModTrees;
import lilypuree.forest_tree.api.gen.SpeciesPlacer;
import lilypuree.forest_tree.api.genera.Species;
import lilypuree.forest_tree.trees.PalmTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.Map;

public class WorldTreeGenerator {

    protected static WorldTreeGenerator INSTANCE;

    protected final BiomeDataBase defaultBiomeDataBase;
    public static final BiomeDataBase DIMENSIONBLACKLISTED = new BiomeDataBase();
    protected final PoissonDiscProviderUniversal circleProvider;
    protected final RandomXOR random = new RandomXOR();
    protected final Map<DimensionType, BiomeDataBase> dimensionMap = new HashMap<>();

    public static void preInit() {
//        if (WorldGenRegistry.isWorldGenEnabled()) {
        new WorldTreeGenerator();
//        }
    }

    public WorldTreeGenerator() {
        INSTANCE = this;//Set this here in case the lines in the constructor lead to calls that use getWorldTreeGenerator
        defaultBiomeDataBase = new BiomeDataBase();
        circleProvider = new PoissonDiscProviderUniversal();
    }

    public static WorldTreeGenerator getWorldTreeGenerator() {
        return INSTANCE;
    }

    public BiomeDataBase getBiomeDataBase(DimensionType dimensionType) {
        return dimensionMap.getOrDefault(dimensionType, getDefaultBiomeDataBase());
    }

    public BiomeDataBase getBiomeDataBase(IWorld world) {
        return getBiomeDataBase(world.getDimension().getType());
    }

    public BiomeDataBase getDefaultBiomeDataBase() {
        return defaultBiomeDataBase;
    }

    public void linkDimensionToDataBase(DimensionType dimensionType, BiomeDataBase dBase) {
        dimensionMap.put(dimensionType, dBase);
    }

    public void BlackListDimension(DimensionType dimensionType) {
        dimensionMap.put(dimensionType, DIMENSIONBLACKLISTED);
    }

    public void clearAllBiomeDataBases() {
        dimensionMap.clear();
        defaultBiomeDataBase.clear();
    }

    public boolean validateBiomeDataBases() {
        return defaultBiomeDataBase.isValid() && dimensionMap.values().stream().allMatch(BiomeDataBase::isValid);
    }

    /**
     * This is for world debugging.
     * The colors signify the different tree spawn failure modes.
     */
    public enum EnumGeneratorResult {
        GENERATED(DyeColor.WHITE),
        NOTREE(DyeColor.BLACK),
        UNHANDLEDBIOME(DyeColor.YELLOW),
        FAILSOIL(DyeColor.BROWN),
        FAILCHANCE(DyeColor.BLUE),
        FAILGENERATION(DyeColor.RED),
        NOGROUND(DyeColor.PURPLE);

        private final DyeColor color;

        private EnumGeneratorResult(DyeColor color) {
            this.color = color;
        }

        public DyeColor getColor() {
            return this.color;
        }

    }

    public PoissonDiscProviderUniversal getCircleProvider() {
        return circleProvider;
    }

    public void makeWoolCircle(IWorld world, PoissonDisc circle, int h, EnumGeneratorResult resultType, SafeChunkBounds safeBounds) {
        makeWoolCircle(world, circle, h, resultType, safeBounds, 0);
    }

    private static final Map<DyeColor, Block> WOOL_BY_COLOR = Util.make(Maps.newEnumMap(DyeColor.class), (p_203402_0_) -> {
        p_203402_0_.put(DyeColor.WHITE, Blocks.WHITE_WOOL);
        p_203402_0_.put(DyeColor.ORANGE, Blocks.ORANGE_WOOL);
        p_203402_0_.put(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL);
        p_203402_0_.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL);
        p_203402_0_.put(DyeColor.YELLOW, Blocks.YELLOW_WOOL);
        p_203402_0_.put(DyeColor.LIME, Blocks.LIME_WOOL);
        p_203402_0_.put(DyeColor.PINK, Blocks.PINK_WOOL);
        p_203402_0_.put(DyeColor.GRAY, Blocks.GRAY_WOOL);
        p_203402_0_.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL);
        p_203402_0_.put(DyeColor.CYAN, Blocks.CYAN_WOOL);
        p_203402_0_.put(DyeColor.PURPLE, Blocks.PURPLE_WOOL);
        p_203402_0_.put(DyeColor.BLUE, Blocks.BLUE_WOOL);
        p_203402_0_.put(DyeColor.BROWN, Blocks.BROWN_WOOL);
        p_203402_0_.put(DyeColor.GREEN, Blocks.GREEN_WOOL);
        p_203402_0_.put(DyeColor.RED, Blocks.RED_WOOL);
        p_203402_0_.put(DyeColor.BLACK, Blocks.BLACK_WOOL);
    });
    private static final Map<DyeColor, Block> CARPET_BY_COLOR = Util.make(Maps.newEnumMap(DyeColor.class), (p_203402_0_) -> {
        p_203402_0_.put(DyeColor.WHITE, Blocks.WHITE_CARPET);
        p_203402_0_.put(DyeColor.ORANGE, Blocks.ORANGE_CARPET);
        p_203402_0_.put(DyeColor.MAGENTA, Blocks.MAGENTA_CARPET);
        p_203402_0_.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_CARPET);
        p_203402_0_.put(DyeColor.YELLOW, Blocks.YELLOW_CARPET);
        p_203402_0_.put(DyeColor.LIME, Blocks.LIME_CARPET);
        p_203402_0_.put(DyeColor.PINK, Blocks.PINK_CARPET);
        p_203402_0_.put(DyeColor.GRAY, Blocks.GRAY_CARPET);
        p_203402_0_.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_CARPET);
        p_203402_0_.put(DyeColor.CYAN, Blocks.CYAN_CARPET);
        p_203402_0_.put(DyeColor.PURPLE, Blocks.PURPLE_CARPET);
        p_203402_0_.put(DyeColor.BLUE, Blocks.BLUE_CARPET);
        p_203402_0_.put(DyeColor.BROWN, Blocks.BROWN_CARPET);
        p_203402_0_.put(DyeColor.GREEN, Blocks.GREEN_CARPET);
        p_203402_0_.put(DyeColor.RED, Blocks.RED_CARPET);
        p_203402_0_.put(DyeColor.BLACK, Blocks.BLACK_CARPET);
    });

    public void makeWoolCircle(IWorld world, PoissonDisc circle, int h, EnumGeneratorResult resultType, SafeChunkBounds safeBounds, int flags) {
        for (int ix = -circle.radius; ix <= circle.radius; ix++) {
            for (int iz = -circle.radius; iz <= circle.radius; iz++) {
                if (circle.isEdge(circle.x + ix, circle.z + iz)) {
                    safeBounds.setBlockState(world, new BlockPos(circle.x + ix, h, circle.z + iz),
                            WOOL_BY_COLOR.get(DyeColor.byId((circle.x ^ circle.z) & 0xF)).getDefaultState(),
                            flags, true);
                }
            }
        }

        if (resultType != EnumGeneratorResult.GENERATED) {
            BlockPos pos = new BlockPos(circle.x, h, circle.z);
            DyeColor color = resultType.getColor();
            safeBounds.setBlockState(world, pos, WOOL_BY_COLOR.get(color).getDefaultState(), true);
            safeBounds.setBlockState(world, pos.up(), CARPET_BY_COLOR.get(color).getDefaultState(), true);
        }
    }

    public EnumGeneratorResult makeTree(IWorld world, BiomeDataBase biomeDataBase, PoissonDisc circle, IGroundFinder groundFinder, SafeChunkBounds safeBounds) {

        circle.add(8, 8);//Move the circle into the "stage"

        BlockPos pos = new BlockPos(circle.x, 0, circle.z);

        Biome biome = world.getBiome(pos);
        BiomeDataBase.BiomeEntry biomeEntry = biomeDataBase.getEntry(biome);

        pos = groundFinder.findGround(biomeEntry, world, pos);

        if (pos == BlockPos.ZERO) {
            return EnumGeneratorResult.NOGROUND;
        }

        random.setXOR(pos);

        BlockState dirtState = world.getBlockState(pos);
//
        EnumGeneratorResult result = EnumGeneratorResult.GENERATED;

        BiomePropertySelectors.SpeciesSelection speciesSelection = biomeEntry.getSpeciesSelector().getSpecies(pos, dirtState, random);
//        if (speciesSelection.isHandled()) {
//            Species species = speciesSelection.getSpecies();
//            if (species.isValid()) {
//                if (species.isAcceptableSoilForWorldgen(world, pos, dirtState)) {
//                    if (biomeEntry.getChanceSelector().getChance(random, species, circle.radius) == EnumChance.OK) {
//                        if (species.generate(world, pos, biome, random, circle.radius, safeBounds)) {
//                            result = EnumGeneratorResult.GENERATED;
//                        } else {
//                            result = EnumGeneratorResult.FAILGENERATION;
//                        }
//                    } else {
//                        result = EnumGeneratorResult.FAILCHANCE;
//                    }
//                } else {
//                    result = EnumGeneratorResult.FAILSOIL;
//                }
//            } else {
//                result = EnumGeneratorResult.NOTREE;
//            }
//        } else {
//            result = EnumGeneratorResult.UNHANDLEDBIOME;
//        }

//        Species species = speciesSelection.getSpecies();
        Species species = ModTrees.baseFamilies.get(0).getCommonSpecies();
        if (species.isValid() && species.isAcceptableSoilForWorldgen(world, pos, dirtState)) {
//            species.getTreeModel().createTree(null, null, pos.up(), new SpeciesPlacer(world), world.getRandom());
        } else {
            result = EnumGeneratorResult.NOTREE;
        }

        //Display wool circles for testing the circle growing algorithm
//        if (ModConfigs.worldGenDebug) {
//        makeWoolCircle(world, circle, pos.getY(), result, safeBounds);
//        }

        circle.add(-8, -8);//Move the circle back to normal coords

        return result;
    }

}
