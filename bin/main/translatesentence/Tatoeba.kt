package translatesentence

class Tatoeba (from: String, to: String) {
    private val sentenceMap: Map<String, String>;

    init {
        val sentenceFrom: MutableMap<Int, String> = mutableMapOf()
        val sentenceTo: MutableMap<Int, String> = mutableMapOf()
        val sentenceMap_: MutableMap<String, String> = mutableMapOf()

        this::class.java.classLoader.getResource("sentences.csv").readText().split('\n').forEach {
            val contents = it.split('\t')
            if (contents.size >= 3) {
                when (contents[1]) {
                    from -> sentenceFrom[contents[0].toInt()] = contents[2]
                    to -> sentenceTo[contents[0].toInt()] = contents[2]
                }
            }
        }

        this::class.java.classLoader.getResource("links.csv").readText().split('\n').forEach {
            val contents = it.split('\t')
            if (contents.size >= 2) {
                sentenceFrom[contents[0].toInt()]?.let {
                    sentenceTo[contents[1].toInt()]?.let { it2 -> sentenceMap_[it] = it2 }
                }
            }
        }

        sentenceMap = sentenceMap_.toMap()
    }

    operator fun get(word: String): Map<String, String> {
        return sentenceMap.filterKeys { it.contains(word) }
    }
}
