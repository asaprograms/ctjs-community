package com.chattriggers.ctjs.api.world

/** Legacy Forge note-block octave values. */
enum class NoteBlockOctave {
    LOW,
    MID,
    HIGH;

    companion object {
        @JvmStatic
        fun fromNoteId(noteId: Int) = when {
            noteId < 12 -> LOW
            noteId == 24 -> HIGH
            else -> MID
        }
    }
}
