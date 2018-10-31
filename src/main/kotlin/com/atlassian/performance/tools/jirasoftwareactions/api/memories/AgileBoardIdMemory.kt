package com.atlassian.performance.tools.jirasoftwareactions.api.memories

import com.atlassian.performance.tools.jiraactions.api.memories.Memory

@Deprecated(
    message = "Use BoardMemory",
    replaceWith = ReplaceWith(
        expression = "BoardMemory",
        imports = [
            "com.atlassian.performance.tools.jirasoftwareactions.api.memories.BoardMemory"
        ]
    )
)
interface AgileBoardIdMemory : Memory<String>