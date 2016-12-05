package openlive.agora.io.filetest;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createFile();
    }

    private void createFile() {
        File file = new File(Environment.getExternalStorageDirectory(), "哈哈哈.txt");
        Log.d("TAG","文件名："+Environment.getExternalStorageDirectory());
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
