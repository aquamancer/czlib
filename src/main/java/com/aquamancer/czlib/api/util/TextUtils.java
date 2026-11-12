package com.aquamancer.czlib.api.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public final class TextUtils {
    public static MutableText applyGradient(Text text, int... stops) {
        return applyGradient(text.getString(), text.getStyle(), stops);
    }

    public static MutableText applyGradient(String str, int... stops) {
        return applyGradient(str, null, stops);
    }

    public static MutableText applyGradient(String str, @Nullable Style style, int... stops) {
        if (stops.length == 0) return Text.literal(str);
        if (stops.length == 1 || str.length() <= 1) return Text.literal(str).withColor(stops[0]);

        int[] chars = str.codePoints().toArray();

        MutableText result = Text.empty();
        for (int i = 0; i < chars.length; i++) {
            double progress = i / (double) (chars.length - 1);
            double stopLocation = progress * (stops.length - 1);
            int bot = (int) stopLocation;
            double t = stopLocation - bot;
            int top = (bot == stops.length - 1) ? bot : bot + 1;

            int color = lerpRGB(stops[bot], stops[top], t);
            MutableText c = Text.literal(new String(Character.toChars(chars[i])));
            if (style != null) {
                c.setStyle(style);
            }
            c.withColor(color);
            result.append(c);
        }
        return result;
    }

    public static int lerpRGB(int u, int v, double delta) {
        if (u == v) return u;

        int r = u >> 16 & 0xFF;
        int g = u >> 8 & 0xFF;
        int b = u & 0xFF;

        int dr = (int) Math.round(delta * ((v >> 16 & 0xFF) - r));
        int dg = (int) Math.round(delta * ((v >> 8 & 0xFF) - g));
        int db = (int) Math.round(delta * ((v & 0xFF) - b));

        return (r + dr) << 16 | (g + dg) << 8 | (b + db);
    }

    private TextUtils() {}
}
