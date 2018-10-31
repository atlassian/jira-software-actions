package com.atlassian.performance.tools.jirasoftwareactions.api.memories

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.Board
import net.jcip.annotations.NotThreadSafe

@NotThreadSafe
class AdaptiveBoardMemory<T : Board>(
    private val random: SeededRandom
) : BoardMemory<T> {
    private val boards = mutableSetOf<T>()

    override fun recall(): T? {
        return random.pick(boards.toList())
    }

    override fun recall(filter: (T) -> Boolean): T? {
        return random.pick(boards.asSequence().filter(filter).toList())
    }

    override fun remember(memories: Collection<T>) {
        boards.addAll(memories)
    }
}