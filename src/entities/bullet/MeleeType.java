package extra.entities.bullet;

import arc.graphics.Color;
import extra.content.KVREffects;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;

public class MeleeType extends BulletType {

    public MeleeType(float damage, float range) {
        super(0.001f, damage);

        this.range = range;
        this.hitSize = range;
        this.lifetime = 2f; // Instant point-blank contact

        // Pure invisible hitbox
        this.drawSize = 0f;
        this.collides = true;
        this.collidesTiles = true;
        this.collidesAir = false; // Ground-only melee
        this.pierce = false;

        // Default Melee Effects & Status
        this.hitEffect = KVREffects.voidBite;
        this.despawnEffect = Fx.none;
        this.shootEffect = Fx.none;
        this.smokeEffect = Fx.none;
        this.hitColor = Color.valueOf("c084fc");

        this.status = StatusEffects.slow;
        this.statusDuration = 45f;
    }

    public MeleeType(float damage) {
        this(damage, 20f);
    }

    @Override
    public void draw(Bullet b) {
        // Pure melee: does not draw any bullet sprites or textures
    }
}
