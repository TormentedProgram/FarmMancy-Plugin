package me.tormented.farmmancy.farmmancer;

import me.tormented.farmmancy.abilities.EventDistributor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class FarmMancerManager {
    private static final FarmMancerManager instance = new FarmMancerManager();

    public static FarmMancerManager getInstance() {
        return instance;
    }
    
    public final HashSet<FarmMancer> FarmMancerToUnload = new HashSet<>();

    public final Set<FarmMancer> farmMancers = new HashSet<>();
    public final HashMap<Player, FarmMancer> farmMancerMap = new HashMap<>();

    public FarmMancer setFarmMancer(Player player) {
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

    public void removeFarmMancer(Player player) {
        for (FarmMancer farmMancer : farmMancers) {
            if (farmMancer._player == player) {
                farmMancer.deactivateAll(true);
                farmMancerMap.remove(player);
                farmMancers.remove(farmMancer);
            }
        }
    }
}
