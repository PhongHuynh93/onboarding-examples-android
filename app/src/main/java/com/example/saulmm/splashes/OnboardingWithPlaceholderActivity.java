package com.example.saulmm.splashes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saulmm.splashes.itemanimator.ItemAnimatorFactory;

import java.util.ArrayList;
import java.util.List;

public class OnboardingWithPlaceholderActivity extends AppCompatActivity {
    private int mContentViewHeight;
    private Toolbar mToolbar;
    private RecyclerAdapter mAdapter;
    private View mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // todo 7 - Fake a long startup time, wait 500ms, after that call onFakeCreate
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onFakeCreate();
            }

        }, 500);
    }

    private void onFakeCreate() {
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_onboarding_placeholder);

//      todo 8 - show a title
        TextView titleTextView = (TextView) findViewById(R.id.text_title);
        ViewCompat.animate(titleTextView).alpha(1).start();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mFab = findViewById(R.id.fab);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(ItemAnimatorFactory.slidein());

        mAdapter = new RecyclerAdapter();
        recyclerView.setAdapter(mAdapter);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        /**
         * todo 9 - lấy height và width của toolbar, nhét trong hàm này do nếu đo height của toolbar ma nhét trong onResume thì cũng ko đó được, phải nhét trong hàm này
         * get height of a toolbar in layout
         */
        mToolbar.getViewTreeObserver().addOnPreDrawListener(
            new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mToolbar.getViewTreeObserver().removeOnPreDrawListener(this);
                    final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

                    mToolbar.measure(widthSpec, heightSpec);
                    mContentViewHeight = mToolbar.getHeight();
                    collapseToolbar();
                    return true;
                }
            });
    }


    private void collapseToolbar() {
        int toolBarHeight;
        // // TODO: 8/22/16 10 - height chuản của toolbar
        TypedValue tv = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        toolBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        // todo 11 - Constructs and returns a ValueAnimator that animates between int values(height mình tự cho và height chuẩn của layout )
        ValueAnimator valueHeightAnimator = ValueAnimator.ofInt(mContentViewHeight, toolBarHeight);
        valueHeightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // todo 12 - Adds a listener to the set of listeners that are sent update events through the life of an animation.
                // toolbar sẽ giảm height từ từ
                ViewGroup.LayoutParams lp = mToolbar.getLayoutParams();
                lp.height = (Integer) animation.getAnimatedValue();
                mToolbar.setLayoutParams(lp);
            }
        });

        // todo, start the animation
        valueHeightAnimator.start();

        // todo, when anim end, add a list of data to lists.
        valueHeightAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                // todo Fire item animator, add a list of data to lists.
                mAdapter.addAll(ModelItem.getFakeItems());

                // todo Animate fab, show it
                ViewCompat.animate(mFab).setStartDelay(600)
                    .setDuration(400).scaleY(1).scaleX(1).start();

            }
        });
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
        private ArrayList<ModelItem> mItems = new ArrayList<>();

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
            return new RecyclerViewHolder(v);
        }

        public void addAll(List<ModelItem> items) {
            int pos = getItemCount();
            mItems.addAll(items);
            notifyItemRangeInserted(pos, mItems.size());
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount()    {
            return mItems.size();
        }


        class RecyclerViewHolder extends RecyclerView.ViewHolder {
            private TextView mTitleTextView;
            private ImageView mImageView;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                mTitleTextView = (TextView) itemView.findViewById(R.id.text_title);
                mImageView = (ImageView) itemView.findViewById(R.id.img_sampleimage);
            }

            public void bind(int position) {
                mImageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), mItems.get(position).getImgId()));
                mTitleTextView.setText(mItems.get(position).getAuthor());
            }
        }

    }

}
