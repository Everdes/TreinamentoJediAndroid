package com.everdessoares.treinamentojediandroid.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.everdessoares.treinamentojediandroid.R;
import com.everdessoares.treinamentojediandroid.interfaces.OnItemClickListener;
import com.everdessoares.treinamentojediandroid.interfaces.OnItemLongClickListener;
import com.everdessoares.treinamentojediandroid.model.bean.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Wolfstein on 07/01/2016.
 */
public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    private Set<Integer> mItemsChecked;
    private ArrayList<Person> mList;
    private ArrayList<Person> mListDeleted;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public PersonAdapter(ArrayList<Person> persons, Context context) {
        mItemsChecked = new HashSet<>();
        mList = persons;
        mListDeleted = new ArrayList<>();
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.model_recycler_view_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Person person = mList.get(position);

        holder.tvName.setText(person.getName());
        holder.tvAge.setText(String.valueOf(person.getAge()));
        holder.tvKind.setText(person.getKind());

        if (isItemChecked(position))
            holder.mView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.dividerColor));
        else
            holder.mView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorTransparent));
    }

    public boolean isItemChecked(int position) {
        return mItemsChecked.contains(Integer.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public void delete() {
        for (int i = mList.size() - 1; i >= 0; i--) {
            if (isItemChecked(i))
                mListDeleted.add(mList.remove(i));
        }

//        notifyItemRemoved(position);

    }

    public void setItemChecked(int position) {
        if (isItemChecked(position))
            mItemsChecked.remove(position);
        else
            mItemsChecked.add(position);

        notifyDataSetChanged();
    }

    public void clearItemsChecked() {
        mItemsChecked.clear();
        notifyDataSetChanged();
    }

    public int totalItemChecked() {
        return mItemsChecked.size();
    }

    public void undoDelete() {
        for (Person person : mListDeleted) {
            mList.add(person);
        }

        this.sort();
        notifyDataSetChanged();
        mListDeleted.clear();
    }

    private void sort() {
        Collections.sort(mList, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.getName().compareTo(p2.getName());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        private TextView tvName;
        private TextView tvAge;
        private TextView tvKind;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            tvName = (TextView) mView.findViewById(R.id.tv_name);
            tvAge = (TextView) mView.findViewById(R.id.tv_age);
            tvKind = (TextView) mView.findViewById(R.id.tv_kind);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (mOnItemClickListener != null && position >= 0) {
                        mOnItemClickListener.OnItemClick(v, position);
                    }
                }
            });

            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();

                    if (mOnItemLongClickListener != null && position >= 0) {
                        mOnItemLongClickListener.OnItemLongClick(v, position);
                    }

                    return false;
                }
            });
        }
    }
}
