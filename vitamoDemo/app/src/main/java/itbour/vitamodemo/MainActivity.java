package itbour.vitamodemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private VideoView vv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vv = (VideoView) findViewById(R.id.vv);
        // 检查包能不能使用
        if (!LibsChecker.checkVitamioLibs(this)) {
            return;
        }

        vv.setVideoPath("http://baobab.wdjcdn.com/1456665467509qingshu.mp4");
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                vv.start();
            }
        });
        // 设置控制器
        vv.setMediaController(new MediaController(this));
    }

}
