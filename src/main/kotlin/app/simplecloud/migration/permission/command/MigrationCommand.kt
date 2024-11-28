package app.simplecloud.migration.permission.command

import app.simplecloud.migration.permission.MigrationPlugin
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class MigrationCommand(private val plugin: MigrationPlugin) : CommandExecutor {
    override fun onCommand(commandSender: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {

        if (plugin.startMigration()) {
            commandSender.sendMessage("Migration started")
        } else {
            commandSender.sendMessage("Migration failed")
        }

        return true
    }
}