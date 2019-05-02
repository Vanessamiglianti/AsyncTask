package com.example.asynctask;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.vanessa.interfaces.DonwloadComplete;

import java.io.File;

public class MainActivity extends AppCompatActivity  implements DonwloadComplete{

    final String TAG = "MainActivity";

    EditText etT= null;
    Button bt= null;
    public static ImageView im= null;
    BackTask myBackTask = null;
    Context context= null;
    String directory= "";
    DonwloadComplete dc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context= getApplicationContext();

        //creo la nnuova class in superclass AsyncTask
        etT= findViewById(R.id.etA);
        bt= findViewById(R.id.btA);
        im=findViewById(R.id.image);

        dc = this;

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directory= createDirectory(); //creo la directory

                myBackTask= new BackTask(context, directory, dc); //ho passato l'stanza della mainactivity dentro il backtask

                //devo recuperare url
                String insertURK = etT.getText().toString();
                myBackTask.execute(insertURK); //comando che fa partire tutta la catena asynctask
            }
        });
    }

    private String createDirectory () {

        //dovrebbe creare la directory e restiruire il path della directory
        String myDir= Environment.getExternalStorageDirectory() + "/" + "AsyncTask" ;//path della memoria interna del telefono
        File f= new File(myDir);

        if(!f.exists()) {
            Log.i(TAG,"Cartella non esistente");
            if( f.mkdir()){
                Log.i(TAG, "Cartella creata correttamente");
            }
        }
        return myDir;
    }

    @Override
    public void onDownloadCompleted (Bitmap bitmap) {

        im.setImageBitmap(bitmap);

    }



}
