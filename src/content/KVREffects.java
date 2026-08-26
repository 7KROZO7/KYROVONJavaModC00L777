package extra.content;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import mindustry.entities.Effect;

public class KVREffects {
    public static Effect warpRift;

    public static void load() {
        warpRift = new Effect(50f, e -> {
            // Dimensional Colors
            Color darkPurple = Color.valueOf("2a1645");
            Color riftViolet = Color.valueOf("7a3fd2");
            Color glowLilac = Color.valueOf("c084fc");
            Color cyanSpark = Color.valueOf("38bdf8");

            // Outer expanding warp shockwave
            Draw.color(riftViolet, glowLilac, e.fin());
            Lines.stroke(e.fout() * 3f);
            Lines.circle(e.x, e.y, e.fin() * 28f);

            // Collapsing inner core void
            Draw.color(darkPurple);
            Fill.circle(e.x, e.y, e.fout() * 12f);

            // Swirling rift sparks
            Draw.color(cyanSpark);
            Angles.randLenVectors(e.id, 8, e.fin() * 22f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, e.fout() * 2f);
            });
        });
    }
}
