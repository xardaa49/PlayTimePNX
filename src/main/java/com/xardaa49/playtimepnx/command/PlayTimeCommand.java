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
package com.xardaa49.playtimepnx.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import com.xardaa49.playtimepnx.database.DatabaseManager;

/**
 * The PlayTimeCommand class represents a command that allows players to view their playtime on the server.
 * The command shows the playtime in days, hours, minutes, and seconds.
 */
public class PlayTimeCommand extends Command {

    private final DatabaseManager databaseManager;

    /**
     * Constructs a PlayTimeCommand with the specified DatabaseManager.
     *
     * @param databaseManager The DatabaseManager used for retrieving the player's playtime from the database.
     */
    public PlayTimeCommand(DatabaseManager databaseManager) {
        super("playtime", "Displays your playtime", "/playtime");
        this.databaseManager = databaseManager;
    }

    /**
     * Executes the playtime command when a player runs it.
     * The command retrieves the player's playtime from the database and sends it in a formatted message.
     *
     * @param sender The sender of the command (must be a player).
     * @param commandLabel The command label (e.g., "/playtime").
     * @param args Any additional arguments passed to the command.
     * @return True if the command executed successfully, false otherwise.
     */
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Retrieve the playtime from the database
            long playtimeInSeconds = databaseManager.getPlaytime(player.getName());

            if (playtimeInSeconds == 0) {
                player.sendMessage("Your playtime is not recorded.");
                return false;
            }

            long days = playtimeInSeconds / (24 * 3600);
            long hours = (playtimeInSeconds % (24 * 3600)) / 3600;
            long minutes = (playtimeInSeconds % 3600) / 60;
            long seconds = playtimeInSeconds % 60;

            player.sendMessage(TextFormat.DARK_GREEN + "Play Time: " + TextFormat.GREEN + days + TextFormat.DARK_GREEN +" days " + TextFormat.GREEN + hours + TextFormat.DARK_GREEN + " hours " + TextFormat.GREEN + minutes + TextFormat.DARK_GREEN + " minutes " + TextFormat.GREEN + seconds + TextFormat.DARK_GREEN + " seconds");
            return true;
        }
        return false;
    }
}
