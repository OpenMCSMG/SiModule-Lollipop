package cn.cyanbukkit.lollipop.command

import cn.cyanbukkit.lollipop.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SetUpCommand : Command("su") {

    fun CommandSender.sendHelp() {
        sendMessage("""
            /su help 
            /su setMiddleBlock - 站在那个方块上设置脚下的方块为中心方块
            /su setMaxRadius <xx> - 设置最大半径
            /su setMinRadius <xx> - 设置初始半径
            /su reload - 重载配置
        """.trimIndent())
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): MutableList<String> {
        return mutableListOf("help", "setMiddleBlock", "setMaxRadius", "reload")
    }

    override fun execute(c: CommandSender, cmd: String, args: Array<out String>): Boolean {
        // |-cu help
        if (args.isEmpty() || args[0] == "help") {
            c.sendHelp()
            return true
        }
        if (c !is Player) {
            c.sendMessage("§c你必须是玩家")
            return true
        }
        when (args[0]) {
            "setMiddleBlock" -> {
                val upOnFooter = c.location.add(0.0, -1.0, 0.0).block
                cyanPlugin.config.set("MiddleDot", "${upOnFooter.world.name},${upOnFooter.x},${upOnFooter.y},${upOnFooter.z}")
                cyanPlugin.saveConfig()
                c.sendMessage("§a设置成功")
            }
            "setMinRadius" -> {
                if (args.size < 2) {
                    c.sendMessage("§c请输入半径")
                    return true
                }
                val radius = args[1].toIntOrNull()
                if (radius == null) {
                    c.sendMessage("§c请输入正确的半径")
                    return true
                }
                cyanPlugin.config.set("MinRadius", radius)
                cyanPlugin.saveConfig()
                c.sendMessage("§a设置成功")
            }
            "reload" -> {
                cyanPlugin.reloadConfig()
                c.sendMessage("§a重载成功")
            }
            "setMaxRadius" -> {
                if (args.size < 2) {
                    c.sendMessage("§c请输入半径")
                    return true
                }
                val radius = args[1].toIntOrNull()
                if (radius == null) {
                    c.sendMessage("§c请输入正确的半径")
                    return true
                }
                cyanPlugin.config.set("MaxRadius", radius)
                cyanPlugin.saveConfig()
                c.sendMessage("§a设置成功")
            }
            else -> {
                c.sendHelp()
            }
        }

        return true
    }

}