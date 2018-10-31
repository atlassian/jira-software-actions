package com.atlassian.performance.tools.jirasoftwareactions.api.boards

/**
 * A Jira Agile board, either Kanban or Scrum.
 *
 * @param [id] string key of the board
 * @param [issuesOnBoard] number of issues on the board. If null then the issue count is unknown
 */
data class AgileBoard(
    override val id: String,
    override var issuesOnBoard: Int?
) : Board {

    constructor(
        id: String
    ) : this(
        id = id,
        issuesOnBoard = null
    )

    override fun isWorthVisiting(): Boolean {
        return issuesOnBoard != 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AgileBoard

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}