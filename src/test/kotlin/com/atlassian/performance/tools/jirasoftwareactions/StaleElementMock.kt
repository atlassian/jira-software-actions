package com.atlassian.performance.tools.jirasoftwareactions

import org.openqa.selenium.*

internal class StaleElementMock(private val webElement: WebElementMock) : WebElement by WebElementMock(emptyMap()) {
    var isStale = false

    override fun findElement(by: By?): WebElement {
        return if (isStale) {
            throw StaleElementReferenceException("stale element reference: element is not attached to the page document")
        } else {
            webElement.findElement(by)
        }
    }

    override fun isEnabled(): Boolean {
        return if (isStale) {
            throw StaleElementReferenceException("stale element reference: element is not attached to the page document")
        } else {
            true
        }
    }

    override fun getAttribute(name: String?): String? {
        return if (isStale) {
            throw StaleElementReferenceException("stale element reference: element is not attached to the page document")
        } else {
            webElement.getAttribute(name)
        }
    }

    override fun findElements(by: By?): MutableList<WebElement> {
        return if (isStale) {
            throw StaleElementReferenceException("stale element reference: element is not attached to the page document")
        } else {
            webElement.findElements(by)
        }
    }
}