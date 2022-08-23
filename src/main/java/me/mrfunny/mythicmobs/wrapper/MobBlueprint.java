package me.mrfunny.mythicmobs.wrapper;

import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.utils.serialize.Position;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.Location;

import java.util.Optional;

public class MobBlueprint {
    private String mobId;

    public MobBlueprint() {
        this.mobId = getClass().getSimpleName().replace("Mob", "");
        if(mobId.equals("")) {
            mobId = "DeveloperMob";
        }
    }

    public MobBlueprint(String id) {
        this.mobId = id;
    }

    public MythicMob getMythicMob() {
        Optional<MythicMob> mob = Mobs.mobManager.getMythicMob(mobId);
        if(mob.isPresent()) {
            return mob.get();
        }
        throw new IllegalArgumentException("No mob present with ID " + mobId);
    }

    public SpawnedMob spawn(Location location) {
        ActiveMob mob = getMythicMob().spawn(Locations.fromBukkit(location), 0);
        return new SpawnedMob(mob);
    }
}
