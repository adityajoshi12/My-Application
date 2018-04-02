package chat.labs.test.chat.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    List<Model> modelList;
    private Context context;

    public CustomAdapter(Context context, List<Model> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Model model = modelList.get(position);
        holder.name.setText(model.get_name());
        holder.rating.setText(model.getRating());
        holder.location.setText(model.getLocation());
        Glide.with(context).load(model.getImageurl()).into(holder.imageView);
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {


                Toast.makeText(context, modelList.get(position).get_name(), Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView rating, name, location;
        public ImageView imageView;
        private ItemClickListener clickListener;

        public ViewHolder(final View itemView) {
            super(itemView);
            rating = itemView.findViewById(R.id.rating);
            name = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.address);
            imageView = itemView.findViewById(R.id.imageload);
            itemView.setOnClickListener(this);

        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }
}
