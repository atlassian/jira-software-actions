package com.atlassian.performance.tools.jirasoftwareactions

import com.atlassian.performance.tools.jiraactions.api.memories.Memory
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.AgileBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.Board
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.AgileBoardIdMemory
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.BoardMemory

/**
 * Translates the deprecated [AgileBoardIdMemory] into a [Memory] of [Board]s.
 *
 * @property boardIdMemory
 */
internal class CompatibleBoardMemory(
    private val boardIdMemory: AgileBoardIdMemory
) : BoardMemory<AgileBoard> {
    override fun recall(): AgileBoard? {
        val id = boardIdMemory.recall()
        return if (id != null) AgileBoard(id) else null
    }

    override fun recall(filter: (AgileBoard) -> Boolean): AgileBoard? {
        return recall()
    }

    override fun remember(memories: Collection<AgileBoard>) {
        boardIdMemory.remember(memories.map { it.id })
    }
}