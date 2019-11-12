package com.example.cbc.the_hack;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.cbc.the_hack.changeavaterview.MainActivity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;

public class PoemActivity extends AppCompatActivity {
//    private ImageButton imageButtonBack;
    private ImageButton imageButtonMain;
    private TextView poemBody;
    private String content;
    private JsonObject jsonObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poem);
//        imageButtonBack=findViewById(R.id.b_back);
        imageButtonMain=findViewById(R.id.b_main);
        poemBody=findViewById(R.id.poem_body);

        Intent intent=getIntent();
        content=intent.getStringExtra("response");
        //content="{\n" +
         //       "    \"ai\": \"碧海阔\\n楚天昏\\n遥望蓝桥路不分\\n十二楼台春梦远\\n万家灯火夜寒温\"\n" +
         //       "}";
        jsonObj = new JsonParser().parse(content).getAsJsonObject();
        content=jsonObj.get("ai").toString();

        //从asset 读取字体
        AssetManager mgr = getAssets();
        Typeface tf = Typeface.createFromAsset(mgr,"fonts/STXINGKA.TTF");
        poemBody.setTypeface(tf);
        poemBody.setText(content.replace("\\n","\n").replace("\"",""));

//        imageButtonBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(PoemActivity.this, AiActivity.class);
//                startActivity(intent);
//            }
//        });

        imageButtonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PoemActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
