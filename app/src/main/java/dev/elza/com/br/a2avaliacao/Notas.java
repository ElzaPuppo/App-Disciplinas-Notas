package dev.elza.com.br.a2avaliacao;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Notas extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SQLiteDatabase bd;
    private Spinner sNome;
    private String text = "";
    private EditText etNota;
    private TextView tvListar;
    private AlertDialog excluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        bd = openOrCreateDatabase("banco", Context.MODE_PRIVATE, null);
        sNome = (Spinner) findViewById(R.id.sNome);
        etNota = (EditText) findViewById(R.id.etNota);
        ArrayList<String> dadosSpinner =  buscarSpinner();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, dadosSpinner);
        sNome.setAdapter(adapter);
        sNome.setOnItemSelectedListener(this);
        tvListar = (TextView) findViewById(R.id.tvListar);

        String lista = adicionarDisciplinas();
        tvListar.setText(lista);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText( adapterView.getContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public ArrayList<String> buscarSpinner(){

        ArrayList<String> dados = new ArrayList<>();
        Cursor registros = bd.query("Disciplinas", new String[]{"nome"}, null, null, null, null, null);
        while (registros.moveToNext()) {
            dados.add(registros.getString(registros.getColumnIndex("nome")));
        }
        return dados;
    }

    public void btCadastrarOnClick(View view) {
        String Disciplina = text;
        String Nota = etNota.getText().toString();
        String cod="";
        String strSQL;
        if (!"".equals(Nota)) {
            double nota = Double.parseDouble(Nota);
            if (nota > 10) {
                Toast.makeText (this, "Nota inválida!", Toast.LENGTH_LONG).show();
                etNota.setText("");
            } else {
                Cursor registros = bd.query("Disciplinas", new String[]{"cod", "nome", "nota"}, null, null, null, null, null);
                while (registros.moveToNext()) {
                    if ((registros.getString(registros.getColumnIndex("nome"))).equals(Disciplina)) {
                        cod = (registros.getString(registros.getColumnIndex("cod")));
                    }
                }
                strSQL = "UPDATE Disciplinas SET nota = " + Nota + " WHERE cod = " + cod;
                bd.execSQL(strSQL);
                Toast.makeText(this, "Nota cadastrada com sucesso!", Toast.LENGTH_LONG).show();
                String lista = adicionarDisciplinas();
                tvListar.setText(lista);
            }
        } else {
            Toast.makeText (this, "Necessário incluir uma nota!", Toast.LENGTH_LONG).show();
        }

    }

    public void btVoltarOnClick(View view) {
        startActivity(new Intent(Notas.this, MainActivity.class));
    }

    public String adicionarDisciplinas(){
        String msg="";
        String nota="";
        Cursor registros = bd.query("Disciplinas", new String[]{"nome", "nota"}, null, null, null, null, null);
        while (registros.moveToNext()) {
            nota = (registros.getString(registros.getColumnIndex("nota")));
            if ( (Double.parseDouble(nota) ) < 0 ) {
                msg += registros.getString(registros.getColumnIndex("nome")) + " :: S/N \n";
            } else{
                msg += registros.getString(registros.getColumnIndex("nome")) + " :: " + nota + "\n";
            }
        }
        return msg;
    }

    public void btLimparOnClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Excluir Notas");
        builder.setMessage("Realmente deseja excluir todas as notas cadastradas?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                bd.execSQL("UPDATE Disciplinas SET nota =  -1");
                Toast.makeText(Notas.this, "Notas excluídas com sucesso!", Toast.LENGTH_SHORT).show();
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
}