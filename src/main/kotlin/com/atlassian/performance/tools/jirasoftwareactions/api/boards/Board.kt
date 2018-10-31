package com.atlassian.performance.tools.jirasoftwareactions.api.boards

interface Board {
    val id: String
    var issuesOnBoard: Int?
    @Deprecated(message = "Use ViewBoardAction.filter")
    fun isWorthVisiting(): Boolean
}