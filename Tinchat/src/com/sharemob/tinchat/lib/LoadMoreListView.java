//package com.sharemob.tinchat.lib;
//import android.content.Context;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AbsListView;
//import android.widget.AbsListView.OnScrollListener;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//
//import com.sharemob.tinchat.R;
//
//public class LoadMoreListView extends ListView implements OnScrollListener {
//
//  private static final String TAG = "LoadMoreListView";
//
//  /**
//   * Listener that will receive notifications every time the list scrolls.
//   */
//  private OnScrollListener mOnScrollListener;
//  private LayoutInflater mInflater;
//  private Boolean isEnable = true;
//
//  public Boolean getIsEnable() {
//    return isEnable;
//  }
//
//  public void setIsEnable(Boolean isEnable) {
//    this.isEnable = isEnable;
//  }
//
//  // footer view
//  private RelativeLayout mFooterView;
//  // private TextView mLabLoadMore;
//  private ProgressBar mProgressBarLoadMore;
//
//  // Listener to process load more items when user reaches the end of the list
//  private OnLoadMoreListener mOnLoadMoreListener;
//  // To know if the list is loading more items
//  private boolean mIsLoadingMore = false;
//  private int mCurrentScrollState;
//
//  public LoadMoreListView(Context context) {
//    super(context);
//    init(context);
//  }
//
//  public LoadMoreListView(Context context, AttributeSet attrs) {
//    super(context, attrs);
//    init(context);
//  }
//
//  public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
//    super(context, attrs, defStyle);
//    init(context);
//  }
//
//  private void init(Context context) {
//
//    mInflater = (LayoutInflater) context
//        .getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
//
//    // footer
//    mFooterView = (RelativeLayout) mInflater.inflate(
//        R.layout.load_more_footer, this, false);//加载底部view
//    /*
//     * mLabLoadMore = (TextView) mFooterView
//     * .findViewById(R.id.load_more_lab_view);
//     */
//    mProgressBarLoadMore = (ProgressBar) mFooterView
//        .findViewById(R.id.load_more_progressBar);
//
//    addFooterView(mFooterView);
//
//    super.setOnScrollListener(this);//设置滚动监听
//  }
//
//  @Override
//  public void setAdapter(ListAdapter adapter) {
//    super.setAdapter(adapter);
//  }
//
//  /**
//   * Set the listener that will receive notifications every time the list
//   * scrolls.
//   * 
//   * @param l
//   *            The scroll listener.
//   */
//  @Override
//  public void setOnScrollListener(AbsListView.OnScrollListener l) {
//    mOnScrollListener = l;
//  }
//
//  /**
//   * Register a callback to be invoked when this list reaches the end (last
//   * item be visible)
//   * 
//   * @param onLoadMoreListener
//   *            The callback to run.
//   */
//
//  public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
//    mOnLoadMoreListener = onLoadMoreListener;
//  }
//
//  public void onScroll(AbsListView view, int firstVisibleItem,
//      int visibleItemCount, int totalItemCount) {
//    if (isEnable) {//是否开启滑动
//    if (mOnScrollListener != null) {
//      mOnScrollListener.onScroll(view, firstVisibleItem,
//          visibleItemCount, totalItemCount);
//    }
//
//    
//
//      if (mOnLoadMoreListener != null) {
//
//        if (visibleItemCount == totalItemCount) {//刚好铺满一个屏幕，
//          mProgressBarLoadMore.setVisibility(View.GONE);
//          return;
//        }
//
//        boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;//滑动到底部
//
//        if (!mIsLoadingMore && loadMore
//            && mCurrentScrollState != SCROLL_STATE_IDLE) { //判断如果滚动到底部，并且已经停止滚动的时候，我们就显示底部加载更多。
//          mProgressBarLoadMore.setVisibility(View.VISIBLE);
//          mIsLoadingMore = true; 
//          onLoadMore();//回调监听
//        }
//
//      }
//    }
//
//  }
//  
//
//  /**
//   *   禁止滑动 ，view重绘，否则导致闪烁
//   */
//public void onScrollStateChanged(AbsListView view, int scrollState) {
//    
//    // bug fix: listview was not clickable after scroll //由于我并不需要listvie item是否可以按。所以我屏蔽掉了这个，否则有可能加载图片会闪烁不停哦。这个地方要注意。
//    if (isEnable) {//是否开启滑动
//      
////		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
////			view.invalidateViews();
////		}
//
//    mCurrentScrollState = scrollState;
//
//    if (mOnScrollListener != null) {
//      mOnScrollListener.onScrollStateChanged(view, scrollState);
//    }
//    }
//  }
//  
//
//  public void onLoadMore() {
//    Log.d(TAG, "onLoadMore");
//    if (mOnLoadMoreListener != null) {
//      mOnLoadMoreListener.onLoadMore();
//    }
//  }
//
//  /**
//   * Notify the loading more operation has finished
//   */
//  public void onLoadMoreComplete() {
//    mIsLoadingMore = false;
//    mProgressBarLoadMore.setVisibility(View.GONE);
//  }
//
//  /**
//   * Interface definition for a callback to be invoked when list reaches the
//   * last item (the user load more items in the list)
//   */
//  public interface OnLoadMoreListener {
//    /**
//     * Called when the list reaches the last item (the last item is visible
//     * to the user)
//     */
//    public void onLoadMore();
//  }
//
//}