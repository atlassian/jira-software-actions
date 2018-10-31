package com.atlassian.performance.tools.jirasoftwareactions.api.memories

import com.atlassian.performance.tools.jiraactions.api.memories.Memory
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.Board

interface BoardMemory<T : Board> : Memory<T> {
    fun recall(filter: (T) -> Boolean): T?
}

