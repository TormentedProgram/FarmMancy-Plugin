package me.tormented.farmmancy;

import me.tormented.farmmancy.abilities.Ability;
import me.tormented.farmmancy.abilities.EventDistributor;
import me.tormented.farmmancy.farmmancer.FarmMancer;
import me.tormented.farmmancy.farmmancer.FarmMancerManager;

public class DatabaseAutosave implements Runnable {

    private static final DatabaseAutosave instance = new DatabaseAutosave();

    public static DatabaseAutosave getInstance() {
        return instance;
    }
    private DatabaseAutosave() {}

    @Override
    public void run() {
        for (FarmMancer farmMancer : FarmMancerManager.getInstance().FarmMancerToUnload) {
            if (farmMancer != null) {
                FarmMancerManager.getInstance().FarmMancerToUnload.remove(farmMancer);
                farmMancer.deactivateAll(false);
                for (Ability ability : farmMancer.getEquippedAbilities()) {
                    if (ability != null) {
                        Ability.unloadAbility(ability.id);
                    }
                }
                FarmMancerManager.getInstance().farmMancerMap.remove(farmMancer.getPlayer());
                FarmMancerManager.getInstance().farmMancers.remove(farmMancer);
                EventDistributor.getInstance().playerAbilityMap.remove(farmMancer.getPlayer().getUniqueId());
                FarmMancy.getInstance().getLogger().info("Successfully unloaded " + farmMancer.getPlayer().getName() + "'s FarmMancer.");
            }
        }
    }
}