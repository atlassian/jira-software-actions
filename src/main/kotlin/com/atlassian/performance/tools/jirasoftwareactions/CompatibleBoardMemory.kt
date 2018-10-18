package com.atlassian.performance.tools.jirasoftwareactions

import com.atlassian.performance.tools.jiraactions.api.memories.Memory
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.AgileBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.Board
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.AgileBoardIdMemory

/**
 * Translates the deprecated [AgileBoardIdMemory] into a [Memory] of [Board]s.
 *
 * @property boardIdMemory
 */
internal class CompatibleBoardMemory(
    private val boardIdMemory: AgileBoardIdMemory
) : Memory<Board> {
    override fun recall(): Board? {
        val id = boardIdMemory.recall()
        return if (id != null) AgileBoard(id) else null
    }

    override fun remember(memories: Collection<Board>) {
        boardIdMemory.remember(memories.map { it.id })
    }
}