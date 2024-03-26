package com.example.nicoletran_bogglegame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.*

class GameScoreFragment : Fragment() {
    private lateinit var scoreTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_score, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scoreTextView = view.findViewById(R.id.score_number)
    }

    fun displayScore(score: Int) {
        scoreTextView.text = score.toString()
    }


}