package extra.content;

import arc.graphics.Color;
import mindustry.gen.UnitEntity;
import mindustry.type.UnitType;

public class KVRUnits {
    public static UnitType ping;

    public static void load() {
        ping = new UnitType("ping") {{
            constructor = UnitEntity::create;
            
            // Physical & Flight Settings
            flying = true;
            lowAltitude = true;
            speed = 3.8f;
            accel = 0.08f;
            drag = 0.04f;
            hitSize = 8f;
            health = 100f;
            physics = false; // Floats freely through structures

            // Passive & Untargetable Flags
            targetable = false;
            hittable = false;
            isEnemy = false;
            playerControllable = false;
            logicControllable = false;
            drawMinimap = false;

            // Purple Engine & Trail Particle Visuals
            engineSize = 2.2f;
            engineOffset = 4f;
            engineColor = Color.valueOf("c084fc");
            
            trailLength = 12;
            trailColor = Color.valueOf("a855f7");
            
            outlineColor = Color.valueOf("2a1645");
        }};
    }
}
