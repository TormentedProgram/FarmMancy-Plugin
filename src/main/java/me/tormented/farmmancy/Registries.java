package me.tormented.farmmancy;

import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.abilities.AbilityRegistry;
import me.tormented.farmmancy.abilities.implementations.*;
import me.tormented.farmmancy.abilities.implementations.customs.*;

public class Registries {
    public static final AbilityRegistry abilityRegistry = new AbilityRegistry();

    static {
        abilityRegistry.register("cow", new AbilityFactory(CowAbility::new));
        abilityRegistry.register("pig", new AbilityFactory(PigAbility::new));
        abilityRegistry.register("chicken", new AbilityFactory(ChickenAbility::new));
        abilityRegistry.register("bee", new AbilityFactory(BeeAbility::new));
        abilityRegistry.register("squid", new AbilityFactory(SquidAbility::new));
        abilityRegistry.register("blaze", new AbilityFactory(BlazeAbility::new));
        abilityRegistry.register("warden", new AbilityFactory(WardenAbility::new));
        abilityRegistry.register("strider", new AbilityFactory(StriderAbility::new));
        abilityRegistry.register("custom/dio", new AbilityFactory(DioAbility::new));
        abilityRegistry.register("enderman", new AbilityFactory(EndermanAbility::new));
        abilityRegistry.register("evoker", new AbilityFactory(EvokerAbility::new));
        abilityRegistry.register("custom/yuyuko", new AbilityFactory(YuyukoAbility::new));
        abilityRegistry.register("custom/reisen", new AbilityFactory(ReisenAbility::new));
    }
}
