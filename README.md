# numGobang
* 简单又好玩的游戏 - N子棋。嗯，是的，不仅仅是五子棋，可以扩展成N子棋的。
* 如何使用

<pre>
<code>
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
  
dependencies {
        compile 'com.github.duckAndroid:numGobang:0.0.1'
}
  
  </code>
</pre>

> 然后：
```
<com.pythoncat.gobang.GobangView
        android:id="@+id/gobang_view_vv"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_centerInParent="true"
        app:borderColor="@color/colorAccent"
        app:maxLines="9"
        app:num="4"
        app:scale="0.8"
        app:whiteFirst="false"/>
```

> 最后
```
 final GobangView gv = (GobangView) findViewById(R.id.gobang_view_vv);
        //  gv.setMaxLine(14);
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
                Toast.makeText(getApplicationContext(),
                        "当前战果：" + text, Toast.LENGTH_SHORT).show();
                gv.reStart();
            }
        });
```

* 彩蛋时刻：感谢[hyman](http://www.imooc.com/learn/641)
