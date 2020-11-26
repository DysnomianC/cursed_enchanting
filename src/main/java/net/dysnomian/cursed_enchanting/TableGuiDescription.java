package net.dysnomian.cursed_enchanting;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class TableGuiDescription extends SyncedGuiDescription {
    private static final int INVENTORY_SIZE = 2;
    
    public static final Identifier ID = new Identifier("cursed_enchanting", "cursed_enchanting_table");

    public TableGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(CursedEnchantingMain.TABLE_SCREEN_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context));

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(300, 200);

        WItemSlot itemSlot = WItemSlot.of(blockInventory, 0);
        root.add(itemSlot, 4, 1);

        root.add(this.createPlayerInventoryPanel(), 0, 3);

        root.validate(this);
    }
}