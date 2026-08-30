package extra.entities.bullet;

import arc.graphics.Color;
import extra.content.KVREffects;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;

public class MeleeType extends BulletType {

    public MeleeType(float damage, float range) {
        super(0.001f, damage);

        this.range = range;
        this.hitSize = range;
        this.lifetime = 2f;

        this.drawSize = 0f;
        this.collides = true;
        this.collidesTiles = true;
        this.collidesAir = false;
        this.pierce = false;

        // Hit FX & Audio (Plays when teeth crunch an enemy)
        this.hitEffect = KVREffects.voidBite;
        this.hitSound = Vars.tree.loadSound("bite");
        this.hitSoundVolume = 1.2f;
        this.hitColor = Color.valueOf("ef4444");

        this.despawnEffect = Fx.none;
        this.shootEffect = Fx.none;
        this.smokeEffect = Fx.none;

        this.status = StatusEffects.slow;
        this.statusDuration = 45f;
    }

    public MeleeType(float damage) {
        this(damage, 38f);
    }

    @Override
    public void draw(Bullet b) {
        // Pure melee: no bullet sprites drawn
    }
}
