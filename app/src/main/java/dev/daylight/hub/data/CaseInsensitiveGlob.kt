package dev.daylight.hub.data

/** SQLite LIKE only folds ASCII. GLOB character sets retain Unicode ignore-case substring search. */
object CaseInsensitiveGlob {
    private val groups:Map<Char,String> by lazy {
        val values=mutableMapOf<Char,MutableSet<Char>>()
        for(code in 0..0xffff) {
            val character=code.toChar();val folded=character.uppercaseChar().lowercaseChar()
            if(character!=folded || character.uppercaseChar()!=character) {
                values.getOrPut(folded){linkedSetOf(folded)}.add(character)
            }
        }
        values.mapValues{(_,characters)->characters.sorted().joinToString("")}
    }
    fun pattern(query:String):String=buildString {
        append('*')
        query.forEach{character->
            val variants=groups[character.uppercaseChar().lowercaseChar()]
            if(variants!=null && variants.length>1)append('[').append(variants).append(']')
            else when(character){'*'->append("[*]");'?'->append("[?]");'['->append("[[]");else->append(character)}
        }
        append('*')
    }
}
