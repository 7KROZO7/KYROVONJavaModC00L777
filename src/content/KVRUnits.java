package extra.content;

import arc.graphics.Color;
import extra.ai.CompanionAI;
import extra.ai.MiteAI;
import mindustry.gen.UnitEntity;
import mindustry.type.UnitType;

public class KVRUnits {
    public static UnitType ping;
    public static UnitType riftMite;

    public static void load() {
        // --- PING ---
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
            physics = false;

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

        // --- RIFT MITE ---
        riftMite = new UnitType("rift-mite") {{
            localizedName = "Rift Mite";
            description = "A short-lived interdimensional mining drone.";
            controller = u -> new MiteAI(); // Custom lag-free AI
            constructor = UnitEntity::create;

            flying = true;
            hovering = true;
            shadowElevation = 0.15f;
            speed = 2.6f;
            accel = 0.15f;
            drag = 0.06f;
            hitSize = 6f;
            health = 150f;
            itemCapacity = 20;

            mineTier = 2;
            mineSpeed = 4.5f;
            mineRange = 70f;
            mineFloor = true;
            mineWalls = true;

            engineSize = 1.4f;
            engineColor = Color.valueOf("38bdf8");
            outlineColor = Color.valueOf("1e132e");
        }};
    }
}
