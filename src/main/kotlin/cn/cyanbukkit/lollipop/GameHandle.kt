package cn.cyanbukkit.lollipop

import cn.cyanbukkit.lollipop.service.Dye.buffChange
import cn.cyanbukkit.lollipop.service.ExpandShrink
import cn.cyanbukkit.lollipop.service.TNTBoomHandle
import cn.cyanbukkit.lollipop.utils.Mode

class GameHandle {

    @Mode("add")
    fun add(i: Int) {
        ExpandShrink.expendCircle(i.toDouble())
    }

    @Mode("del")
    fun del(i: Int) {
        ExpandShrink.shrinkCircle(i.toDouble())
    }

    @Mode("tnt")
    fun tnt(i: Int) {
        TNTBoomHandle.simpleBoom(i)
    }

    @Mode("dye")
    fun dye(i: Int, j: Int) {
        buffChange(i, j)
    }





}