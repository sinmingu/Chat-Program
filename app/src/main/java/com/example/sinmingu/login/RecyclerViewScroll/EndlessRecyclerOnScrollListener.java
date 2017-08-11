package com.example.sinmingu.login.RecyclerViewScroll;


import android.support.v7.widget.RecyclerView;

/**
 * Created by sinmingu on 2017-08-11.
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = "EndlessScrollListener";

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleBtnInterval = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount,lastVisibleItem;

    private int currentPage = 1;

    RecyclerViewPositionHelper mRecyclerViewHelper;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        //recyclerView로부터 각종 데이터 가져오는 객체
        mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
        //현재 화면상에 보이는 item 개수
        visibleItemCount = recyclerView.getChildCount();
        //adapter에 설정된 총 item의 개수를 가져온다.
        totalItemCount = mRecyclerViewHelper.getItemCount();
        //현재 화면상에 보이는 첫번째 item의 position을 반환
        firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
        //현재 화면상에 보이는 마지막 item의 position을 반환
        lastVisibleItem = mRecyclerViewHelper.findLastVisibleItemPosition();


        if(totalItemCount-1 == lastVisibleItem){
            //리스트뷰의 마지막 item을 보고있을 때.
            onLastPosition(lastVisibleItem);
        }else if(firstVisibleItem == 0 && newState == RecyclerView.SCROLL_STATE_IDLE){
            //리스트뷰의 첫번째 item을 보고있을 때.
            onFirstPosition(firstVisibleItem,lastVisibleItem);
        }

        if(totalItemCount-1 - lastVisibleItem > visibleBtnInterval){
            //리스트뷰의 마지막 item의 position과 현재 화면상의 마지막 item의 간격이 50을 초과할때 호출
            onToggleMoveToDown(true);
        }else{
            onToggleMoveToDown(false);
        }

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
//        mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
//        visibleItemCount = recyclerView.getChildCount();
//        totalItemCount = mRecyclerViewHelper.getItemCount();
//        firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
//        lastVisibleItem = mRecyclerViewHelper.findLastVisibleItemPosition();
//
//        if (loading) {
//            if (totalItemCount > previousTotal) {
//                loading = false;
//                previousTotal = totalItemCount;
//            }
//        }
//        if (!loading && (totalItemCount - visibleItemCount)
//                <= (firstVisibleItem + visibleThreshold)) {
//            // End has been reached
//            // Do something
//            currentPage++;
//
//            onLoadMore(currentPage);
//
//            loading = true;
//        }
    }



    public abstract void onLastPosition(int lastPagePosition);
    /**
     * @param firstPagePosition :현재 화면에 보이는 아이템 중 가장 위에 있는 아이템의 position
     * @param lastPagePosition  :현재 화면에 보이는 아이템 중 가장 아래에 있는 아이템의 position
     * **/
    public abstract void onFirstPosition(int firstPagePosition, int lastPagePosition);

    /**
     * @param toggle 토글해야하면 true, 하지 않아도 되면 false반환**/
    public abstract void onToggleMoveToDown(boolean toggle);

}