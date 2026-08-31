package extra.content;

import arc.Core;
import arc.audio.Sound;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import extra.abilities.DevourAbility;
import extra.ai.CompanionAI;
import extra.entities.bullet.MeleeType;
import mindustry.Vars;
import mindustry.entities.abilities.RegenAbility;
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
            outlineColor = Color.valueOf("c084fc"); // Light purple outline
        }};

        // --- 2. CHASM BITER (Segmented Worm with Devour Melee) ---
        chasmBiter = new UnitType("chasm-biter") {{
            localizedName = "Chasm Biter";
            description = "A heavy segmented rift beast. Devours defeated prey to regenerate its armor and flesh.";
            constructor = CrawlUnit::create;

            speed = 0.75f;
            drag = 0.4f;
            hitSize = 10f;
            health = 720f;
            armor = 6f;
            omniMovement = false;
            rotateSpeed = 2.8f;

            healColor = Color.valueOf("c084fc");    // Light purple heal flash
            outlineColor = Color.valueOf("c084fc"); // Light purple outline

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
                reload = 120f; // 2.0s per bite cycle
                shootCone = 360f;
                mirror = false;
                
                // Renders underneath the unit body layer
                top = false;
                layerOffset = -0.01f;

                // Sound played when the jaws snap shut
                Sound biteSound = Vars.tree.loadSound("krv-bite");
                if (biteSound == null || biteSound == Sounds.none) {
                    biteSound = Vars.tree.loadSound("bite");
                }
                shootSound迷
                shootSound = (biteSound != null && biteSound != Sounds.none) ? biteSound : Sounds.plantBreak;

                parts.add(new RegionPart() {{
                    name = "krv-chasm-biter-mandible";
                    x = 3.5f;
                    y = 6.0f;
                    mirror = true;
                    under = true;
                    layerOffset = -0.01f;

                    // 1. Resting: -50 deg wide V-shape
                    rotation = -50f;

                    // 2. Strike: Snaps 55 deg inward AND thrusts +1.5px forward
                    progress = PartProgress.recoil;
                    moveRot = 55f;
                    moveX = -1.5f;
                    moveY = 1.5f;
                }});

                // 300 Damage, point-blank reach (20px)
                bullet = new MeleeType(300f, 20f);
            }});
        }

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
