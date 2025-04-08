package me.tormented.farmmancy.FarmMancer;

import me.tormented.farmmancy.abilities.AbilityRegistry;
import me.tormented.farmmancy.abilities.EventDistributor;
import me.tormented.farmmancy.abilities.implementations.*;
import me.tormented.farmmancy.abilities.implementations.customs.DioAbility;
import me.tormented.farmmancy.abilities.implementations.customs.YuyukoAbility;
import me.tormented.farmmancy.abilities.implementations.customs.ReisenAbility;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TickingCow implements Runnable {

    public static AbilityRegistry abilityRegistry = new AbilityRegistry();

    static {
        abilityRegistry.register("cow", CowAbility::new);
        abilityRegistry.register("pig", PigAbility::new);
        abilityRegistry.register("chicken", ChickenAbility::new);
        abilityRegistry.register("bee", BeeAbility::new);
        abilityRegistry.register("strider", StriderAbility::new);
        abilityRegistry.register("custom/dio", DioAbility::new);
        abilityRegistry.register("custom/yuyuko", YuyukoAbility::new);
        abilityRegistry.register("custom/reisen", ReisenAbility::new);
    }

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
                farmMancerMap.remove(player);
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
