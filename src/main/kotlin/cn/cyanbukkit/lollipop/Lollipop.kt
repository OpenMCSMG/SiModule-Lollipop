package cn.cyanbukkit.lollipop

import cn.cyanbukkit.lollipop.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import cn.cyanbukkit.lollipop.listener.PlayerListener
import cn.cyanbukkit.lollipop.listener.PlayerListener.tempScoreBoard
import cn.cyanbukkit.lollipop.service.Dye
import cn.cyanbukkit.lollipop.service.ExpandShrink
import cn.cyanbukkit.lollipop.service.TNTBoomHandle
import cn.cyanbukkit.lollipop.service.runStatus
import cn.cyanbukkit.lollipop.utils.Scoreboard
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block

object Lollipop {

    lateinit var middleBlock: Block
    var maxRadius = 0
    lateinit var MainMaterial: Material
    val colorMaterial = mutableListOf<Material>()
    var keepTime = 0

    fun init() {
        cyanPlugin.logger.info("加载插件配置文件")
        middleBlock = cyanPlugin.config.getString("MiddleDot")?.let {
            val split = it.split(",")
            cyanPlugin.server.getWorld(split[0])?.getBlockAt(split[1].toInt(), split[2].toInt(), split[3].toInt())
        } ?: cyanPlugin.server.worlds[0].spawnLocation.block
        maxRadius = cyanPlugin.config.getInt("MaxRadius")
        MainMaterial = Material.getMaterial(cyanPlugin.config.getString("MainMaterial") ?: "DIAMOND_BLOCK")
            ?: Material.DIAMOND_BLOCK
        cyanPlugin.config.getStringList("ColorMaterial").forEach {
            if (Material.getMaterial(it) != null) {
                colorMaterial.add(Material.getMaterial(it)!!)
            }
        }
        keepTime = cyanPlugin.config.getInt("KeepTime")
        ExpandShrink.nowRadius = cyanPlugin.config.getInt("MinRadius")
        // 禁止刷怪
        middleBlock.world.setGameRule(org.bukkit.GameRule.DO_MOB_LOOT, false)
        middleBlock.world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, false)
        middleBlock.world.entities.forEach {
            it.remove()
        }

        cyanPlugin.logger.info("初始化默认圈")
        ExpandShrink.serCircle(maxRadius.toDouble(), Material.AIR, false)
        ExpandShrink.serCircle(cyanPlugin.config.getDouble("MinRadius"), MainMaterial, true)


        Scoreboard.init(cyanPlugin)

        cyanPlugin.logger.info("玩家监听上色事件")
        cyanPlugin.server.pluginManager.registerEvents(PlayerListener, cyanPlugin)
        TNTBoomHandle.load()
        cyanPlugin.logger.info("启动获取棒棒糖数量投射在计分板")
        tempScoreBoard()
        runStatus()
        Dye.init()
        // 给棒棒糖整个杆子 长度  maxRadius * 2 从起始点
        Bukkit.getScheduler().runTaskLater(cyanPlugin, Runnable {
            for (i in middleBlock.x..middleBlock.x + maxRadius * 2) {
                middleBlock.world.getBlockAt(i, middleBlock.y - 1, middleBlock.z).type = Material.QUARTZ_BLOCK
            }
        }, 40L)
    }

}