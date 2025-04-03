package me.tormented.farmmancy.FarmMancer;

import me.tormented.farmmancy.FarmConfig;
import me.tormented.farmmancy.FarmMancy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class FarmMancer {
    public static NamespacedKey magic_hoe_key = new NamespacedKey(FarmMancy.getInstance(), "magical_hoe");

    public Player _player;
    private final List<Cow> cows = new ArrayList<>();
    private final List<Pig> pigs = new ArrayList<>();
    private final List<Chicken> chickens = new ArrayList<>();

    private final List<LivingEntity> entities = new ArrayList<>();

    private final List<Bee> bees = new ArrayList<>();

    private static final float outerRadius = 3f;
    private static final float innerRadius = 2f;
    private static final float healingValue = 5f;

    private int startTick = 0;

    public static ItemStack Cowification(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        String itemName = item.getType().toString();
        String[] words = itemName.split("_");
        StringBuilder formattedNameBuilder = new StringBuilder();

        for (String word : words) {
            formattedNameBuilder.append(word.substring(0, 1).toUpperCase())  // Capitalize first letter
                    .append(word.substring(1).toLowerCase())  // Lowercase the rest
                    .append(" ");
        }

        String formattedName = formattedNameBuilder.toString().trim();

        if (itemMeta != null) {
            itemMeta.lore(List.of(
                    Component.text("As Legends foretold..", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("A powerful mage wielded this to vanquish the demon lord.", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)));

            itemMeta.setEnchantmentGlintOverride(true);

            Component displayName = itemMeta.displayName(); // Adventure API
            String funnyName = (displayName != null) ? displayName.toString() : formattedName;

            itemMeta.customName(Component.text("Magical " + funnyName + " of Destruction", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));

            itemMeta.getPersistentDataContainer().set(FarmMancer.magic_hoe_key, PersistentDataType.BYTE, (byte) 1);

            item.setItemMeta(itemMeta);
        }
        return item;
    }

    public FarmMancer(Player player) {
        _player = player;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.getPersistentDataContainer().has(magic_hoe_key, PersistentDataType.BYTE)) {
                    return;
                }
            }
        }

        ItemStack MagicHoe = Cowification(new ItemStack(Material.NETHERITE_HOE, 1));

        player.give(MagicHoe);
    }

    public void cleanup(boolean kill) {
        for (LivingEntity entity : entities) {
            if (kill) {
                entity.setHealth(0f);
            } else {
                entity.remove();
            }
        }
        cows.clear();
        pigs.clear();
        chickens.clear();
        bees.clear();
        entities.clear();
        AttributeInstance attribute = _player.getAttribute(Attribute.MAX_HEALTH);
        if (attribute != null) {
            attribute.setBaseValue(attribute.getDefaultValue());
        }
        _player = null;
    }

    public void beeAbility(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        player.setHealth(player.getHealth() - 8f);

        Vector velocityVector = player.getVelocity();
        velocityVector.setY(2f);
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 240, 1));
        player.setVelocity(velocityVector);
    }

    public void pigAbility(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealthAttribute != null) {
            double maxHealth = maxHealthAttribute.getValue();

            if (player.getHealth() < maxHealth) {
                Pig pig = (Pig) event.getRightClicked();
                pigs.remove(pig);
                Location location = player.getLocation();
                Vector direction = location.getDirection();
                Location targetLocation = location.add(direction.multiply(1));
                pig.teleport(targetLocation);
                pig.setHealth(0);
                player.heal(healingValue);
            }
        }
    }


    public void chickenAbility(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        Chicken chicken = (Chicken) event.getRightClicked();
        chickens.remove(chicken);

        Location loc = player.getLocation();
        Vector direction = loc.getDirection();
        Location targetLoc = loc.add(direction.multiply(1));

        chicken.teleport(targetLoc);
        chicken.setHealth(0);

        Vector velocityVector = player.getVelocity();
        velocityVector.setY(1.1f);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 80, 1));
        player.setVelocity(velocityVector);
    }

    public void cowAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (cows.isEmpty()) {
            return;
        }

        Cow flingingCow = cows.getLast();

        if (!flingingCow.hasMetadata("FarmMancy_OwnedMob")) {
            return;
        }

        cows.remove(flingingCow);

        Location loc = player.getLocation();
        Vector direction = loc.getDirection();
        Location targetLoc = loc.add(direction.multiply(outerRadius));

        flingingCow.removeMetadata("FarmMancy_OwnedMob", FarmMancy.getInstance());
        flingingCow.setMetadata("FarmMancy_Projectile", new FixedMetadataValue(FarmMancy.getInstance(), this));
        flingingCow.teleport(targetLoc);

        Vector velocity = direction.multiply(FarmConfig.getInstance().getCowShootVelocity());

        flingingCow.setVelocity(velocity);
    }

    public void createCows(int amount, boolean isBaby) {
        for (int i = 0; i < amount; i++) {
            Cow specialCow = _player.getLocation().getWorld().spawn(_player.getLocation(), Cow.class);

            if (isBaby) {
                specialCow.setBaby();
            }

            specialCow.setMetadata("FarmMancy_OwnedMob", new FixedMetadataValue(FarmMancy.getInstance(), this));

            cows.add(specialCow);
        }
    }

    public void createPigs(int amount, boolean isBaby) {
        for (int i = 0; i < amount; i++) {
            Pig specialPig = _player.getLocation().getWorld().spawn(_player.getLocation(), Pig.class);
            if (isBaby) {
                specialPig.setBaby();
            }

            specialPig.setMetadata("FarmMancy_OwnedMob", new FixedMetadataValue(FarmMancy.getInstance(), this));

            pigs.add(specialPig);
        }
    }

    public void createChickens(int amount, boolean isBaby) {
        for (int i = 0; i < amount; i++) {
            Chicken specialChicken = _player.getLocation().getWorld().spawn(_player.getLocation(), Chicken.class);

            if (isBaby) {
                specialChicken.setBaby();
            }

            specialChicken.setMetadata("FarmMancy_OwnedMob", new FixedMetadataValue(FarmMancy.getInstance(), this));

            chickens.add(specialChicken);
        }
    }

    public void createAll(int amount, boolean isBaby) {
        startTick = FarmMancy.getInstance().getServer().getCurrentTick();

        Bee specialBee = _player.getLocation().getWorld().spawn(_player.getLocation(), Bee.class);
        specialBee.setMetadata("FarmMancy_OwnedMob", new FixedMetadataValue(FarmMancy.getInstance(), this));
        bees.add(specialBee);

        createPigs(amount, isBaby);
        createCows(amount, isBaby);
        createChickens(amount, isBaby);

        entities.addAll(cows.stream().map(entity -> (LivingEntity) entity).toList());
        entities.addAll(pigs.stream().map(entity -> (LivingEntity) entity).toList());
        entities.addAll(chickens.stream().map(entity -> (LivingEntity) entity).toList());
    }

    public void tick() {
        float lifetime = FarmMancy.getInstance().getServer().getCurrentTick() - startTick;
        lifetime = (lifetime / 8);

        AttributeInstance attribute = _player.getAttribute(Attribute.MAX_HEALTH);
        if (attribute != null) {
            attribute.setBaseValue(attribute.getDefaultValue() + (pigs.size() * 2.5));
        }

        for (int i = 0; i < cows.size(); i++) {
            float rotation = (float) (((double) i / cows.size()) * Math.TAU + lifetime);
            Cow cow = cows.get(i);
            if (cow.isDead()) {
                cows.remove(cow);
                continue;
            }
            cow.teleport(_player.getLocation().setRotation((float) Math.toDegrees(rotation), 0f).add(
                    Math.cos(rotation) * outerRadius,
                    1f,
                    Math.sin(rotation) * outerRadius
            ));
        }

        for (int i = 0; i < bees.size(); i++) {
            Location bottomLocation = new Location(_player.getWorld(), _player.getLocation().getX(), _player.getLocation().getY() + 3, _player.getLocation().getZ());
            Bee bee = bees.get(i);
            if (bee.isDead()) {
                bees.remove(bee);
                continue;
            }
            bee.teleport(bottomLocation);
        }

        for (int i = 0; i < pigs.size(); i++) {
            float rotation = -(float) (((double) i / pigs.size()) * Math.TAU + lifetime);
            Pig pig = pigs.get(i);
            if (pig.isDead()) {
                pigs.remove(pig);
                continue;
            }
            pig.teleport(_player.getLocation().setRotation((float) Math.toDegrees(rotation), 0f).add(
                    Math.cos(rotation) * innerRadius,
                    3f,
                    Math.sin(rotation) * innerRadius
            ));
        }

        for (int i = 0; i < chickens.size(); i++) {
            float rotation = -(float) (((double) i / chickens.size()) * Math.TAU + lifetime);
            Chicken chicken = chickens.get(i);
            if (chicken.isDead()) {
                chickens.remove(chicken);
                continue;
            }
            chicken.teleport(_player.getLocation().setRotation((float) Math.toDegrees(rotation), 0f).add(
                    Math.cos(rotation) * innerRadius,
                    0f,
                    Math.sin(rotation) * innerRadius
            ));
        }
    }
}
