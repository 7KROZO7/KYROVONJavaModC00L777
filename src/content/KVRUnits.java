package extra.content;

import arc.graphics.Color;
import arc.struct.Seq;
import extra.ai.CompanionAI;
import mindustry.ai.types.MinerAI;
import mindustry.content.Items;
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

        // --- RIFT MITE ---
        riftMite = new UnitType("rift-mite") {{
            localizedName = "Rift Mite";
            description = "A short-lived interdimensional mining drone.";
            controller = u -> new MinerAI();
            constructor = UnitEntity::create;

            flying = true;
            drag = 0.06f;
            accel = 0.15f;
            speed = 2.8f;
            hitSize = 6f;
            health = 150f; // 150 HP / 5 HP per sec = 30-second lifespan
            itemCapacity = 20;

            // Ore extraction parameters
            mineFloor = true;
            mineWalls = true;
            mineTier = 2;
            mineSpeed = 6.5f;
            mineRange = 60f;

            // Targetable ores for Serpulo/Erekir & Kyrovon fallback
            mineItems = Seq.with(
                Items.copper, Items.lead, Items.coal, Items.titanium,
                Items.beryllium, Items.graphite
            );

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
