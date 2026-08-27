package extra.ui;

import arc.scene.event.DragListener;
import arc.scene.event.InputEvent;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import extra.content.KVRUnits;
import mindustry.Vars;
import mindustry.gen.Groups;
import mindustry.gen.Tex;
import mindustry.gen.Unit;

public class PingHUDWidget {
    private static Table hudTable;

    public static void build() {
        if (hudTable != null) return;

        hudTable = new Table();
        hudTable.setSize(44f, 44f);
        hudTable.setPosition(20f, 120f, Align.bottomLeft);

        hudTable.table(Tex.buttonOver, t -> {
            t.margin(4f);
            t.add("[#c084fc]●[]").style(mindustry.ui.Styles.outlineLabel).center();
        }).size(44f, 44f);

        // Official Arc DragListener
        hudTable.addListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                hudTable.moveBy(x - hudTable.getWidth() / 2f, y - hudTable.getHeight() / 2f);
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                // If dragged less than 6px total, treat as a Tap
                if (getDeltaX() < 6f && getDeltaY() < 6f) {
                    Unit ping = Groups.unit.find(u -> u.type == KVRUnits.ping && u.team == Vars.player.team());
                    if (ping != null && ping.isValid()) {
                        if (PingBubbleUI.isVisible()) {
                            PingBubbleUI.hide();
                        } else {
                            PingBubbleUI.show(ping);
                        }
                    }
                }
            }
        });

        Vars.ui.hudGroup.addChild(hudTable);
    }

    public static void setVisible(boolean visible) {
        if (hudTable != null) {
            hudTable.visible = visible;
        }
    }
}
