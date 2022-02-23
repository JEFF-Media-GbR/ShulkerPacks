package me.darkolythe.shulkerpacks;

import de.jeff_media.chestsort.api.ChestSortEvent;
import de.jeff_media.chestsort.api.ChestSortPostSortEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChestSortListener implements Listener {

    @EventHandler
    public void onSort(ChestSortPostSortEvent event) {
        ChestSortEvent sortEvent = event.getChestSortEvent();
        if(sortEvent.isCancelled()) return;
        if(!(event.getChestSortEvent().getPlayer() instanceof Player)) return;
        ShulkerListener.saveShulker((Player) event.getChestSortEvent().getPlayer(),event.getChestSortEvent().getPlayer().getOpenInventory().getTitle());
    }
}
