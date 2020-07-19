package lilypuree.forest_tree.client.gui;

import se.mickelus.mgui.gui.GuiElement;

public class GuiExtended extends GuiElement {

    protected boolean enabled;
    protected boolean dragging;

    public GuiExtended(int x, int y, int widthIn, int heightIn){
        super(x,y,widthIn,heightIn);
        this.enabled = true;
    }

    public void mouseDragged(double mouseX, double mouseY, double deltaX, double deltaY) {
        if(!isDragging()) return;
        this.elements.forEach((element) -> {
            if(element instanceof GuiExtended){
                ((GuiExtended) element).mouseDragged(mouseX, mouseY, deltaX, deltaY);
            }
        });
    }

    public void mouseScrolled(double mouseX, double mouseY, double scroll){
        this.elements.forEach((element) -> {
            if(element instanceof GuiExtended){
                ((GuiExtended) element).mouseScrolled(mouseX, mouseY, scroll);
            }
        });
    }

    @Override
    protected void calculateFocusState(int refX, int refY, int mouseX, int mouseY) {
        if(enabled){
            super.calculateFocusState(refX, refY, mouseX, mouseY);
        }else {
            this.hasFocus = false;
        }
    }

    @Override
    public void mouseReleased(int x, int y) {
        dragging = false;
        super.mouseReleased(x, y);
    }

    @Override
    public boolean onClick(int x, int y) {
        if(enabled && super.onClick(x, y)){
            dragging = true;
            return true;
        }
        return false;
    }

    public boolean isDragging(){
        return this.dragging;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Sets whether this text box is enabled. Disabled text boxes cannot be typed in.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setDragging(boolean dragging){
        this.dragging = dragging;
    }

    //copied from Widget
    public static final int UNSET_FG_COLOR = -1;
    protected int packedFGColor = UNSET_FG_COLOR;
    public int getFGColor() {
        if (packedFGColor != UNSET_FG_COLOR) return packedFGColor;
        return this.enabled ? 16777215 : 10526880; // White : Light Grey
    }
    public void setFGColor(int color) {
        this.packedFGColor = color;
    }
    public void clearFGColor() {
        this.packedFGColor = UNSET_FG_COLOR;
    }
}
