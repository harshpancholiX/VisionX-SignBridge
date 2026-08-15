package com.harsh.visionx_signbridge;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FavoriteAdapter
        extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private ArrayList<FavoriteModel> list;
    private OnFavoriteRemoveListener listener;

    public interface OnFavoriteRemoveListener {
        void onRemove(FavoriteModel model);
    }

    public FavoriteAdapter(
            ArrayList<FavoriteModel> list,
            OnFavoriteRemoveListener listener) {

        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favourite, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        FavoriteModel model = list.get(position);

        holder.tvPhrase.setText(model.getPhrase());
        holder.tvTranslation.setText(model.getTranslation());

        if (model.getImage() != null &&
                !model.getImage().isEmpty()) {

            Glide.with(holder.itemView.getContext())
                    .load(model.getImage())
                    .placeholder(R.drawable.signlanguage)
                    .into(holder.ivSign);

        } else if (model.getGif() != null &&
                !model.getGif().isEmpty()) {

            Glide.with(holder.itemView.getContext())
                    .asGif()
                    .load(model.getGif())
                    .into(holder.ivSign);
        } else {
            holder.ivSign.setImageResource(R.drawable.signlanguage);
        }

        holder.btnRemove.setOnClickListener(v ->
                listener.onRemove(model)
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvPhrase;
        TextView tvTranslation;
        ImageView ivSign;
        ImageButton btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPhrase = itemView.findViewById(
                    R.id.tvFavoritePhrase);

            tvTranslation = itemView.findViewById(
                    R.id.tvFavoriteTranslation);

            ivSign = itemView.findViewById(
                    R.id.ivFavoriteSign);

            btnRemove = itemView.findViewById(
                    R.id.btnRemoveFavorite);
        }
    }
}
