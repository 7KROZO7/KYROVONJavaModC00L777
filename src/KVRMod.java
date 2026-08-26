package extra;

import arc.Events;
import extra.content.KVREffects;
import extra.content.KVRUnits;
import mindustry.Vars;
import mindustry.game.EventType.Trigger;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.mod.Mod;

public class KVRMod extends Mod {

    public KVRMod() {
    }

    @Override
    public void loadContent() {
        KVREffects.load();
        KVRUnits.load();
    }

    @Override
    public void init() {
        // Runs every frame with zero overhead (instant spawn detection)
        Events.run(Trigger.update, () -> {
            if (!Vars.state.isPlaying() || Vars.player == null) return;

            Unit playerUnit = Vars.player.unit();
            if (playerUnit == null || !playerUnit.isValid() || playerUnit.dead) return;

            // Find existing Ping for player's team
            Unit ping = Groups.unit.find(u -> u.type == KVRUnits.ping && u.team == Vars.player.team());

            // If Ping is missing or destroyed, warp him in
            if (ping == null || !ping.isValid() || ping.dead) {
                float sx = playerUnit.x + 24f;
                float sy = playerUnit.y + 24f;

                KVREffects.warpRift.at(sx, sy);
                KVRUnits.ping.spawn(Vars.player.team(), sx, sy);
            }
        });
    }
}
