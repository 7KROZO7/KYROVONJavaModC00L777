package extra.content;

import arc.graphics.Color;
import extra.ai.CompanionAI;
import mindustry.content.StatusEffects;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.part.RegionPart;
import mindustry.gen.Sounds;
import mindustry.gen.UnitEntity;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.unit.LegsUnitType;
import mindustry.Vars;

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

        // --- 2. CHASM BITER (Ground Melee Unit) ---
        chasmBiter = new UnitType("chasm-biter") {{
            localizedName = "Chasm Biter";
            description = "A savage ground walker that closes distance to tear enemy armor with crushing mandibles.";
            constructor = LegsUnitType::create;

            speed = 1.1f;
            drag = 0.1f;
            hitSize = 10f;
            health = 240f;
            armor = 3f;
            legCount = 4;
            legLength = 12f;
            legForwardScl = 0.8f;
            legMoveSpace = 1.2f;
            hovering = false;

            weapons.add(new Weapon("krv-jaw") {{
                x = 0f;
                y = 4f;
                reload = 30f;
                shootCone = 45f;
                mirror = false;
                top = true;
                shootSound = Vars.tree.loadSound("bite"); // Loads assets/sounds/bite.ogg

                bullet = new BasicBulletType(3.0f, 55f) {{
                    lifetime = 6f;
                    hitSize = 10f;
                    
                    // Directly links custom Java bite animation:
                    hitEffect = KVREffects.voidBite;
                    despawnEffect = mindustry.content.Fx.none;
                    shootEffect = mindustry.content.Fx.none;
                    smokeEffect = mindustry.content.Fx.none;

                    status = StatusEffects.slow;
                    statusDuration = 30f; // 0.5s slow on hit
                }};
            }});
        }};
    }
}
