package br.com.menardi.scheduling.service

import br.com.menardi.scheduling.model.ExecutionWindow
import com.beust.klaxon.KlaxonException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.io.StringReader
import java.time.LocalDateTime

internal class JsonParserServiceTest {
    private val service = JsonParserService()

    @Test
    fun deveRetornarUmObjetoDadoUmReaderComJsonValido() {
        val reader = StringReader(
            """
            {
            	"start": "2019-11-10 09:00:00",
            	"end": "2019-11-11 12:00:00"
            }
        """
        )

        val executionWindowResult = service.parseFromReader<ExecutionWindow>(reader)
        val executionWindowExpected = ExecutionWindow(
            start = LocalDateTime.of(2019, 11, 10, 9, 0, 0),
            end = LocalDateTime.of(2019, 11, 11, 12, 0, 0)
        )

        assertEquals(executionWindowExpected, executionWindowResult)
    }

    @Test
    fun deveLancarClassCastExceptionDadoUmReaderComObjetoInvalido() {
        val reader = StringReader("{}")

        assertThrows(ClassCastException::class.java) { service.parseArrayFromReader<ExecutionWindow>(reader) }
    }

    @Test
    fun deveLancarKlaxonExceptionDadoUmReaderComJsonInvalido() {
        val reader2 = StringReader(
            """
            {
                "start": "2021-02-22 10:37:32
            }
        """
        )

        assertThrows(KlaxonException::class.java) { service.parseArrayFromReader<ExecutionWindow>(reader2) }
    }

    @Test
    fun deveRetornarUmArrayDadoUmReaderComArrayValido() {
        val reader = StringReader(
            """
            [
                {
            	    "start": "2019-11-10 09:00:00",
            	    "end": "2019-11-11 12:00:00"
                },
                {
                    "start": "2021-02-22 10:24:23",
                    "end": "2021-02-23 12:00:00"
                }
            ]
        """
        )

        val arrayExecutionWindowResult = service.parseArrayFromReader<ExecutionWindow>(reader)
        val arrayExecutionWindowExpected = listOf(
            ExecutionWindow(
                start = LocalDateTime.of(2019, 11, 10, 9, 0, 0),
                end = LocalDateTime.of(2019, 11, 11, 12, 0, 0)
            ), ExecutionWindow(
                start = LocalDateTime.of(2021, 2, 22, 10, 24, 23),
                end = LocalDateTime.of(2021, 2, 23, 12, 0, 0)
            )
        )

        assertEquals(arrayExecutionWindowExpected, arrayExecutionWindowResult)
    }

    @Test
    fun deveRetornarUmArrayVazioDadoUmReaderComArrayVazio() {
        val reader = StringReader("[]")

        val arrayExecutionWindowResult = service.parseArrayFromReader<ExecutionWindow>(reader)
        val arrayExecutionWindowExpected = emptyList<ExecutionWindow>()

        assertEquals(arrayExecutionWindowExpected, arrayExecutionWindowResult)
    }

    @Test
    fun deveLancarKlaxonExceptionDadoUmReaderComArrayInvalido() {
        val reader = StringReader("[{}, {}]")

        assertThrows(KlaxonException::class.java) { service.parseArrayFromReader<ExecutionWindow>(reader) }
    }

    @Test
    fun deveRetornarUmaStringComJsonValidoDadoUmObjeto() {
        val executionWindow = ExecutionWindow(
            start = LocalDateTime.of(2019, 11, 10, 9, 0, 0),
            end = LocalDateTime.of(2019, 11, 11, 12, 0, 0)
        )

        val jsonStringResult = service.toJsonString(executionWindow)
        val jsonStringExpected = """{"end" : "2019-11-11 12:00:00", "start" : "2019-11-10 09:00:00"}"""

        assertEquals(jsonStringExpected, jsonStringResult)
    }

    @Test
    fun deveRetornarUmaStringComJsonValidoDadoUmArray() {
        val listExecutionWindow = listOf(
            ExecutionWindow(
                start = LocalDateTime.of(2019, 11, 10, 9, 0, 0),
                end = LocalDateTime.of(2019, 11, 11, 12, 0, 0)
            ), ExecutionWindow(
                start = LocalDateTime.of(2021, 2, 22, 10, 24, 23),
                end = LocalDateTime.of(2021, 2, 23, 12, 0, 0)
            )
        )

        val jsonResult = service.toJsonString(listExecutionWindow)
        val jsonExpected =
            """[{"end" : "2019-11-11 12:00:00", "start" : "2019-11-10 09:00:00"}, {"end" : "2021-02-23 12:00:00", "start" : "2021-02-22 10:24:23"}]"""

        assertEquals(jsonExpected, jsonResult)
    }

    @Test
    fun deveRetornarUmaStringComArrayVazioValidoDadoUmArrayVazio() {
        val listExecutionWindow = emptyList<ExecutionWindow>()

        val jsonResult = service.toJsonString(listExecutionWindow)
        val jsonExpected = "[]"

        assertEquals(jsonExpected, jsonResult)
    }
}