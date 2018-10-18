package com.atlassian.performance.tools.jirasoftwareactions.api.memories

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.memories.Memory
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.Board
import net.jcip.annotations.NotThreadSafe

@NotThreadSafe
class AdaptiveBoardMemory<T : Board>(
    private val random: SeededRandom
) : Memory<T> {
    private val boards = mutableSetOf<T>()

    override fun recall(): T? {
        return random.pick(boards.toList())
    }

    override fun remember(memories: Collection<T>) {
        memories.forEach {
            if (!boards.add(it)) {
                boards.remove(it)
                boards.add(it)
            }
        }
    }
}