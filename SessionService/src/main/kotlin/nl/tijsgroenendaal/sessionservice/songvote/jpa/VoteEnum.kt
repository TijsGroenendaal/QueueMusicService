package nl.tijsgroenendaal.sessionservice.songvote.jpa

enum class VoteEnum(val value: Int) {
    PLUS(1),
    MINUS(-1),
}