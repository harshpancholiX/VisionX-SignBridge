package com.harsh.visionx_signbridge;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.List;
import java.util.Locale;

public class SignAdapter extends RecyclerView.Adapter<SignAdapter.SignViewHolder> {

    private Context context;
    private List<SignModel> signList;
    private TextToSpeech textToSpeech;

    public SignAdapter(Context context, List<SignModel> signList) {
        this.context = context;
        this.signList = signList;


        textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.ENGLISH);
                textToSpeech.setSpeechRate(0.9f);
            }
        });
    }

    @NonNull
    @Override
    public SignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sign, parent, false);
        return new SignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SignViewHolder holder, int position) {
        SignModel sign = signList.get(position);

        int resId = sign.getImageResource();
        String resourceTypeName = context.getResources().getResourceTypeName(resId);

        if ("raw".equals(resourceTypeName)) {
            // It's a Lottie animation
            holder.ivSignImage.setVisibility(View.GONE);
            holder.lottieSign.setVisibility(View.VISIBLE);
            holder.lottieSign.setAnimation(resId);
            holder.lottieSign.playAnimation();
        } else {
            // It's a normal image
            holder.lottieSign.setVisibility(View.GONE);
            holder.ivSignImage.setVisibility(View.VISIBLE);
            holder.ivSignImage.setImageResource(resId);
        }

        holder.tvSignName.setText(sign.getSignName());
        holder.tvSignCategory.setText(sign.getHindiName());


        holder.ivPlaySign.setOnClickListener(v -> speak(sign.getSignName()));


        holder.itemView.setOnClickListener(v -> speak(sign.getSignName()));
    }

    private void speak(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void updateList(List<SignModel> newList) {
        this.signList = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return signList.size();
    }

    public void releaseTTS() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    static class SignViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSignImage;
        LottieAnimationView lottieSign;
        TextView tvSignName;
        TextView tvSignCategory;
        ImageView ivPlaySign;

        public SignViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSignImage = itemView.findViewById(R.id.ivSignImage);
            lottieSign = itemView.findViewById(R.id.lottieSign);
            tvSignName = itemView.findViewById(R.id.tvSignName);
            tvSignCategory = itemView.findViewById(R.id.tvSignCategory);
            ivPlaySign = itemView.findViewById(R.id.ivPlaySign);
        }
    }
}
