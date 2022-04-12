package com.zoom.happiestplaces.review.addReview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zoom.happiestplaces.databinding.ListItemMenuReviewBinding;
import com.zoom.happiestplaces.model.MenuItem;
import com.zoom.happiestplaces.model.MenuItemReview;
import com.zoom.happiestplaces.review.ReviewRepository;
import com.zoom.happiestplaces.util.AppUtils;

import java.util.List;


public class MenuItemReviewAdapter extends RecyclerView.Adapter<MenuItemReviewAdapter.MenuItemReviewViewHolder> {
    ListItemMenuReviewBinding mBinding;
    Context mContext;
    ReviewRepository repository;
    private List<MenuItem> mFoodMenuList;
    public MenuItemReviewAdapter(Context context,ReviewRepository repo) {
        mContext = context;
        repository=repo;
    }

    @NonNull
    @Override
    public MenuItemReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding=ListItemMenuReviewBinding.inflate(LayoutInflater.from(mContext),parent,false);
        return new MenuItemReviewViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemReviewViewHolder holder, int position) {
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


    public class MenuItemReviewViewHolder extends RecyclerView.ViewHolder  {
        private ListItemMenuReviewBinding mBinding;
        public MenuItemReviewViewHolder(@NonNull ListItemMenuReviewBinding binding) {
            super(binding.getRoot());
            mBinding=binding;
        }
        public void bind(MenuItem FoodMenu) {
            if(FoodMenu.getName()!=null)
                mBinding.itemName.setText(FoodMenu.getName());
            if (FoodMenu.getImageUrl()!= null)
                AppUtils.setImage(mContext, FoodMenu.getImageUrl(), mBinding.itemImg);
            MenuItemReview menuItemReview =repository.getMenuItemReview(FoodMenu.getId());
            if(menuItemReview!=null && menuItemReview.getNumStars()>0)
            mBinding.ratingBar.setRating(menuItemReview.getNumStars());
            mBinding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    //adds or updates
                    repository.addReviewMenuItem(FoodMenu.getId(),v);
                }
            });

        }



    }
}


