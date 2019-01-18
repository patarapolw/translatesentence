package translatesentence

import com.huaban.analysis.jieba.JiebaSegmenter
import net.duguying.pinyin.Pinyin

class CESentenceDictionary (databaseUrl: String) {
    private val py = Pinyin()
    private val tatoeba = Tatoeba(databaseUrl)
    private val jieba = JiebaSegmenter()

    data class CEResult (val chinese: String, val pinyin: String, val english: String)

    operator fun get(s: String): List<CEResult> {
        val result = tatoeba.search(s, from = "cmn", to = "eng")
        return result.map { CEResult(chinese = it.from, pinyin = getPinyin(it.from), english = it.to) }
    }

    private fun getPinyin(s: String): String {
        val output = mutableListOf<String>()

        var previousIsHan = false
        jieba.sentenceProcess(s).forEach { segment ->
            if (Regex("\\p{IsHan}").containsMatchIn(segment)) {
                if (previousIsHan) output.add(" ")
                previousIsHan = true
            } else {
                previousIsHan = false
            }

            output.add(segment)
        }

        return py.translate(output.joinToString(""))
    }
}