package it.unina.cinemates_desktop

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val prevNextButton by cssclass()
        val redButton by cssclass()
        val buttonBorderColor = c("#D87277")
    }

    init {
        label and heading {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
        }

        prevNextButton {
            backgroundColor += Color.web("C52E21")
            borderWidth += box(2.px)
            borderColor += box(buttonBorderColor)
            borderRadius += box(10.px)
            backgroundRadius += box(10.px)
            backgroundInsets += box(0.px)
            textFill = Color.WHITE
            and(hover) {
                backgroundColor += Color.web("D23E31")
            }
        }

        redButton {
            backgroundColor += Color.web("C52E21")
            borderWidth += box(2.px)
            borderColor += box(buttonBorderColor)
            borderRadius += box(10.px)
            minHeight = 38.px
            backgroundRadius += box(10.px)
            backgroundInsets += box(0.px)
            textFill = Color.WHITE
            and(hover) {
                backgroundColor += Color.web("D23E31")
            }
        }
    }
}