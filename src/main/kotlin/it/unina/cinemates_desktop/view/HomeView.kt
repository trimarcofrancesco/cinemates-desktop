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
import javafx.scene.layout.GridPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
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

    private var reviewsPage: Int = 0
    private var reviewsList: ArrayList<Review> = arrayListOf()

    init {

        GlobalScope.launch { viewModel.getInappropriates() }
        subscribe<HomeViewModel.GetInappropriatesResponse> {
            it.getInappropriatesResponse?.let { inappropriatesResponse ->
                reviewsList = ArrayList(inappropriatesResponse.reviews)
                bindInappropriates(reviewsList)
            }
        }

        timerangeCombobox.items = FXCollections.observableArrayList("Sempre", "Settimana", "Mese")
        timerangeCombobox.value = "Sempre"

        logoutBtn.action {
            loginViewModel.logout()
        }
    }

    private fun bindInappropriates(reviewsList: ArrayList<Review>) {
        val verticalBox = VBox()
        verticalBox.padding = Insets(20.0, 20.0, 20.0, 20.0)
        val horizontalBox = HBox()
        horizontalBox.alignment = Pos.CENTER_LEFT
        verticalBox.add(horizontalBox)

        reviewsList.forEachIndexed { index, review ->
            if(index <= 3){
                val profilePic = ImageView("/propic_1.png")
                profilePic.fitHeight = 30.0
                profilePic.fitWidth = 30.0

                val usernameLabel = Label(review.username)

                horizontalBox.add(profilePic)
                horizontalBox.add(usernameLabel)

                val reviewLabel = Label(review.reviewText)

                verticalBox.add(reviewLabel)

                reviewsReportedAsInappropriateGrid.add(verticalBox, index%2, if(index<2) 0 else 1, 1, 1)
            }
        }

    }

    override fun onDock() {
        primaryStage.width = 1200.0
        primaryStage.height = 600.0
    }

}
