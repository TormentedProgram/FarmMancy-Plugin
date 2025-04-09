package me.tormented.farmmancy;

import me.tormented.farmmancy.abilities.AbilityRegistry;
import me.tormented.farmmancy.abilities.implementations.*;
import me.tormented.farmmancy.abilities.implementations.customs.*;

public class Registries {
    public static final AbilityRegistry abilityRegistry = new AbilityRegistry();

    static {
        abilityRegistry.register("cow", CowAbility::new);
        abilityRegistry.register("pig", PigAbility::new);
        abilityRegistry.register("chicken", ChickenAbility::new);
        abilityRegistry.register("bee", BeeAbility::new);
        abilityRegistry.register("squid", SquidAbility::new);
        abilityRegistry.register("blaze", BlazeAbility::new);
        abilityRegistry.register("warden", WardenAbility::new);
        abilityRegistry.register("strider", StriderAbility::new);
        abilityRegistry.register("custom/dio", DioAbility::new);
        abilityRegistry.register("enderman", EndermanAbility::new);
        abilityRegistry.register("evoker", EvokerAbility::new);
        abilityRegistry.register("custom/yuyuko", YuyukoAbility::new);
        abilityRegistry.register("custom/reisen", ReisenAbility::new);
    }
}
