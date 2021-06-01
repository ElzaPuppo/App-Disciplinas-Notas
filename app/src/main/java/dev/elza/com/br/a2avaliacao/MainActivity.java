package dev.elza.com.br.a2avaliacao;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {
    private Button lvCadastrarD;
    private Button lvCadastrarN;
    private SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bd = openOrCreateDatabase("banco", Context.MODE_PRIVATE, null);
        bd.execSQL("CREATE TABLE IF NOT EXISTS Disciplinas(cod integer PRIMARY KEY AUTOINCREMENT, nome TEXT, nota integer)");
    }

    public void btCadastrarDOnClick(View view) {
        startActivity(new Intent(MainActivity.this, Disciplinas.class));
    }

    public void btCadastrarNOnClick(View view) {
        startActivity(new Intent(MainActivity.this, Notas.class));
    }
    public void btMediaNOnClick(View view) {
        double Media = 0;
        double Total = 0;
        String cod = "";
        String nota = "";
        double Nota= 0;
        double total = 0;
        NumberFormat duascasas = NumberFormat.getInstance();
        duascasas.setMaximumFractionDigits(2);

        Cursor registros = bd.query("Disciplinas", new String[]{"cod", "nota"},null, null, null, null, null);
        while (registros.moveToNext()) {
            cod = registros.getString(registros.getColumnIndex("cod"));
            nota = registros.getString(registros.getColumnIndex("nota"));
            if (!"".equals(nota)){
                Nota=Double.parseDouble(nota);
            }
            if (Nota > -1) {
                Total =  Nota + Total;
                total ++;
            }
        }
        if (total != 0){
            Media = Total/total;
            Media = Double.valueOf(duascasas.format(Media));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Média");
            builder.setMessage("A Média das Disciplinas é " + Media);
            builder.create().show();
        } else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Média");
            builder.setMessage("Não há notas cadastradas");
            builder.create().show();
        }



    }
}