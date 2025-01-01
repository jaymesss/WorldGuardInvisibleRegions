package me.jaymesss.invisibleregions;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import lombok.Getter;
import me.jaymesss.invisibleregions.listener.InvisibleListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class InvisibleRegions extends JavaPlugin {

    @Getter
    protected static InvisibleRegions instance;

    private StateFlag flag;

    private boolean debug;
    @Getter
    private boolean bypassPermission;

    private final List<UUID> invisible = Lists.newCopyOnWriteArrayList();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        debug = getConfig().getBoolean("debug");
        bypassPermission = getConfig().getBoolean("bypass-permission");
        getServer().getPluginManager().registerEvents(new InvisibleListener(), this);
    }

    @Override
    public void onDisable() {
        for (UUID uuid : invisible) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            Bukkit.getOnlinePlayers().forEach(p -> {
                p.showPlayer(this, player);
            });
        }
    }

    @Override
    public void onLoad() {
        registerCustomFlag();
    }

    public void debug(String string) {
        if (!debug) return;
        getLogger().log(Level.INFO,string + " (DEBUG)");
    }

    private void registerCustomFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("invisibility", false);
            registry.register(flag);
            this.flag = flag;
        }
        catch (FlagConflictException ex) {
            getLogger().log(Level.SEVERE, "A WorldGuard flag with the name 'invisibility' has already been registered.");
        }
    }

    public boolean isInvisibilityRegionAtPlayerLocation(Player player) {
        Location location = player.getLocation();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager manager = container.get(BukkitAdapter.adapt(location.getWorld()));
        if (manager == null) {
            return false;
        }
        RegionQuery query = container.createQuery();
        ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(location));
        return regions.testState(WorldGuardPlugin.inst().wrapPlayer(player), flag);
    }

    public boolean isInvisible(Player player) {
        return invisible.contains(player.getUniqueId());
    }

    public void disableInvisibility(Player player) {
        invisible.remove(player.getUniqueId());
        player.removeMetadata("in-invisibility-region", this);
    }

    public void enableInvisibility(Player player) {
        invisible.add(player.getUniqueId());
        player.setMetadata("in-invisibility-region", new FixedMetadataValue(this, true));
    }

    public List<Player> getInvisiblePlayers() {
        return invisible.stream().map(Bukkit::getPlayer).toList();
    }
}
