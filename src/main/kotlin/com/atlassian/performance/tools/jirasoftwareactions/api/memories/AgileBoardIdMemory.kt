package com.atlassian.performance.tools.jirasoftwareactions.api.memories

import com.atlassian.performance.tools.jiraactions.api.memories.Memory

@Deprecated(message = "Use AgileBoardMemory",
    replaceWith = ReplaceWith(
        expression = "AgileBoardMemory",
        imports = ["com.atlassian.performance.tools.jirasoftwareactions.api.memories.AgileBoardMemory"])
)
interface AgileBoardIdMemory : Memory<String>