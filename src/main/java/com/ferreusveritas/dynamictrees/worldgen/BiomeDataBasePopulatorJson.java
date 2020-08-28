package com.ferreusveritas.dynamictrees.worldgen;

import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lilypuree.forest_tree.api.registration.WorldGenRegistry.BiomeDataBaseJsonCapabilityRegistryEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BiomeDataBasePopulatorJson implements IBiomeDataBasePopulator {

    public static final String SPECIES = "species";
    public static final String DENSITY = "density";
    public static final String CHANCE = "chance";

    public static final String CANCELVANILLA = "cancelvanilla";
    public static final String MULTIPASS = "multipass";
    public static final String SUBTERRANEAN = "subterranean";
    public static final String FORESTNESS = "forestness";
    public static final String BLACKLIST = "blacklist";
    public static final String RESET = "reset";

    public static final String WHITE = "white";
    public static final String SELECT = "select";
    public static final String APPLY = "apply";
    public static final String NAME = "name";
    public static final String TYPE = "type";

    public  BiomeDataBasePopulatorJson(ResourceLocation jsonLocation) {
//        this(JsonHelper.load(jsonLocation));
    }

    @Override
    public void populate(BiomeDataBase biomeDataBase) {
        Stream<Biome> stream = Lists.newArrayList(Registry.BIOME).stream();
        stream.forEach(biome ->{

        });
    }


    public static void cleanup() {
//        jsonBiomeApplierMap = new HashMap<>();
//        jsonBiomeSelectorMap = new HashMap<>();
//        blacklistedBiomes = new HashSet<>();
    }

    public static void registerJsonCapabilities(BiomeDataBaseJsonCapabilityRegistryEvent event) {

//        event.register(NAME, jsonElement -> {
//            if(jsonElement != null && jsonElement.isJsonPrimitive()) {
//                JsonPrimitive primitive = jsonElement.getAsJsonPrimitive();
//                if(primitive.isString()) {
//                    String biomeMatch = primitive.getAsString();
//                    return b-> b.getRegistryName().toString().matches(biomeMatch);
//                }
//            }
//
//            return b -> false;
//        });
//
//        event.register(TYPE, jsonElement -> {
//            if(jsonElement != null) {
//                if (jsonElement.isJsonPrimitive()) {
//                    String typeMatch = jsonElement.getAsString();
//                    List<BiomeDictionary.Type> types = Arrays.asList(typeMatch.split(",")).stream().map(BiomeDictionary.Type::getType).collect(Collectors.toList());
//                    return b -> biomeHasTypes(b, types);
//                } else
//                if (jsonElement.isJsonArray()) {
//                    List<BiomeDictionary.Type> types = new ArrayList<>();
//                    for(JsonElement element : jsonElement.getAsJsonArray()) {
//                        if(element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
//                            types.add(BiomeDictionary.Type.getType(element.getAsString()));
//                        }
//                    }
//                    return b -> biomeHasTypes(b, types);
//                }
//            }
//
//            return b -> false;
//        });
//
//        event.register(SPECIES, new JsonBiomePropertyApplierSpecies());
//
//        event.register(DENSITY, new JsonBiomePropertyApplierDensity());
//
//        event.register(CHANCE, new JsonBiomePropertyApplierChance());
//
//        event.register(CANCELVANILLA, (dbase, element, biome) -> {
//            if(element.isJsonPrimitive()) {
//                boolean cancel = element.getAsBoolean();
//                //System.out.println("Biome " + (cancel ? "cancelled" : "uncancelled") + " for vanilla: " + biome);
//                dbase.setCancelVanillaTreeGen(biome, cancel);
//            }
//        });
//
//        event.register(MULTIPASS, (dbase, element, biome) -> {
//            if(element.isJsonPrimitive()) {
//                boolean multipass = element.getAsBoolean();
//
//                if(multipass) {
//                    //System.out.println("Biome set for multipass: " + biome);
//
//                    //Enable poisson disc multipass of roofed forests to ensure maximum density even with large trees
//                    //by filling in gaps in the generation with smaller trees
//                    dbase.setMultipass(biome, pass -> {
//                        switch(pass) {
//                            case 0: return 0;//Zero means to run as normal
//                            case 1: return 5;//Return only radius 5 on pass 1
//                            case 2: return 3;//Return only radius 3 on pass 2
//                            default: return -1;//A negative number means to terminate
//                        }
//                    });
//                }
//            }
//        });
//
//        event.register(SUBTERRANEAN,  (dbase, element, biome) -> {
//            if(element.isJsonPrimitive()) {
//                boolean subterranean = element.getAsBoolean();
//                //System.out.println("Biome set to subterranean: " + biome);
//                dbase.setIsSubterranean(biome, subterranean);
//            }
//        });
//
//        event.register(FORESTNESS, (dbase, element, biome) -> {
//            if(element.isJsonPrimitive()) {
//                float forestness = element.getAsFloat();
//                //System.out.println("Forestness set for biome: " + biome + " at " + forestness);
//                dbase.setForestness(biome, forestness);
//            }
//        });
//
//        event.register(BLACKLIST, (dbase, element, biome) -> {
//            if(element.isJsonPrimitive()) {
//                boolean blacklist = element.getAsBoolean();
//                if(blacklist) {
//                    //System.out.println("Blacklisted biome: " + biome);
//                    blacklistedBiomes.add(biome);
//                } else {
//                    blacklistedBiomes.remove(biome);
//                }
//            }
//        });
//
//        event.register(RESET, (dbase, element, biome) -> {
//            dbase.setCancelVanillaTreeGen(biome, false);
//            dbase.setSpeciesSelector(biome, (pos, dirt, rnd) -> new BiomePropertySelectors.SpeciesSelection(), BiomeDataBase.Operation.REPLACE);
//            dbase.setDensitySelector(biome, (rnd, nd) -> -1, BiomeDataBase.Operation.REPLACE);
//            dbase.setChanceSelector(biome, (rnd, spc, rad) -> BiomePropertySelectors.EnumChance.UNHANDLED, BiomeDataBase.Operation.REPLACE);
//            dbase.setForestness(biome, 0.0f);
//            dbase.setIsSubterranean(biome, false);
//            dbase.setMultipass(biome, pass -> (pass == 0 ? 0 : -1));
//        });

    }
}
