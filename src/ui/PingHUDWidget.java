package extra.ui;

import arc.Core;
import arc.graphics.Color;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import extra.content.KVRUnits;
import mindustry.Vars;
import mindustry.gen.Groups;
import mindustry.gen.Tex;
import mindustry.gen.Unit;

public class PingHUDWidget {
    private static Table hudTable;
    private static float startX, startY;
    private static boolean isDragging = false;

    public static void build() {
        if (hudTable != null) return;

        hudTable = new Table();
        hudTable.setSize(44f, 44f);
        hudTable.setPosition(20f, 120f, Align.bottomLeft);

        // Ping HUD Box Styling
        hudTable.table(Tex.buttonOver, t -> {
            t.margin(4f);
            t.add("[#c084fc]●[]").style(mindustry.ui.Styles.outlineLabel).center();
        }).size(44f, 44f);

        // Drag vs Tap Listener
        hudTable.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                startX = x;
                startY = y;
                isDragging = false;
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                float dx = x - startX;
                float dy = y - startY;

                if (Math.abs(dx) > 6f || Math.abs(dy) > 6f) {
                    isDragging = true;
                }

                if (isDragging) {
                    hudTable.setPosition(hudTable.x + dx, hudTable.y + dy);
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // If it wasn't a drag, register as a clean Tap
                if (!isDragging) {
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
