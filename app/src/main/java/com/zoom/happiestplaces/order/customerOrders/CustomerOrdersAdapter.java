package com.zoom.happiestplaces.order.customerOrders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zoom.happiestplaces.databinding.ListItemCustomerOrdersBinding;
import com.zoom.happiestplaces.databinding.ListItemOrderBinding;
import com.zoom.happiestplaces.model.OrderMenuItem;
import com.zoom.happiestplaces.model.response.OrderResponse;
import com.zoom.happiestplaces.util.AppConstants;
import com.zoom.happiestplaces.util.DateUtil;

import java.util.List;

public class CustomerOrdersAdapter extends RecyclerView.Adapter<CustomerOrdersAdapter.CustomerOrdersViewHolder> {
    ListItemCustomerOrdersBinding mBinding;
    Context mContext;
    private List<OrderResponse> mOrderList;
    public CustomerOrdersAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public CustomerOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding=ListItemCustomerOrdersBinding.inflate(LayoutInflater.from(mContext),parent,false);
        return new CustomerOrdersViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerOrdersViewHolder holder, int position) {
        OrderResponse OrderItem =mOrderList.get(position);
        holder.bind(OrderItem);
    }

    @Override
    public int getItemCount() {
        if (mOrderList == null) {
            return 0;
        }
        return mOrderList.size();
    }
    public void setList(List<OrderResponse> OrderList) {
        mOrderList = OrderList;
        notifyDataSetChanged();
    }

    public class CustomerOrdersViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private ListItemCustomerOrdersBinding mBinding;
        public CustomerOrdersViewHolder(@NonNull ListItemCustomerOrdersBinding binding) {
            super(binding.getRoot());
            mBinding=binding;
            binding.getRoot().setOnClickListener(this);
        }
        public void bind(OrderResponse orderItem) {
            if(orderItem.getTable()!=null)
                mBinding.tableValue.setText(orderItem.getTable().getNumber());
            if(orderItem.getTotal_price()!=null)
                mBinding.totalPrice.setText(AppConstants.CURRENCY+" "+orderItem.getTotal_price().toString());
            if(orderItem.getStatus()!=null)
                mBinding.status.setText(orderItem.getStatus());
            if(orderItem.getTime()!=null)
                mBinding.time.setText(DateUtil.getReviewOrderDateFormat(orderItem.getTime()));
            mBinding.more.setVisibility(View.INVISIBLE);
                if(orderItem.getOrderDishResponses().size()>0)
                {
                    mBinding.item1.setText(orderItem.getOrderDishResponses().get(0).getMenuItem().getName());
                }
            if(orderItem.getOrderDishResponses().size()>1)
            {
                mBinding.item2.setText(orderItem.getOrderDishResponses().get(1).getMenuItem().getName());
                mBinding.item2.setVisibility(View.VISIBLE);
            }
            if(orderItem.getOrderDishResponses().size()>2)
            {
                mBinding.item3.setText(orderItem.getOrderDishResponses().get(2).getMenuItem().getName());
                mBinding.item3.setVisibility(View.VISIBLE);
                //+" X "+orderItem.getOrderDishResponses().get(2).getQty()
                if(orderItem.getOrderDishResponses().size()-3>0)
                {
                    mBinding.more.setVisibility(View.VISIBLE);
                    int more=orderItem.getOrderDishResponses().size()-3;
                    mBinding.more.setText("+"+more+"More");
                }
            }
        }

        @Override
        public void onClick(View view) {
        }
    }
}



