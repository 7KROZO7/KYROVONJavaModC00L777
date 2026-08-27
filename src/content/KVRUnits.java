package extra.content;

import arc.graphics.Color;
import extra.ai.CompanionAI;
import mindustry.ai.types.MinerAI;
import mindustry.gen.UnitEntity;
import mindustry.type.UnitType;

public class KVRUnits {
    public static UnitType ping;
    public static UnitType riftMite;

    public static void load() {
        // --- PING (Companion Guide) ---
        ping = new UnitType("ping") {{
            localizedName = "Ping";
            description = "A friendly dimensional observer drone.";
            aiController = CompanionAI::new;
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

        // --- RIFT MITE (30-Second Mining Drone) ---
        riftMite = new UnitType("rift-mite") {{
            localizedName = "Rift Mite";
            description = "A short-lived interdimensional mining drone.";
            controller = u -> new MinerAI();
            constructor = UnitEntity::create;

            flying = true;
            drag = 0.06f;
            accel = 0.15f;
            speed = 2.4f;
            hitSize = 6f;
            health = 150f; // 150 HP / 5 HP per sec = Exactly 30 seconds lifespan
            itemCapacity = 20;

            mineTier = 2;   // Mines Bismuth, Coltan, Shungite, Fulgurite
            mineSpeed = 6.5f;

            targetable = false;
            hittable = false;
            isEnemy = false;
            playerControllable = false;

            engineSize = 1.4f;
            engineColor = Color.valueOf("38bdf8");
            outlineColor = Color.valueOf("1e132e");
        }};
    }
}
