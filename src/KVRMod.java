package extra;

import arc.Events;
import arc.util.Time;
import extra.content.KVREffects;
import extra.content.KVRUnits;
import extra.ui.PingBubbleUI;
import extra.ui.PingHUDWidget;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.EventType.Trigger;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.mod.Mod;

public class KVRMod extends Mod {

    @Override
    public void loadContent() {
        KVREffects.load();
        KVRUnits.load();
    }

    @Override
    public void init() {
        PingHUDWidget.build();

        // --- DEVOUR KILL HEALING HOOK ---
        Events.on(EventType.UnitDestroyEvent.class, event -> {
            if (event.unit == null) return;

            Unit victim = event.unit;
            Groups.unit.each(killer -> killer.type == KVRUnits.chasmBiter && killer.team != victim.team && killer.within(victim, 38f), killer -> {
                float healAmount = Math.max(victim.maxHealth * 0.05f, killer.maxHealth * 0.05f);
                killer.heal(healAmount);
                KVREffects.purpleHeal.at(killer.x, killer.y); // Light purple healing pulse
            });
        });

        // Main game update loop
        Events.run(Trigger.update, () -> {
            if (!Vars.state.isPlaying() || Vars.player == null) return;

            PingBubbleUI.updatePosition();

            // 30s Mite Lifespan Loop
            Groups.unit.each(u -> u.type != null && u.type.name != null && u.type.name.contains("rift-mite"), u -> {
                u.health -= (150f / (30f * 60f)) * Time.delta;
                if (u.health <= 0) {
                    KVREffects.warpRift.at(u.x, u.y);
                    u.remove();
                }
            });

            Unit playerUnit = Vars.player.unit();
            if (playerUnit == null || !playerUnit.isValid() || playerUnit.dead) return;

            Unit ping = Groups.unit.find(u -> u.type != null && u.type.name != null && u.type.name.contains("ping") && u.team == Vars.player.team());

            if (ping == null || !ping.isValid() || ping.dead) {
                float sx = playerUnit.x + 24f;
                float sy = playerUnit.y + 24f;

                KVREffects.warpRift.at(sx, sy);
                Unit newPing = KVRUnits.ping.create(Vars.player.team());
                if (newPing != null) {
                    newPing.set(sx, sy);
                    newPing.elevation = 1f;
                    newPing.add();

                    PingBubbleUI.showGreeting(newPing);
                }
            }
        });
    }
}
