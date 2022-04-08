package com.example.foodorder.review.displayReview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.databinding.ListItemDisplayReviewBinding;
import com.example.foodorder.databinding.ListItemMenuReviewBinding;
import com.example.foodorder.model.Customer;
import com.example.foodorder.model.MenuItem;
import com.example.foodorder.model.MenuItemReview;
import com.example.foodorder.model.RestaurantReview;
import com.example.foodorder.model.Review;
import com.example.foodorder.review.ReviewRepository;
import com.example.foodorder.util.AppUtils;
import com.example.foodorder.util.DateUtil;

import java.util.List;

public class DisplayReviewAdapter extends RecyclerView.Adapter<DisplayReviewAdapter.DisplayReviewViewHolder> {
    ListItemDisplayReviewBinding mBinding;
    Context mContext;
    private List<RestaurantReview> mReviewList;
    public DisplayReviewAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public DisplayReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding=ListItemDisplayReviewBinding.inflate(LayoutInflater.from(mContext),parent,false);
        return new DisplayReviewViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayReviewViewHolder holder, int position) {
        RestaurantReview review =mReviewList.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        if (mReviewList == null) {
            return 0;
        }
        return mReviewList.size();
    }
    public void setList(List<RestaurantReview> reviewList) {
        mReviewList = reviewList;
        notifyDataSetChanged();
    }


    public class DisplayReviewViewHolder extends RecyclerView.ViewHolder  {
        private ListItemDisplayReviewBinding mBinding;
        public DisplayReviewViewHolder(@NonNull ListItemDisplayReviewBinding binding) {
            super(binding.getRoot());
            mBinding=binding;
        }
        public void bind(RestaurantReview review) {
            if(review.getCustomer()!=null) {
                if (review.getCustomer().getName() != null ||
                        !TextUtils.isEmpty(review.getCustomer().getName()))
                    mBinding.itemTitle.setText(review.getCustomer().getName());
                if (review.getCustomer().getPhotoURL() != null)
                    AppUtils.setImage(mContext, review.getCustomer().getPhotoURL(),
                            mBinding.itemProfileImg);
            }

                mBinding.ratingBarReview.setRating(review.getNum_of_stars());
            mBinding.itemRating.setText(Float.toString(review.getNum_of_stars()));
            if(!TextUtils.isEmpty(review.getTextReview()))
                mBinding.reviewText.setText(review.getTextReview());
            else
                mBinding.reviewText.setVisibility(View.GONE);
            if(review.getTime()!=null){
                mBinding.time.setText(DateUtil.getDisplayReviewDateFormat(review.getTime()));
            }

        }
    }
}


