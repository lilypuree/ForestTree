package lilypuree.forest_tree.client.gui;

import lilypuree.forest_tree.client.gui.GuiExtended;
import se.mickelus.mgui.gui.GuiElement;
import se.mickelus.mgui.gui.GuiRect;

public class GuiScrollBar extends GuiExtended {


    private boolean scrolling;
    private float scrollDistance;

    private int contentHeight;
    private int border = 0;

    private GuiElement guibar;
    private int scrollAmount = 20;

    public GuiScrollBar(int x, int y, int width, int height, int contentHeight){
        super(x, y, width, height);
        this.addChild(new GuiRect(0, 0, width, height, 0x000000));
        this.contentHeight = contentHeight;
        guibar =  new GuiElement(0,0,width,getBarHeight());
        initBar();
    }

    public void initBar(){
        int barHeight = getBarHeight();
        guibar.clearChildren();
        guibar.addChild(new GuiRect(0, 0, width, barHeight, 0x808080));
        guibar.addChild(new GuiRect(0,0,width-1, barHeight - 1, 0xC0C0C0));
        this.addChild(guibar);
    }

    public void setContentHeight(int contentHeight){
        this.contentHeight = contentHeight;
        initBar();
    }


    public int getScrollAmount()
    {
        return scrollAmount;
    }

    public void setScrollAmount(int scrollAmount) {
        this.scrollAmount = scrollAmount;
        applyScrollLimits();
    }

    public float getScrollDistance() {
        return scrollDistance;
    }

    private int getMaxScroll()
    {
        return this.contentHeight - (this.height - this.border);
    }

    private void applyScrollLimits()
    {
        int max = getMaxScroll();

        if (max < 0)
        {
            max /= 2;
        }

        if (this.scrollDistance < 0.0F)
        {
            this.scrollDistance = 0.0F;
        }

        if (this.scrollDistance > max)
        {
            this.scrollDistance = max;
        }
        updateScrollBar();
    }

    private void updateScrollBar(){
        int extraHeight = (contentHeight + border) - height;
        if(extraHeight>0){
            int barHeight = getBarHeight();
            int barTop = (int)this.scrollDistance * (height - barHeight) / extraHeight;
            guibar.setY(barTop);
        }else {
            guibar.setY(0);
        }
    }

    @Override
    protected void calculateFocusState(int refX, int refY, int mouseX, int mouseY) {
        if(enabled){
            boolean gainFocus = mouseX >= this.getX() + refX && mouseX < this.getX() + refX + this.getWidth() && mouseY >= this.getY() + refY && mouseY < this.getY() + refY + this.getHeight();
            if (gainFocus != this.hasFocus && !scrolling) {
                this.hasFocus = gainFocus;
                if (this.hasFocus) {
                    this.onFocus();
                } else {
                    this.onBlur();
                }
            }
        }else {
            this.hasFocus = false;
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, double deltaX, double deltaY) {
        if(this.scrolling ){
            int maxScroll = height - getBarHeight();
            double moved = deltaY / maxScroll;
            this.scrollDistance += getMaxScroll() * moved;
            applyScrollLimits();
        }
    }

    @Override
    public void mouseReleased(int x, int y) {
        scrolling = false;
    }

    @Override
    public boolean onClick(int x, int y) {
        if (this.hasFocus()) {
            scrolling = true;
            return true;
        } else {
            return false;
        }
    }

    public void scroll(double scroll){
        if (scroll != 0)
        {
            this.scrollDistance += -scroll * getScrollAmount();
            applyScrollLimits();
        }
    }

    private int getBarHeight()
    {
        int barHeight = (height * height) / this.contentHeight;

        if (barHeight < 32) barHeight = 32;

        if (barHeight > height - border*2)
            barHeight = height - border*2;

        return barHeight;
    }
}
