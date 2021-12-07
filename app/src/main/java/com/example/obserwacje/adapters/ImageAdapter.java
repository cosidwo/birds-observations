package com.example.obserwacje.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.obserwacje.R;
import com.example.obserwacje.entities.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

//class used to display images RecyclerView
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private final Context context;
    private final List<Image> images;

    //constructor
    public ImageAdapter(Context context, List<Image> images){
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    //loading image and information from List object
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Image image = images.get(position);
        String species = image.getSpecies();
        String name = image.getName();
        String place = image.getPlace();
        String county = image.getCounty();
        String date = image.getDate();
        holder.textView.setText(species+", obs. "+name+", "+place+" (gm. "+county+"). "+date);
        Picasso.get().load(image.getImageURI()).fit().centerCrop().into(holder.imageView);
    }

    //getting number of items
    @Override
    public int getItemCount() {
        return images.size();
    }


    //nested class
    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public ImageView imageView;

        public ImageViewHolder(View view){
            super(view);
            textView = view.findViewById(R.id.galleryTextView);
            imageView = view.findViewById(R.id.imageView);
        }
    }
}
