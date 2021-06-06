package it.unina.cinemates_desktop.view

import it.unina.cinemates_desktop.model.Review
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import javafx.scene.shape.Rectangle
import tornadofx.View
import tornadofx.action

class ReportedReviewDetail(review: Review) : View() {

    override val root : AnchorPane by fxml("/views/ReportedReviewDetail.fxml")

    private val profilePicImageView : ImageView by fxid("profilePicImageView")
    private val usernameLabel : Label by fxid("usernameLabel")
    private val reviewTextLabel: Label by fxid("reviewTextLabel")
    private val restoreBtn: Button by fxid("restoreBtn")
    private val deletePermanentlyBtn: Button by fxid("deletePermanentlyBtn")

    override fun onDock() {
        currentStage?.width = 500.0
        currentStage?.height = 300.0
        currentStage?.centerOnScreen()
    }

    init {
        println("opened " + review.reviewText)

        var profilePicUrl = review.profilePic

        if (profilePicUrl.startsWith("propic")) {
            profilePicUrl = when (profilePicUrl) {
                "propic1" -> "/propic_1.png"
                "propic2" -> "/propic_2.png"
                "propic3" -> "/propic_3.png"
                "propic4" -> "/propic_4.png"
                "propic5" -> "/propic_5.png"
                else -> "/propic_1.png"
            }
        }

        profilePicImageView.image = Image(profilePicUrl)

        val clip = Rectangle(profilePicImageView.fitWidth, profilePicImageView.fitHeight)
        clip.arcWidth = 50.0
        clip.arcHeight = 50.0
        profilePicImageView.clip = clip

        usernameLabel.text = review.username
        reviewTextLabel.text = review.reviewText

        restoreBtn.action {
            println("Restore " + review.reviewId)
        }

        deletePermanentlyBtn.action {
            println("Delete " + review.reviewId)
        }
    }
}