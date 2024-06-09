package cn.cyanbukkit.lollipop.listener

import cn.cyanbukkit.lollipop.Lollipop
import cn.cyanbukkit.lollipop.Lollipop.colorMaterial
import cn.cyanbukkit.lollipop.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import cn.cyanbukkit.lollipop.service.Dye
import cn.cyanbukkit.lollipop.service.ExpandShrink
import cn.cyanbukkit.lollipop.service.ExpandShrink.blocks
import cn.cyanbukkit.lollipop.service.JinDu
import cn.cyanbukkit.lollipop.service.popAmount
import cn.cyanbukkit.lollipop.utils.Scoreboard
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.Criteria


object PlayerListener : Listener {

    var canBuild = false

    @EventHandler
    fun onPlayerMove(event: PlayerDeathEvent) {
        event.entity.spigot().respawn()
        event.entity.teleport(Lollipop.middleBlock.location.add(0.5, 1.0, 0.5))
    }

    @EventHandler
    fun onPlayerJoin(event: org.bukkit.event.player.PlayerJoinEvent) {
        val player = event.player
        // 计分板
//        Scoreboard(player, listOf("§制造棒棒糖"), 20 ).update {
//            it.set("§a当前半径: ", 6)
//            it.set("  §f${ExpandShrink}", 5)
//            it.set("§a上色进度: ", 4)
//            it.set("  §f${JinDu()}", 3)
//            it.set("§a造了多少棒棒糖: ", 2)
//            it.set("  §f${popAmount}", 1)
//        }

        player.scoreboard = obj.scoreboard!!
        // 进入传送
        player.teleport(Lollipop.middleBlock.location.add(0.5, 1.0, 0.5))
    }


    @EventHandler
    fun onBreak(event: org.bukkit.event.block.BlockBreakEvent) {
        if (!canBuild) {
            event.isCancelled = true
            return
        }
    }

    @EventHandler
    fun onPlace(event: org.bukkit.event.block.BlockPlaceEvent) {
        if (!canBuild) {
            event.isCancelled = true
            return
        }
    }

    lateinit var obj: org.bukkit.scoreboard.Objective

    fun tempScoreBoard() {
        val sb = Bukkit.getScoreboardManager()!!.newScoreboard
        obj = sb.registerNewObjective("lollipop", Criteria.DUMMY, "lollipop")
        obj.displayName = "§c棒棒糖"
        obj.displaySlot = org.bukkit.scoreboard.DisplaySlot.SIDEBAR
        obj.renderType = org.bukkit.scoreboard.RenderType.INTEGER
        object : BukkitRunnable() {
            override fun run() {
                obj.scoreboard!!.entries.forEach {
                    obj.scoreboard!!.resetScores(it)
                }
                obj.getScore("§a当前半径: ").score = ExpandShrink.nowRadius
                obj.getScore("§a造了多少棒棒糖: ").score = popAmount
            }
        }.runTaskTimer(cyanPlugin, 0, 20)
    }

    @EventHandler
    fun shangSe(e: PlayerMoveEvent) {
        // 脚下的方块在blocks 就染ColorMaterial
        val block = e.player.location.add(0.0, -1.0, 0.0).block
        Dye.default(block)
    }

}