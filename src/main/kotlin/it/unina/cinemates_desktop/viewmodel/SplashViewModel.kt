package it.unina.cinemates_desktop.viewmodel

import it.unina.cinemates_desktop.model.UserModel
import it.unina.cinemates_desktop.model.toUser
import it.unina.cinemates_desktop.util.AppPreferences
import it.unina.cinemates_desktop.view.HomeView
import it.unina.cinemates_desktop.view.LoginView
import it.unina.cinemates_desktop.view.SplashView
import tornadofx.ViewModel
import tornadofx.ViewTransition
import tornadofx.seconds

class SplashViewModel: ViewModel() {

    private val user : UserModel by inject()

    private fun userIsSignedIn(): Boolean = AppPreferences.loginResponse != null

    private fun loadUserFromPreferences() {
        user.item = AppPreferences.loginResponse?.toUser()
    }

    fun loadNextView() {
        if (userIsSignedIn()) {
            loadUserFromPreferences()
            find(SplashView::class).replaceWith(HomeView::class, transition = ViewTransition.FadeThrough(.5.seconds), centerOnScreen = true)
        } else {
            find(SplashView::class).replaceWith(LoginView::class, transition = ViewTransition.FadeThrough(.5.seconds), centerOnScreen = true)
        }
    }
}