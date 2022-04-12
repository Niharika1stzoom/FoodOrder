package com.zoom.happiestplaces.review.displayReview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zoom.happiestplaces.databinding.ListItemDisplayReviewBinding;
import com.zoom.happiestplaces.databinding.ListItemMenuReviewBinding;
import com.zoom.happiestplaces.model.RestaurantReview;
import com.zoom.happiestplaces.util.AppUtils;
import com.zoom.happiestplaces.util.DateUtil;

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


