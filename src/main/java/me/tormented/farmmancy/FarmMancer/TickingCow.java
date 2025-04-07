package me.tormented.farmmancy.FarmMancer;

import me.tormented.farmmancy.abilities.EventDistributor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TickingCow implements Runnable {

    private static final TickingCow instance = new TickingCow();

    public static TickingCow getInstance() {
        return instance;
    }

    public Set<FarmMancer> farmMancers = new HashSet<>();

    public HashMap<Player, FarmMancer> farmMancerMap = new HashMap<>();

    private TickingCow() {
    }

    public FarmMancer setCowMancer(Player player) {
        for (FarmMancer farmMancer : farmMancers) {
            if (farmMancer._player == player) {
                return farmMancer;
            }
        }
        FarmMancer farmMancer = new FarmMancer(player);
        EventDistributor.getInstance().playerAbilityMap.put(player.getUniqueId(), farmMancer);
        farmMancers.add(farmMancer);
        farmMancerMap.put(player, farmMancer);
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
        for (FarmMancer farmMancer : farmMancers) {
            farmMancer.tickEquippedAbilities();
        }
    }
}
