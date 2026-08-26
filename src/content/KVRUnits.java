package extra.content;

import arc.graphics.Color;
import extra.ai.CompanionAI;
import mindustry.gen.UnitEntity;
import mindustry.type.UnitType;

public class KVRUnits {
    public static UnitType ping;

    public static void load() {
        ping = new UnitType("ping") {{
            localizedName = "Ping";
            description = "A friendly dimensional guide drone.";
            
            // Link Companion Follow AI
            aiController = CompanionAI::new;
            constructor = UnitEntity::create;
            
            // Physical & Flight Settings
            flying = true;
            lowAltitude = true;
            speed = 4.2f;
            accel = 0.12f;
            drag = 0.05f;
            hitSize = 8f;
            health = 100f;
            physics = false;

            // Passive & Untargetable Flags
            targetable = false;
            hittable = false;
            isEnemy = false;
            playerControllable = false;
            logicControllable = false;
            drawMinimap = false;

            // Particle Trail & Engine Visuals
            engineSize = 2.2f;
            engineOffset = 4f;
            engineColor = Color.valueOf("c084fc");
            
            trailLength = 12;
            trailColor = Color.valueOf("a855f7");
            outlineColor = Color.valueOf("2a1645");
        }};
    }
}
