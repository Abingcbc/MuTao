package com.example.cbc.the_hack;

import android.content.Intent;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.cbc.the_hack.changeavaterview.MainActivity;
import com.example.cbc.the_hack.util.OkHttp3Util;
import kotlin.text.Regex;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studio.carbonylgroup.textfieldboxes.SimpleTextChangedWatcher;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AiActivity extends AppCompatActivity {
    private List<String> list = new ArrayList<String>();
    private Spinner spinnertype;
    private RadioGroup rg_worktype;
    private RadioGroup rg_jjtype;
    private ArrayAdapter<String> adapter;
    private LinearLayout layoutJJ;
    private LinearLayout layoutSC;
    private TextFieldBoxes jjbox;
    private TextFieldBoxes scbox;
    private String upload_api;

    private int worktype;//作品类型，0/绝句，1/诗词
    private int jjtype;//绝句类型，0/五言，1/七言
    private int sctype;//词类型，从1---
    private String jjkeyword;//关键字
    private String sckeyword;//关键字
    private int jjerrorCode;//0为错误格式
    private int scerrorCode;//0为错误格式
    private ImageButton imageButtonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai);
        worktype=0;
        sctype=1;
        jjerrorCode=-1;
        scerrorCode=-1;
        jjtype=5;
        spinnertype=(Spinner)findViewById(R.id.spinner);
        rg_jjtype=findViewById(R.id.word_JJ);
        rg_worktype=findViewById(R.id.rg_type);
        layoutJJ=findViewById(R.id.layout_JJ);
        layoutSC=findViewById(R.id.layout_SC);
        jjbox=findViewById(R.id.text_field_boxes_jj);
        scbox=findViewById(R.id.text_field_boxes_sc);
        imageButtonBack=findViewById(R.id.b_backtomain);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.types,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinnertype.setAdapter(adapter);
        spinnertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sctype=position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        rg_worktype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_JJ:
                        worktype=0;
                        layoutSC.setVisibility(View.GONE);
                        layoutJJ.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_SC:
                        worktype=1;
                        layoutJJ.setVisibility(View.GONE);
                        layoutSC.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        rg_jjtype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.word_5:
                        jjtype=5;
                        break;
                    case R.id.word_7:
                        jjtype=7;
                        break;
                }
            }
        });
        jjbox.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                // What you want to do when text changes
                Log.d("hellotag", "onClick: "+theNewText);
                if(checkAll(theNewText)){
                    jjbox.setError("非中文或标点(包含空格)",false);
                    jjerrorCode=1;
                }else if(theNewText==""){
                    jjbox.setError("不能为空",false);
                    jjerrorCode=-1;
                }else{
                    jjbox.validate();
                    jjerrorCode=0;
                }
                jjkeyword=theNewText;
            }
        });
        scbox.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                // What you want to do when text changes
                Log.d("hellotag", "onClick: "+theNewText);
                if(checkPunc(theNewText)){
                    scbox.setError("非中文或标点!",false);
                    scerrorCode=1;
                }else if(theNewText==""){
                    scbox.setError("不能为空",false);
                    scerrorCode=-1;
                }else{
                    scbox.validate();
                    scerrorCode=0;
                }
                sckeyword=theNewText;
            }
        });
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AiActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.b_create).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(worktype==0&&jjerrorCode!=0){
                    Toast.makeText(AiActivity.this,"绝句关键词错误!",Toast.LENGTH_LONG).show();
                    return;
                }
                if(worktype==1&&scerrorCode!=0){
                    Toast.makeText(AiActivity.this,"词关键词错误!",Toast.LENGTH_LONG).show();
                    return;
                }
                HashMap<String, String> params = new HashMap<>();
                if(worktype==0){
                    upload_api="http://47.103.21.70/getRecJJ";
                    params.put("keyword",jjkeyword);
                    params.put("words",""+jjtype);
                }else{
                    upload_api="http://47.103.21.70/getRecSC";
                    params.put("keyword",sckeyword);
                    params.put("type",""+sctype);
                }
                try {
                    generatePoem(params);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void generatePoem(HashMap<String,String> params) throws IOException {
        OkHttp3Util.doPost(upload_api, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("+++", "onFailure: " + e.getMessage());
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (response.isSuccessful()) {
                    Intent intent0=new Intent(AiActivity.this, PoemActivity.class);
                    String s=response.body().string();
                    intent0.putExtra("response",s);
                    startActivity(intent0);
                }else{
                    Looper.prepare();
                    Toast.makeText(AiActivity.this, "服务器无响应!请稍后再试!", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }});
    }

    public boolean checkAll(String s) {
        boolean b = false;

        String tmp = s;
        tmp = tmp.replaceAll("\\p{P}", "");
        tmp = tmp.replaceAll("[a-zA-Z]", "");
        tmp = tmp.replaceAll("\\p{N}", "");
        tmp=tmp.replaceAll("\\p{Z}","");

        if (s.length() != tmp.length()) {
            b = true;
        }
        return b;
    }
    public boolean checkPunc(String s) {
        boolean b = false;

        String tmp = s;
        tmp = tmp.replaceAll("\\p{P}", "");
        tmp = tmp.replaceAll("[a-zA-Z]", "");
        tmp = tmp.replaceAll("\\p{N}", "");
        if (s.length() != tmp.length()) {
            b = true;
        }
        return b;
    }
}
