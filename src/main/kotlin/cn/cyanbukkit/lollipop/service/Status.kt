package cn.cyanbukkit.lollipop.service

import cn.cyanbukkit.lollipop.Lollipop
import cn.cyanbukkit.lollipop.Lollipop.colorMaterial
import cn.cyanbukkit.lollipop.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import cn.cyanbukkit.lollipop.service.ExpandShrink.blocks
import org.bukkit.Bukkit.getOnlinePlayers
import org.bukkit.scheduler.BukkitRunnable

fun checkIsColor(): Boolean {
    blocks.forEach {
        if (!colorMaterial.contains(it.type)) {
            return false
        }
    }
    return true
}


var popAmount = 0
var tempTime = Lollipop.keepTime
var keeping = false

fun runStatus() {
    object : BukkitRunnable() {
        override fun run() {
            keeping = checkIsColor()

            if (keeping) { // 保持阶段
                tempTime--
                getOnlinePlayers().forEach {
                    it.sendTitle("§a继续保持", "§7$tempTime/${Lollipop.keepTime}", 0, 20, 0)
                }
                if (tempTime <= 0) {
                    popAmount++
                    keeping = false
                    getOnlinePlayers().forEach {
                        it.sendTitle("§a保持成功", "§7当前棒棒糖制造了$popAmount", 0, 20, 0)
                    }
                    blocks.forEach {
                        it.type = Lollipop.MainMaterial
                    }
                    tempTime = Lollipop.keepTime
                }
            } else {
                if (tempTime  !=  Lollipop.keepTime) {
                    // 重置时间
                    tempTime = Lollipop.keepTime
                    getOnlinePlayers().forEach {
                        it.sendTitle("§c保持失败", "§7继续加油", 0, 20, 0)
                    }
                }
            }
        }
    }.runTaskTimer(cyanPlugin, 0, 20)
}


fun JinDu() : String {
    var setEd = 0
    blocks.forEach {
        if (colorMaterial.contains(it.type)) {
            setEd++
        }
    }
    return "§f${setEd}/${blocks.size}"
}