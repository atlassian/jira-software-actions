package com.atlassian.performance.tools.jirasoftwareactions.api.page

import com.atlassian.performance.tools.jiraactions.api.page.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable
import java.time.Duration

class SprintOnBacklog(
    private val driver: WebDriver,
    private val element: WebElement
) {
    private val id: String
        get() = element.getAttribute("data-sprint-id")
    private val sprintActionsTriggerLocator = By.xpath("//*[@class='aui-button js-sprint-actions-trigger'][@data-sprint-id='$id']")
    private val editSprintLocator = By.xpath("//*[@id='gh-sprint-update'][@data-sprint-id='$id']")
    private val timeout = Duration.ofSeconds(5)

    fun openEditSprintDialog(): EditSprintDialog {
        driver.wait(timeout, elementToBeClickable(sprintActionsTriggerLocator)).click()
        driver.wait(timeout, elementToBeClickable(editSprintLocator)).click()
        return EditSprintDialog(driver)
    }
}
