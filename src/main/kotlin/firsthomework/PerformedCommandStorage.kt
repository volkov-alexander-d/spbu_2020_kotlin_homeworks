package firsthomework

import kotlinx.serialization.decodeFromString
import kotlin.collections.ArrayDeque
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileWriter

class PerformedCommandStorage {
    val arrayDeque: ArrayDeque<Int>
        get() {
            return _arrayDeque
        }
    private val _arrayDeque: ArrayDeque<Int> = ArrayDeque()
    private val actions: java.util.Stack<Action> = java.util.Stack()
    fun addAction(action: Action) = actions.addElement(action)
    fun undo() {
        if (actions.isEmpty()) {
            println("No action has been taken yet.")
        } else {
            actions.lastElement().undo(this)
            actions.removeLast()
        }
    }

    fun print() {
        if (this._arrayDeque.size == 0) {
            println("No elements (empty queue)")
        } else {
            println(this._arrayDeque.joinToString(" "))
        }
    }

    fun serialize(path: String) {
        val temporaryList = actions.toList()
        val stringInJsonFormat = Json.encodeToString(temporaryList)
        val newFile = FileWriter(path)
        newFile.write(stringInJsonFormat)
        newFile.flush()
    }

    fun deserialize(path: String) {
        val stringInJsonFormat = File(path).inputStream().readAllBytes().toString(Charsets.UTF_8)
        val listWithActions = Json.decodeFromString<List<Action>>(stringInJsonFormat)
        for (action in listWithActions) {
            action.execute(this)
        }
    }
}
