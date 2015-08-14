package com.example.optimus158.virtualcontactmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by optimus158 on 11-Aug-15.
 * <p/>
 * Adapter class for providing Data to Groups list in Drawer of main Screen
 */
public class RecyclerViewGroupsAdapter extends RecyclerView.Adapter<RecyclerViewGroupsAdapter.GroupViewHolder> {
    private Context context;
    private List<GroupsModel> list;

    /**
     * Constructor for getting Context and Groups list
     *
     * @param context
     * @param list
     */
    public RecyclerViewGroupsAdapter(Context context, List<GroupsModel> list) {
        this.context = context;
        this.list = list;
    }

    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_cardview, parent, false);
        return new GroupViewHolder(v);
    }

    public void onBindViewHolder(GroupViewHolder holder, final int position) {
        // setting the name and image
        holder.textView1.setText(list.get(position).getGroupName() + "\n" + list.get(position).getGroupCount());
        if (list.get(position).getPicturePath().equals("")) {
            Bitmap bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory
                    .decodeResource(context.getResources(),
                            R.drawable.default_image), 100, 100);
            holder.imageView.setImageBitmap(bitmap);
        } else {
            Bitmap bitmap = ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(list.get(position).getPicturePath()),
                    100, 100);
            holder.imageView.setImageBitmap(bitmap);
        }
        //If any list item i.e. cardview clicked then its details opened in new Activity
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectedGroup", list.get(position));
                intent.putExtras(bundle);
                intent.putExtra("option", "browse");
                context.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return list.size();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * ViewHolder Class for adapter
     */
    public static class GroupViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textView1;
        ImageView imageView;

        public GroupViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardViewContact);
            textView1 = (TextView) itemView.findViewById(R.id.textViewNameContact);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewCardViewContact);

        }
    }
}
