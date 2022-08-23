package me.mrfunny.mythicmobs.wrapper;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.mobs.MobExecutor;
import me.mrfunny.mythicmobs.internal.MobSignal;

public class Mobs {

    public static MythicBukkit MYTHIC_MOBS = null;

    public static MobExecutor mobManager;

    public static void init() {
        MYTHIC_MOBS = MythicBukkit.inst();
        mobManager = MYTHIC_MOBS.getMobManager();
    }

    public static void sendSignal(ActiveMob mob, MobSignal signal) {
        mob.signalMob(mob.getEntity(), signal.toString());
    }

    public static String signalStr(MobSignal signal) {
        return signal.toString();
    }
}
