package com.example.frutiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText et_nombre;
    private ImageView iv_personaje;
    private MediaPlayer mp;
    private TextView tv_bestScore;

    //*1*Se crea una variable int para usarlo a la hora de mostrar personajes aleatorios cuando se abra la app
    int num_aleatorio = (int) (Math.random() * 10); //El segundo int es un Casting, porque Math() retorna un tipo Double.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        et_nombre = (EditText)findViewById(R.id.txt_nombre);
        iv_personaje = (ImageView)findViewById(R.id.imageView_Personaje);
        tv_bestScore = (TextView)findViewById(R.id.textView_BestScore);

        //*1* Se muestra una imagen aleatoria cada que inicia la app, utilizando la variable num_aleatorio
        int id;
        if(num_aleatorio == 0 || num_aleatorio == 10){
            id = getResources().getIdentifier("mango", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        } else if(num_aleatorio == 1 || num_aleatorio == 9){
            id = getResources().getIdentifier("fresa", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        } else if(num_aleatorio == 2 || num_aleatorio == 8){
            id = getResources().getIdentifier("manzana", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        } else if(num_aleatorio == 3 || num_aleatorio == 7){
            id = getResources().getIdentifier("sandia", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        } else if(num_aleatorio == 4 || num_aleatorio == 5 || num_aleatorio == 6){
            id = getResources().getIdentifier("uva", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase(); //Abrir base de datos creada en la otra clase.

        Cursor consulta = BD.rawQuery("select * from puntaje where score = (select max(score) from puntaje)", null);

        if(consulta.moveToFirst()){
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);
            tv_bestScore.setText("Record: " + temp_score + " de " + temp_nombre);
            BD.close();
        } else {
            BD.close();
        }

        //Al objeto mp le asignamos una cancion MediaPlayer, reproducida y en loop.
        mp = MediaPlayer.create(this, R.raw.alphabet_song);
        mp.start();
        mp.setLooping(true);
    }

    public void Jugar(View view){
        String nombre = et_nombre.getText().toString();

        if(!nombre.equals("")){
            mp.stop(); //detiene la cancion en reproduccion
            mp.release(); //libera la variable, y la deja disponible para su posterior uso
            Intent intent = new Intent(this, MainActivity2_Nivel1.class);
            intent.putExtra("jugador", nombre); //Envio este dato a la siguiente Activity
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Primero debes escribir tu nombre", Toast.LENGTH_SHORT).show();

            //Se tiene que abrir el teclado para comenzar a escribir dentro del EditText et_nombre
            et_nombre.requestFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_nombre, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override //Vamos a controlar el boton BACK
    public void onBackPressed(){
        //Necesito que al presionarse, no haga nada.
    }
}