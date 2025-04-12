package me.tormented.farmmancy.commands;

public class Commands {
    private final static Commands instance = new Commands();

    public static Commands getInstance() {
        return instance;
    }

    public MenuSpawnCommand menuSpawnCommand;
    public ImbueCowCommand imbueCowCommand;
    public FarmMancyCommand farmMancyCommand;
    public ChangeMenuCommand changeMenuCommand;
    public FarmAbilityCommand farmAbilityCommand;
    public FarmUnlockCommand farmUnlockCommand;
    public FarmMancerCommand farmMancerCommand;

    private Commands() {
    }

    public void load() {
        menuSpawnCommand = new MenuSpawnCommand();
        imbueCowCommand = new ImbueCowCommand();
        farmMancyCommand = new FarmMancyCommand();
        changeMenuCommand = new ChangeMenuCommand();
        farmAbilityCommand = new FarmAbilityCommand();
        farmUnlockCommand = new FarmUnlockCommand();
        farmMancerCommand = new FarmMancerCommand();
    }
}
