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
        warpRift = new Effect(55f, e -> {
            Color darkVoid = Color.valueOf("13091f");
            Color portalBase = Color.valueOf("7a3fd2");
            Color neonLilac = Color.valueOf("c084fc");
            Color sparkCyan = Color.valueOf("38bdf8");

            float scale = Mathf.curve(e.fin(), 0f, 0.15f) * Mathf.curve(e.fout(), 0f, 0.15f);
            float baseRadius = 24f * scale;

            if (baseRadius <= 0.1f) return;

            // 1. Swirling fluid outer rim
            Draw.color(neonLilac, portalBase, e.fin());
            Lines.stroke(e.fout() * 3f);
            int segments = 14;
            for (int i = 0; i < segments; i++) {
                float rot = e.fin() * 360f;
                float a1 = (360f / segments) * i + rot;
                float a2 = (360f / segments) * (i + 1) + rot;

                float r1 = baseRadius + Mathf.sin(a1 * 3f + e.fin() * 20f) * (3.5f * scale);
                float r2 = baseRadius + Mathf.sin(a2 * 3f + e.fin() * 20f) * (3.5f * scale);

                Lines.line(
                    e.x + Angles.trnsx(a1, r1), e.y + Angles.trnsy(a1, r1),
                    e.x + Angles.trnsx(a2, r2), e.y + Angles.trnsy(a2, r2)
                );
            }

            // 2. Void core
            Draw.color(darkVoid);
            Fill.circle(e.x, e.y, baseRadius * 0.85f);

            // 3. Rotating spiral arms
            Draw.color(portalBase, neonLilac, e.fout());
            for (int i = 0; i < 3; i++) {
                float spiralAngle = (i * 120f) + (e.fin() * 540f);
                Lines.stroke(2f * e.fout());
                Lines.arc(e.x, e.y, baseRadius * 0.55f, 0.3f, spiralAngle);
            }

            // 4. Dimensional spark droplets
            Draw.color(sparkCyan);
            Angles.randLenVectors(e.id, 10, baseRadius * 1.5f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, e.fout() * 2.2f);
            });
        });
    }
}
