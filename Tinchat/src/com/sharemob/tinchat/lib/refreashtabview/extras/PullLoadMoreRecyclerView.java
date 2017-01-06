package com.sharemob.tinchat.lib.refreashtabview.extras;

import com.sharemob.tinchat.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2016-7-23 上午8:43:37]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
@SuppressWarnings("unused")
public class PullLoadMoreRecyclerView extends LinearLayout {
    public static interface PullLoadMoreListener {
        void onRefresh();

        void onLoadMore();
        
    }
    
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PullLoadMoreListener mPullLoadMoreListener;
    private boolean hasMore = true;
    private boolean isRefresh = false;
    private boolean isLoadMore = false;
    private boolean pullRefreshEnable = true;
    private boolean pushRefreshEnable = true;
    public View mFooterView;
    private FrameLayout mEmptyViewContainer;
    private Context mContext;
    private TextView loadMoreText;
    private LinearLayout loadMoreLayout;
    private PullLoadMoreRecyclerView.AdapterDataObserver mEmptyDataObserver;

    public PullLoadMoreRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public PullLoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RecyclerView RecyclerView(){
    	return mRecyclerView;
    }
    
    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.pull_loadmore_recyclerview_layout, null);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutOnRefresh(this));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setVerticalScrollBarEnabled(true);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnScrollListener(new RecyclerViewOnScroll(this));

        mRecyclerView.setOnTouchListener(new onTouchRecyclerView());

        mFooterView = view.findViewById(R.id.footerView);
        mEmptyViewContainer = (FrameLayout) view.findViewById(R.id.emptyView);

        loadMoreLayout = (LinearLayout) view.findViewById(R.id.loadMoreLayout);
        loadMoreText = (TextView) view.findViewById(R.id.loadMoreText);

        mFooterView.setVisibility(View.GONE);
        mEmptyViewContainer.setVisibility(View.GONE);

        this.addView(view);
    }


    /**
     * LinearLayoutManager
     */
    public void setLinearLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * GridLayoutManager
     */

    public void setGridLayout(int spanCount) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, spanCount);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }


    /**
     * StaggeredGridLayoutManager
     */

    public void setStaggeredGridLayout(int spanCount) {
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(spanCount, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void scrollToTop() {
        mRecyclerView.scrollToPosition(0);
    }

    public void scrollToBottom(){
    	if(mRecyclerView.getAdapter().getItemCount()>0){
    		int position=mRecyclerView.getAdapter().getItemCount()-1;
        	mRecyclerView.scrollToPosition(position);
    	}
    	mFooterView.setVisibility(View.GONE);
//    	this.setPullLoadMoreCompleted();
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            mRecyclerView.setAdapter(adapter);
            if (mEmptyDataObserver == null) {
                mEmptyDataObserver = new PullLoadMoreRecyclerView.AdapterDataObserver();
            }
            adapter.registerAdapterDataObserver(mEmptyDataObserver);
        }
    }

    public void showEmptyView() {
        RecyclerView.Adapter<?> adapter = mRecyclerView.getAdapter();
        if (adapter != null && mEmptyViewContainer.getChildCount() != 0) {
            if (adapter.getItemCount() == 0) {
                mFooterView.setVisibility(View.GONE);
                mEmptyViewContainer.setVisibility(VISIBLE);
            } else {
                mEmptyViewContainer.setVisibility(GONE);
            }
        }
    }

    public void setPullRefreshEnable(boolean enable) {
        pullRefreshEnable = enable;
        setSwipeRefreshEnable(enable);
    }

    public boolean getPullRefreshEnable() {
        return pullRefreshEnable;
    }

    public void setSwipeRefreshEnable(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }

    public boolean getSwipeRefreshEnable() {
        return mSwipeRefreshLayout.isEnabled();
    }


    public void setColorSchemeResources(int... colorResIds) {
        mSwipeRefreshLayout.setColorSchemeResources(colorResIds);

    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public void setRefreshing(final boolean isRefreshing) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (pullRefreshEnable)
                    mSwipeRefreshLayout.setRefreshing(isRefreshing);
            }
        });

    }

    /**
     * When view detached from window , unregister adapter data observer, avoid momery leak.
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        RecyclerView.Adapter<?> adapter = mRecyclerView.getAdapter();
        if (adapter != null) {
//            adapter.unregisterAdapterDataObserver(mEmptyDataObserver);
        }
    }

    /**
     * Solve IndexOutOfBoundsException exception
     */
    public class onTouchRecyclerView implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return isRefresh || isLoadMore;
        }
    }

    /**
     * This Observer receives adapter data change.
     * When adapter's item count greater than 0 and empty view has been set,then show the empty view.
     * when adapter's item count is 0 ,then empty view hide.
     */
    private class AdapterDataObserver extends android.support.v7.widget.RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            showEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            showEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            showEmptyView();
        }
    }

    public boolean getPushRefreshEnable() {
        return pushRefreshEnable;
    }

    public void setPushRefreshEnable(boolean pushRefreshEnable) {
        this.pushRefreshEnable = pushRefreshEnable;
    }

    public LinearLayout getFooterViewLayout() {
        return loadMoreLayout;
    }

    public void setFooterViewBackgroundColor(int color) {
        loadMoreLayout.setBackgroundColor(ContextCompat.getColor(mContext, color));
    }

    public void setFooterViewText(CharSequence text) {
        loadMoreText.setText(text);
    }

    public void setFooterViewText(int resid) {
        loadMoreText.setText(resid);
    }

    public void setFooterViewTextColor(int color) {
        loadMoreText.setTextColor(ContextCompat.getColor(mContext, color));
    }

    public void setEmptyView(View emptyView) {
        mEmptyViewContainer.removeAllViews();
        mEmptyViewContainer.addView(emptyView);
    }

    public void refresh() {
        if (mPullLoadMoreListener != null) {
            mPullLoadMoreListener.onRefresh();
        }
    }

	public void loadMore() {
        if (mPullLoadMoreListener != null && hasMore) {
            mFooterView.animate()
                    .translationY(0)
                    .setDuration(300)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mFooterView.setVisibility(View.VISIBLE);
                        }
                    })
                    .start();
            invalidate();
            mPullLoadMoreListener.onLoadMore();
        }
    }


	public void setPullLoadMoreCompleted() {
        isRefresh = false;
        setRefreshing(false);
        isLoadMore = false;
        mFooterView.animate()
                .translationY(mFooterView.getHeight())
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
        setSwipeRefreshEnable(false);
    }


    public void setOnPullLoadMoreListener(PullLoadMoreListener listener) {
        mPullLoadMoreListener = listener;
    }


    public boolean isLoadMore() {
        return isLoadMore;
    }

    public void setIsLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

}