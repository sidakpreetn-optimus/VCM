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

import java.util.List;

/**
 * Created by optimus158 on 07-Aug-15.
 * <p/>
 * Adapter class for providing Data to Contacts list on main Screen
 */
public class RecyclerViewContactsAdapter extends RecyclerView.Adapter<RecyclerViewContactsAdapter.ContactViewHolder> {

    private Context context;
    private List<ContactsModel> list;

    /**
     * Constructor for getting Context and Contacts list
     *
     * @param context
     * @param list
     */
    public RecyclerViewContactsAdapter(Context context, List<ContactsModel> list) {
        this.context = context;
        this.list = list;
    }

    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_cardview, parent, false);
        return new ContactViewHolder(v);
    }

    public void onBindViewHolder(ContactViewHolder holder, final int position) {
        // setting the name and image
        holder.textView1.setText(list.get(position).getFirstName() + " " + list.get(position).getLastName());
        holder.textView2.setText(list.get(position).getPhoneNo());
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
        // If any list item i.e. cardview clicked then its details opened in new Activity
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, ContactDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectedContact", list.get(position));
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
     * ViewHolder Class for Adapter
     */
    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textView1;
        TextView textView2;
        ImageView imageView;

        public ContactViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardViewContact);
            textView1 = (TextView) itemView.findViewById(R.id.textViewNameContact);
            textView2 = (TextView) itemView.findViewById(R.id.textViewPhoneNoContact);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewCardViewContact);

        }
    }
}
