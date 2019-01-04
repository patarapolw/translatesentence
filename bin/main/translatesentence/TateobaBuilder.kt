package translatesentence

import java.io.File
import java.sql.DriverManager

class TateobaBuilder () {
    private val conn = DriverManager
            .getConnection("jdbc:sqlite:/Users/patarapolw/GitHubProjects/translatesentence/src/main/resources/tatoeba.db")

    init {
        val statement = conn.createStatement();
        statement.execute("""
            CREATE TABLE IF NOT EXISTS sentences (
                id      INTEGER PRIMARY KEY,
                lang    TEXT NOT NULL,
                "text"  TEXT NOT NULL
            );
            CREATE TABLE IF NOT EXISTS "links" (
                id              INTEGER PRIMARY KEY AUTOINCREMENT,
                sentence_id     INTEGER NOT NULL,
                    FOREIGN KEY (sentence_id) REFERENCES sentences(id),
                translation_id  INTERGER NOT NULL,
                    FOREIGN KEY (translation_id) REFERENCES sentences(id)
            )
        """.trimIndent())
    }

    fun build(preferredLanguages: List<String>? = null) {
        val languages = preferredLanguages?.toSet()
        val sentenceIds = mutableSetOf<Int>()

        File("/Users/patarapolw/GitHubProjects/translatesentence/translatesentence/sentences.csv").forEachLine {
            val contents = it.trimEnd().split('\t')
//            println(contents)

            val sentenceId = contents[0].toInt()
            val language = contents[1]

            if (languages != null && languages.contains(language)) {
//                val preStatement = conn.prepareStatement("""
//                    INSERT INTO sentences (id, lang, "text")
//                    VALUES (?, ?, ?)
//                """.trimIndent())
//
//                preStatement.setInt(1, sentenceId)
//                preStatement.setString(2, language)
//                preStatement.setString(3, contents[2])
//
//                preStatement.executeUpdate()

                sentenceIds.add(sentenceId)
            }
        }

        File("/Users/patarapolw/GitHubProjects/translatesentence/translatesentence/links.csv").forEachLine {
            val contents =  it.trimEnd().split('\t')
            val sentenceId = contents[0].toInt()
            val translationId = contents[1].toInt()

            println(contents)

            if (sentenceIds.containsAll(setOf(sentenceId, translationId))) {
                val preStatement = conn.prepareStatement("""
                    INSERT INTO "links" (sentence_id, translation_id)
                    VALUES (?, ?)
                """.trimIndent())

                preStatement.setInt(1, sentenceId)
                preStatement.setInt(2, translationId)

                preStatement.executeUpdate()
            }


        }
    }
}

fun main(args : Array<String>) {
    TateobaBuilder().build(listOf("zho", "eng"))
}