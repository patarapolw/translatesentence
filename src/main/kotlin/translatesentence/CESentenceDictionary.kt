package translatesentence

import net.duguying.pinyin.Pinyin

class CESentenceDictionary (databaseUrl: String) {
    private val py = Pinyin()
    private val tatoeba = Tatoeba(databaseUrl)

    data class CEResult (val chinese: String, val pinyin: String, val english: String)

    operator fun get(s: String): List<CEResult> {
        val result = tatoeba.search(s, from = "cmn", to = "eng")
        return result.map { CEResult(chinese = it.from, pinyin = py.translate(it.from), english = it.to) }
    }
}