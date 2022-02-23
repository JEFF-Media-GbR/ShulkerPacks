package me.darkolythe.shulkerpacks;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;

public final class ShulkerPacks extends JavaPlugin {

    ShulkerListener shulkerlistener;

    private static ShulkerPacks plugin;

    String prefix = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.BLUE.toString() + "ShulkerPacks" + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "] ";

    static Map<Player, ItemStack> openshulkers = new HashMap<>();
    Map<Player, Boolean> fromhand = new HashMap<>();
    Map<Player, Inventory> openinventories = new HashMap<>();
    Map<Player, Inventory> opencontainer = new HashMap<>();
    private Map<Player, Long> pvp_timer = new HashMap<>();
    boolean canopeninchests = true;
    boolean openpreviousinv = false;
    List<String> blacklist = new ArrayList<>();
    String defaultname = ChatColor.BLUE + "Shulker Pack";
    boolean pvp_timer_enabled = false;
    boolean shiftclicktoopen = false;
    boolean canopeninenderchest, canopeninbarrels, canplaceshulker, canopenininventory, canopeninair;
    float volume;

    /*
    Sets up the plugin
     */
    @Override
    public void onEnable() {
        plugin = this;
        shulkerlistener = new ShulkerListener(this);

        getServer().getPluginManager().registerEvents(shulkerlistener, this);

        getCommand("shulkerpacks").setExecutor(new CommandReload());

        ConfigHandler.loadConfig(this);

        @SuppressWarnings("unused")
		Metrics metrics = new Metrics(this);

        shulkerlistener.checkIfValid();

        getLogger().log(Level.INFO, (prefix + ChatColor.GREEN + "ShulkerPacks has been enabled!"));

        Bukkit.getScheduler().runTaskLater(this, () -> {
            if(Bukkit.getPluginManager().getPlugin("ChestSort") != null) {
                Bukkit.getPluginManager().registerEvents(new ChestSortListener(), this);
            }
        },1L); // Yeah, waiting 1 tick is a dirty hack. But since ChestSort softdepends on ShulkerPacks, I don't see another way. And tbh noone will use any shulkers within the first tick

    }

    /*
    Doesnt do much. Just says a message
     */
    @Override
    public void onDisable() {
        Iterator<Player> it = this.openinventories.keySet().iterator();
        while (it.hasNext()) {
            it.next().closeInventory();
        }
        getLogger().log(Level.INFO, (prefix + ChatColor.RED + "ShulkerPacks has been disabled!"));
    }


    public static ShulkerPacks getInstance() {
        return plugin;
    }


    public boolean getPvpTimer(Player player) {
        if (pvp_timer.containsKey(player)) {
            return System.currentTimeMillis() - pvp_timer.get(player) < 7000;
        }
        return false;
    }

    public void setPvpTimer(Player player) {
        if (pvp_timer_enabled) {
            pvp_timer.put(player, System.currentTimeMillis());
        }
    }
}
