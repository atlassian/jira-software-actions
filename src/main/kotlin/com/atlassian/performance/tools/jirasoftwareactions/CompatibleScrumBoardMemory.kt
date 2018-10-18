package com.atlassian.performance.tools.jirasoftwareactions

import com.atlassian.performance.tools.jiraactions.api.memories.Memory
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.ScrumBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.AgileBoardIdMemory

/**
 * Translates the deprecated [AgileBoardIdMemory] into a [Memory] of [ScrumBoard]s.
 *
 * @property boardIdMemory
 */
internal class CompatibleScrumBoardMemory(
    private val boardIdMemory: AgileBoardIdMemory
) : Memory<ScrumBoard> {
    override fun recall(): ScrumBoard? {
        val id = boardIdMemory.recall()
        return if (id != null) ScrumBoard(id) else null
    }

    override fun remember(memories: Collection<ScrumBoard>) {
        boardIdMemory.remember(memories.map { it.id })
    }
}