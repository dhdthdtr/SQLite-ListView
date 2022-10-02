package com.example.firstquestion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseManager dbManager;

    Button btnAdd;
    EditText editText;
    ListView listView;

    ArrayList<String> listItem;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DatabaseManager(this);

        listItem = new ArrayList<>();

        btnAdd = findViewById(R.id.btnAdd);
        editText = findViewById(R.id.editText);
        listView = findViewById(R.id.listView);

        getData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userItem = editText.getText().toString();
                Word item = new Word(userItem);
                if(!userItem.equals("")){
                    dbManager.addWord(item);
                    editText.setText("");
                    listItem.clear();
                    getData();
                } else {
                    Toast.makeText(MainActivity.this, "please fill in the blank", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = listView.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, item, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getData(){
        Cursor cursor = dbManager.getWordCursor();
        while(cursor.moveToNext()){
            listItem.add(cursor.getString(1));
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItem);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> wordList = new ArrayList<>();

                for(String item : listItem){
                    if(item.toLowerCase().contains(newText.toLowerCase())){
                        wordList.add(item);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, wordList);
                listView.setAdapter(adapter);
                return true;
            }
        });
        return true;
    }


}