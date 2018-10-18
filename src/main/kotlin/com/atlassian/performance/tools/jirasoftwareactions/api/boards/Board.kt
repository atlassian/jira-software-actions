package com.atlassian.performance.tools.jirasoftwareactions.api.boards

interface Board {
    val id: String
    val issuesOnBoard: Int?
    fun isWorthVisiting(): Boolean
}