package me.tormented.farmmancy.inventoryMenu;

public abstract sealed class ClickResponse permits
        ClickResponse.Nothing,
        ClickResponse.CloseMenu,
        ClickResponse.NavigateMenu {

    public static final class Nothing extends ClickResponse {
    }

    public static final class CloseMenu extends ClickResponse {
    }

    public static final class NavigateMenu extends ClickResponse {
        private final String key;

        public NavigateMenu(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

}
