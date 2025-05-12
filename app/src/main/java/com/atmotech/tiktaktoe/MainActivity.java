package com.atmotech.tiktaktoe;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView[] cells;
    private TextView status;
    private TextView restartButton;
    private String currentPlayer = "X";
    private String gameMode;
    private boolean gameActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        status = findViewById(R.id.status);
        restartButton = findViewById(R.id.restartButton);
        cells = new TextView[9];
        for (int i = 0; i < 9; i++) {
            cells[i] = findViewById(getResources().getIdentifier("cell" + i, "id", getPackageName()));
            cells[i].setOnClickListener(this::onCellClick);
        }

        gameMode = getIntent().getStringExtra("mode");
        if (gameMode == null) {
            gameMode = "player";
        }
        restartButton.setOnClickListener(v -> resetGame());
        updateStatus();
    }

    private void onCellClick(View v) {
        if (!gameActive) return;
        TextView clickedCell = (TextView) v;
        if (clickedCell.getText().toString().isEmpty()) {
            clickedCell.setText(currentPlayer);
            if (checkWin()) {
                status.setText("Player " + currentPlayer + " Wins!");
                gameActive = false;
                Toast.makeText(this, "Player " + currentPlayer + " Wins!", Toast.LENGTH_LONG).show();
            } else if (isBoardFull()) {
                status.setText("It's a Draw!");
                gameActive = false;
                Toast.makeText(this, "It's a Draw!", Toast.LENGTH_LONG).show();
            } else {
                currentPlayer = currentPlayer.equals("X") ? "O" : "X";
                updateStatus();
                if (gameMode.equals("computer") && currentPlayer.equals("O")) {
                    computerMove();
                }
            }
        }
    }

    private void computerMove() {
        int move = -1;

        // Check for winning move
        for (int i = 0; i < 9; i++) {
            if (cells[i].getText().toString().isEmpty()) {
                cells[i].setText("O");
                if (checkWin()) {
                    move = i;
                    break;
                } else {
                    cells[i].setText("");
                }
            }
        }

        if (move == -1) {
            // Check for blocking move
            for (int i = 0; i < 9; i++) {
                if (cells[i].getText().toString().isEmpty()) {
                    cells[i].setText("X");
                    if (checkWin()) {
                        move = i;
                        cells[i].setText(""); 
                        break;
                    } else {
                        cells[i].setText("");
                    }
                }
            }
        }

        if (move == -1) {
            // Choose strategic move: center, corners, sides
            int[] preferred = {4, 0, 2, 6, 8, 1, 3, 5, 7};
            for (int pos : preferred) {
                if (cells[pos].getText().toString().isEmpty()) {
                    move = pos;
                    break;
                }
            }
        }

        // Place "O" at the chosen move
        cells[move].setText("O");

        if (checkWin()) {
            status.setText("Computer Wins!");
            gameActive = false;
            Toast.makeText(this, "Computer Wins!", Toast.LENGTH_LONG).show();
        } else if (isBoardFull()) {
            status.setText("It's a Draw!");
            gameActive = false;
            Toast.makeText(this, "It's a Draw!", Toast.LENGTH_LONG).show();
        } else {
            currentPlayer = "X";
            updateStatus();
        }
    }

    private boolean checkWin() {
        int[][] winPositions = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
                {0, 4, 8}, {2, 4, 6}             // Diagonals
        };
        for (int[] pos : winPositions) {
            if (!cells[pos[0]].getText().toString().isEmpty() &&
                    cells[pos[0]].getText().equals(cells[pos[1]].getText()) &&
                    cells[pos[1]].getText().equals(cells[pos[2]].getText())) {
                return true;
            }
        }
        return false;
    }

    private boolean isBoardFull() {
        for (TextView cell : cells) {
            if (cell.getText().toString().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void resetGame() {
        for (TextView cell : cells) {
            cell.setText("");
        }
        currentPlayer = "X";
        gameActive = true;
        updateStatus();
    }

    private void updateStatus() {
        if (gameMode.equals("computer") && currentPlayer.equals("O")) {
            status.setText("Computer's Turn");
        } else {
            status.setText("Player " + currentPlayer + "'s Turn");
        }
    }
}