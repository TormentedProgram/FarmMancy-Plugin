package me.tormented.farmmancy.Commands;

public class Commands {
    private final static Commands instance = new Commands();

    public static Commands getInstance() {
        return instance;
    }

    public MenuCommand menuCommand;
    public imbueCowCommand imbueCowCommand;
    public FarmMancerCommand farmMancerCommand;
    public ReloadConfigCommand reloadConfigCommand;
    public ChangeMenuCommand changeMenuCommand;
    public FarmAbilityCommand farmAbilityCommand;

    private Commands() {
    }

    public void load() {
        menuCommand = new MenuCommand();
        imbueCowCommand = new imbueCowCommand();
        farmMancerCommand = new FarmMancerCommand();
        reloadConfigCommand = new ReloadConfigCommand();
        changeMenuCommand = new ChangeMenuCommand();
        farmAbilityCommand = new FarmAbilityCommand();
    }
}
