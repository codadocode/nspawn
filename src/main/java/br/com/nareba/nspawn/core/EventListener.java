package br.com.nareba.nspawn.core;

import br.com.nareba.nspawn.Nspawn;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {
    private final Nspawn plugin;
    public EventListener (Nspawn plugin)   {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)   {
        Player playerEvent = event.getPlayer();
        if (plugin.getSpawnList().containsKey("default"))   {
            playerEvent.setLevel(plugin.getServer().getLevelByName(plugin.getSpawnList().get("default").getLevelName()));
            playerEvent.setPosition(plugin.getSpawnList().get("default").getSpawnPosition());
        }
    }
}
