package com.example.nicoletran_bogglegame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.*


class GameBoardFragment : Fragment() {
    private val selectedLetters = StringBuilder()
    private lateinit var displayWord: TextView
    private lateinit var clearButton: Button
    private lateinit var submitButton: Button
    private lateinit var gameCommunication: GameCommunication
    private var totalScore = 0

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
                Toast.makeText(requireContext(), R.string.less_than_four, Toast.LENGTH_SHORT).show()
            } else{
                if (!atleast2Vowels){
                    Toast.makeText(requireContext(), R.string.two_vowels, Toast.LENGTH_SHORT).show()
                } else {
                    if (isWordInDictionary(word)){
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
        }
    }

    private fun setupGameBoard() {
        val letters = listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')
        val random = java.util.Random()
        for (i in 1..16) {
            val buttonId = resources.getIdentifier("button$i", "id", context?.packageName)
            val button = view?.findViewById<Button>(buttonId)
            button?.let {
                val letter = letters[random.nextInt(letters.size)].toString()
                it.text = letter
                it.setOnClickListener {
                    selectedLetters.append(letter)
                    displayWord.text = selectedLetters.toString()
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
        displayWord.text = ""
        totalScore = 0
        gameCommunication?.updateScore(totalScore)  // Reset and update the score display
        setupGameBoard()
    }

}
