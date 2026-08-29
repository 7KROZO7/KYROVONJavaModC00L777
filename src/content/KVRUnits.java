package extra.content;

import arc.graphics.Color;
import extra.abilities.DevourAbility;
import extra.ai.CompanionAI;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.part.DrawPart.PartMove;
import mindustry.entities.part.DrawPart.PartProgress;
import mindustry.entities.part.RegionPart;
import mindustry.gen.CrawlUnit;
import mindustry.gen.UnitEntity;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class KVRUnits {
    public static UnitType ping;
    public static UnitType chasmBiter;

    public static void load() {
        // --- 1. PING ---
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

        // --- 2. CHASM BITER (Devour Predator) ---
        chasmBiter = new UnitType("chasm-biter") {{
            localizedName = "Chasm Biter";
            description = "A heavy segmented rift beast. Devours defeated prey to regenerate its armor and flesh.";
            constructor = CrawlUnit::create;

            speed = 0.75f;
            drag = 0.4f;
            hitSize = 14f;
            health = 720f;
            armor = 6f;
            omniMovement = false;
            rotateSpeed = 2.8f;

            // Worm segments
            segments = 4;
            segmentScl = 3f;
            segmentPhase = 5f;
            segmentMag = 0.5f;

            // Register Devour Ability
            abilities.add(new DevourAbility(0.05f));

            weapons.add(new Weapon("krv-chasm-biter-jaw") {{
                x = 0f;
                y = 5f;
                reload = 90f; // 1.5s per bite
                shootCone = 35f;
                mirror = false;
                top = true;
                shootSound = Vars.tree.loadSound("bite");

                parts.add(new RegionPart("-mandible") {{
                    x = 3.5f;
                    y = 1.5f;
                    moveRot = 35f;
                    moves.add(new PartMove(PartProgress.reload, -1f, -1f, 30f));
                    mirror = true;
                    under = false;
                }});

                // Bite Hitbox with 5% Devour Lifesteal on Kill
                bullet = new BasicBulletType(0f, 100f) {{
                    lifetime = 1f;
                    hitSize = 14f;
                    range = 14f;

                    drawSize = 0f;
                    width = 0f;
                    height = 0f;

                    hitEffect = KVREffects.voidBite;
                    despawnEffect = Fx.none;
                    shootEffect = Fx.none;
                    smokeEffect = Fx.none;

                    status = StatusEffects.slow;
                    statusDuration = 45f;
                }};
            }});
        }};
    }
}
