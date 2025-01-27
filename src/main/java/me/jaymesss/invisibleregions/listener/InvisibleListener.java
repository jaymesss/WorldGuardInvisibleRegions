package me.jaymesss.invisibleregions.listener;

import me.jaymesss.invisibleregions.InvisibleRegions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class InvisibleListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        InvisibleRegions.getInstance().getInvisiblePlayers().forEach(p -> {
            player.hidePlayer(InvisibleRegions.getInstance(), p);
        });
        checkStep(player);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        checkStep(player);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockY() == event.getTo().getBlockY() && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }
        checkStep(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        InvisibleRegions.getInstance().disableInvisibility(event.getPlayer());
    }

    private void checkStep(Player player) {
        boolean flagActive = InvisibleRegions.getInstance().isInvisibilityRegionAtPlayerLocation(player);
        boolean invisActive = InvisibleRegions.getInstance().isInvisible(player);
        if (!flagActive && invisActive) {
            for (Player target : Bukkit.getOnlinePlayers()) {
                target.showPlayer(InvisibleRegions.getInstance(), player);
            }
            InvisibleRegions.getInstance().disableInvisibility(player);
            InvisibleRegions.getInstance().debug(player.getName() + " has been revealed to everyone.");
            return;
        }
        if (!flagActive || invisActive) return;
        if (InvisibleRegions.getInstance().isBypassPermission() && player.hasPermission("invisibleregions.always-visible")) {
            InvisibleRegions.getInstance().debug(player.getName() + " was not set invisible since they have permission to bypass.");
            return;
        }
        for (Player target : Bukkit.getOnlinePlayers()) {
            target.hidePlayer(InvisibleRegions.getInstance(), player);
        }
        InvisibleRegions.getInstance().enableInvisibility(player);
        InvisibleRegions.getInstance().debug(player.getName() + " has been hidden from everyone.");
    }
}
