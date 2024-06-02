package com.nubsvd.mysqlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    ListView myListView;
    MainListAdapter sc_adapter;
    MyOpenHelper myOpenHelper = null;

    //起動時
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        //リストビュー
        myListView = findViewById(R.id.Listview);

        //db
        myOpenHelper = new MyOpenHelper(this);
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        //select
        Cursor c = db.rawQuery("select * from mypasstb",null);

        //adapterの準備
        //表示するカラム名
        String[] from = {"_id","name"};

        //バインドするViewリスト
        int[] to = {android.R.id.text1,android.R.id.text2};


        //adapterの生成
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,c,from,to,0);


        //バインドして表示
        myListView.setAdapter(adapter);

        //リストビューをタップしたときの各行のデータを取得
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                //各要素の取得
                //_id
                String s1 = ((TextView)view.findViewById(android.R.id.text1)).getText().toString();
                //name
//                String s2 = ((TextView)view.findViewById(android.R.id.text2)).getText().toString();

                //参照、更新へ
                Intent intent = new Intent(getApplication(),Setting.class);

                //モード指定　_idを渡す
                intent.putExtra("KBN",s1);

                //行く
                startActivity(intent);

            }
        });

    }

    //リターン時
    @Override
    protected void onRestart(){
        super.onRestart();
        reload();
    }


    //登録画面を押した時は新規登録へ画面を推移
    public void Register(View view){
        Intent intent = new Intent(getApplication(), Setting.class);

        //モード指定　からは更新
        intent.putExtra("KBN","");

        //行く
        startActivity(intent);
    }

    public void reload(){
        Intent intent = getIntent();
        overridePendingTransition(0,0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
    }

}

