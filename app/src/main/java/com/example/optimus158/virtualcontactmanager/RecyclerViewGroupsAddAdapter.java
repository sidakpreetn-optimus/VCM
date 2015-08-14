package com.example.optimus158.virtualcontactmanager;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by optimus158 on 11-Aug-15.
 * <p/>
 * Adapeter Class for Recycler view in Adding a new Group
 */
public class RecyclerViewGroupsAddAdapter extends RecyclerView.Adapter<RecyclerViewGroupsAddAdapter.GroupsAddViewHolder> {

    private Context context;
    private List<ContactsModel> list;

    /**
     * Constructor for getting the context and the list to populate the recycler view
     *
     * @param context
     * @param list
     */
    public RecyclerViewGroupsAddAdapter(Context context, List<ContactsModel> list) {
        this.context = context;
        this.list = list;
    }

    public GroupsAddViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_cardview_groupadd_list, parent, false);
        return new GroupsAddViewHolder(v);
    }

    public void onBindViewHolder(GroupsAddViewHolder holder, final int position) {
        // Setting the name and phone no to cardview views
        holder.textView1.setText(list.get(position).getFirstName() + " " + list.get(position).getLastName());
        holder.textView2.setText(list.get(position).getPhoneNo());
        holder.checkBox.setChecked(list.get(position).isSelected());
        holder.checkBox.setTag(list.get(position));
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                ContactsModel contact = (ContactsModel) cb.getTag();
                contact.setIsSelected(cb.isChecked());
                list.get(position).setIsSelected(cb.isChecked());
            }
        });
    }

    /**
     * Method for getting the checked contacts
     *
     * @return
     */
    public List<ContactsModel> getContactsList() {
        return list;
    }

    public int getItemCount() {
        return list.size();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * ViewHolder class for adapter
     */
    public static class GroupsAddViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textView1;
        TextView textView2;
        CheckBox checkBox;

        public GroupsAddViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardViewAddGroup);
            textView1 = (TextView) itemView.findViewById(R.id.textViewNameAddGroup);
            textView2 = (TextView) itemView.findViewById(R.id.textViewPhoneNoAddGroup);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBoxAddGroup);

        }
    }
}
