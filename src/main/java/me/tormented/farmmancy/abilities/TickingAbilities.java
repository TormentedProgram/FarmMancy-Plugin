package me.tormented.farmmancy.abilities;

import me.tormented.farmmancy.farmmancer.FarmMancer;
import me.tormented.farmmancy.farmmancer.FarmMancerManager;

public class TickingAbilities implements Runnable {
    private static final TickingAbilities instance = new TickingAbilities();

    public static TickingAbilities getInstance() {
        return instance;
    }

    private TickingAbilities() {
    }

    @Override
    public void run() {
        for (FarmMancer farmMancer : FarmMancerManager.getInstance().farmMancers) {
            farmMancer.tickEquippedAbilities();
        }
    }
}
