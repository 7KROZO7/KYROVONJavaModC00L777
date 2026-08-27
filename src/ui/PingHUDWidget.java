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
        hudTable.setPosition(24f, 130f, Align.bottomLeft);

        hudTable.table(Tex.pane, container -> {
            container.margin(4f);

            // 1. EMPTY DRAG HANDLE (Only drags the UI)
            Table dragGrip = container.table(Tex.buttonTrans, grip -> {
                grip.add("[#a080ff] ≡ []").style(mindustry.ui.Styles.outlineLabel).center();
            }).size(32f, 36f).padRight(4f).get();

            dragGrip.addListener(new DragListener() {
                @Override
                public void drag(InputEvent event, float x, float y, int pointer) {
                    hudTable.moveBy(x - dragGrip.getWidth() / 2f, y - dragGrip.getHeight() / 2f);
                }
            });

            // 2. PRESSABLE BUTTON (Only opens dialogue / mites)
            container.button("[#c084fc]Ping[]", () -> {
                Unit ping = Groups.unit.find(u -> u.type == KVRUnits.ping && u.team == Vars.player.team());
                if (ping != null && ping.isValid()) {
                    if (PingBubbleUI.isVisible()) {
                        PingBubbleUI.hide();
                    } else {
                        PingBubbleUI.show(ping);
                    }
                }
            }).size(64f, 36f);
        });

        Vars.ui.hudGroup.addChild(hudTable);
    }
}
