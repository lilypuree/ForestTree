package lilypuree.forest_tree.trees.species;

import lilypuree.forest_tree.trees.block.ModBlockProperties;
import net.minecraft.util.math.Vec3i;

public abstract class SpeciesBase implements Species {
    @Override
    public float getThickness(int distanceToTrunk, int age, boolean isEnd, Vec3i sourcePos) {
//        float width = 0.2f;
//        width = 0.3f - distanceToTrunk * 0.001f;
//        if(getID() == 26){
//            width *= 0.75f;
//            //if branch == branchEnd2__y_ do custom render
//            //get tile entity, if fruit type is 1
//            //render custom fronds and bananas
//            //also dates have custom rendering
//        }
//        if(getID() == 24){ //bamboo
//            width = 0.15f - distanceToTrunk * 0.005f;
//            //do custom rendering for bamboo.
//        }
//        if(isConifer()){
//            //render custom leaves in the right place (horizontal or straight)
//            //if there is snow render snow
//        }
//        //render christmas lights
//        width = Math.max(width, 0.1f);
//        //stumps should have custom rendering
//        if(isEnd || isConifer()&&sourcePos.getY() == 0){
//            width *= 0.5f;
//        }else if(!isConifer() && !(sourcePos.getX() == 0 && sourcePos.getZ() == 0)){
//            width *= 0.75f;
//        }
//        //if block is facing down or horizontal & is end, and id is 1 -> render faux palm
//        //if block is facing down but not straight down, and is either end or not
//        //render it normally
//        //if block is facing up// horizontally do the same thing
//
//        //set yrotation propery
//        //check if moss exists
//        return width;

        //placeholder thickness
        return 1.0f/12.0f * (age+1.0f);
    }

    @Override
    public boolean isConifer() {
        return false;
    }

    @Override
    public boolean canLoseLeaves() {
        return false;
    }

    @Override
    public boolean fruits() {
        return false;
    }
}
