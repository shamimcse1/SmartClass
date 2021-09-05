package com.codercamp.smartclass.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codercamp.smartclass.model.Model;

import java.util.List;

import codercamp.smartclass.R;

public class BookShowAdapter extends RecyclerView.Adapter<BookShowAdapter.MyViewHolder>{

    private List<Model> list;
    private Context context;

    public BookShowAdapter(List<Model> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_book_time,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Model model = list.get(position);

        if (model!= null){
            holder.roomNo.setText("Room No : "+model.getRoomNo());
            holder.time.setText("Time " +model.getTime());
            holder.bookedBy.setText("Booked By : "+model.getBatchNo());
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView roomNo,time,bookedBy;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            roomNo = itemView.findViewById(R.id.RoomNo);
            time = itemView.findViewById(R.id.Time);
            bookedBy = itemView.findViewById(R.id.BookedBy);
        }
    }
}
