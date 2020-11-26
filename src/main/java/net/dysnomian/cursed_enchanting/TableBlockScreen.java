package net.dysnomian.cursed_enchanting;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class TableBlockScreen extends CottonInventoryScreen<TableGuiDescription> {
    public TableBlockScreen(TableGuiDescription gui, PlayerEntity player, Text title) {
        super(gui, player, title);
	}
}
