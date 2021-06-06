package it.unina.cinemates_desktop.view

import it.unina.cinemates_desktop.model.UserModel
import it.unina.cinemates_desktop.viewmodel.LoginViewModel
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import tornadofx.*

class HomeView : View("Cinemates") {
    private val user: UserModel by inject()
    private val loginViewModel: LoginViewModel by inject()

    override val root : BorderPane by fxml("/views/HomeView.fxml")

    val timerangeCombobox : ComboBox<String> by fxid("timerangeCombobox")
    val reviewsReportedAsInappropriateGrid : GridPane by fxid("reviewsReportedAsInappropriateGrid")

    init {
        timerangeCombobox.items = FXCollections.observableArrayList("Sempre", "Settimana", "Mese")
        timerangeCombobox.value = "Sempre"

        val verticalBox = VBox()
        verticalBox.padding = Insets(20.0, 20.0, 20.0, 20.0)
        val horizontalBox = HBox()
        horizontalBox.alignment = Pos.CENTER_LEFT
        verticalBox.add(horizontalBox)

        val profilePic = ImageView("/propic_1.png")
        profilePic.fitHeight = 30.0
        profilePic.fitWidth = 30.0

        val usernameLabel = Label("Test")

        horizontalBox.add(profilePic)
        horizontalBox.add(usernameLabel)

        val reviewLabel = Label("Lorem ipsum")

        verticalBox.add(reviewLabel)

        reviewsReportedAsInappropriateGrid.add(verticalBox, 0, 0, 1, 1)
    }

    override fun onDock() {
        primaryStage.width = 1200.0
        primaryStage.height = 600.0
    }

    /*
    <children>
      <VBox prefHeight="200.0" prefWidth="100.0">
         <children>
            <HBox alignment="CENTER_LEFT" prefHeight="100.0" prefWidth="200.0">
               <children>
                  <ImageView fitHeight="48.0" fitWidth="48.0" pickOnBounds="true" preserveRatio="true">
                     <image>
                        <Image url="@../propic_1.png" />
                     </image>
                  </ImageView>
                  <Label text="Label" />
               </children>
            </HBox>
         </children>
      </VBox>
   </children>
     */

}
