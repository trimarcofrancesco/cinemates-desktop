package it.unina.cinemates_desktop.view

import it.unina.cinemates_desktop.model.UserModel
import it.unina.cinemates_desktop.viewmodel.LoginViewModel
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
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
        verticalBox.padding = Insets(5.0, 5.0, 5.0, 5.0)
        val horizontalBox = HBox()
        verticalBox.add(horizontalBox)

        val usernameLabel = Label("Test")
        horizontalBox.add(usernameLabel)

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
