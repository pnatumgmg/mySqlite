package com.nubsvd.mysqlite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Setting extends AppCompatActivity {

    private  MyOpenHelper helper;
    String kbn = "";

    String toastMessage = "登録しました。戻るを押してください";
    String toastMessage2 = "登録するものがありません";
    String toastMessage3 = "更新しました。戻るを押してください";
    String toastMessage4 = "登録するものがありません";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        //DBを作成
        helper = new MyOpenHelper(getApplicationContext());

        //データを受け取る
        Intent intent = getIntent();
        String KBN = ((Intent) intent).getStringExtra("KBN");

        Button button = findViewById(R.id.button2);
        View view = findViewById(R.id.Layout);

        if(KBN.length() != 0){
            //参照
            kbn = KBN;

            //ボタンのテキストを更新
            button.setText("登録");

            //背景色を変更
            view.setBackgroundColor(Color.YELLOW);

            //既存データ参照
            readDate(KBN);


        }else{
            //新規
            kbn = "登録";

            //ボタンのテキストを登録
            button.setText("登録");

            //背景の色を変更
            view.setBackgroundColor(Color.CYAN);

        }
    }

    /**
     *データを参照する
     * @param read
     */
    public void readDate(String read){
        SQLiteDatabase db = helper.getWritableDatabase();

        EditText text1 = findViewById(R.id.editTextText);
        EditText text2 = findViewById(R.id.editTextText2);
        EditText text3 = findViewById(R.id.editTextText3);

        Cursor cursor = db.query(
                "myPasstb",
                new String[]{"name","ID","pass" },
                "_ID = ?",
                new String[]{read},
                null,null,null
        );
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount(); i++){
            text1.setText(cursor.getString(0));
            text2.setText(cursor.getString(1));
            text3.setText(cursor.getString(2));
        }

        cursor.close();

    }

    public void saveData(View view){
        SQLiteDatabase db = helper.getWritableDatabase();

        EditText txt1 = findViewById(R.id.editTextText);
        EditText txt2 = findViewById(R.id.editTextText2);
        EditText txt3 = findViewById(R.id.editTextText3);

        String name = txt1.getText().toString();
        String ID = txt2.getText().toString();
        String PS = txt3.getText().toString();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("ID", ID);
        values.put("pass", PS);

        //ボタンが登録の場合
        if(kbn== "登録"){
            if(name.length() != 0){
                //新規登録
                db.insert("myPasstb", null, values);
                //トーストを表示
                toastMake(toastMessage, 0, +400);
            }else{
                toastMake(toastMessage2, 0, +400);
            }
            //ボタンが更新の場合
        }else {
            if(name.length() != 0){
                //更新
                UPData(kbn);
                //トーストを表示
                toastMake(toastMessage3, 0, +400);
            }else{
                //トーストを表示
                toastMake(toastMessage4, 0, +400);
            }

        }

    }

    /**
     * データの更新
     * @param read
     */
    public void UPData(String read){
        SQLiteDatabase db = helper.getReadableDatabase();

        EditText txt1 = findViewById(R.id.editTextText);
        EditText txt2 = findViewById(R.id.editTextText2);
        EditText txt3 = findViewById(R.id.editTextText3);

        String name = txt1.getText().toString();
        String ID = txt2.getText().toString();
        String PS = txt3.getText().toString();

        ContentValues upvalue = new ContentValues();
        upvalue.put("name",name);
        upvalue.put("ID",ID);
        upvalue.put("pass",PS);

        db.update("myPasstb",upvalue,"_id=?",new String[]{read});

    }

    private void toastMake(String message, int x, int y){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        //位置調整
        toast.setGravity(Gravity.CENTER, x, y);
        toast.show();
    }


    public void onClose(View view){
        finish();
    }
}