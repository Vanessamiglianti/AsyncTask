package com.example.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.vanessa.interfaces.DonwloadComplete;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// aggiungo <> e devo mettere 3 tipi di dato
//il primo tipo di dato è l'input che la asynctask prende verso l'esterno
// ed è anche linput della nostra do in back ground
//il secondo tipo è l'argomento che diamo in ingresso al onProgressUpdate
//il terzo tipo è il tipo di ritorno della do in background
//è anche il tipo di input della onPostExecute

public class BackTask extends AsyncTask<String, Integer, String> {

    final String TAG = "BackTask";

    String storeDir= "";
    ProgressDialog pd = null;
    Context context = null; //chi rappresenta l'interfaccia grafica

    DonwloadComplete dc= null; //per richiamare i metodi delle interfacce, oggetto di tipo interfaccia

    public BackTask(Context context, String directory, DonwloadComplete dc) {  //costruttore della nostra classe

        this.context= context;
        storeDir= directory;

        pd= new ProgressDialog(this.context);
        pd.setTitle("Downloading file...");
        pd.setMax(100);
        this.dc= dc;
    }

    //creo la onpreexecute in funzione ai 3 tipi di dato
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd.show(); //visualizzo la barra del progresso se lo facessi nel do in back l'app andrebbe in crash

    }

    //creo la do in background in funzione ai 3 tipi di dato
    @Override
    protected String doInBackground(String... strings) {

        URL url;
        int count= 0; //tiene traccia di quanto sto scaricando

        try {   //gestisco l'eccezione
            url = new URL(strings[0]); //creo oggetto url
            File f = new File (storeDir);
            if (f.exists()) {

                HttpURLConnection con= (HttpURLConnection) url.openConnection();  //apro la connessione, va castata in tipo httpurlconn
                InputStream  is= con.getInputStream();  //connettore della connessione, legge i dati, punto dal quale vado a leggere

                String fullPath= url.getPath(); //così se nella new aggiunge qualcosa al path recupero tutto
                String fileName = fullPath.substring(fullPath.lastIndexOf("/")+1); //restituisce l'indice dell'ulitmo slash
                //estrapola da l'url il nome del file
                String fullName= storeDir + "/" + fileName; //concatena il path alla memoria del telefono e ci aggancia il nome del file

                FileOutputStream fos= new FileOutputStream(fullName); // leggo da input, passo all'uotput

                int fileLenght = con.getContentLength(); //sappiamo i byte del file che stiamo scaricando
                byte data[]= new byte[1024]; //array di byte che mi contiene cosa vado a leggere
                long total=0; //contiene quello che sto leggendo

                //ciclo di download: leggo i dati, posteggio in data e riscrivo
                while ((count = is.read(data)) != -1) {
                    //legge dall'inputstream e legge quanto è data cioè 1024 byte, restituisce -1 quando ha finito di leggere

                    total += count;

                    //aggiornare GUI

                    fos.write(data);
                }

                is.close();
                fos.flush();
                fos.close();

                return fullName;  // me la ritrovo nella stringa s della onPostExecute

            }
            else
                Log.i(TAG, "Error! Cannot find the folder " + storeDir);
        }
        catch (IOException e) {
            e.printStackTrace(); //stampami lo stack del errore
        }
        return null;
    }

    //creo la onpostexecute in funzione ai 3 tipi di dato
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        //visualizzo l'immagine
        Bitmap myImage= BitmapFactory.decodeFile(s); //come la visualizzo nella main activity?
        this.dc.onDownloadCompleted(myImage);
        //MainActivity.im.setImageBitmap(myImage);

    }
}
