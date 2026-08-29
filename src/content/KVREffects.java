package extra.content;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import mindustry.entities.Effect;

public class KVREffects {
    public static Effect warpRift;
    public static Effect voidBite;

    public static void load() {
        // --- 1. RIFT PORTAL ---
        warpRift = new Effect(55f, e -> {
            Color darkVoid = Color.valueOf("13091f");
            Color portalBase = Color.valueOf("7a3fd2");
            Color neonLilac = Color.valueOf("c084fc");
            Color sparkCyan = Color.valueOf("38bdf8");

            float scale = Mathf.curve(e.fin(), 0f, 0.15f) * Mathf.curve(e.fout(), 0f, 0.15f);
            float baseRadius = 24f * scale;

            if (baseRadius <= 0.1f) return;

            Draw.color(neonLilac, portalBase, e.fin());
            int segments = 14;
            for (int i = 0; i < segments; i++) {
                float rot = e.fin() * 360f;
                float a1 = (360f / segments) * i + rot;
                float a2 = (360f / segments) * (i + 1) + rot;

                float r1 = baseRadius + Mathf.sin(a1 * 3f + e.fin() * 20f) * (3.5f * scale);
                float r2 = baseRadius + Mathf.sin(a2 * 3f + e.fin() * 20f) * (3.5f * scale);

                arc.graphics.g2d.Lines.line(
                    e.x + Angles.trnsx(a1, r1), e.y + Angles.trnsy(a1, r1),
                    e.x + Angles.trnsx(a2, r2), e.y + Angles.trnsy(a2, r2)
                );
            }

            Draw.color(darkVoid);
            Fill.circle(e.x, e.y, baseRadius * 0.85f);
        });

        // --- 2. JAW BITE HIT EFFECT (0.5s / 30 Ticks) ---
        voidBite = new Effect(30f, e -> {
            TextureRegion topJaw = Core.atlas.find("krv-bite-jaw-top");
            TextureRegion bottomJaw = Core.atlas.find("krv-bite-jaw-bottom");

            // Jaws snap inward from 16px to 0px over the first 8 ticks
            float clampOffset = Mathf.curve(e.fin(), 0f, 0.25f);
            float currentDistance = (1f - clampOffset) * 16f;

            // Render top and bottom teeth clamping shut
            Draw.color(Color.white, e.fout());
            if (topJaw.found()) {
                Draw.rect(topJaw, e.x, e.y + currentDistance);
            }
            if (bottomJaw.found()) {
                Draw.rect(bottomJaw, e.x, e.y - currentDistance);
            }

            // Burst of dimensional sparks on crunch (after tick 8)
            if (e.fin() > 0.25f) {
                Draw.color(Color.valueOf("c084fc"), Color.valueOf("38bdf8"), e.fin());
                Angles.randLenVectors(e.id, 8, e.fin() * 18f, (x, y) -> {
                    Fill.circle(e.x + x, e.y + y, e.fout() * 2.2f);
                });
            }
        });
    }
}
