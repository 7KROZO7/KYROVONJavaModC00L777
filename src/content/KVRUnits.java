package extra.content;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import extra.abilities.DevourAbility;
import extra.ai.CompanionAI;
import extra.entities.bullet.MeleeType;
import mindustry.entities.abilities.RegenAbility;
import mindustry.entities.part.DrawPart.PartMove;
import mindustry.entities.part.DrawPart.PartProgress;
import mindustry.entities.part.RegionPart;
import mindustry.gen.CrawlUnit;
import mindustry.gen.Sounds;
import mindustry.gen.UnitEntity;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class KVRUnits {
    public static UnitType ping;
    public static UnitType chasmBiter;

    public static void load() {
        // --- 1. PING (Companion Drone) ---
        ping = new UnitType("ping") {{
            localizedName = "Ping";
            description = "A friendly dimensional observer drone.";
            controller = u -> new CompanionAI();
            constructor = UnitEntity::create;

            flying = true;
            lowAltitude = true;
            speed = 4.2f;
            accel = 0.12f;
            drag = 0.05f;
            hitSize = 8f;
            health = 100f;
            physics = true;

            targetable = false;
            hittable = false;
            isEnemy = false;
            playerControllable = false;
            logicControllable = false;
            drawMinimap = false;

            engineSize = 2.2f;
            engineOffset = 4f;
            engineColor = Color.valueOf("c084fc");
            trailLength = 12;
            trailColor = Color.valueOf("a855f7");
            outlineColor = Color.valueOf("2a1645");
        }};

        // --- 2. CHASM BITER (Segmented Worm with Devour Melee) ---
        chasmBiter = new UnitType("chasm-biter") {{
            localizedName = "Chasm Biter";
            description = "A heavy segmented rift beast. Devours defeated prey to regenerate its armor and flesh.";
            constructor = CrawlUnit::create;

            speed = 0.75f;
            drag = 0.4f;
            hitSize = 10f; // Adjusted for 16x16 sprite
            health = 720f;
            armor = 6f;
            omniMovement = false;
            rotateSpeed = 2.8f;

            drawBody = false;
            drawCell = false;

            // Vanilla CrawlUnit terrain & block crushing physics
            crushDamage = 15f;
            crushFragile = true;
            crawlSlowdown = 0.5f;
            crawlSlowdownFrac = 0.5f;

            // Worm segments (matches segment0, segment1, segment2)
            segments = 3;
            segmentScl = 3f;
            segmentPhase = 5f;
            segmentMag = 0.5f;

            // Passive HP Regen & Kill Devour
            abilities.add(new RegenAbility() {{
                amount = 0.8f;
            }});
            abilities.add(new DevourAbility(0.05f));

            weapons.add(new Weapon("chasm-biter-jaw") {{
                x = 0f;
                y = 0f;
                reload = 90f; // 1.5s per bite
                shootCone = 360f;
                mirror = false;
                top = true;
                shootSound = Sounds.none;

                parts.add(new RegionPart("-mandible") {{
                    x = 3.5f;
                    y = 6.0f; // Sits at the front rim (+8px boundary) of 16x16 canvas
                    progress = PartProgress.reload;
                    moveRot = 35f;
                    moves.add(new PartMove(PartProgress.reload, -0.5f, -0.5f, 25f));
                    mirror = true;
                    under = true; // Tucked underneath head
                }});

                // 200 Damage, point-blank melee reach
                bullet = new MeleeType(200f, 20f);
            }});
        }

        // Texture Loader Override to resolve all segments cleanly from mod atlas
        @Override
        public void load() {
            super.load();
            segmentRegions = new TextureRegion[segments];
            for (int i = 0; i < segments; i++) {
                segmentRegions[i] = Core.atlas.find(
                    "krv-chasm-biter-segment" + i,
                    Core.atlas.find("chasm-biter-segment" + i, Core.atlas.find("krv-chasm-biter-segment", Core.atlas.find("error")))
                );
            }
        }};
    }
}
