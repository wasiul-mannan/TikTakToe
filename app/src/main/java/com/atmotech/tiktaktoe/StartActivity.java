package com.atmotech.tiktaktoe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView twoPlayerButton = findViewById(R.id.twoPlayerButton);
        TextView computerButton = findViewById(R.id.computerButton);

        twoPlayerButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            intent.putExtra("mode", "twoPlayer");
            startActivity(intent);
        });

        computerButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            intent.putExtra("mode", "computer");
            startActivity(intent);
        });
    }
}