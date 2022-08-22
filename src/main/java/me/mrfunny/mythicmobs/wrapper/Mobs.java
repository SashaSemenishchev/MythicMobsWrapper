package me.mrfunny.mythicmobs.wrapper;

import io.lumine.mythic.core.mobs.ActiveMob;
import me.mrfunny.mythicmobs.internal.MobSignal;

public class Mobs {
    public static void sendSignal(ActiveMob mob, MobSignal signal) {
        mob.signalMob(mob.getEntity(), signal.toString());
    }

    public static String signalStr(MobSignal signal) {
        return signal.toString();
    }
}
