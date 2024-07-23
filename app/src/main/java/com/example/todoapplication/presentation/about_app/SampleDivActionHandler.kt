package com.example.todoapplication.presentation.about_app

import androidx.navigation.NavController
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div.json.expressions.ExpressionResolver
import com.yandex.div2.DivAction


class SampleDivActionHandler(
    private val navController: NavController,
    private val expressionResolver: ExpressionResolver
) : DivActionHandler() {
    override fun handleAction(action: DivAction, viewFacade: DivViewFacade): Boolean {
        super.handleAction(action, viewFacade)
        val url = action.url?.evaluate(expressionResolver)

        if (url?.scheme == "nav-action" && url.host == "close") {
            navController.popBackStack()
            return true
        }
        return false
    }
}
