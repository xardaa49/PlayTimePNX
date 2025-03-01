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
package com.xardaa49.playtimepnx;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.command.CommandMap;
import com.xardaa49.playtimepnx.database.DatabaseManager;
import com.xardaa49.playtimepnx.command.PlayTimeCommand;
import com.xardaa49.playtimepnx.listener.PlayerListener;

/**
 * The Main class represents the main entry point for the PlayTimePNX plugin.
 * It handles plugin initialization, database setup, command registration, and event listener registration.
 */
public class Main extends PluginBase {

    private DatabaseManager databaseManager;

    /**
     * Called when the plugin is enabled. This method initializes the database manager,
     * creates the necessary database tables, registers commands, and registers event listeners.
     */
    @Override
    public void onEnable() {

        String databasePath = getDataFolder() + "/playtime.db";

        databaseManager = new DatabaseManager(databasePath);
        databaseManager.createDatabaseTable();

        CommandMap commandMap = getServer().getCommandMap();

        commandMap.register("oynamasurem", new PlayTimeCommand(databaseManager));
        getServer().getPluginManager().registerEvents(new PlayerListener(databaseManager), this);
    }
}
