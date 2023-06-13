package com.example.myc;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Board> arrayList;
    private Context context;

    public CustomAdapter(ArrayList<Board> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    // 뷰홀더 객체를 생성함
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    // 뷰홀더에 데이터를 넣는 작업 수행
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tv_title.setText(arrayList.get(position).getTitle());
        holder.tv_context.setText(arrayList.get(position).getContext());
        holder.tv_userName.setText(arrayList.get(position).getUserName());

        holder.linear_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent boardShow = new Intent(context, BoardShow.class);

                ((MainActivity)context).startActivity(boardShow);
            }
        });
    }

    @Override
    // 데이터의 개수를 반환해줌
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_context;
        TextView tv_userName;
        LinearLayout linear_holder;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.linear_holder = itemView.findViewById(R.id.linear_holder);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.tv_context = itemView.findViewById(R.id.tv_context);
            this.tv_userName = itemView.findViewById(R.id.tv_userName);
        }
    }

}