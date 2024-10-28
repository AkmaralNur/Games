package com.example.games;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean player1Turn = true;
    private int[] gameState = new int[9];
    private int[][] winningPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };
    private int player1Score = 0;
    private int player2Score = 0;

    private TextView playerTurnText;
    private TextView player1ScoreText;
    private TextView player2ScoreText;
    private Button[] buttons = new Button[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerTurnText = findViewById(R.id.playerTurn);
        player1ScoreText = findViewById(R.id.player1Score);
        player2ScoreText = findViewById(R.id.player2Score);

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        // Инициализация кнопок игрового поля
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            final int index = i;
            buttons[i] = (Button) gridLayout.getChildAt(i);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gameState[index] == 0) {
                        gameState[index] = player1Turn ? 1 : 2;
                        buttons[index].setText(player1Turn ? "X" : "O");
                        buttons[index].setTextColor(Color.BLACK); // Установка цвета текста на черный
                        buttons[index].setEnabled(false); // Отключаем кнопку после нажатия

                        if (checkWin()) {
                            if (player1Turn) {
                                player1Score++;
                                player1ScoreText.setText("Игрок 1: " + player1Score);
                                playerTurnText.setText("Игрок 1 победил!");
                            } else {
                                player2Score++;
                                player2ScoreText.setText("Игрок 2: " + player2Score);
                                playerTurnText.setText("Игрок 2 победил!");
                            }
                        } else if (isDraw()) {
                            playerTurnText.setText("Ничья!");
                        } else {
                            player1Turn = !player1Turn;
                            playerTurnText.setText(player1Turn ? "Ход игрока 1" : "Ход игрока 2");
                        }
                    }
                }
            });
        }

        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    private boolean checkWin() {
        for (int[] position : winningPositions) {
            if (gameState[position[0]] != 0 &&
                    gameState[position[0]] == gameState[position[1]] &&
                    gameState[position[1]] == gameState[position[2]]) {
                highlightWinningButtons(position);
                disableAllButtons(); // Disable all buttons after a win
                return true;
            }
        }
        return false;
    }

    private void highlightWinningButtons(int[] winningPosition) {
        int winningColor = player1Turn ? Color.GREEN : Color.BLUE;
        for (int index : winningPosition) {
            buttons[index].setBackgroundColor(winningColor);
        }
    }

    private void disableAllButtons() {
        for (Button button : buttons) {
            button.setEnabled(false);
        }
    }

    private boolean isDraw() {
        for (int state : gameState) {
            if (state == 0) return false; // Если есть хотя бы одна пустая ячейка, возвращаем false
        }
        return true; // Все ячейки заполнены
    }

    private void resetGame() {
        gameState = new int[9];
        player1Turn = true;
        playerTurnText.setText("Ход игрока 1");

        for (Button button : buttons) {
            button.setText("");
            button.setEnabled(true);
            button.setBackgroundColor(Color.LTGRAY); // Or any default color
        }
    }
}
