package me.tormented.farmmancy.FarmMancer;

import me.tormented.farmmancy.abilities.EventDistributor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class TickingCow implements Runnable {

    private static final TickingCow instance = new TickingCow();

    public static TickingCow getInstance() {
        return instance;
    }

    public Set<FarmMancer> farmMancers = new HashSet<>();


    private TickingCow() {}

    public FarmMancer setCowMancer(Player player) {
        for (FarmMancer farmMancer : farmMancers) {
            if (farmMancer._player == player) {
                return farmMancer;
            }
        }
        FarmMancer farmMancer = new FarmMancer(player);
        EventDistributor.getInstance().playerAbilityMap.put(player.getUniqueId(), farmMancer);
        farmMancers.add(farmMancer);
        return farmMancer;
    }

    public void removeCowMancer(Player player) {
        for (FarmMancer farmMancer : farmMancers) {
            if (farmMancer._player == player) {
                farmMancer.deactivateAll(true);
                farmMancers.remove(farmMancer);
            }
        }
    }

    @Override
    public void run() {

    }
}
