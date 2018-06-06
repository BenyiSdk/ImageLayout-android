package com.benyi.sdk.imageLayout.app;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import com.benyi.sdk.imageLayout.listener.ImageLayoutActionListener;
import com.benyi.sdk.imageLayout.model.Action;
import com.benyi.sdk.imageLayout.util.ActionHistory;
import com.benyi.sdk.imageLayout.util.StringUtil;
import com.benyi.sdk.imageLayout.view.ImageLayout;
import com.benyi.sdk.imageLayout.view.ImageLayoutGestureListenerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImageLayoutActionListener<String> {
    private ImageLayoutGestureListenerView gestureListenerView;
    private ImageLayout inboxActivityLayout;
    private ActionHistory actionHistory;
    public List<String> list=new ArrayList<>();
    private List<String> topList = new ArrayList<>();
    public int nowIndex = 0;
    private boolean isList=false;
    private InboxFullscreenZoomView inboxFullscreenZoomView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gestureListenerView = (ImageLayoutGestureListenerView) findViewById(R.id.inbox_gesture_listener_view);
        inboxActivityLayout = (ImageLayout) findViewById(R.id.inbox_activity_layout);
        inboxActivityLayout.setInboxActionListener(this);
        gestureListenerView.addListener(inboxActivityLayout);
        inboxActivityLayout.setInboxGestureListenerView(gestureListenerView);
        inboxFullscreenZoomView=(InboxFullscreenZoomView) findViewById(R.id.inbox_fullscreen_zoom_view);
        inboxActivityLayout.setInboxFullscreenZoomView(inboxFullscreenZoomView);
        findViewById(R.id.inbox_undo_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undo();
            }
        });
        findViewById(R.id.inbox_trash_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trash();
            }
        });
        findViewById(R.id.inbox_library_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                library();
            }
        });
        findViewById(R.id.inbox_title_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView();
            }
        });
        list.add("http://oxces1l8i.bkt.clouddn.com/uploads/item/2017/11/10/5a0525c46efd897d634e0583bd88489091827db0ab35f.jpg");
        list.add("http://oxces1l8i.bkt.clouddn.com/uploads/item/2017/11/22/5a15202544dd7bc0809d537ce7e356957cb24e2662f22.jpg");
        list.add("http://oxces1l8i.bkt.clouddn.com/uploads/item/2017/11/30/5a1f7c0e246481f9985c2a71e2882c5fd895676c77f6f.jpg");
        list.add("http://oxces1l8i.bkt.clouddn.com/uploads/item/2017/12/14/5a3235043fe3b009cb8c33ceeb701993d8f96c45b3a65.jpg");
        actionHistory = new ActionHistory();
        MainActivity.this.inboxActivityLayout.initFromPosition(nowIndex);
        changeNum();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.inboxActivityLayout.isFullscreenZoomViewShow()) {
            this.inboxActivityLayout.closeFullScreenPhoto();
        }
    }

    public void setTitleText(int position, int total) {
        TextView localTextView = (TextView) findViewById(R.id.inbox_subtitle_text_view);
        if (position >= total) {
            localTextView.setText("");
            return;
        }
        localTextView.setText(position + 1 + " of " + total);
    }

    public void importPhotoToFloder(String floder) {
        if (this.inboxActivityLayout.isAnimatorSetInit()) {
            return;
        }
        this.inboxActivityLayout.bottomPhotoToFloder(floder);
    }

    public void importPhoto(String floder) {
        if (isReady()) {
            return;
        }
        String data =(String)inboxActivityLayout.getShowResoureData(nowIndex);
        topList.add(data);
        actionHistory.addBottomAction(StringUtil.dateToString(),floder, data);
        list.remove(nowIndex);
        importPhotoToFloder(floder);
    }

    public void startDelPhoto() {
        isList=!isList;
        inboxActivityLayout.refreshShowCardView();
        inboxActivityLayout.requestLayout();
//        Intent localIntent = new Intent(this, MainActivity.class);
//        localIntent.putExtra("EXTRA_MESSAGE", "some message");
//        startActivity(localIntent);
//        overridePendingTransition(2131034124, 2131034118);
    }

    protected void changeTrashCount(int total) {
        TextView textView = (TextView) findViewById(R.id.inbox_trash_counter);
        if (total == 0) {
            textView.setVisibility(View.INVISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(total + "");
        }

    }

    protected boolean isReady() {
        if (this.gestureListenerView.isInMove()) {
            return true;
        } else {
            if (this.inboxActivityLayout.isAnimatorSetInit()) {
                return true;
            }
        }
        return false;
    }

    public void startGridView(int position) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("CURRENT_POSITION", position);
        startActivityForResult(intent, 10);
        overridePendingTransition(2131034118, 2131034118);
    }

    public void trash() {
        if (isReady()) {
            return;
        }
        startDelPhoto();
    }

    public void library() {
        if (isReady()) {
            return;
        }
        importPhoto("");
//        startLibray();
    }

    public void startLibray() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(2131034124, 2131034118);
    }

    public void gridView() {
        if (isReady()) {
            return;
        }
        startGridView(0);
    }

    public void undo() {
        if (isReady()) {
            return;
        }
        if (this.inboxActivityLayout.isFullscreenZoomViewShow()) {
            this.inboxActivityLayout.closeFullScreenPhoto();
        }
        startUndoAnimation();
        startUndo();
    }

    public void startUndo() {
        Action action=actionHistory.getLast();
        if(action!=null){
            if (action.isRightAction()) {
                nowIndex++;
                this.inboxActivityLayout.undoRight();
            } else if (action.isLeftAction()) {
                nowIndex--;
                this.inboxActivityLayout.undoLeft();
            } else if (action.isTopAction()) {
                list.add(nowIndex,(String) action.data);
                topList.remove(action.data);
                this.inboxActivityLayout.undoTop(list.indexOf(action.data));
            } else if (action.isBottomAction()) {
                list.add(nowIndex,(String)action.data);
                this.inboxActivityLayout.undoBottom(list.indexOf(action.data));
            } else if (action.isSelectAction()) {
                nowIndex=action.oldIndex;
                this.inboxActivityLayout.undoSelect(action.oldIndex);
            }
        }
    }

    public void startUndoAnimation() {
        findViewById(R.id.inbox_undo_button).startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
    }
    public void startTrashAnimation() {
        findViewById(R.id.inbox_trash_button).startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
    }

    @Override
    public void undoAction() {
        changeNum();
    }

    @Override
    public void animationStateChange(boolean isStart) {
        if (isStart) {
            this.gestureListenerView.startAnimation();
        } else {
            this.gestureListenerView.endAnimation();
        }
    }

    @Override
    public void nextAction() {
        nowIndex++;
        actionHistory.addNextAction(StringUtil.dateToString());
        changeNum();
    }

    @Override
    public void prevAction() {
        nowIndex--;
        actionHistory.addPrevAction(StringUtil.dateToString());
        changeNum();
    }

    @Override
    public void undoSelectAction() {
        changeNum();
    }

    @Override
    public void topAction() {
        String data =(String)inboxActivityLayout.getShowResoureData(nowIndex);
        topList.add(data);
        actionHistory.addTopAction(StringUtil.dateToString(), data);
        list.remove(nowIndex);
        startTrashAnimation();
        changeNum();
    }

    @Override
    public void bottomAction() {
        importPhoto("");
    }

    @Override
    public void bottomAction(String str) {
        changeNum();
    }

    public void changeNum() {
        setTitleText(nowIndex, list.size());
        changeTrashCount(topList.size());
    }

    @Override
    public void loadResourceToCardView(String data, View view) {
        View childView=((ViewGroup)view).getChildAt(0);;
        if(childView instanceof ImageView){
                    Glide.with(view.getContext())
                            .load(data)
                            .into((ImageView) childView);
        }

    }

    @Override
    public void initfullScreenPhoto(String data, View view) {
        if(view instanceof InboxFullscreenZoomView){
            inboxFullscreenZoomView.init(data);
        }
    }

    @Override
    public int getShowIndex() {
        return nowIndex;
    }

    @Override
    public String getShowResourceData(int position) {
        if (list!=null&&position < list.size()) {
            return list.get(position);
        }
        return null;
    }


    @Override
    public Rect getTopHideRect() {
        Rect rect = new Rect();
        View view = findViewById(R.id.inbox_trash_can);
        View button = view.findViewById(R.id.inbox_trash_button);
        int[] screen = new int[2];
        button.getLocationOnScreen(screen);
        rect.top = screen[1];
        rect.left = screen[0];
        rect.right = rect.left + view.getWidth();
        rect.bottom = rect.top + view.getHeight();
        return rect;
    }

    @Override
    public Rect getBottomHideRect() {
        Rect rect = new Rect();
        int[] screen = new int[2];
        View view = findViewById(R.id.inbox_library_button);
        view.getLocationOnScreen(screen);
        rect.top = screen[1];
        rect.left = screen[0];
        rect.right = rect.left + view.getWidth();
        rect.bottom = rect.top + view.getHeight();
        return rect;
    }

    @Override
    public void addViewToCardView(ViewGroup cardView, String data) {
        cardView.removeAllViewsInLayout();
        int index=list.indexOf(data);
        if(isList&&index%2==0){
            GridView gridView=new GridView(this);
            gridView.setNumColumns(4);
            gridView.setAdapter(new ImageAdapter(topList));
            cardView.addView(gridView,-1,cardView.getLayoutParams());
        }else{
            ImageView imageView=new ImageView(this);
            cardView.addView(imageView,-1,cardView.getLayoutParams());
        }
    }
}
