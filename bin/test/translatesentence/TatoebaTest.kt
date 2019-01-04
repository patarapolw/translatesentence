package translatesentence

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class TatoebaTest {
    private val tatoeba = Tatoeba("zho", "eng")

    @TestFactory
    fun testSearchChinese() = listOf(
            "你好",
            "中文"
    ).map { input ->
        dynamicTest("Search for $input") {
            println(tatoeba[input])
            println(input)
        }
    }
}

