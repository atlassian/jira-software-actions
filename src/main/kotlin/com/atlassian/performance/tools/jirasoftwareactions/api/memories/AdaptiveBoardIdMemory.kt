package com.atlassian.performance.tools.jirasoftwareactions.api.memories

import com.atlassian.performance.tools.jiraactions.SeededRandom

class AdaptiveBoardIdMemory(
    private val random: SeededRandom
) : AgileBoardIdMemory {
    private val boardIds = mutableSetOf<String>()

    override fun recall(): String? {
        if (boardIds.isEmpty()) {
            return null
        }

        return random.pick(boardIds.toList())
    }

    override fun remember(memories: Collection<String>) {
        boardIds.addAll(memories)
    }
}