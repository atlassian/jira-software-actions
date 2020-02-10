package com.atlassian.performance.tools.jirasoftwareactions.api.scenario

import com.atlassian.performance.tools.dockerinfrastructure.api.browser.DockerisedChrome
import com.atlassian.performance.tools.dockerinfrastructure.api.jira.JiraSoftwareFormula
import com.atlassian.performance.tools.jiraactions.api.ActionMetric
import com.atlassian.performance.tools.jiraactions.api.ActionResult
import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.measure.output.CollectionActionMetricOutput
import com.atlassian.performance.tools.jiraactions.api.memories.User
import com.atlassian.performance.tools.jiraactions.api.memories.UserMemory
import com.atlassian.performance.tools.jiraactions.api.w3c.DisabledW3cPerformanceTimeline
import com.atlassian.performance.tools.jirasoftwareactions.api.JiraSoftwareScenario
import com.atlassian.performance.tools.jirasoftwareactions.api.actions.ViewBacklogAction.Companion.VIEW_BACKLOG
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.assertj.core.api.Assertions
import org.junit.Test
import java.nio.file.Paths
import java.time.Clock
import java.util.*

class JiraSoftwareScenarioIT {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    /**
     * During the test, you can connect to WebDriver by the VNC viewer.
     * DockerisedChrome opens the port 5900 so executing command
     * docker ps
     * will show you which port on your host is open and mapped to 5900 on the container.
     * The default password is `secret`.
     */
    @Test
    fun shouldRunScenarioWithoutErrors() {
        val version = System.getenv("JIRA_SOFTWARE_VERSION") ?: "8.5.3"
        logger.info("Testing Jira $version")
        val scenario = JiraSoftwareScenario()
        val metrics = mutableListOf<ActionMetric>()
        val actionMeter = ActionMeter(
            virtualUser = UUID.randomUUID(),
            output = CollectionActionMetricOutput(metrics),
            clock = Clock.systemUTC(),
            w3cPerformanceTimeline = DisabledW3cPerformanceTimeline()
        )
        val user = User("admin", "admin")
        val userMemory = object : UserMemory {
            override fun recall(): User {
                return user
            }

            override fun remember(memories: Collection<User>) {
                throw Exception("not implemented")
            }
        }

        JiraSoftwareFormula.Builder()
            .version(version)
            .build()
            .provision()
            .use { jira ->
                val recordings = Paths.get("build/diagnoses/recordings/" + this::class.java.simpleName)
                DockerisedChrome(recordings).start().use { browser ->
                    val driver = browser.driver
                    val webJira = WebJira(
                        driver,
                        jira.getUri(),
                        user.password
                    )
                    val logInAction = scenario.getLogInAction(
                        webJira,
                        actionMeter,
                        userMemory
                    )
                    val setupAction = scenario.getSetupAction(
                        webJira,
                        actionMeter
                    )
                    val actions = scenario.getActions(
                        webJira,
                        SeededRandom(123),
                        actionMeter
                    )

                    logInAction.run()
                    setupAction.run()
                    actions.forEach { action ->
                        action.run()
                    }
                }
            }

        val results = metrics.map { metric ->
            metric.result
        }
        Assertions.assertThat(results).containsOnly(ActionResult.OK)
        val viewIssueMetrics = metrics.filter {
            VIEW_BACKLOG.label == it.label
        }
        Assertions.assertThat(viewIssueMetrics).allMatch { m -> m.observation != null }
    }
}
