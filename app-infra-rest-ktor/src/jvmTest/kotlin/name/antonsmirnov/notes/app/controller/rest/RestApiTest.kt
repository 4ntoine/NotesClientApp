package name.antonsmirnov.notes.app.controller.rest

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.junit.WireMockRule
import kotlinx.coroutines.runBlocking
import name.antonsmirnov.notes.usecase.NoteJson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import kotlin.math.absoluteValue

class RestApiTest {
    private val random = Random()

    @Rule
    @JvmField
    val wireMockRule = WireMockRule(wireMockConfig().dynamicPort())

    private lateinit var api: RestApi

    @Before
    fun setUp() {
        api = RestApi(wireMockRule.baseUrl())
    }

    private fun generateString() = random.nextLong().absoluteValue.toString()

    @Test
    fun testAddNote() {
        val expectedId = generateString()
        val expectedNote = NoteJson(null, generateString(), generateString())
        wireMockRule.stubFor(get(urlMatching("/api/add.*"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""{ "id":"$expectedId" }""")))

        runBlocking {
            val response = api.addNote(expectedNote)
            println(response)
            assertEquals(expectedId, response.id)
        }

        verify(getRequestedFor(urlEqualTo("/api/add?title=${expectedNote.title}&body=${expectedNote.body}")))
    }

    @Test
    fun testListNotes() {
        val expectedNote = NoteJson(null, generateString(), generateString())
        wireMockRule.stubFor(get(urlEqualTo("/api/list"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(
                    """
                    |{ "notes": [
                    |   {
                    |       "id":"${expectedNote.id}",
                    |       "title":"${expectedNote.title}",
                    |       "body":"${expectedNote.body}"
                    |   }
                    |]}""".trimMargin())))

        runBlocking {
            val response = api.listNotes()
            println(response)
            assertNotNull(response.notes)
            assertTrue(response.notes.isNotEmpty())
            val actualNote = response.notes.find {
                it.title == expectedNote.title && it.body == expectedNote.body
            }
            assertNotNull(actualNote)
            assertNotNull(actualNote!!.id)
        }
    }
}