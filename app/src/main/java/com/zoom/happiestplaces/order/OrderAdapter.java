package com.zoom.happiestplaces.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


import com.zoom.happiestplaces.databinding.ListItemOrderBinding;
import com.zoom.happiestplaces.model.MenuItem;
import com.zoom.happiestplaces.model.OrderMenuItem;
import com.zoom.happiestplaces.util.AppConstants;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    ListItemOrderBinding mBinding;
    Context mContext;
    private List<OrderMenuItem> mOrderList;
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
        OrderMenuItem OrderItem =mOrderList.get(position);
        holder.bind(OrderItem);
    }

    @Override
    public int getItemCount() {
        if (mOrderList == null) {
            return 0;
        }
        return mOrderList.size();
    }
    public void setList(List<OrderMenuItem> OrderList) {
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
        public void bind(OrderMenuItem menuItem) {
            if(menuItem.getName()!=null)
                mBinding.itemTitle.setText(menuItem.getName());
            if(menuItem.getQty()!=0)
                mBinding.itemQty.setText(" X  "+Integer.toString(menuItem.getQty()));
            Double total=menuItem.getQty()*menuItem.getPrice();
            mBinding.totalItemValue.setText(AppConstants.CURRENCY+" "+Double.toString(total));
            }

            @Override
        public void onClick(View view) {
        }
    }
}



