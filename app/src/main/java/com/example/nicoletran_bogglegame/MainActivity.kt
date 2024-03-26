package com.example.nicoletran_bogglegame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), GameCommunication {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gameBoardFragment = GameBoardFragment()
        supportFragmentManager.beginTransaction().replace(R.id.gameFragment, gameBoardFragment).commit()
    }

    override fun updateScore(score: Int) {
        val scoreFragment = supportFragmentManager.findFragmentById(R.id.scoreFragment) as? GameScoreFragment
        scoreFragment?.displayScore(score)
    }
}