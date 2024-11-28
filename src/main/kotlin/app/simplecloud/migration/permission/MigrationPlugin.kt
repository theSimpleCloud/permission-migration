package app.simplecloud.migration.permission

import app.simplecloud.migration.permission.command.MigrationCommand
import app.simplecloud.migration.permission.database.DatabaseHelper
import app.simplecloud.migration.permission.provider.GroupProvider
import app.simplecloud.migration.permission.provider.PlayerProvider
import eu.thesimplecloud.module.permission.PermissionPool
import net.luckperms.api.LuckPerms
import net.luckperms.api.node.types.InheritanceNode
import net.luckperms.api.node.types.PermissionNode
import net.luckperms.api.node.types.WeightNode
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.log


class MigrationPlugin : JavaPlugin() {

    override fun onEnable() {
        Bukkit.getPluginCommand("migrate")?.setExecutor(MigrationCommand(this))

    }

    override fun onDisable() {

    }

    fun startMigration(): Boolean {
        logger.info("Found database type: ${DatabaseHelper.getDatabaseType(this)}")

        val provider = Bukkit.getServicesManager().getRegistration(
            LuckPerms::class.java
        )

        if (provider != null) {
            val api = provider.provider
            GroupProvider().migrate(api, this)
            PlayerProvider().migrate(api, DatabaseHelper.selectDatabase(this), this)
            return true
        }

        return false
    }



}