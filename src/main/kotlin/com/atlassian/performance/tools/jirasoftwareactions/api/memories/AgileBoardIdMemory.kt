package com.atlassian.performance.tools.jirasoftwareactions.api.memories

import com.atlassian.performance.tools.jiraactions.api.memories.Memory

@Deprecated(
    message = "Use Memory<Board>",
    replaceWith = ReplaceWith(
        expression = "Memory<Board>",
        imports = [
            "com.atlassian.performance.tools.jiraactions.api.memories.Memory",
            "com.atlassian.performance.tools.jirasoftwareactions.api.boards.Board"
        ]
    )
)
interface AgileBoardIdMemory : Memory<String>