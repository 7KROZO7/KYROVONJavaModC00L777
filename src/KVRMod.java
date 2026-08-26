package extra;

import arc.Events;
import arc.util.Time;
import extra.content.KVREffects;
import extra.content.KVRUnits;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.mod.Mod;

public class KVRMod extends Mod {

    public KVRMod() {
        // Constructor
    }

    @Override
    public void loadContent() {
        KVREffects.load();
        KVRUnits.load();
    }

    @Override
    public void init() {
        // Triggered every time you land in a sector or start a match
        Events.on(EventType.WorldLoadEvent.class, event -> {
            Time.runTask(60f, () -> { // Delay 1s for landing animation
                if (Vars.player != null && Vars.player.unit() != null) {
                    // Prevent duplicate Pings on the same team
                    boolean alreadyExists = Groups.unit.contains(u -> u.type == KVRUnits.ping && u.team == Vars.player.team());
                    
                    if (!alreadyExists) {
                        float spawnX = Vars.player.x + 20f;
                        float spawnY = Vars.player.y + 20f;

                        // Play Dimensional Warp Effect
                        KVREffects.warpRift.at(spawnX, spawnY);

                        // Spawn Ping
                        KVRUnits.ping.spawn(Vars.player.team(), spawnX, spawnY);
                    }
                }
            });
        });
    }
}
