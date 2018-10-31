package com.atlassian.performance.tools.jirasoftwareactions

import com.atlassian.performance.tools.jiraactions.api.memories.Memory
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.ScrumBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.AgileBoardIdMemory
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.BoardMemory

/**
 * Translates the deprecated [AgileBoardIdMemory] into a [Memory] of [ScrumBoard]s.
 *
 * @property boardIdMemory
 */
internal class CompatibleScrumBoardMemory(
    private val boardIdMemory: AgileBoardIdMemory
) : BoardMemory<ScrumBoard> {
    override fun recall(): ScrumBoard? {
        val id = boardIdMemory.recall()
        return if (id != null) ScrumBoard(id) else null
    }

    override fun recall(filter: (ScrumBoard) -> Boolean): ScrumBoard? {
        return recall()
    }

    override fun remember(memories: Collection<ScrumBoard>) {
        boardIdMemory.remember(memories.map { it.id })
    }
}