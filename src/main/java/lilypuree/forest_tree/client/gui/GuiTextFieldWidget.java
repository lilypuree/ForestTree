package lilypuree.forest_tree.client.gui;

import com.google.common.base.Predicates;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import lilypuree.forest_tree.client.gui.GuiExtended;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.math.MathHelper;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class GuiTextFieldWidget extends GuiExtended {
    private final FontRenderer fontRenderer;

    private int baseX;

    private String text;
    private int maxStringLength = 32;
    private int cursorCounter;

    /**
     * If this value is true along with isFocused, keyTyped will process the keys.
     */
    private boolean isEnabled = true;
    private boolean shiftDown;

    /**
     * The current character index that should be used as start of the rendered text.
     */
    private int lineScrollOffset;
    private int cursorPosition;
    /**
     * other selection position, maybe the same as the cursor
     */
    private int selectionEnd;
    private int enabledColor = 14737632;
    private int disabledColor = 7368816;

    private Runnable onClickHandler = () -> {};
    private Consumer<String> guiResponder = s -> {};
    private Predicate<String> validator = Predicates.alwaysTrue();
    private BiFunction<String, Integer, String> textFormatter = (string, integer) -> {
        return string;
    };

    private boolean enableBackgroundDrawing;
    private boolean selected = false;

    public GuiTextFieldWidget(FontRenderer fontIn, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.fontRenderer = fontIn;
        this.text = "";
    }

    private void setInnerText(String newString) {
        text = newString;
    }

    /**
     * Increments the cursor counter
     */
    public void tick() {
        ++this.cursorCounter;
    }

    public void setResponder(Consumer<String> responderIn) {
        this.guiResponder = responderIn;
    }

    public void setValidator(Predicate<String> validatorIn) {
        this.validator = validatorIn;
    }

    public void setTextFormatter(BiFunction<String, Integer, String> textFormatterIn) {
        this.textFormatter = textFormatterIn;
    }

    /**
     * Sets the maximum length for the text in this text box. If the current text is longer than this length, the current
     * text will be trimmed.
     */
    public void setMaxStringLength(int length) {
        this.maxStringLength = length;
        if (this.text.length() > length) {
            this.setInnerText(this.text.substring(0, length));
            this.onTextChanged(this.text);
        }
    }

    /**
     * Sets the text of the textbox, and moves the cursor to the end.
     */
    public void setText(String textIn) {
        if (this.validator.test(textIn)) {
            if (textIn.length() > this.maxStringLength) {
                this.setInnerText(textIn.substring(0, this.maxStringLength));
            } else {
                this.setInnerText(textIn);
            }

            this.setCursorPositionEnd();
            this.setSelectionPos(this.cursorPosition);
            this.onTextChanged(textIn);
        }
    }

    /**
     * Returns the contents of the textbox
     */
    public String getText() {
        return this.text;
    }

    /**
     * returns the text between the cursor and selectionEnd
     */
    public String getSelectedText() {
        int i = Math.min(this.cursorPosition, this.selectionEnd);
        int j = Math.max(this.cursorPosition, this.selectionEnd);
        return this.text.substring(i, j);
    }

    public boolean canWrite() {
        return this.isVisible() && this.isEnabled() && this.selected;
    }

    /**
     * Adds the given text after the cursor, or replaces the currently selected text if there is a selection.
     */
    public void writeText(String textToWrite) {
        String s = "";
        String writeText = SharedConstants.filterAllowedCharacters(textToWrite);
        int selMin = Math.min(this.cursorPosition, this.selectionEnd);
        int selMax = Math.max(this.cursorPosition, this.selectionEnd);
        int available = this.maxStringLength - this.text.length() - (selMin - selMax);
        if (!this.text.isEmpty()) {
            s = s + this.text.substring(0, selMin);
        }

        int written;
        if (available < writeText.length()) {
            s = s + writeText.substring(0, available);
            written = available;
        } else {
            s = s + writeText;
            written = writeText.length();
        }

        if (!this.text.isEmpty() && selMax < this.text.length()) {
            s = s + this.text.substring(selMax);
        }

        if (this.validator.test(s)) {
            this.setInnerText(s);
            this.clampCursorPosition(selMin + written);
            this.setSelectionPos(this.cursorPosition);
            this.onTextChanged(this.text);
        }
    }

    private void onTextChanged(String newText) {
        if (this.guiResponder != null) {
            this.guiResponder.accept(newText);
        }
    }

    private void delete(int position) {
        if (Screen.hasControlDown()) {
            this.deleteWords(position);
        } else {
            this.deleteFromCursor(position);
        }
    }

    /**
     * Deletes the given number of words from the current cursor's position, unless there is currently a selection, in
     * which case the selection is deleted instead.
     */
    public void deleteWords(int num) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                this.deleteFromCursor(this.getNthWordFromCursor(num) - this.cursorPosition);
            }
        }
    }

    /**
     * Deletes the given number of characters from the current cursor's position, unless there is currently a selection,
     * in which case the selection is deleted instead.
     */
    public void deleteFromCursor(int num) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                boolean flag = num < 0;
                int i = flag ? this.cursorPosition + num : this.cursorPosition;
                int j = flag ? this.cursorPosition : this.cursorPosition + num;
                String s = "";
                if (i >= 0) {
                    s = this.text.substring(0, i);
                }

                if (j < this.text.length()) {
                    s = s + this.text.substring(j);
                }

                if (this.validator.test(s)) {
                    this.setInnerText(s);
                    if (flag) {
                        this.moveCursorBy(num);
                    }

                    this.onTextChanged(this.text);
                }
            }
        }
    }


    /**
     * Gets the starting index of the word at the specified number of words away from the cursor position.
     */
    public int getNthWordFromCursor(int numWords) {
        return this.getNthWordFromPos(numWords, this.getCursorPosition());
    }

    /**
     * Gets the starting index of the word at a distance of the specified number of words away from the given position.
     */
    private int getNthWordFromPos(int n, int pos) {
        return this.getNthWordFromPosWS(n, pos, true);
    }

    /**
     * Like getNthWordFromPos (which wraps this), but adds option for skipping consecutive spaces
     */
    private int getNthWordFromPosWS(int n, int pos, boolean skipWs) {
        int i = pos;
        boolean flag = n < 0;
        int j = Math.abs(n);

        for (int k = 0; k < j; ++k) {
            if (!flag) {
                int l = this.text.length();
                i = this.text.indexOf(32, i);
                if (i == -1) {
                    i = l;
                } else {
                    while (skipWs && i < l && this.text.charAt(i) == ' ') {
                        ++i;
                    }
                }
            } else {
                while (skipWs && i > 0 && this.text.charAt(i - 1) == ' ') {
                    --i;
                }

                while (i > 0 && this.text.charAt(i - 1) != ' ') {
                    --i;
                }
            }
        }

        return i;
    }

    /**
     * Moves the text cursor by a specified number of characters and clears the selection
     */
    public void moveCursorBy(int num) {
        this.setCursorPosition(this.cursorPosition + num);
    }

    /**
     * Sets the current position of the cursor.
     */
    public void setCursorPosition(int pos) {
        this.clampCursorPosition(pos);
        if (!this.shiftDown) {
            this.setSelectionPos(this.cursorPosition);
        }

        this.onTextChanged(this.text);
    }

    public void clampCursorPosition(int pos) {
        this.cursorPosition = MathHelper.clamp(pos, 0, this.text.length());
    }

    /**
     * Moves the cursor to the very start of this text box.
     */
    public void setCursorPositionZero() {
        this.setCursorPosition(0);
    }

    /**
     * Moves the cursor to the very end of this text box.
     */
    public void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }

    /**
     * Sets the position of the selection anchor (the selection anchor and the cursor position mark the edges of the
     * selection). If the anchor is set beyond the bounds of the current text, it will be put back inside.
     */
    public void setSelectionPos(int position) {
        int textLength = this.text.length();
        this.selectionEnd = MathHelper.clamp(position, 0, textLength);
        if (this.fontRenderer != null) {
            if (this.lineScrollOffset > textLength) {
                this.lineScrollOffset = textLength;
            }

            int j = this.getAdjustedWidth();
            String s = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), j);
            int lengthToRender = s.length() + this.lineScrollOffset;
            if (this.selectionEnd == this.lineScrollOffset) {
                this.lineScrollOffset -= this.fontRenderer.trimStringToWidth(this.text, j, true).length();
            }

            if (this.selectionEnd > lengthToRender) {
                this.lineScrollOffset += this.selectionEnd - lengthToRender;
            } else if (this.selectionEnd <= this.lineScrollOffset) {
                this.lineScrollOffset -= this.lineScrollOffset - this.selectionEnd;
            }
            this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, textLength);
        }
    }


    @Override
    protected void onBlur() {
        this.cursorCounter = 0;
    }

    @Override
    public boolean onClick(int x, int y) {
        if (this.hasFocus()) {
            selected = true;
            onClickHandler.run();
            int i = MathHelper.floor(x) - baseX;
//            if (this.enableBackgroundDrawing) {
//                i -= 4;
//            }
            String s = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getAdjustedWidth());
            this.setCursorPosition(this.fontRenderer.trimStringToWidth(s, i).length() + this.lineScrollOffset);
            return true;
        } else {
            return false;
        }
    }

    public void setOnClickHandler(Runnable onClickHandler){
        this.onClickHandler = onClickHandler;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }

    public boolean isSelected(){
        return selected;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {


        if (!this.canWrite()) {
            return false;
        } else {
            InputMappings.Input mouseKey = InputMappings.getInputByCode(keyCode, scanCode);
            if(Minecraft.getInstance().gameSettings.keyBindInventory.isActiveAndMatches(mouseKey)){
                return true;
            }

            this.shiftDown = Screen.hasShiftDown();
            if (Screen.isSelectAll(keyCode)) {
                this.setCursorPositionEnd();
                this.setSelectionPos(0);
                return true;
            } else if (Screen.isCopy(keyCode)) {
                Minecraft.getInstance().keyboardListener.setClipboardString(this.getSelectedText());
                return true;
            } else if (Screen.isPaste(keyCode)) {
                if (this.isEnabled) {
                    this.writeText(Minecraft.getInstance().keyboardListener.getClipboardString());
                }

                return true;
            } else if (Screen.isCut(keyCode)) {
                Minecraft.getInstance().keyboardListener.setClipboardString(this.getSelectedText());
                if (this.isEnabled) {
                    this.writeText("");
                }

                return true;
            } else {
                switch (keyCode) {
                    case 259:
                        if (this.isEnabled) {
                            this.shiftDown = false;
                            this.delete(-1);
                            this.shiftDown = Screen.hasShiftDown();
                        }

                        return true;
                    case 260:
                    case 264:
                    case 265:
                    case 266:
                    case 267:
                    default:
                        return false;
                    case 261:
                        if (this.isEnabled) {
                            this.shiftDown = false;
                            this.delete(1);
                            this.shiftDown = Screen.hasShiftDown();
                        }

                        return true;
                    case 262:
                        if (Screen.hasControlDown()) {
                            this.setCursorPosition(this.getNthWordFromCursor(1));
                        } else {
                            this.moveCursorBy(1);
                        }

                        return true;
                    case 263:
                        if (Screen.hasControlDown()) {
                            this.setCursorPosition(this.getNthWordFromCursor(-1));
                        } else {
                            this.moveCursorBy(-1);
                        }

                        return true;
                    case 268:
                        this.setCursorPositionZero();
                        return true;
                    case 269:
                        this.setCursorPositionEnd();
                        return true;
                }
            }
        }
    }


    public boolean charTyped(char typedChar) {
        if (!this.canWrite()) {
            return false;
        } else if (SharedConstants.isAllowedCharacter(typedChar)) {
            if (this.isEnabled) {
                this.writeText(Character.toString(typedChar));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void calculateFocusState(int refX, int refY, int mouseX, int mouseY) {
        super.calculateFocusState(refX, refY, mouseX, mouseY);
    }

    @Override
    public void draw(MatrixStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        baseX = refX + this.x;
        super.draw(matrixStack, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        drawText(refX, refY);
    }

    public void drawText(int refX, int refY) {
        int renderY = refY + this.y;
        if (this.isVisible()) {
            if (this.getEnableBackgroundDrawing()) {
                fill(baseX - 1, this.y - 1, baseX + this.width + 1, renderY + this.height + 1, -6250336);
                fill(baseX, renderY, baseX + this.width, renderY + this.height, -16777216);
            }

            int color = this.isEnabled ? this.enabledColor : this.disabledColor;
            int lenBehindCursor = this.cursorPosition - this.lineScrollOffset;
            int selOffset = this.selectionEnd - this.lineScrollOffset;
            String renderString = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getAdjustedWidth());
            boolean cursorInString = lenBehindCursor >= 0 && lenBehindCursor <= renderString.length();
            boolean drawBar = this.selected && this.cursorCounter / 6 % 2 == 0 && cursorInString;
            int startX = this.enableBackgroundDrawing ? baseX + 4 : baseX;
            int startY = this.enableBackgroundDrawing ? renderY + (this.height - 8) / 2 : renderY;
            int startX2 = startX;
            if (selOffset > renderString.length()) {
                selOffset = renderString.length();
            }

            if (!renderString.isEmpty()) {
                String stringBehindCursor = cursorInString ? renderString.substring(0, lenBehindCursor) : renderString;
                startX2 = this.fontRenderer.drawStringWithShadow(this.textFormatter.apply(stringBehindCursor, this.lineScrollOffset), (float) startX, (float) startY, color);
            }

            boolean notFilled = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int barPos = startX2;
            if (!cursorInString) {
                barPos = lenBehindCursor > 0 ? startX + this.width : startX;
            } else if (notFilled) {
                barPos = startX2 - 1;
                --startX2;
            }

            if (!renderString.isEmpty() && cursorInString && lenBehindCursor < renderString.length()) {
                this.fontRenderer.drawStringWithShadow(this.textFormatter.apply(renderString.substring(lenBehindCursor), this.cursorPosition), (float) startX2, (float) startY, color);
            }

//            if (!notFilled && this.suggestion != null) {
//                this.fontRenderer.drawStringWithShadow(this.suggestion, (float)(barPos - 1), (float)startY, -8355712);
//            }

            if (drawBar) {
                if (notFilled) {
                    AbstractGui.fill(barPos, startY - 1, barPos + 1, startY + 1 + 9, -3092272);
                } else {
                    this.fontRenderer.drawStringWithShadow("_", (float) barPos, (float) startY, color);
                }
            }

            if (selOffset != lenBehindCursor) {
                int l1 = startX + this.fontRenderer.getStringWidth(renderString.substring(0, selOffset));
                this.drawSelectionBox(barPos, startY - 1, l1 - 1, startY + 1 + 9);
            }

        }
    }

    /**
     * Draws the blue selection box.
     */
    private void drawSelectionBox(int startX, int startY, int endX, int endY) {
        if (startX < endX) {
            int i = startX;
            startX = endX;
            endX = i;
        }

        if (startY < endY) {
            int j = startY;
            startY = endY;
            endY = j;
        }

//        if (endX > this.x + this.width) {
//            endX = this.x + this.width;
//        }
//
//        if (startX > this.x + this.width) {
//            startX = this.x + this.width;
//        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        RenderSystem.color4f(0.0F, 0.0F, 255.0F, 255.0F);
        RenderSystem.disableTexture();
        RenderSystem.enableColorLogicOp();
        RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double) startX, (double) endY, 0.0D).endVertex();
        bufferbuilder.pos((double) endX, (double) endY, 0.0D).endVertex();
        bufferbuilder.pos((double) endX, (double) startY, 0.0D).endVertex();
        bufferbuilder.pos((double) startX, (double) startY, 0.0D).endVertex();
        tessellator.draw();
        RenderSystem.disableColorLogicOp();
        RenderSystem.enableTexture();
    }

    /**
     * returns the maximum number of character that can be contained in this textbox
     */
    private int getMaxStringLength() {
        return this.maxStringLength;
    }

    /**
     * returns the width of the textbox depending on if background drawing is enabled
     */
    public int getAdjustedWidth() {
        return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
    }

    /**
     * Gets whether the background and outline of this text box should be drawn (true if so).
     */
    private boolean getEnableBackgroundDrawing() {
        return this.enableBackgroundDrawing;
    }

    /**
     * Sets whether or not the background and outline of this text box should be drawn.
     */
    public void setEnableBackgroundDrawing(boolean enableBackgroundDrawingIn) {
        this.enableBackgroundDrawing = enableBackgroundDrawingIn;
    }

    /**
     * returns the current position of the cursor
     */
    public int getCursorPosition() {
        return this.cursorPosition;
    }

    /**
     * Sets the color to use when drawing this text box's text. A different color is used if this text box is disabled.
     */
    public void setTextColor(int color) {
        this.enabledColor = color;
    }

    /**
     * Sets the color to use for text in this text box when this text box is disabled.
     */
    public void setDisabledTextColour(int color) {
        this.disabledColor = color;
    }

}
