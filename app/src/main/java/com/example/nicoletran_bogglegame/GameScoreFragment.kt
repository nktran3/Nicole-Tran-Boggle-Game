package com.example.nicoletran_bogglegame

import android.os.Bundle
import android.text.*
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.*

class GameScoreFragment : Fragment() {
    private lateinit var scoreTextView: TextView
    private lateinit var newGameButton: Button
    private lateinit var gameCommunication: GameCommunication
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game_score, container, false)
        gameCommunication = activity as GameCommunication
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scoreTextView = view.findViewById(R.id.score_number)
        newGameButton = view.findViewById(R.id.new_game_button)

        newGameButton.setOnClickListener{
            gameCommunication.resetGame()
        }

    }

    fun displayScore(score: Int) {
        val scoreNumber = score.toString()
        val spannableString = SpannableString(scoreNumber)
        spannableString.setSpan(UnderlineSpan(), 0, scoreNumber.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        scoreTextView.text = spannableString
    }


}