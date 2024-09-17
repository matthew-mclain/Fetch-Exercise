package com.example.fetch_exercise;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemAdapter = new ItemAdapter(null);
        recyclerView.setAdapter(itemAdapter);

        ItemViewModel itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        itemViewModel.getItemList().observe(this, items -> {
            itemAdapter = new ItemAdapter(items);
            recyclerView.setAdapter(itemAdapter);
        });
    }
}