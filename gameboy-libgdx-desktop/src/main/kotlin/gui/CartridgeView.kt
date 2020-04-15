package gui

import gameboy.GameBoy
import javafx.scene.control.Label
import tornadofx.*

class CartridgeView(private val gb: GameBoy): View() {

    private var romGbc: Label by singleAssign()
    private var romTitle: Label by singleAssign()
    private var romLicensee: Label by singleAssign()
    private var romType: Label by singleAssign()
    private var romSgb: Label by singleAssign()
    private var romDestination: Label by singleAssign()
    private var romHeaderChecksum: Label by singleAssign()
    private var romVersion: Label by singleAssign()
    private var romOldLicensee: Label by singleAssign()


    override val root = gridpane {
        row {
            label("Title:")
            romTitle = label()
        }
        row {
            label("Type:")
            romType = label()
        }
        row {
            label("Licensee Code:")
            romLicensee = label()
        }
        row {
            label("GBC:")
            romGbc = label()
        }
        row {
            label("Support SGB:")
            romSgb = label()
        }
        row {
            label("Destination:")
            romDestination = label()
        }
        row {
            label("Old Licensee Code:")
            romOldLicensee = label()
        }
        row {
            label("Version Number:")
            romVersion = label()
        }
        row {
            label("Header Checksum Matches:")
            romHeaderChecksum = label()
        }

    }

    fun update() {
        romTitle.text = gb.cartridge?.title
        romType.text = gb.cartridge?.type.toString()
        romLicensee.text = gb.cartridge?.licensee
        romGbc.text = gb.cartridge?.isGbc.toString()
        romSgb.text = gb.cartridge?.isSgb.toString()
        romDestination.text = "${gb.cartridge?.destinationCode} ${gb.cartridge?.destination}"
        romOldLicensee.text = gb.cartridge?.oldLicenseeCode.toString()
        romVersion.text = gb.cartridge?.versionNumber.toString()
        romHeaderChecksum.text = gb.cartridge?.headerChecksum.toString()
    }
}