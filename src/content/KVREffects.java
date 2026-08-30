package extra.content;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.entities.Effect;

public class KVREffects {
    public static Effect warpRift;
    public static Effect voidBite;
    public static Effect blueHeal;

    public static void load() {
        // --- 1. DIMENSIONAL WARP RIFT ---
        warpRift = new Effect(75f, e -> {
            Color darkVoid = Color.valueOf("13091f");
            Color portalBase = Color.valueOf("7a3fd2");
            Color neonLilac = Color.valueOf("c084fc");
            Color lightningBlue = Color.valueOf("38bdf8");
            Color coreWhite = Color.valueOf("ffffff");

            Draw.color(lightningBlue, coreWhite, Mathf.absin(Time.time, 2f, 1f));
            Lines.stroke(e.fout() * 2f + 0.5f);

            int bolts = 6;
            for (int i = 0; i < bolts; i++) {
                float baseAngle = (360f / bolts) * i + Mathf.randomSeed(e.id + i, 360f);
                float len = 28f * (0.8f + Mathf.sin(e.fin() * 30f + i) * 0.4f);

                float px = e.x, py = e.y;
                int segments = 4;
                for (int s = 1; s <= segments; s++) {
                    float nextLen = (len / segments) * s;
                    float offsetAngle = baseAngle + Mathf.randomSeed(e.id + i * 10 + s + (int)(e.fin() * 15f), -35f, 35f);
                    float nx = e.x + Angles.trnsx(offsetAngle, nextLen);
                    float ny = e.y + Angles.trnsy(offsetAngle, nextLen);

                    Lines.line(px, py, nx, ny);
                    px = nx;
                    py = ny;
                }
            }

            float portalProgress = Mathf.curve(e.fin(), 0.15f, 0.4f) * Mathf.curve(e.fout(), 0f, 0.25f);
            float baseRadius = 26f * portalProgress;

            if (baseRadius > 0.5f) {
                Draw.color(neonLilac, portalBase, e.fin());
                Lines.stroke(portalProgress * 3f);
                int rimSegments = 14;
                for (int i = 0; i < rimSegments; i++) {
                    float rot = e.fin() * 480f;
                    float a1 = (360f / rimSegments) * i + rot;
                    float a2 = (360f / rimSegments) * (i + 1) + rot;

                    float r1 = baseRadius + Mathf.sin(a1 * 3f + e.fin() * 25f) * (4f * portalProgress);
                    float r2 = baseRadius + Mathf.sin(a2 * 3f + e.fin() * 25f) * (4f * portalProgress);

                    Lines.line(
                        e.x + Angles.trnsx(a1, r1), e.y + Angles.trnsy(a1, r1),
                        e.x + Angles.trnsx(a2, r2), e.y + Angles.trnsy(a2, r2)
                    );
                }

                Draw.color(darkVoid);
                Fill.circle(e.x, e.y, baseRadius * 0.85f);

                Draw.color(portalBase, neonLilac, e.fout());
                for (int i = 0; i < 3; i++) {
                    float spiralAngle = (i * 120f) + (e.fin() * 720f);
                    Lines.stroke(2.2f * portalProgress);
                    Lines.arc(e.x, e.y, baseRadius * 0.55f, 0.3f, spiralAngle);
                }

                Draw.color(lightningBlue);
                Angles.randLenVectors(e.id, 12, baseRadius * 1.6f, (x, y) -> {
                    Fill.circle(e.x + x, e.y + y, e.fout() * 2.5f);
                });
            }
        });

        // --- 2. JAW BITE HIT EFFECT (Red Solid Crunch) ---
        voidBite = new Effect(24f, e -> {
            TextureRegion topJaw = Core.atlas.find("krv-bite-jaw-top");
            TextureRegion bottomJaw = Core.atlas.find("krv-bite-jaw-bottom");

            float clampOffset = Mathf.curve(e.fin(), 0f, 0.2f);
            float currentDistance = (1f - clampOffset) * 16f;

            Draw.color(Color.white, e.fout());
            if (topJaw.found()) {
                Draw.rect(topJaw, e.x, e.y + currentDistance);
            }
            if (bottomJaw.found()) {
                Draw.rect(bottomJaw, e.x, e.y - currentDistance);
            }

            if (e.fin() > 0.2f) {
                Draw.color(Color.valueOf("ef4444"), Color.valueOf("991b1b"), e.fin());
                Angles.randLenVectors(e.id, 8, e.fin() * 16f, (x, y) -> {
                    Fill.circle(e.x + x, e.y + y, e.fout() * 2.4f);
                });
            }
        });

        // --- 3. BLUE HEALING AURA EFFECT ---
        blueHeal = new Effect(32f, e -> {
            Color blue = Color.valueOf("38bdf8");
            Color lightBlue = Color.valueOf("bae6fd");

            Draw.color(blue, lightBlue, e.fout());
            Lines.stroke(e.fout() * 2.5f);
            Lines.circle(e.x, e.y, e.fin() * 22f);

            Angles.randLenVectors(e.id, 6, e.fin() * 18f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, e.fout() * 2.2f);
            });
        });
    }
}
