package com.harsh.visionx_signbridge.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.harsh.visionx_signbridge.FavoriteAdapter;
import com.harsh.visionx_signbridge.FavoriteModel;
import com.harsh.visionx_signbridge.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavoriteFragment extends Fragment {

    private RecyclerView rvFavorites;
    private LinearLayout layoutEmptyFavorite;

    private ArrayList<FavoriteModel> favoriteList;
    private FavoriteAdapter adapter;

    private static final String BASE_URL =
            "http://10.176.197.132/signbridgeAPI/";

    public FavoriteFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_favorite,
                container,
                false);

        rvFavorites = view.findViewById(
                R.id.rvFavorites);

        layoutEmptyFavorite = view.findViewById(
                R.id.layoutEmptyFavorite);

        favoriteList = new ArrayList<>();

        rvFavorites.setLayoutManager(
                new LinearLayoutManager(requireContext()));

        adapter = new FavoriteAdapter(
                favoriteList,
                this::removeFavorite
        );

        rvFavorites.setAdapter(adapter);

        loadFavorites();

        return view;
    }

    private void loadFavorites() {

        String url = BASE_URL + "get_favorites.php";

        StringRequest request =
                new StringRequest(
                        Request.Method.GET,
                        url,

                        response -> {

                            try {

                                JSONObject json =
                                        new JSONObject(response);

                                boolean success =
                                        json.getBoolean("success");

                                favoriteList.clear();

                                if (success) {

                                    JSONArray data =
                                            json.getJSONArray("data");

                                    for (int i = 0;
                                         i < data.length();
                                         i++) {

                                        JSONObject obj =
                                                data.getJSONObject(i);

                                        FavoriteModel model =
                                                new FavoriteModel(

                                                        obj.getInt("id"),

                                                        obj.getString(
                                                                "phrase"),

                                                        obj.getString(
                                                                "translation"),

                                                        obj.optString(
                                                                "image"),

                                                        obj.optString(
                                                                "gif"),

                                                        obj.optString(
                                                                "audio")
                                                );

                                        favoriteList.add(model);
                                    }
                                }

                                adapter.notifyDataSetChanged();

                                updateEmptyState();

                            } catch (Exception e) {

                                Toast.makeText(
                                        requireContext(),
                                        "Unable to load favorites",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        },

                        error -> {

                            Toast.makeText(
                                    requireContext(),
                                    "Unable to connect to SignBridge server",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                );

        Volley.newRequestQueue(requireContext())
                .add(request);
    }

    private void removeFavorite(
            FavoriteModel model) {

        String url =
                BASE_URL + "favorite_phrase.php";

        StringRequest request =
                new StringRequest(
                        Request.Method.POST,
                        url,

                        response -> {

                            try {

                                JSONObject json =
                                        new JSONObject(response);

                                if (json.getBoolean("success")) {

                                    favoriteList.remove(model);

                                    adapter.notifyDataSetChanged();

                                    updateEmptyState();

                                    Toast.makeText(
                                            requireContext(),
                                            "Removed from favorites",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }

                            } catch (Exception e) {

                                Toast.makeText(
                                        requireContext(),
                                        "Something went wrong",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        },

                        error -> Toast.makeText(
                                requireContext(),
                                "Server connection failed",
                                Toast.LENGTH_SHORT
                        )
                ) {

                    @Override
                    protected Map<String, String>
                    getParams() {

                        Map<String, String> params =
                                new HashMap<>();

                        params.put(
                                "id",
                                String.valueOf(model.getId())
                        );

                        params.put(
                                "action",
                                "remove"
                        );

                        return params;
                    }
                };

        Volley.newRequestQueue(requireContext())
                .add(request);
    }

    private void updateEmptyState() {

        if (favoriteList.isEmpty()) {

            layoutEmptyFavorite.setVisibility(
                    View.VISIBLE);

            rvFavorites.setVisibility(
                    View.GONE);

        } else {

            layoutEmptyFavorite.setVisibility(
                    View.GONE);

            rvFavorites.setVisibility(
                    View.VISIBLE);
        }
    }
}
