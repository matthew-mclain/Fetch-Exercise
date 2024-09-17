package com.example.fetch_exercise;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemViewModel extends AndroidViewModel {
    private static final String TAG = "ItemViewModel";

    private final MutableLiveData<List<Item>> itemList = new MutableLiveData<>();

    public ItemViewModel(Application application) {
        super(application);
        fetchItems();
    }

    public LiveData<List<Item>> getItemList() {
        return itemList;
    }

    private void fetchItems() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Item>> call = apiService.getItems();

        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Item> items = response.body();

                    // Filter out items without name
                    List<Item> filteredItems = new ArrayList<>();
                    for (Item item : items) {
                        if (item.getName() != null && !item.getName().isEmpty()) {
                            filteredItems.add(item);
                        }
                    }

                    // Sort items by listId, then by name
                    filteredItems.sort(Comparator.comparingInt(Item::getListId).thenComparing(Item::getName));

                    itemList.setValue(filteredItems);
                } else {
                    itemList.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.e(TAG, "Error fetching data", t);
                itemList.setValue(new ArrayList<>());
            }
        });
    }
}
