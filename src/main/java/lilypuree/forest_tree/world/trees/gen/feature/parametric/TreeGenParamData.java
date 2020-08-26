package lilypuree.forest_tree.world.trees.gen.feature.parametric;

import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;
import java.util.Map;

public class TreeGenParamData {

    Map<Parameter, Integer> integerParams;
    Map<Parameter, Float> floatParams;
    Map<Parameter, float[]> floatArrayParams;

    public TreeGenParamData() {
        integerParams = new HashMap<>();
        floatParams = new HashMap<>();
        floatArrayParams = new HashMap<>();
    }

    public void setParameterValue(Parameter param, Object value) {
        switch (param.type) {
            case 0:
                if (value instanceof String) {
                    integerParams.put(param, Integer.valueOf((String) value));
                } else if (value instanceof Integer) {
                    integerParams.put(param, (int) value);
                }
                break;
            case 1:
                if (value instanceof String) {
                    floatParams.put(param, Float.valueOf((String) value));
                } else if (value instanceof Float) {
                    floatParams.put(param, (float) value);
                }
                break;
            case 2:
                if (value instanceof float[]) {
                    floatArrayParams.put(param, (float[]) value);
                }
        }
    }


    public void setIntegerValue(Parameter param, int value) {
        if (param.type == 0) {
            integerParams.put(param, value);
        }
    }

    public void setFloatValue(Parameter parameter, float value) {
        if (parameter.type == 1) {
            floatParams.put(parameter, value);
        }
    }

    public void setFloatArray(Parameter parameter, float[] array) {
        if (parameter.type == 2) {
            floatArrayParams.put(parameter, array);
        }
    }

    public void setFloatArrayEntry(Parameter parameter, int index, float value) {
        if (index >= 16) return;
        if (parameter.type == 2) {
            float[] array = floatArrayParams.get(parameter);
            if (array != null) {
                array[index] = value;
            } else {
                array = new float[16];
                array[index] = value;
                floatArrayParams.put(parameter, array);
            }
        }
    }

    public static TreeGenParamData deserializeNbt(CompoundNBT compound) {
        TreeGenParamData treeGenParamData = new TreeGenParamData();
        if (compound.contains("TreeGenerator")) {
            CompoundNBT generatorTag = compound.getCompound("TreeGenerator");
            for (Module module : Module.values()) {
                for (Parameter parameter : Parameter.parameters[module.index]) {
                    switch (parameter.type) {
                        case 0:
                            treeGenParamData.integerParams.put(parameter, generatorTag.getInt(parameter.name));
                            break;
                        case 1:
                            treeGenParamData.floatParams.put(parameter, generatorTag.getFloat(parameter.name));
                            break;
                        case 2:
                            int[] array = generatorTag.getIntArray(parameter.name);
                            treeGenParamData.floatArrayParams.put(parameter, BranchDirectionHelper.intArrayToFloatArray(array));
                            break;
                    }
                }
            }
            return treeGenParamData;
        }
        return null;
    }


    public CompoundNBT writeToNbt(CompoundNBT compound) {
        CompoundNBT tag = new CompoundNBT();

        for (Module module : Module.values()) {
            for (Parameter parameter : Parameter.parameters[module.index]) {
                switch (parameter.type) {
                    case 0:
                        tag.putInt(parameter.name, integerParams.getOrDefault(parameter, 1));
                        break;
                    case 1:
                        tag.putFloat(parameter.name, floatParams.getOrDefault(parameter, 1.0f));
                        break;
                    case 2:
                        float[] array = floatArrayParams.getOrDefault(parameter, new float[]{1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1});
                        tag.putIntArray(parameter.name, BranchDirectionHelper.floatArrayToIntArray(array));
                        break;
                }
            }
        }
        compound.put("TreeGenerator", tag);
        return compound;
    }

    public int getIntParameter(Parameter parameter) {
        if (parameter.type == 0) {
            return integerParams.get(parameter);
        }
        return -1;
    }

    public float getFloatParameter(Parameter parameter) {
        if (parameter.type == 1) {
            return floatParams.get(parameter);
        }
        return -1;
    }

    public float[] getFloatArrayParameter(Parameter parameter) {
        if (parameter.type == 2) {
            return floatArrayParams.get(parameter);
        }
        return null;
    }

    public float getFloatArrayEntry(Parameter parameter, int index) {
        float[] array = getFloatArrayParameter(parameter);
        if (array == null || index >= 16) return -1;
        else {
            return array[index];
        }
    }
}

