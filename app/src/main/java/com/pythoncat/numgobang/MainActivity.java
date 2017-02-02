package com.pythoncat.numgobang;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.pythoncat.gobang.GobangView;

public class MainActivity extends AppCompatActivity {

    private AlertDialog dialog;

    public Activity get() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final GobangView gv = (GobangView) findViewById(R.id.gobang_view_vv);
        gv.setGameOverListener(new GobangView.OnGameOverListener() {
            @Override
            public void isOver(GobangView.GameResult result) {
                String text;
                if (result == GobangView.GameResult.WhiteWin) {
                    text = "白棋胜利!";
                } else if (result == GobangView.GameResult.BlackWin) {
                    text = "黑棋胜利!";
                } else if (result == GobangView.GameResult.Dogfail) {
                    text = "平局";
                } else {
                    text = "战斗还没有结束呢...";
                }
                dialog = new AlertDialog.Builder(get())
                        .setTitle("战斗结束")
                        .setMessage("当前战果：" + text)
                        .setNegativeButton("不玩了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(),
                                        "大爷下次再来玩啊~", Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 500);
                            }
                        })
                        .setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                gv.reStart();
                            }
                        }).create();
                dialog.show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
