package app.simplecloud.migration.permission.provider

import eu.thesimplecloud.module.permission.PermissionPool
import net.luckperms.api.LuckPerms
import net.luckperms.api.node.types.InheritanceNode
import net.luckperms.api.node.types.PermissionNode
import net.luckperms.api.node.types.WeightNode
import org.bukkit.plugin.java.JavaPlugin

class GroupProvider {

    fun migrate(provider: LuckPerms, plugin: JavaPlugin) {
        PermissionPool.instance.getPermissionGroupManager().getAllPermissionGroups().forEach { simpleCloudGroup ->
            println(simpleCloudGroup.getName())
            simpleCloudGroup.getPermissions().forEach { permission ->
                plugin.logger.info("Adding permission ${permission.permissionString} to group ${simpleCloudGroup.getName()}")
                provider.groupManager.modifyGroup(simpleCloudGroup.getName()) {
                    val builder = PermissionNode.builder(permission.permissionString)
                        .permission(permission.permissionString)
                        .value(permission.active)
                    if (permission.timeoutTimestamp > 0) {
                        builder.expiry(permission.timeoutTimestamp/ 1000)
                    }
                    it.data().add(WeightNode.builder().weight(simpleCloudGroup.getPriority()).value(true).build())
                    it.data().add(builder.build())
                }
                    .thenAccept {
                        plugin.logger.info("Added permission ${permission.permissionString} to group ${simpleCloudGroup.getName()}")
                    }
                    .exceptionally { it.printStackTrace(); null }
            }


            simpleCloudGroup.getAllInheritedPermissionGroupNames().forEach { inheritedGroupName ->
                plugin.logger.info("Adding inheritance $inheritedGroupName to group ${simpleCloudGroup.getName()}")
                provider.groupManager.modifyGroup(simpleCloudGroup.getName()) {
                    it.data().add(InheritanceNode.builder(inheritedGroupName).build())
                }
                    .thenAccept {
                        plugin.logger.info("Added inheritance $inheritedGroupName to group ${simpleCloudGroup.getName()}")
                    }
                    .exceptionally { it.printStackTrace(); null }
            }


        }
    }

}