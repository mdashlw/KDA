package ru.mdashlw.kda.internal.command.colors

import ru.mdashlw.kda.api.command.Colors
import java.awt.Color

object DefaultColors : Colors {
    override var default: Color? = null
    override var success: Color = Color.GREEN
    override var warning: Color = Color.YELLOW
    override var error: Color = Color.RED
}
