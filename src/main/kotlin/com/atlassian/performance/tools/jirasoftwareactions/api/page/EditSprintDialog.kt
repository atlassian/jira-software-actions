package com.atlassian.performance.tools.jirasoftwareactions.api.page

import com.atlassian.performance.tools.jiraactions.api.page.wait
import com.atlassian.performance.tools.jiraactions.api.webdriver.sendKeysWhenClickable
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated
import java.time.Duration

class EditSprintDialog(
    private val driver: WebDriver
) {
    private val editSprintDialogLocator = By.id("ghx-dialog-edit-sprint")
    private val sprintNameFieldLocator = By.id("ghx-sprint-name")
    private val duration = Duration.ofSeconds(30)

    fun waitForDialog(): EditSprintDialog {
        driver.wait(
            duration,
            visibilityOfElementLocated(editSprintDialogLocator)
        )
        return this
    }

    fun editSprintName(newSprintName: String): ViewBacklogPage {
        val sprintNameField = driver.findElement(sprintNameFieldLocator)
        sprintNameField.clear()
        sprintNameField.sendKeysWhenClickable(driver, newSprintName).submit()
        return ViewBacklogPage(driver)
    }
}
