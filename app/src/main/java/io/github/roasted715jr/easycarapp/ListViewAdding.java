package io.github.roasted715jr.easycarapp;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdding extends AppCompatActivity {

    ArrayList<String> itemsList = new ArrayList<String>();
//    ArrayAdapter<String> adapter = null; //Handles the data of the ListView

    Button addBtn;
    CustomAdapter adapter;
    int clickCounter = 0;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_adding);

        addBtn = (Button) findViewById(R.id.btn_add);
        adapter = new CustomAdapter(getApplicationContext(), R.layout.list_item, R.id.list, itemsList);
        listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItems(v);
            }
        });
    }

    //Create a method which will handle dynamic insertion
    public void addItems(View v) {
        itemsList.add("Clicked: " + clickCounter++);
        adapter.notifyDataSetChanged();
    }
}
