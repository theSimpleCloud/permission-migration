package app.simplecloud.migration.permission.provider

import eu.thesimplecloud.module.permission.PermissionPool
import net.luckperms.api.LuckPerms
import net.luckperms.api.node.types.InheritanceNode
import net.luckperms.api.node.types.PermissionNode
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

class PlayerProvider {

    fun migrate(provider: LuckPerms, players: List<String>, plugin: JavaPlugin) {
        plugin.logger.info("Migrating players")
        players.forEach { uniqueId ->
            if (players.isEmpty()) {
                plugin.logger.warning("No players found")
                return
            }
            plugin.logger.info("Migrating player $uniqueId")
            val cloudPlayer = PermissionPool.instance.getPermissionPlayerManager().getPermissionPlayer(UUID.fromString(uniqueId)).getBlockingOrNull()
            if (cloudPlayer == null) {
                plugin.logger.warning("Player $uniqueId not found")
                return
            }


            cloudPlayer.getPermissionGroupInfoList().forEach { group ->
                plugin.logger.info("Adding player $uniqueId to group ${group.permissionGroupName}...")
                provider.userManager.loadUser(UUID.fromString(uniqueId), cloudPlayer.getName()).join()
                provider.userManager.modifyUser(UUID.fromString(uniqueId)) {
                    var builder = InheritanceNode.builder(group.permissionGroupName.lowercase())
                        .value(true)

                    if (group.timeoutTimestamp > 0) {
                        builder = builder.expiry(group.timeoutTimestamp/1000)
                    }
                    it.data().add(builder.build())
                }
                    .thenAccept {
                        plugin.logger.info("Added player $uniqueId to group ${group.permissionGroupName}")
                    }
                    .exceptionally { it.printStackTrace(); null }
            }


            cloudPlayer.getAllNotExpiredPermissions().forEach { permission ->
                println("Adding permission ${permission.permissionString} to player $uniqueId...")
                provider.userManager.modifyUser(UUID.fromString(uniqueId)) {
                    var builder = PermissionNode.builder(permission.permissionString)
                        .permission(permission.permissionString)
                        .value(permission.active)

                    if (permission.timeoutTimestamp > 0) {
                        builder = builder.expiry(permission.timeoutTimestamp/1000)
                    }


                    it.data().add(builder.build())
                }
                    .thenAccept {
                        plugin.logger.info("Added permission ${permission.permissionString} to player $uniqueId")
                    }
                    .exceptionally { it.printStackTrace(); null }
            }

            cloudPlayer.getHighestPermissionGroup().let { highestGroup ->
                plugin.logger.info("Setting primary group of player $uniqueId to ${highestGroup.getName()}")
                provider.userManager.modifyUser(UUID.fromString(uniqueId)) {
                    it.primaryGroup = highestGroup.getName().lowercase()
                }
                    .thenAccept {
                        plugin.logger.info("Set primary group of player $uniqueId to ${highestGroup.getName()}")
                    }
                    .exceptionally { it.printStackTrace(); null }
            }

            provider.userManager.cleanupUser(provider.userManager.getUser(UUID.fromString(uniqueId))!!)
            provider.userManager.saveUser(provider.userManager.getUser(UUID.fromString(uniqueId))!!)

        }
    }

}