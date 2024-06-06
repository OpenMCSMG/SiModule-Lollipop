package cn.cyanbukkit.lollipop.service

import cn.cyanbukkit.lollipop.Lollipop
import cn.cyanbukkit.lollipop.Lollipop.maxRadius
import cn.cyanbukkit.lollipop.Lollipop.middleBlock
import cn.cyanbukkit.lollipop.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import org.bukkit.Bukkit
import org.bukkit.Bukkit.getOnlinePlayers
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import kotlin.math.sqrt

object ExpandShrink {

    var nowRadius = 0
    val blocks = mutableListOf<Block>()


    /**
     * 单单缩小
     */
    fun shrinkCircle(radius: Double) {
        val need = nowRadius - radius
        if (need < cyanPlugin.config.getDouble("MinRadius")) {
            getOnlinePlayers().forEach {
                it.sendTitle("§c缩小失败", "§c已经是最小半径了", 10, 40, 10)
            }
            return
        }
        serCircle(nowRadius.toDouble(), Material.AIR, false)
        serCircle(need,  Lollipop.MainMaterial, true)
        getOnlinePlayers().forEach {
            it.sendTitle("§a缩小成功", "§a半径减少到 $need 请重新给棒棒糖上色", 10, 40, 10)
        }
    }
    //TODO ： 解决棒棒糖杆不会跟着缩小的问题

    /**
     * 扩大
     */
    fun expendCircle(add: Double) {
        val need = add + nowRadius
        if (need > maxRadius) {
            getOnlinePlayers().forEach {
                it.sendTitle("§c扩大失败", "§c已经是最大半径了", 10, 40, 10)
            }
            return
        }
        serCircle(need, Lollipop.MainMaterial, true)

        getOnlinePlayers().forEach {
            it.sendTitle("§a扩大成功", "§a半径增加 $add 请重新给棒棒糖上色", 10, 40, 10)
        }
    }

    /**
     * 直接按照指定的半径设置圆
     */
    fun serCircle(radius: Double, material: Material, isAdd: Boolean) {
        nowRadius = radius.toInt()
        val world = middleBlock.world
        val centerX = middleBlock.x
        val centerZ = middleBlock.z
        // 将半径转换为整数
        val intRadius = radius.toInt()
        for (x in (centerX - intRadius)..centerX + intRadius) {
            for (z in (centerZ - intRadius)..centerZ + intRadius) {
                println("x: $x, z: $z")
                // 计算当前方块与中心点的距离dt
                // 我实在 不理解  平方根 处理 x 与 z轴的 平方相加处理 就能计算出数是不是在圆形区域内  因为我似乎不懂
                val distance = sqrt(
                    (x - centerX).toDouble() * (x - centerX).toDouble() +
                            (z - centerZ).toDouble() * (z - centerZ).toDouble()
                )
                if (distance <= radius) {
                    // 将方块放置在世界中
                    Bukkit.getScheduler().runTaskLater(cyanPlugin, Runnable {
                        val b = world.getBlockAt(x, middleBlock.y, z)
                        b.type = material
                        if (isAdd) {
                            if (!blocks.contains(b)) {
                                blocks.add(b)
                            }
                        } else {
                            blocks.remove(b)
                        }
                    }, 1L)
                }
            }
        }
    }


}