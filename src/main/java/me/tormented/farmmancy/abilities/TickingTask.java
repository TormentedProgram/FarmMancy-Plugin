package me.tormented.farmmancy.abilities;

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
    }
}
