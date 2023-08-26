package me.lexo;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class MusicBlockPlugin extends JavaPlugin implements Listener {
    private List<MusicBlock> musicBlocks;
    private int musicRadius;

    @Override
    public void onEnable() {
        // Cargar y guardar la configuración
        this.saveDefaultConfig();
        FileConfiguration config = this.getConfig();
        musicBlocks = new ArrayList<>();
        musicRadius = config.getInt("music_radius", 10); // Obtener el radio de reproducción de la configuración

        // Registrar eventos
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.JUKEBOX) { // Comprobar si se coloca un Jukebox
            Location blockLocation = event.getBlock().getLocation();
            MusicBlock musicBlock = new MusicBlock(blockLocation);
            musicBlocks.add(musicBlock);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.JUKEBOX) { // Comprobar si se rompe un Jukebox
            Location blockLocation = event.getBlock().getLocation();
            MusicBlock musicBlock = getMusicBlockAtLocation(blockLocation);
            if (musicBlock != null) {
                musicBlocks.remove(musicBlock);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();
        for (MusicBlock musicBlock : musicBlocks) {
            Location blockLocation = musicBlock.getLocation();
            if (blockLocation.getWorld() != playerLocation.getWorld()) {
                continue;
            }

            if (blockLocation.distance(playerLocation) <= musicRadius) {
                musicBlock.playMusic();
            }  {

            }
        }
    }

    private MusicBlock getMusicBlockAtLocation(Location location) {
        for (MusicBlock musicBlock : musicBlocks) {
            if (musicBlock.getLocation().equals(location)) {
                return musicBlock;
            }
        }
        return null;
    }

    public class MusicBlock {
        private final Location location;

        public MusicBlock(Location location) {
            this.location = location;
        }

        public Location getLocation() {
            return location;
        }

        public void playMusic() {
            World world = location.getWorld();
            world.playSound(location, Sound.MUSIC_DISC_11, SoundCategory.RECORDS, 1, 1);
        }

    }
}