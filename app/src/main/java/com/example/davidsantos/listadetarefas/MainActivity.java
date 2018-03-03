package com.example.davidsantos.listadetarefas;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davidsantos.listadetarefas.db.TaskContract;
import com.example.davidsantos.listadetarefas.db.TaskDBHelper;

public class MainActivity extends AppCompatActivity {

    private TaskDBHelper helper;

    private ListView listaTarefas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new TaskDBHelper(this);

        listaTarefas = findViewById(R.id.listaTarefas);

        listaTarefas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {

                final TextView textoTarefa = view.findViewById(R.id.textoTarefa);
                final String tarefa = textoTarefa.getText().toString();

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage("Tem certeza que deseja excluir a tarefa '"+tarefa+"'?")
                        .setTitle("Atenção");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {





                        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",TaskContract.TABLE, TaskContract.Columns.TAREFA,tarefa);

                        SQLiteDatabase db = helper.getWritableDatabase();
                        db.execSQL(sql);

                        Toast.makeText(MainActivity.this, "Tarefa excluida com sucesso!", Toast.LENGTH_SHORT).show();

                        updateUI();

                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                AlertDialog dialog;
                builder.create().show();


                return false;
            }
        });

        final Button addTarefa = findViewById(R.id.addTarefa);
        addTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTarefa();
            }
        });
        updateUI();
    }

    private void addTarefa(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View alertView = getLayoutInflater().inflate(R.layout.alert_tarefa, null);



        final EditText textoTarefa = alertView.findViewById(R.id.textoTarefa);
        final EditText textoPrazo = alertView.findViewById(R.id.textoPrazo);

        builder.setView(alertView);
        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db = helper.getWritableDatabase();

                ContentValues values = new ContentValues();

                values.clear();
                values.put(TaskContract.Columns.TAREFA, textoTarefa.getText().toString());
                values.put(TaskContract.Columns.PRAZO, textoPrazo.getText().toString());

                db.insertWithOnConflict(TaskContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);

                updateUI();

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    private void updateUI(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor  = db.query(TaskContract.TABLE, new String[]{TaskContract.Columns._ID,
                TaskContract.Columns.TAREFA, TaskContract.Columns.PRAZO},null, null, null, null, null);

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                R.layout.celula_tarefa,
                cursor, new String[]{TaskContract.Columns.TAREFA,
                TaskContract.Columns.PRAZO},
                new int[]{R.id.textoTarefa, R.id.textoPrazo}, 0);

        listaTarefas.setAdapter(listAdapter);

    }

}
