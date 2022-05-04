package nl.stanroelofs.gameboy.memory.io.graphics

abstract class Pixel (var color: Int, var priority: Boolean)

class PixelCGB(color: Int, var palette: Int, priority: Boolean) : Pixel(color, priority)

class PixelDMG (color : Int, var palette1: Boolean, priority: Boolean) : Pixel(color, priority)

