package com.example.simpletodo;

import org.apache.commons.io.FileUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button btnAdd;
    EditText adItems;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        adItems = findViewById(R.id.adItems);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        // To remove an item on a long click
        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int pos) {
                items.remove(pos);
                itemsAdapter.notifyItemRemoved(items.size() - 1);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        // To edit an item on a click
        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int pos) {
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra(KEY_ITEM_TEXT, items.get(pos));
                i.putExtra(KEY_ITEM_POSITION, pos);
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        // To bind data with the adapter
        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // To add an item when clicking on the ADD button
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = adItems.getText().toString();
                items.add(todoItem);
                itemsAdapter.notifyItemInserted(items.size() - 1);
                adItems.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == EDIT_TEXT_CODE && resultCode == RESULT_OK) {
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            int pos = data.getExtras().getInt(KEY_ITEM_POSITION);
            items.set(pos, itemText);
            itemsAdapter.notifyItemChanged(pos);
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.w("MainActivity", "Unknown call to EditActivity");
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // To load items by reading them from the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("Main Activity", "Error readding items", e);
            items = new ArrayList<>();
        }
    }

    // To save items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("Main Activity", "Error writing items", e);
        }
    }
}