package me.tormented.farmmancy.inventoryMenu;

import org.jetbrains.annotations.NotNull;

public final class Fillers {

    private Fillers() {
    }

    public static class FillAll implements MenuFiller {

        private final MenuItem item;

        public FillAll(@NotNull MenuItem item) {
            this.item = item;
        }

        @Override
        public void fillMenu(@NotNull Menu menu) {
            for (int y = 1; y <= menu.getRows(); y++) {
                for (int x = 1; x <= menu.getColumns(); x++) {
                    if (menu.getItem(x, y) != null) continue;
                    menu.setItem(x, y, item.clone());
                }
            }
        }
    }

    public static class FillBorder implements MenuFiller {

        private final MenuItem item;
        private final int topLeftOffsetX;
        private final int topLeftOffsetY;
        private final int bottomRightOffsetX;
        private final int bottomRightOffsetY;


        public FillBorder(@NotNull MenuItem item) {
            this.item = item;
            this.topLeftOffsetX = 0;
            this.topLeftOffsetY = 0;
            this.bottomRightOffsetX = 0;
            this.bottomRightOffsetY = 0;
        }

        public FillBorder(@NotNull MenuItem item, int topLeftOffsetX, int topLeftOffsetY, int bottomRightOffsetX, int bottomRightOffsetY) {
            this.item = item;
            this.topLeftOffsetX = topLeftOffsetX;
            this.topLeftOffsetY = topLeftOffsetY;
            this.bottomRightOffsetX = bottomRightOffsetX;
            this.bottomRightOffsetY = bottomRightOffsetY;
        }

        @Override
        public void fillMenu(@NotNull Menu menu) {
            for (int y = 1 + topLeftOffsetY; y <= menu.getRows() - bottomRightOffsetY; y++) {
                menu.setItem(1 + topLeftOffsetX, y, item.clone());
                menu.setItem(menu.getColumns() - bottomRightOffsetX, y, item.clone());
            }
            for (int x = 2 + topLeftOffsetX; x <= menu.getColumns() - 1 - bottomRightOffsetX; x++) {
                menu.setItem(x, 1 + topLeftOffsetY, item.clone());
                menu.setItem(x, menu.getRows() - bottomRightOffsetY, item.clone());
            }
        }
    }
}