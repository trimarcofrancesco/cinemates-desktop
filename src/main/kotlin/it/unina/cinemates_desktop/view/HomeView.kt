package it.unina.cinemates_desktop.view

import it.unina.cinemates_desktop.model.Review
import it.unina.cinemates_desktop.model.UserModel
import it.unina.cinemates_desktop.viewmodel.HomeViewModel
import it.unina.cinemates_desktop.viewmodel.LoginViewModel
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tornadofx.*


class HomeView : View("Cinemates") {
    private val user: UserModel by inject()
    private val loginViewModel: LoginViewModel by inject()
    private val viewModel : HomeViewModel by inject()

    override val root : BorderPane by fxml("/views/HomeView.fxml")

    val timerangeCombobox : ComboBox<String> by fxid("timerangeCombobox")
    val reviewsReportedAsInappropriateGrid : GridPane by fxid("reviewsReportedAsInappropriateGrid")
    val logoutBtn: Button by fxid("logoutBtn")
    val reviewsPreviousPageBtn: Button by fxid("reviewsPreviousPage")
    val reviewsNextPageBtn: Button by fxid("reviewsNextPage")

    private var reviewsPage: Int = 0
    private var reviewsList: ArrayList<Review> = arrayListOf()
    private var chunkedReviewsList: List<List<Review>> = listOf()

    init {
        GlobalScope.launch { viewModel.getInappropriates() }
        subscribe<HomeViewModel.GetInappropriatesResponse> {
            it.getInappropriatesResponse?.let { inappropriatesResponse ->
                reviewsList = ArrayList(inappropriatesResponse.reviews)
                chunkedReviewsList = reviewsList.chunked(4)
                bindInappropriates(reviewsList)
            }
        }

        timerangeCombobox.items = FXCollections.observableArrayList("Sempre", "Settimana", "Mese")
        timerangeCombobox.value = "Sempre"

        reviewsPreviousPageBtn.action {
            if (reviewsPage > 0) {
                reviewsPage -= 1
                reviewsReportedAsInappropriateGrid.removeAllRows()
                bindInappropriates(chunkedReviewsList[reviewsPage])
                //bindInappropriates(reviewsList.subList((reviewsPage * 4) - 4, (reviewsPage * 4) - 1))
            }
        }

        reviewsNextPageBtn.action {
            if (reviewsPage < chunkedReviewsList.size - 1) {
                reviewsPage += 1
                reviewsReportedAsInappropriateGrid.removeAllRows()
                bindInappropriates(chunkedReviewsList[reviewsPage])
                //bindInappropriates(reviewsList.chunked())
            }
        }

        logoutBtn.action {
            loginViewModel.logout()
        }
    }

    private fun bindInappropriates(reviewsList: List<Review>) {
        for (index in reviewsList.indices) {
            if (index > 3) break

            val verticalBox = VBox()
            verticalBox.padding = Insets(20.0, 20.0, 20.0, 20.0)
            verticalBox.spacing = 10.0

            verticalBox.setOnMouseClicked {
                ReportedReviewDetail(reviewsList[index]).openWindow()
            }

            val horizontalBox = HBox()
            horizontalBox.alignment = Pos.CENTER_LEFT
            horizontalBox.spacing = 10.0
            verticalBox.add(horizontalBox)

            var profilePicUrl = reviewsList[index].profilePic

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

            val profilePic = ImageView(profilePicUrl)
            profilePic.fitHeight = 30.0
            profilePic.fitWidth = 30.0

            val clip = Rectangle(profilePic.fitWidth, profilePic.fitHeight)
            clip.arcWidth = 30.0
            clip.arcHeight = 30.0
            profilePic.clip = clip

            horizontalBox.add(profilePic)

            val usernameLabel = Label(reviewsList[index].username)
            usernameLabel.textFill = Color.WHITE
            usernameLabel.style = "-fx-font-weight: bold"

            horizontalBox.add(profilePic)
            horizontalBox.add(usernameLabel)

            val reviewLabel = Label(reviewsList[index].reviewText)
            reviewLabel.textFill = Color.WHITE

            verticalBox.add(reviewLabel)

            reviewsReportedAsInappropriateGrid.add(verticalBox, index % 2, if (index < 2) 0 else 1, 1, 1)
        }
    }

    override fun onDock() {
        primaryStage.width = 1200.0
        primaryStage.height = 600.0
    }

}
