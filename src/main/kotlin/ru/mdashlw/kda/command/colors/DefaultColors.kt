package ru.mdashlw.kda.command.colors

import ru.mdashlw.kda.command.Colors
import java.awt.Color

object DefaultColors : Colors {
    override var default: Color? = null
    override var success: Color? = Color.GREEN
    override var error: Color? = Color.RED
}
