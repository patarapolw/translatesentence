package translatesentence

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class CESentenceDictionaryTest {
    private val ceSentenceDictionary = CESentenceDictionary("jdbc:sqlite:tatoeba.db")

    @TestFactory
    fun testSearchChinese() = listOf(
            "你好",
            "中文"
    ).map { input ->
        DynamicTest.dynamicTest("Search for $input") {
            println(ceSentenceDictionary[input])
        }
    }
}