package cn.cyanbukkit.lollipop.service

import cn.cyanbukkit.lollipop.Lollipop
import cn.cyanbukkit.lollipop.Lollipop.colorMaterial
import cn.cyanbukkit.lollipop.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import cn.cyanbukkit.lollipop.service.ExpandShrink.blocks
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit.getOnlinePlayers
import org.bukkit.block.Block
import org.bukkit.scheduler.BukkitRunnable

object Dye {

    var now_size = 1

    val linkedList = mutableListOf<Data>()

    fun default(b: Block) {
        // 检查方块是否是Lollipop.MainMaterial类型
        if (b.type == Lollipop.MainMaterial) {
            // 更改方块类型为随机颜色材料
            b.type = colorMaterial.random()
            // 染色周围方块
            dyeSurroundingBlocks(b, now_size)
        }
    }

    fun init() {
        object : BukkitRunnable() {
            override fun run() {
                // 一直检查是否有新的数据然后执行
                if (linkedList.isEmpty()) {
                    getOnlinePlayers().forEach {
                        default(it.location.add(0.0, -1.0, 0.0).block)
                    }
                } else {
                    // 获取第一个数据
                    val data = linkedList[0]
                    data.keepTime--
                    if (data.keepTime < 1) {
                        linkedList.removeAt(0)
                        now_size = 1
                    } else {
                        getOnlinePlayers().forEach {
                            it.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("§c还剩${data.keepTime / 20}秒"))
                        }
                        now_size = data.size
                        getOnlinePlayers().forEach {
                            default(it.location.add(0.0, -1.0, 0.0).block)
                        }
                    }
                }
            }
        }.runTaskTimer(cyanPlugin, 0, 1L)
    }


    private fun dyeSurroundingBlocks(block: Block, size: Int) {
        val world = block.world
        val x = block.x
        val y = block.y
        val z = block.z

        // 遍历周围的方块
        for (dx in -size..size) {
            for (dy in -size..size) {
                for (dz in -size..size) {
                    if (dx != 0 || dy != 0 || dz != 0) { // 不包括自身
                        val relativeBlock = world.getBlockAt(x + dx, y + dy, z + dz)
                        if (!blocks.contains(relativeBlock)) {
                            continue
                        }
                        if (relativeBlock.type == Lollipop.MainMaterial) {
                            relativeBlock.type = colorMaterial.random()
                        }
                    }
                }
            }
        }
    }


    data class Data(
        val size: Int,
        var keepTime: Int
    )

}


