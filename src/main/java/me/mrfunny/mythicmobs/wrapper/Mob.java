package me.mrfunny.mythicmobs.wrapper;

public class Mob {
    private String mobId;

    public Mob() {
        this.mobId = getClass().getSimpleName().replace("Mob", "");
        if(mobId.equals("")) {
            mobId = "DeveloperMob";
        }
    }
}
