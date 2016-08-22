package com.example.saulmm.splashes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;


public class OnboardingWithCenterAnimationActivity extends AppCompatActivity {
    public static final int STARTUP_DELAY = 300;
    public static final int ANIM_ITEM_DURATION = 1000;
    public static final int ITEM_DELAY = 300;

    private boolean animationStarted = false;

    /**
     * make the icon at the center first
     *
     * but when onCreate call, make it dissapear and replace with icon moved
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        // fixme: 8/22/16 make this screen full screen
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_center);
    }

    /**
     * fixme
     * If you want to play the animation immediately, without requiring interaction, then you might want to call it from the onWindowFocusChanged() method in your Activity,
     * which will get called when Android brings your window into focus.
     *
     * @see <a href="http://stackoverflow.com/questions/7289827/how-to-start-animation-immediately-after-oncreate"></a>
     * @param hasFocus
     *
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // todo do not forget to write the if/else case to check the focus
        if (!hasFocus || animationStarted) {
            return;
        }

        animate();

        super.onWindowFocusChanged(hasFocus);
    }

    private void animate() {
        ImageView logoImageView = (ImageView) findViewById(R.id.img_logo);
        ViewGroup container = (ViewGroup) findViewById(R.id.container);

        // TODO: 8/22/16 move up
        ViewCompat.animate(logoImageView)
            .translationY(-250)
            .setStartDelay(STARTUP_DELAY)
            .setDuration(ANIM_ITEM_DURATION).setInterpolator(
                new DecelerateInterpolator(1.2f)).start();

        for (int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);
            ViewPropertyAnimatorCompat viewAnimator;

            // TODO: 8/22/16 show the button
            if (!(v instanceof Button)) {
                viewAnimator = ViewCompat.animate(v)
                        .translationY(50).alpha(1)
                        .setStartDelay((ITEM_DELAY * i) + 500)
                        .setDuration(1000);
            } else {
                // // TODO: 8/22/16 enlarge the view
                viewAnimator = ViewCompat.animate(v)
                        .scaleY(1).scaleX(1)
                        .setStartDelay((ITEM_DELAY * i) + 500)
                        .setDuration(500);
            }

            viewAnimator.setInterpolator(new DecelerateInterpolator()).start();
        }
    }
}
