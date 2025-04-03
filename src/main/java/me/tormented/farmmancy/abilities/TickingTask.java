package me.tormented.farmmancy.abilities;

import me.tormented.farmmancy.FarmMancer.FarmMancer;

public class TickingTask implements Runnable {

    private static final TickingTask instance = new TickingTask();

    public static TickingTask getInstance() {
        return instance;
    }
    private TickingTask() {};

    @Override
    public void run() {
        for (Ability ability : EventDistributor.getInstance().entityMobunitionAbilityMap.values()) {
            if (ability instanceof Hook.Ticking ticking) ticking.onTick(Hook.CallerSource.TRACKED_ENTITY);
        }
        for (FarmMancer farmMancer : EventDistributor.getInstance().playerAbilityMap.values()) {
            for (Ability ability : farmMancer.getEquippedAbilities()) {
                if (ability instanceof Hook.Ticking ticking) ticking.onTick(Hook.CallerSource.PLAYER);
            }
        }
    }
}
