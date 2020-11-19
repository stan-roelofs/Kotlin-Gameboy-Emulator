package gameboy.memory.io.graphics

open class Pixel (var color: Int, var priority: Boolean)

class PixelCGB(color: Int, priority: Boolean) : Pixel(color, priority)

class PixelDMG (color : Int, var palette1: Boolean, priority: Boolean) : Pixel(color, priority)
