package com.example.foodorder.foodmenu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.databinding.ListItemFoodMenuBinding;
import com.example.foodorder.model.MenuItem;
import com.example.foodorder.util.AppUtils;
import com.example.foodorder.util.SharedPrefUtils;

import java.util.List;

public class FoodMenuAdapter extends RecyclerView.Adapter<FoodMenuAdapter.FoodMenuViewHolder> {
    ListItemFoodMenuBinding mBinding;
    Context mContext;
    private List<MenuItem> mFoodMenuList;
    public FoodMenuAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public FoodMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding=ListItemFoodMenuBinding.inflate(LayoutInflater.from(mContext),parent,false);
        return new FoodMenuViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodMenuViewHolder holder, int position) {
        MenuItem foodMenuItem =mFoodMenuList.get(position);
        holder.bind(foodMenuItem);
    }

    @Override
    public int getItemCount() {
        if (mFoodMenuList == null) {
            return 0;
        }
        return mFoodMenuList.size();
    }
    public void setList(List<MenuItem> FoodMenuList) {
        mFoodMenuList = FoodMenuList;
        notifyDataSetChanged();
    }


    public class FoodMenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ListItemFoodMenuBinding mBinding;
        public FoodMenuViewHolder(@NonNull ListItemFoodMenuBinding binding) {
            super(binding.getRoot());
            mBinding=binding;
            binding.getRoot().setOnClickListener(this);
            binding.addButton.setOnClickListener(this);
            binding.addQty.setOnClickListener(this);
            binding.remQty.setOnClickListener(this);

        }
        public void bind(MenuItem FoodMenu) {
            if(FoodMenu.getName()!=null)
                mBinding.itemName.setText(FoodMenu.getName());
            if(FoodMenu.getDescription()!=null)
                mBinding.itemDesc.setText(FoodMenu.getDescription());
            if(FoodMenu.getPrice()!=null)
                mBinding.itemPrice.setText("Rs "+FoodMenu.getPrice().toString());
            if (FoodMenu.getImageUrl()!= null)
                AppUtils.setImage(mContext, FoodMenu.getImageUrl(), mBinding.itemProfileImg);
            MenuItem cartItem=SharedPrefUtils.containsItem(mContext,FoodMenu.getId());
            if(cartItem!=null) {
                mBinding.addButton.setVisibility(View.GONE);
                mBinding.qtyButtonGroup.setVisibility(View.VISIBLE);
                mBinding.qtyLabel.setText(Integer.toString(cartItem.getQty()));
            }
            else{
                mBinding.addButton.setVisibility(View.VISIBLE);
                mBinding.qtyButtonGroup.setVisibility(View.GONE);
                //mBinding.qtyLabel.setText(Integer.toString(cartItem.getQty()));
            }

        }
        @Override
        public void onClick(View view) {
            Log.d("Scanner","Got clicked");
            if(view.getId()==mBinding.addButton.getId())
            {
                mBinding.addButton.setVisibility(View.GONE);
                mBinding.qtyButtonGroup.setVisibility(View.VISIBLE);
                SharedPrefUtils.addItem(mContext,mFoodMenuList.get(getAdapterPosition()));
                notifyItemChanged(getAdapterPosition());
            }
            if(view.getId()==mBinding.addQty.getId())
            {
                SharedPrefUtils.incrementQty(mContext,mFoodMenuList.get(getAdapterPosition()));
                notifyItemChanged(getAdapterPosition());
            }
            if(view.getId()==mBinding.remQty.getId())
            {
                SharedPrefUtils.decrementQty(mContext,mFoodMenuList.get(getAdapterPosition()));
                notifyItemChanged(getAdapterPosition());
                //remove
            }
            if(view.getId()==mBinding.btnShare.getId())
            {
                AppUtils.shareFoodMenu(mContext,mFoodMenuList.get(getAdapterPosition()));
            }
            else
                viewFoodMenu(mFoodMenuList.get(getAdapterPosition()));
        }
        private void viewFoodMenu(MenuItem FoodMenu) {
           /* Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(FoodMenu.getImageUrl()));
            mContext.startActivity(Intent.createChooser(browserIntent, "Choose one"));

            */
        }
    }
}

