package com.example.davidsantos.listadetarefas.db;

import android.provider.BaseColumns;

/**
 * Created by david.santos on 30/01/2018.
 */

public class TaskContract {

    public static final String DB_NAME = "david.santos.listadetarefas";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "TAREFAS";

    public class Columns{
        public static final String TAREFA = "tarefa";
        public static final String PRAZO = "prazo";
        public static final String _ID = BaseColumns._ID;
    }

}
