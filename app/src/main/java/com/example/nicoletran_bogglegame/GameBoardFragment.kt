package com.example.nicoletran_bogglegame


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.*

class GameBoardFragment : Fragment() {
    private val selectedLetters = StringBuilder()
    private val usedWords = mutableSetOf<String>()
    private lateinit var displayWord: TextView
    private lateinit var clearButton: Button
    private lateinit var submitButton: Button
    private lateinit var gameCommunication: GameCommunication
    private var totalScore = 0
    private val selectedButtonIds = mutableListOf<Int>()
    private val adjacentButtons = mapOf(
        R.id.button1 to listOf(R.id.button2, R.id.button5, R.id.button6),
        R.id.button2 to listOf(R.id.button1, R.id.button3, R.id.button5, R.id.button6, R.id.button7),
        R.id.button3 to listOf(R.id.button2, R.id.button4, R.id.button6, R.id.button7, R.id.button8),
        R.id.button4 to listOf(R.id.button3, R.id.button7, R.id.button8),
        R.id.button5 to listOf(R.id.button1, R.id.button2, R.id.button6, R.id.button9, R.id.button10),
        R.id.button6 to listOf(R.id.button1, R.id.button2, R.id.button3, R.id.button5, R.id.button7, R.id.button9, R.id.button10, R.id.button11),
        R.id.button7 to listOf(R.id.button2, R.id.button3, R.id.button4, R.id.button6, R.id.button8, R.id.button10, R.id.button11, R.id.button12),
        R.id.button8 to listOf(R.id.button3, R.id.button4, R.id.button7, R.id.button11, R.id.button12),
        R.id.button9 to listOf(R.id.button5, R.id.button6, R.id.button10, R.id.button13, R.id.button14),
        R.id.button10 to listOf(R.id.button5, R.id.button6, R.id.button7, R.id.button9, R.id.button11, R.id.button13, R.id.button14, R.id.button15),
        R.id.button11 to listOf(R.id.button6, R.id.button7, R.id.button8, R.id.button10, R.id.button12, R.id.button14, R.id.button15, R.id.button16),
        R.id.button12 to listOf(R.id.button7, R.id.button8, R.id.button11, R.id.button15, R.id.button16),
        R.id.button13 to listOf(R.id.button9, R.id.button10, R.id.button14),
        R.id.button14 to listOf(R.id.button9, R.id.button10, R.id.button11, R.id.button13, R.id.button15),
        R.id.button15 to listOf(R.id.button10, R.id.button11, R.id.button12, R.id.button14, R.id.button16),
        R.id.button16 to listOf(R.id.button11, R.id.button12, R.id.button15)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_game_board, container, false)
        gameCommunication = activity as GameCommunication
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGameBoard()
        displayWord = view.findViewById(R.id.display_word)
        clearButton = view.findViewById(R.id.clear_button)
        submitButton = view.findViewById(R.id.submit_button)

        clearButton.setOnClickListener {
            selectedLetters.clear()
            selectedButtonIds.clear()
            displayWord.text = ""
        }

        submitButton.setOnClickListener{
            val word = selectedLetters.toString()
            val vowels = "AEIOU"
            var vowelCount = 0
            var atleast2Vowels = false
            for (char in word) {
                if (char in vowels){
                    vowelCount++
                }
                if (vowelCount >= 2){
                    atleast2Vowels = true
                }
            }
            if (word.length < 4){
                val score = -10
                if (totalScore < 10){
                    totalScore = 0
                } else {
                    totalScore += score
                }
                gameCommunication.updateScore(totalScore)
                Toast.makeText(requireContext(), getString(R.string.less_than_four) + ", $score", Toast.LENGTH_SHORT).show()
            } else{
                if (!atleast2Vowels){
                    val score = -10
                    if (totalScore < 10){
                        totalScore = 0
                    } else {
                        totalScore += score
                    }
                    gameCommunication.updateScore(totalScore)
                    Toast.makeText(requireContext(), getString(R.string.two_vowels) + ", $score", Toast.LENGTH_SHORT).show()
                } else if (word in usedWords){
                    Toast.makeText(requireContext(), R.string.word_used, Toast.LENGTH_SHORT).show()
                } else {
                    if (isWordInDictionary(word)){
                        usedWords.add(word)
                        val score = calculateScore(word)
                        totalScore += score
                        gameCommunication.updateScore(totalScore)
                        Toast.makeText(requireContext(), getString(R.string.correct) + ", +$score", Toast.LENGTH_SHORT).show()
                    } else {
                        val score = -10
                        if (totalScore < 10){
                            totalScore = 0
                        } else {
                            totalScore += score
                        }
                        gameCommunication.updateScore(totalScore)
                        Toast.makeText(requireContext(), getString(R.string.incorrect) + ", $score", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            selectedLetters.clear()
            selectedButtonIds.clear()
            displayWord.text = ""
        }
    }

    private fun setupGameBoard() {
        val vowels = listOf('A', 'E', 'I', 'O', 'U')
        val alphabet = listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')
        val letters = mutableListOf<Char>()
        var vowelCount = 0

        while (letters.size < 16) {
            if (letters.size >= 14 && vowelCount < 2) {
                letters.add(vowels.random())
                vowelCount++
            } else {
                val letter = alphabet.random()
                letters.add(letter)
                if (letter in vowels) {
                    vowelCount++
                }
            }
        }

        for (i in 1..16) {
            val buttonId = resources.getIdentifier("button$i", "id", context?.packageName)
            val button = view?.findViewById<Button>(buttonId)
            button?.let {
                val buttonLetter = letters[i - 1].toString()
                it.text = buttonLetter
                it.setOnClickListener {
                    if (selectedButtonIds.contains(it.id)){
                        Toast.makeText(context, R.string.letter_used, Toast.LENGTH_SHORT).show()
                    }else if (selectedButtonIds.isEmpty() || selectedButtonIds.last() in adjacentButtons[it.id]!!) {
                        selectedLetters.append(buttonLetter)
                        displayWord.text = selectedLetters.toString()
                        selectedButtonIds.add(it.id)
                    } else {
                        Toast.makeText(context, R.string.adjacent, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun isWordInDictionary(word:String): Boolean {
        requireContext().assets.open("words.txt").bufferedReader().useLines { lines ->
            lines.forEach { line ->
                if (word.equals(line, ignoreCase = true)) {
                    return true
                }
            }
        }
        return false
    }

    private fun calculateScore(word: String): Int {
        var score = 0
        val vowels = "AEIOU"
        val consonants = "SZPXQ"
        var containsConstants = false
        for (char in word){
            if (char in vowels){
                score += 5
            } else if (char in consonants){
                score += 1
                containsConstants = true
            } else {
                score += 1
            }
        }

        if (containsConstants){
            score += score
        }
        return score
    }

     fun resetGame() {
        selectedLetters.clear()
        selectedButtonIds.clear()
        usedWords.clear()
        displayWord.text = ""
        totalScore = 0
        gameCommunication.updateScore(totalScore)
        setupGameBoard()
    }

}
