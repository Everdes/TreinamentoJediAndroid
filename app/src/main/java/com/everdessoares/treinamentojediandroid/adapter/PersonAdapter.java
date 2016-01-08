package com.everdessoares.treinamentojediandroid.adapter;

import android.content.Context;
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

/**
 * Created by Wolfstein on 07/01/2016.
 */
public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    ArrayList<Person> mList;
    LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public PersonAdapter(ArrayList<Person> persons, Context context) {
        mList = persons;
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

    public void delete(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;

        private TextView tvName;
        private TextView tvAge;
        private TextView tvKind;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;

            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvAge = (TextView) view.findViewById(R.id.tv_age);
            tvKind = (TextView) view.findViewById(R.id.tv_kind);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (mOnItemClickListener != null && position >= 0) {
                        mOnItemClickListener.OnItemClick(v, position);
                    }
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
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
