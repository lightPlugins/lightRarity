package de.lightplugins.rarity.commands.manager;

import de.lightplugins.rarity.commands.main.ReloadCommand;
import de.lightplugins.rarity.enums.MessagePath;
import de.lightplugins.rarity.master.Main;
import de.lightplugins.rarity.util.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class MainCommandManager implements CommandExecutor {

    private final ArrayList<SubCommand> subCommands = new ArrayList<>();
    public ArrayList<SubCommand> getSubCommands() {
        return subCommands;
    }

    public Main plugin;

    public MainCommandManager(Main plugin) {
        this.plugin = plugin;
        subCommands.add(new ReloadCommand());
    }

    @Override
    public boolean onCommand( CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player player) {
            if (args.length > 0) {
                for(int i = 0; i < subCommands.size(); i++) {
                    if(args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {

                        try {
                            if(getSubCommands().get(i).perform(player, args)) {

                            }
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException("Something went wrong in executing /" + s + " " + Arrays.toString(args), e);
                        }
                    }
                }
            } else {

                Main.util.sendMessage(player, MessagePath.WrongCommand.getPath()
                        .replace("#command#", "/le help"));
            }
        }

        return false;
    }
}
