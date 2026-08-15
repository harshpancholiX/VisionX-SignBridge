package com.harsh.visionx_signbridge.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harsh.visionx_signbridge.Model.LanguageModel;
import com.harsh.visionx_signbridge.R;

import java.util.ArrayList;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    private final Context context;
    private final ArrayList<LanguageModel> languageList;

    private final SharedPreferences preferences;

    public LanguageAdapter(Context context, ArrayList<LanguageModel> languageList) {

        this.context = context;
        this.languageList = languageList;

        preferences = context.getSharedPreferences(
                "SignBridgePreferences",
                Context.MODE_PRIVATE
        );
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_language, parent, false);

        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull LanguageViewHolder holder,
            int position
    ) {

        LanguageModel language = languageList.get(position);

        holder.tvLanguageName.setText(language.getName());
        holder.tvNativeName.setText(language.getNativeName());
        holder.tvLanguageCode.setText(language.getCode());
        holder.tvDescription.setText(language.getDescription());

        holder.ivLanguage.setImageResource(language.getIcon());

        updateSelection(holder, language);

        holder.itemView.setOnClickListener(v -> {

            int clickedPosition = holder.getBindingAdapterPosition();

            if (clickedPosition == RecyclerView.NO_POSITION) {
                return;
            }

            selectLanguage(clickedPosition);
        });

        // Premium press animation
        holder.itemView.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {

                case android.view.MotionEvent.ACTION_DOWN:

                    v.animate()
                            .scaleX(0.97f)
                            .scaleY(0.97f)
                            .setDuration(100)
                            .setInterpolator(new DecelerateInterpolator())
                            .start();

                    break;

                case android.view.MotionEvent.ACTION_UP:
                    v.performClick();
                case android.view.MotionEvent.ACTION_CANCEL:

                    v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(150)
                            .setInterpolator(new DecelerateInterpolator())
                            .start();

                    break;
            }

            return false;
        });
    }

    private void selectLanguage(int position) {

        // Remove previous selection
        for (LanguageModel language : languageList) {
            language.setSelected(false);
        }

        // Select new language
        LanguageModel selectedLanguage = languageList.get(position);
        selectedLanguage.setSelected(true);

        // Save selected language
        preferences.edit()
                .putInt("selected_language_id", selectedLanguage.getId())
                .putString("selected_language_name", selectedLanguage.getName())
                .putString("selected_language_code", selectedLanguage.getCode())
                .apply();

        // Open Wikipedia page for Sign Language
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(selectedLanguage.getWikiUrl())
        );
        context.startActivity(intent);

        notifyDataSetChanged();
    }

    private void updateSelection(
            LanguageViewHolder holder,
            LanguageModel language
    ) {

        if (language.isSelected()) {

            holder.cardLanguage.setBackgroundResource(
                    R.drawable.language_selected
            );

            holder.ivSelected.setVisibility(View.VISIBLE);

            holder.ivSelected.setScaleX(0f);
            holder.ivSelected.setScaleY(0f);

            holder.ivSelected.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(250)
                    .start();

        } else {

            holder.cardLanguage.setBackgroundResource(
                    R.drawable.language_normal
            );

            holder.ivSelected.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    public static class LanguageViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvLanguageName;
        TextView tvNativeName;
        TextView tvLanguageCode;
        TextView tvDescription;

        ImageView ivLanguage;
        ImageView ivSelected;

        View cardLanguage;

        public LanguageViewHolder(@NonNull View itemView) {

            super(itemView);

            cardLanguage = itemView.findViewById(R.id.cardLanguage);

            tvLanguageName =
                    itemView.findViewById(R.id.tvLanguageName);

            tvNativeName =
                    itemView.findViewById(R.id.tvNativeName);

            tvLanguageCode =
                    itemView.findViewById(R.id.tvLanguageCode);

            tvDescription =
                    itemView.findViewById(R.id.tvDescription);

            ivLanguage =
                    itemView.findViewById(R.id.ivLanguage);

            ivSelected =
                    itemView.findViewById(R.id.ivSelected);
        }
    }
}