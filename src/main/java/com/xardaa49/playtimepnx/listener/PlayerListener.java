/***
 *    ooooooooo.   oooo                        ooooooooooooo  o8o
 *    `888   `Y88. `888                        8'   888   `8  `"'
 *     888   .d88'  888   .oooo.   oooo    ooo      888      oooo  ooo. .oo.  .oo.    .ooooo.
 *     888ooo88P'   888  `P  )88b   `88.  .8'       888      `888  `888P"Y88bP"Y88b  d88' `88b
 *     888          888   .oP"888    `88..8'        888       888   888   888   888  888ooo888
 *     888          888  d8(  888     `888'         888       888   888   888   888  888    .o
 *    o888o        o888o `Y888""8o     .8'         o888o     o888o o888o o888o o888o `Y8bod8P'
 *                                 .o..P'
 *                                 `Y8P'
 *
 *
 *    @name PlayTimePNX
 *    @author xArdaa49
 *    @version 1.0.0
 */
package com.xardaa49.playtimepnx.listener;

import cn.nukkit.event.Listener;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.Player;
import com.xardaa49.playtimepnx.database.DatabaseManager;

/**
 * Listener class that handles player events related to playtime tracking.
 * It updates or initializes the player's playtime in the database when they join the server.
 */
public class PlayerListener implements Listener {

    private final DatabaseManager databaseManager;

    /**
     * Constructs a PlayerListener with the specified DatabaseManager.
     *
     * @param databaseManager The DatabaseManager used to interact with the database for playtime management.
     */
    public PlayerListener(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Handles the PlayerJoinEvent, which is triggered when a player joins the server.
     * It checks if the player is new or returning. If the player is new, their playtime is initialized in the database.
     * If the player is returning, their playtime is updated in the database.
     *
     * @param event The PlayerJoinEvent triggered when a player joins the server.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        long currentPlaytime = databaseManager.getPlaytime(player.getName());

        if (currentPlaytime == 0) {
            databaseManager.addNewPlayer(player.getName());
        } else {
            databaseManager.updatePlaytime(player.getName(), currentPlaytime);
        }
    }
}
