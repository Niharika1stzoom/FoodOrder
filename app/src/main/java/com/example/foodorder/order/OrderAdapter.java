package com.example.foodorder.order;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.foodorder.databinding.ListItemOrderBinding;
import com.example.foodorder.model.MenuItem;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    ListItemOrderBinding mBinding;
    Context mContext;
    private List<MenuItem> mOrderList;
    public OrderAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding=ListItemOrderBinding.inflate(LayoutInflater.from(mContext),parent,false);
        return new OrderViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        MenuItem OrderItem =mOrderList.get(position);
        holder.bind(OrderItem);
    }

    @Override
    public int getItemCount() {
        if (mOrderList == null) {
            return 0;
        }
        return mOrderList.size();
    }
    public void setList(List<MenuItem> OrderList) {
        mOrderList = OrderList;
        notifyDataSetChanged();
    }



    public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ListItemOrderBinding mBinding;
        public OrderViewHolder(@NonNull ListItemOrderBinding binding) {
            super(binding.getRoot());
            mBinding=binding;
            binding.getRoot().setOnClickListener(this);


        }
        public void bind(MenuItem Order) {
            if(Order.getName()!=null)
                mBinding.itemTitle.setText(Order.getName());
            if(Order.getQty()!=0)
                mBinding.itemQty.setText(" X  "+Integer.toString(Order.getQty()));
            Double total=Order.getQty()*Order.getPrice();
            mBinding.totalItemValue.setText("Rs "+Double.toString(total));
            //mBinding.qtyLabel.setText(Integer.toString(cartItem.getQty()));
            }

            @Override
        public void onClick(View view) {

        }
    }
}



