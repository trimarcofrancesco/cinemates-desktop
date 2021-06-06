package it.unina.cinemates_desktop.view

import it.unina.cinemates_desktop.Styles
import it.unina.cinemates_desktop.model.Review
import it.unina.cinemates_desktop.model.UserModel
import it.unina.cinemates_desktop.viewmodel.HomeViewModel
import it.unina.cinemates_desktop.viewmodel.LoginViewModel
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.*
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

    private val timerangeCombobox : ComboBox<String> by fxid("timerangeCombobox")

    private val reviewsReportedAsInappropriateGrid : GridPane by fxid("reviewsReportedAsInappropriateGrid")
    private val commentsReportedAsInappropriateGrid : GridPane by fxid("commentsReportedAsInappropriateGrid")

    private val profileMenuButton: MenuButton by fxid("profileMenuButton")
    private val logoutBtn: MenuItem by fxid("logoutBtn")
    private val reviewsPreviousPageBtn: Button by fxid("reviewsPreviousPage")
    private val reviewsNextPageBtn: Button by fxid("reviewsNextPage")

    private val commentsPreviousPageBtn: Button by fxid("commentsPreviousPageBtn")
    private val commentsNextPageBtn: Button by fxid("commentsNextPageBtn")

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

        var profilePicUrl = user.profile.value.profilePic

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

        val profilePic = ImageView("/propic_1.png")
        profilePic.fitHeight = 40.0
        profilePic.fitWidth = 40.0

        val clip = Rectangle(profilePic.fitWidth, profilePic.fitHeight)
        clip.arcWidth = 40.0
        clip.arcHeight = 40.0
        profilePic.clip = clip

        profileMenuButton.graphic = profilePic
        profileMenuButton.background = Background.EMPTY


        reviewsPreviousPageBtn.also {
            it.action {
                if (reviewsPage > 0) {
                    reviewsPage -= 1
                    reviewsReportedAsInappropriateGrid.removeAllRows()
                    bindInappropriates(chunkedReviewsList[reviewsPage])
                    //bindInappropriates(reviewsList.subList((reviewsPage * 4) - 4, (reviewsPage * 4) - 1))
                }
            }

            it.addClass(Styles.prevNextButton)
        }

        reviewsNextPageBtn.also {
            it.action {
                if (reviewsPage < chunkedReviewsList.size - 1) {
                    reviewsPage += 1
                    reviewsReportedAsInappropriateGrid.removeAllRows()
                    bindInappropriates(chunkedReviewsList[reviewsPage])
                    //bindInappropriates(reviewsList.chunked())
                }
            }

            it.addClass(Styles.prevNextButton)
        }

        logoutBtn.action {
            loginViewModel.logout()
        }
    }

    private fun bindInappropriates(reviewsList: List<Review>) {
        for (index in reviewsList.indices) {
            if (index > 3) break

            val verticalBox = VBox()
            verticalBox.style {
                backgroundRadius += box(10.px)
                backgroundInsets += box(0.2.px)
                backgroundColor += Color.web("D23E31")
            }
            verticalBox.padding = Insets(15.0, 15.0, 15.0, 15.0)
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
