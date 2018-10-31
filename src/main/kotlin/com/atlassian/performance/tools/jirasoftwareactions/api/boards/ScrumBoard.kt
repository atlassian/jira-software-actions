package com.atlassian.performance.tools.jirasoftwareactions.api.boards

class ScrumBoard(
    override val id: String,
    override var issuesOnBoard: Int?,
    var issuesInBacklog: Int?
) : Board {
    constructor(
        id: String
    ) : this(
        id = id,
        issuesOnBoard = null,
        issuesInBacklog = null
    )

    override fun isWorthVisiting(): Boolean {
        return issuesOnBoard != 0 || issuesInBacklog != 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ScrumBoard

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}