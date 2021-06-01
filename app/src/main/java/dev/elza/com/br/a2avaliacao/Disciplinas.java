package dev.elza.com.br.a2avaliacao;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Disciplinas extends AppCompatActivity {

    private SQLiteDatabase bd;
    private EditText etDisciplina;
    private TextView tvListar;
    private AlertDialog excluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplinas);
        bd = openOrCreateDatabase("banco", Context.MODE_PRIVATE, null);
        bd.execSQL("CREATE TABLE IF NOT EXISTS Disciplinas(cod integer PRIMARY KEY AUTOINCREMENT, nome TEXT, nota NUMERIC)");
        etDisciplina = (EditText) findViewById(R.id.etDisciplina);
        tvListar = (TextView) findViewById(R.id.tvListar);

        String lista = adicionarDisciplinas();
        tvListar.setText(lista);

    }

    public void btCadastrarOnClick(View view) {
        String Disciplina = etDisciplina.getText().toString();
        String cod="";
        String strSQL;
        Cursor registros = bd.query("Disciplinas", new String[]{"cod", "nome", "nota"},null, null, null, null, null);
        while (registros.moveToNext()) {
            if ((registros.getString(registros.getColumnIndex("nome"))).equals(Disciplina) ) {
                cod = "existe";
            }
        }
        if (cod !=""){
            Toast.makeText (this, "Disciplina já existe!", Toast.LENGTH_LONG).show();
            etDisciplina.setText("");
        } else {
            strSQL = "INSERT INTO Disciplinas ('nome', 'nota') VALUES ('"+Disciplina+"', -1)";
            bd.execSQL(strSQL);
            Toast.makeText (this, "Disciplina cadastrada com sucesso!", Toast.LENGTH_LONG).show();
            String lista = adicionarDisciplinas();
            tvListar.setText(lista);
        }

    }



    public void btVoltarOnClick(View view) {
        startActivity(new Intent(Disciplinas.this, MainActivity.class));
    }
    public void btLimparOnClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Excluir Disciplinas");
        builder.setMessage("Realmente deseja excluir todas as disciplinas cadastradas?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                bd.execSQL("DELETE FROM Disciplinas");
                Toast.makeText(Disciplinas.this, "Disciplinas excluídas com sucesso!", Toast.LENGTH_SHORT).show();
                String lista = adicionarDisciplinas();
                tvListar.setText(lista);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        excluir = builder.create();
        excluir.show();
    }

    public String adicionarDisciplinas(){
        String msg="";
        Cursor registros = bd.query("Disciplinas", new String[]{"nome"}, null, null, null, null, null);
        while (registros.moveToNext()) {
                msg += registros.getString(registros.getColumnIndex("nome")) + " \n";
        }
        return msg;
    }


}