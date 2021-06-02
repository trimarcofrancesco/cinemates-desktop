package it.unina.cinemates_desktop.model

import it.unina.cinemates_desktop.util.AppPreferences
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class User(loginResponse: LoginResponse) {
    val profileProperty = SimpleObjectProperty(this, "profile", loginResponse.profile)
    var profile by profileProperty

    val accessTokenProperty = SimpleStringProperty(this, "username", loginResponse.accessToken)
    var accessToken by accessTokenProperty

    val expiresInProperty = SimpleLongProperty(this, "username", loginResponse.expiresIn ?: 0)
    var expiresIn by expiresInProperty

    val refreshTokenProperty = SimpleStringProperty(this, "username", loginResponse.refreshToken)
    var refreshToken by refreshTokenProperty

    val idTokenProperty = SimpleStringProperty(this, "username", loginResponse.idToken)
    var idToken by idTokenProperty

    /*val userIdProperty = SimpleStringProperty(this, "username", loginResponse.profile.userId)
    var userId by userIdProperty

    val usernameProperty = SimpleStringProperty(this, "username", loginResponse.profile.username)
    var username by usernameProperty

    val emailProperty = SimpleStringProperty(this, "email", loginResponse.profile.email)
    var email by emailProperty

    val profilePicProperty = SimpleStringProperty(this, "email", loginResponse.profile.profilePic)
    var profilePic by profilePicProperty*/
}

data class UserCredential(
    val username: String,
    val password: String,
    val isAdmin: String
)

class UserModel : ItemViewModel<User>() {
    val profile = bind(User::profileProperty)
    val accessToken = bind(User::accessTokenProperty)
    val expiresIn = bind(User::expiresInProperty)
    val refreshToken = bind(User::refreshTokenProperty)
    val idToken = bind(User::idTokenProperty)

    /*val username = bind(User::usernameProperty)
    val email = bind(User::emailProperty)*/
}