package it.unina.cinemates_desktop.view

import it.unina.cinemates_desktop.Styles
import it.unina.cinemates_desktop.model.Comment
import it.unina.cinemates_desktop.model.Review
import it.unina.cinemates_desktop.model.StatsResponse
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

    private val usersCountLabel : Label by fxid("usersCountLabel")
    private val reviewsCountLabel : Label by fxid("reviewsCountLabel")
    private val listsCountLabel : Label by fxid("listsCountLabel")
    private val searchesCountLabel : Label by fxid("searchesCountLabel")

    private val reviewsReportedAsInappropriateGrid : GridPane by fxid("reviewsReportedAsInappropriateGrid")
    private val commentsReportedAsInappropriateGrid : GridPane by fxid("commentsReportedAsInappropriateGrid")

    private val profileMenuButton: MenuButton by fxid("profileMenuButton")
    private val logoutBtn: MenuItem by fxid("logoutBtn")
    private val reviewsPreviousPageBtn: Button by fxid("reviewsPreviousPageBtn")
    private val reviewsNextPageBtn: Button by fxid("reviewsNextPageBtn")

    private val commentsPreviousPageBtn: Button by fxid("commentsPreviousPageBtn")
    private val commentsNextPageBtn: Button by fxid("commentsNextPageBtn")

    private var reviewsPage: Int = 0
    private var reviewsList: ArrayList<Review> = arrayListOf()
    private var chunkedReviewsList: List<List<Review>> = listOf()
    private var commentsPage: Int = 0
    private var commentsList: ArrayList<Comment> = arrayListOf()
    private var chunkedCommentsList: List<List<Comment>> = listOf()

    init {
        println("HomeView init")

        subscribe<HomeViewModel.GenericErrorEvent> {
            it.genericError?.let { genericError ->
                if (genericError.message == "The incoming token has expired") {
                    loginViewModel.logout()
                    alert(Alert.AlertType.WARNING, header = "Sessione scaduta, effettua nuovamente l'accesso", owner = currentWindow)
                } else {
                    alert(Alert.AlertType.WARNING, header = genericError.message, owner = currentWindow)
                }
            }
        }

        subscribe<HomeViewModel.NetworkErrorEvent> {
            println("NetworkErrorEvent")
            alert(Alert.AlertType.WARNING, header = it.message, owner = currentWindow)
        }

        subscribe<HomeViewModel.GetInappropriatesResponse> {
            it.getInappropriatesResponse?.let { inappropriatesResponse ->
                reviewsList = ArrayList(inappropriatesResponse.reviews)
                chunkedReviewsList = reviewsList.chunked(4)
                commentsList = ArrayList(inappropriatesResponse.comments)
                chunkedCommentsList = commentsList.chunked(4)
                bindInappropriatesReviews(reviewsList)
                bindInappropriatesComments(commentsList)
            }
        }

        subscribe<HomeViewModel.GetStatsResponse> {
            it.getStatsResponse?.let { statsResponse ->
                bindStats(statsResponse)
            }
        }

        timerangeCombobox.items = FXCollections.observableArrayList("Sempre", "Settimana", "Mese")

        timerangeCombobox.setOnAction {
            GlobalScope.launch {
                when (timerangeCombobox.value) {
                    "Sempre" -> {
                        viewModel.currentPeriod = HomeViewModel.PERIOD.EVER.value
                    }
                    "Settimana" -> {
                        viewModel.currentPeriod = HomeViewModel.PERIOD.WEEK.value
                    }
                    "Mese" -> {
                        viewModel.currentPeriod = HomeViewModel.PERIOD.MONTH.value
                    }
                    else -> viewModel.currentPeriod = HomeViewModel.PERIOD.EVER.value
                }
                viewModel.getStats(viewModel.currentPeriod)
            }
        }

        shakeHomeView()
    }

    fun shakeHomeView() {
        timerangeCombobox.value = "Sempre"
        viewModel.currentPeriod = HomeViewModel.PERIOD.EVER.value

        GlobalScope.launch {
            viewModel.getInappropriates()
            viewModel.getStats(viewModel.currentPeriod)
        }

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
                    bindInappropriatesReviews(chunkedReviewsList[reviewsPage])
                }
            }

            it.addClass(Styles.prevNextButton)
        }

        reviewsNextPageBtn.also {
            it.action {
                if (reviewsPage < chunkedReviewsList.size - 1) {
                    reviewsPage += 1
                    reviewsReportedAsInappropriateGrid.removeAllRows()
                    bindInappropriatesReviews(chunkedReviewsList[reviewsPage])
                }
            }

            it.addClass(Styles.prevNextButton)
        }

        commentsPreviousPageBtn.also {
            it.action {
                if (commentsPage > 0) {
                    commentsPage -= 1
                    commentsReportedAsInappropriateGrid.removeAllRows()
                    bindInappropriatesComments(chunkedCommentsList[commentsPage])
                }
            }

            it.addClass(Styles.prevNextButton)
        }

        commentsNextPageBtn.also {
            it.action {
                if (commentsPage < chunkedCommentsList.size - 1) {
                    commentsPage += 1
                    commentsReportedAsInappropriateGrid.removeAllRows()
                    bindInappropriatesComments(chunkedCommentsList[commentsPage])
                }
            }

            it.addClass(Styles.prevNextButton)
        }

        logoutBtn.action {
            loginViewModel.logout()
        }
    }

    private fun bindStats(statsResponse: StatsResponse) {
        statsResponse.usersCount?.let {
            usersCountLabel.text = it.toString()
            listsCountLabel.text = (it * 2).toString()
        }
        statsResponse.reviewsCount?.let { reviewsCountLabel.text = it.toString() }
        statsResponse.searchesCount?.let { searchesCountLabel.text = it.toString() }
    }

    private fun bindInappropriatesComments(commentsList: List<Comment>) {
        for (index in commentsList.indices) {
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
               ReportedCommentDetailsView(commentsList[index]).openWindow()
            }

            val horizontalBox = HBox()
            horizontalBox.alignment = Pos.CENTER_LEFT
            horizontalBox.spacing = 10.0
            verticalBox.add(horizontalBox)

            var profilePicUrl = commentsList[index].profilePic

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

            val usernameLabel = Label(commentsList[index].username)
            usernameLabel.textFill = Color.WHITE
            usernameLabel.style = "-fx-font-weight: bold"

            horizontalBox.add(profilePic)
            horizontalBox.add(usernameLabel)

            val reviewLabel = Label(commentsList[index].commentText)
            reviewLabel.textFill = Color.WHITE

            verticalBox.add(reviewLabel)

            commentsReportedAsInappropriateGrid.add(verticalBox, index % 2, if (index < 2) 0 else 1, 1, 1)
        }
    }

    private fun bindInappropriatesReviews(reviewsList: List<Review>) {
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
                ReportedReviewDetailsView(reviewsList[index]).openWindow()
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