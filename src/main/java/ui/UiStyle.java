package ui;

import java.awt.Color;
import java.awt.Font;

/** Central place for the fonts and colors used across the UI. */
public final class UiStyle {

    public static final Font PLAIN = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    public static final Font BOLD = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    public static final Font TITLE = new Font(Font.SANS_SERIF, Font.BOLD, 18);

    public static final Color ACCENT = new Color(41, 128, 185);
    public static final Color WARM = new Color(211, 84, 0);
    public static final Color BORDER = new Color(127, 140, 141);

    private UiStyle() {
    }
}
