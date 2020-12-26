package com.example.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    Button btnSave;
    EditText edItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        btnSave = findViewById(R.id.btnSave);
        edItems = findViewById(R.id.edItems);

        getSupportActionBar().setTitle("Edit item");

        edItems.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra(MainActivity.KEY_ITEM_TEXT, edItems.getText().toString());
                i.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}