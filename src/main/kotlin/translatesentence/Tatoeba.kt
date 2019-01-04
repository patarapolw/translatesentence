package translatesentence

import java.sql.DriverManager

class Tatoeba (databaseUrl: String) {
    private val conn = DriverManager.getConnection(databaseUrl)

    data class TateobaEntry (val from: String, val to: String)

    fun search(word: String, from: String = "cmn", to: String = "eng"): List<TateobaEntry> {
        val preStatement = conn.prepareStatement("""
            SELECT
                s1."text", s2."text"
            FROM
                sentences AS s1
                INNER JOIN "links"          ON s1.id = "links".sentence_id
                INNER JOIN sentences AS s2  ON "links".translation_id = s2.id
            WHERE
                s1."text" LIKE ? AND s1.lang = ? AND s2.lang = ?
        """.trimIndent())

        preStatement.setString(1, "%$word%")
        preStatement.setString(2, from)
        preStatement.setString(3, to)

        val resultSet = preStatement.executeQuery()
        val output = mutableListOf<TateobaEntry>()

        while (resultSet.next()) {
            output.add(TateobaEntry(from = resultSet.getString(1), to = resultSet.getString(2)))
        }

        return output.distinctBy { it.from }.toList()
    }
}
