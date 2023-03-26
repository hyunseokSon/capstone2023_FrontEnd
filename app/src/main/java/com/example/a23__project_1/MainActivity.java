package com.example.a23__project_1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import com.airbnb.lottie.LottieAnimationView;

import android.os.Looper;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.animation.Animator;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Map;


public class MainActivity extends AppCompatActivity{

    private LottieAnimationView animationView;
    private Animation animFlip;
    private ConstraintLayout layout_loading;
    private LinearLayout layout_main;
    private TextView txtTitle, txtSubtitle;
    private FrameLayout layout_slidingRootNav;

    //Fragment
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentFirst fragmentFirst = new FragmentFirst();
    private FragmentSecond fragmentSecond = new FragmentSecond();
    private FragmentThird fragmentThird = new FragmentThird();
    private FragmentForth fragmentForth = new FragmentForth();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Main);
        setContentView(R.layout.activity_main);

        txtTitle = (TextView)findViewById(R.id.txt_title);
        txtSubtitle = (TextView)findViewById(R.id.txt_subtitle);
        layout_loading = (ConstraintLayout)findViewById(R.id.layout_mainloading);
        layout_main=(LinearLayout) findViewById(R.id.layout_main);
        animationView = (LottieAnimationView) findViewById(R.id.lottie);
        //layout_slidingRootNav = (FrameLayout) findViewById(R.id.activity_slidingRootNav);



        animFlip = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.flip);

        /**********************************************************
         *  로딩 액티비티 동작
         * ********************************************************/

        // txtView 애니메이션 동작 코드
        txtSubtitle.startAnimation(animFlip);
        txtTitle.startAnimation(animFlip);

        //animationView.setAnimation("loading.json");
        // lottie 애니메이션 동작 코드
        animationView.playAnimation();
        animationView.setRepeatCount(1);

        startLoading();

        /**********************************************************
         *  main 액티비티 동작
         * ********************************************************/

        //툴바 옆, setting 관련 코드
        ImageButton btn_setting = (ImageButton) findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.a23__project_1.SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein_right, R.anim.stay);

            }
        });

        //menu 관련 코드
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");


        setSupportActionBar(toolbar);

        new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withMenuLayout(R.layout.activity_menu)
                .inject();



        //사용자 정보 확인
        LinearLayout btn_userInfo = (LinearLayout) findViewById(R.id.btn_userInfo);
        btn_userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, UserinfoActivity.class);
                startActivity(intent);
            }
        });

        //나의 여정 기록 확인
        LinearLayout btn_history = (LinearLayout) findViewById(R.id.btn_history);
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        //나의 일정 정보
        LinearLayout btn_myplan = (LinearLayout) findViewById(R.id.btn_myplan);
        btn_myplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyplanActivity.class);
                //intent.putExtra("profile", image_profile);
                startActivity(intent);
            }
        });

        // 카카오 계정 로그아웃하기.
        LinearLayout btn_out = (LinearLayout) findViewById(R.id.btn_menulogout);
        btn_out.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                showMessage();
            }
        });

        //지도 확인
        LinearLayout btn_map = (LinearLayout) findViewById(R.id.btn_map);
        btn_map.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        //화면 추가할 프래그먼트 추가
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout_main, fragmentFirst).commitAllowingStateLoss();

        //메뉴클릭 리스너 등록
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());


        //animation listener
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    //로딩 화면  메소드
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                layout_loading.setVisibility(View.GONE);
 //               layout_slidingRootNav.setVisibility(View.VISIBLE);
                layout_main.setVisibility(View.VISIBLE);

            }
        }, 4500);
    }


    //뒤로가기 버튼 클릭 관련 코드
    private final long finishtimeed = 1000;
    private long presstime = 0;

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - presstime;

        if (0 <= intervalTime && finishtimeed >= intervalTime) {
            finish();
        } else {
            presstime = tempTime;
            Toast.makeText(getApplicationContext(), "한번더 누르시면 앱이 종료됩니다", Toast.LENGTH_SHORT).show();
        }
    }

    public void showMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("로그아웃할 시 앱이 종료됩니다. \n로그아웃 하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        //로그아웃 "예"
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "로그아웃이 완료되었습니다.\n앱을 종료합니다.", Toast.LENGTH_SHORT).show();
                finish();
//                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
//                    @Override
//                    public void onCompleteLogout() {
//                        finish(); // 현재 액티비티 종료
//                    }
//                });
            }
        });

        //로그아웃 "아니오"
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setNegativeButton("아니오", null);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.firstItem:
                    transaction.replace(R.id.frameLayout_main, fragmentFirst).commitAllowingStateLoss();
                    break;
                case R.id.secondItem:
                    transaction.replace(R.id.frameLayout_main, fragmentSecond).commitAllowingStateLoss();
                    break;
                case R.id.thirdItem:
                    transaction.replace(R.id.frameLayout_main, fragmentThird).commitAllowingStateLoss();
                    break;

                case R.id.forthItem:
                        transaction.replace(R.id.frameLayout_main, fragmentForth).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

}