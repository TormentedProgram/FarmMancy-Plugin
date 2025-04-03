package me.tormented.farmmancy.Commands;

public class Commands {
    private final static Commands instance = new Commands();

    public static Commands getInstance() {
        return instance;
    }

    public MenuCommand menuCommand;
    public imbueCowCommand imbueCowCommand;
    public SetFarmMancerCommand setFarmMancerCommand;
    public ReloadConfigCommand reloadConfigCommand;
    public ChangeMenuCommand changeMenuCommand;

<<<<<<< Updated upstream
    private Commands() {}
=======
    private Commands() {
    }
>>>>>>> Stashed changes

    public void load() {
        menuCommand = new MenuCommand();
        imbueCowCommand = new imbueCowCommand();
        setFarmMancerCommand = new SetFarmMancerCommand();
        reloadConfigCommand = new ReloadConfigCommand();
        changeMenuCommand = new ChangeMenuCommand();
    }
}
