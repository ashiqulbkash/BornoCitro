package com.bornochitra

import com.bornochitra.core.tips.ContextualTip
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.w3c.dom.Element

/** The app's UI text: Bangla only, in the default string set. */
class StringResourcesTest {

    /** One string set, without the strings marked `translatable="false"`. */
    private class StringSet(folder: String) {
        private val document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            .parse(File("src/main/res/$folder/strings.xml"))

        val strings: Map<String, String> = elements("string")
            .filter { it.getAttribute("translatable") != "false" }
            .associate { it.getAttribute("name") to it.textContent }

        /** Each plural's items by quantity. */
        val plurals: Map<String, Map<String, String>> = elements("plurals").associate { plural ->
            val items = plural.getElementsByTagName("item")
            plural.getAttribute("name") to (0 until items.length).map { items.item(it) as Element }
                .associate { it.getAttribute("quantity") to it.textContent }
        }

        val allTexts: Map<String, String> =
            strings + plurals.flatMap { (name, items) -> items.map { (quantity, text) -> "$name[$quantity]" to text } }

        private fun elements(tag: String): List<Element> = document.getElementsByTagName(tag).let { nodes ->
            (0 until nodes.length).map { nodes.item(it) as Element }
        }
    }

    private val bangla = StringSet("values")
    private val formatArgs = Regex("""%(\d+\$)?[ds%]""")

    @Test
    fun `every string has text`() {
        bangla.allTexts.forEach { (name, text) -> assertTrue("$name is blank", text.isNotBlank()) }
    }

    @Test
    fun `the app has no other string set, so the UI is Bangla on every phone`() {
        val otherSets = File("src/main/res").listFiles()
            .orEmpty()
            .filter { it.isDirectory && it.name.startsWith("values-") && File(it, "strings.xml").exists() }

        assertEquals(emptyList<String>(), otherSets.map { it.name })
    }

    @Test
    fun `the strings are Bangla, with no Latin letters`() {
        bangla.allTexts.forEach { (name, text) ->
            val latin = formatArgs.replace(text, "").filter { it in 'a'..'z' || it in 'A'..'Z' }
            assertTrue("$name has Latin letters: \"$text\"", latin.isEmpty())
        }
    }

    @Test
    fun `the splash tagline leaves the app name to the wordmark above it`() {
        val tagline = checkNotNull(bangla.strings["splash_tagline"])
        assertTrue(tagline.isNotBlank())
        assertTrue("\"$tagline\" repeats the app name", !tagline.contains(bangla.strings.getValue("app_name")))
    }

    @Test
    fun `toolbar titles are short`() {
        val titles = bangla.strings.filterKeys { it.startsWith("title_") }
        assertTrue(titles.isNotEmpty())
        titles.forEach { (name, text) ->
            assertTrue("$name is longer than three words: \"$text\"", text.trim().split(Regex("""\s+""")).size <= 3)
        }
    }

    @Test
    fun `every contextual tip has its own wording`() {
        val tips = bangla.strings.filterKeys { it.startsWith("tip_") }
        assertEquals(ContextualTip.entries.size, tips.size)
        assertEquals(tips.size, tips.values.toSet().size)
    }

    @Test
    fun `tip wording never blames the child`() {
        val discouraging = listOf("ভুল", "খারাপ", "ব্যর্থ", "পারোনি", "wrong", "fail", "bad", "mistake")

        bangla.strings.filterKeys { it.startsWith("tip_") }.forEach { (name, text) ->
            discouraging.forEach { word ->
                assertFalse("$name contains \"$word\"", text.contains(word, ignoreCase = true))
            }
        }
    }

    @Test
    fun `summaries carry the count and the percentage in every plural form`() {
        listOf("result_session_summary", "fill_blanks_summary").forEach { name ->
            val items = bangla.plurals.getValue(name)
            assertTrue(items.isNotEmpty())
            items.values.forEach { text ->
                assertTrue("$name lacks the count: \"$text\"", text.contains("%1\$d"))
                assertTrue("$name lacks the percentage: \"$text\"", text.contains("%2\$d%%"))
            }
        }
    }
}
