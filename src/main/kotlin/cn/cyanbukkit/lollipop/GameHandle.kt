package cn.cyanbukkit.lollipop

import cn.cyanbukkit.lollipop.service.Dye
import cn.cyanbukkit.lollipop.service.ExpandShrink
import cn.cyanbukkit.lollipop.service.TNTBoomHandle
import cn.cyanbukkit.lollipop.service.popAmount
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
        Dye.linkedList.add(Dye.Data(i, j * 20))
    }

    @Mode("template")
    fun template(i: Int) {
        popAmount += i
    }

    @Mode("randomtp")
    fun randomTp() {
        ExpandShrink.randomTp()
    }





}