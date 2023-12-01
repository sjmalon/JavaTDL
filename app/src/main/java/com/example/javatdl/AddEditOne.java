package com.example.javatdl;

//import static android.os.Build.VERSION_CODES.R;
import static com.example.javatdl.MyApplication.getMainActivityOn;
import static com.example.javatdl.TDLs.valueOf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AddEditOne extends AppCompatActivity {

    private static final String TAG = "TDLs App: AddEditOne ";

    Button btn_back, btn_add, btn_ok, btn_delete;
    EditText et_List, et_Items;
    int id;
    int id2;
    String item, item0;

    List<TDLs> TDLsList;
    MyApplication myApplication= (MyApplication) this.getApplication();

    SQLiteDatabase sqLiteDatabase;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_one);

        TDLsList=myApplication.getTDLsList();
        myApplication.setMainActivityOn(0);

        dataBaseHelper=new DataBaseHelper(AddEditOne.this);

        dataBaseHelper.getReadableDatabase();
        List<TDLs> everything = dataBaseHelper.getEverything();

        sqLiteDatabase=this.dataBaseHelper.getWritableDatabase();

        btn_back=findViewById(R.id.btn_back);
        btn_ok=findViewById(R.id.btn_ok);
        btn_add=findViewById(R.id.btn_add);
        et_List=findViewById(R.id.et_List);
        et_Items=findViewById(R.id.et_Items);
        btn_delete=findViewById(R.id.btn_delete);


        Intent intent=getIntent();
        id=intent.getIntExtra("id", -1);
        item=intent.getStringExtra("item");
        TDLs tdL=null;

        if (id>=1) {
            id2=id;
            //edit TDL
            for(TDLs t: everything) {
                if (t.getId()==id) {
                    tdL=t;
                }
            }

            Log.d(TAG,"tdL: " + tdL.toString());

            et_List.setText(tdL.getName());
            et_Items.setText(tdL.getItems());
        }
        else {
            //create new TDL
        }

        btn_ok.setOnClickListener((view) -> {

            if (id >= 1) {
                //update

                String et_ItemsTXT=et_Items.getText().toString();
                TDLs updateTDL = new TDLs(id, et_List.getText().toString(), et_Items.getText().toString());

                dataBaseHelper.updateItems(updateTDL);

                /*
                Boolean checkupdatedata=dataBaseHelper.updateOne(updateTDL, et_ItemsTXT);
                if(checkupdatedata=true) {
                    Toast.makeText(AddEditOne.this, "Entry Updated " + "id2= " + id2 + "id= " + id, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AddEditOne.this, "New Entry Not Updated", Toast.LENGTH_SHORT).show();
                }

                 */

                //TDLsList.set(id, updateTDL);
                //dataBaseHelper.replaceOne(sqLiteDatabase, valueOf(id), updateTDL, TDLsList);

            } else {
                //add new TDL
                //create TDLs object
                int nextId = myApplication.getNextId();
                TDLs newTDLs = new TDLs(nextId, et_List.getText().toString(), et_Items.getText().toString());


                //add the object to the global list of TDLs

                dataBaseHelper.addOne(newTDLs);
                //TDLsList.add(newTDLs);
                myApplication.setNextId(nextId++);
            }

            //go back to main activity

            Intent intent1 = new Intent(AddEditOne.this, MainActivity.class);
            startActivity(intent1);

        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(AddEditOne.this, MainActivity.class);
                startActivity(intent2);

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    TDLs tdL = null;

                    for (TDLs t : everything) {
                        if (t.getId() == id) {
                            tdL = t;
                        }
                    }

                    et_Items.setText(tdL.getItems() + myApplication.getNewItem() + "\n");
                    tdL.setItems(tdL.getItems() + myApplication.getNewItem() + "\n");

                    Toast.makeText(AddEditOne.this, "add works", Toast.LENGTH_SHORT).show();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TDLs tdLs = new TDLs(id, et_List.getText().toString(), et_Items.getText().toString());
                dataBaseHelper.deleteOne(tdLs);

                Intent intent3=new Intent(AddEditOne.this, MainActivity.class);
                startActivity(intent3);
            }
        });

        recyclerView=findViewById(R.id.lv_templates2);

        //use this setting to improve performance if you know that changes
        //in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        //use a linear layout manager
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //specify an adapter
        //mAdapter=new RecycleViewAdapter(TDLsList, this);
        mAdapter=new RecycleViewAdapter(everything, this);
        recyclerView.setAdapter(mAdapter);

    }
}