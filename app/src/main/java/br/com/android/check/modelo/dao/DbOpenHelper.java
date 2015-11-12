package br.com.android.check.modelo.dao;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

public class DbOpenHelper extends SQLiteOpenHelper {

    public static final int VERSION = 7;
    public static final String DATABASE = "banco", tbUsuario = "usuario", tbVendedor = "vendedor",
            tbVisita = "visita";
    private Context ctx;

    public DbOpenHelper(Context context) {
        super(context, DATABASE, null, VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ddl = "create table if not exists " + tbUsuario + "(_id integer primary key autoincrement, "
                + "login text, senha text, perfil text)";
        db.execSQL(ddl);

        ddl = "create table if not exists " + tbVendedor
                + "(_id integer primary key autoincrement, nome text, telefone text)";
        db.execSQL(ddl);

        ddl = "create table if not exists " + tbVisita
                + "(_id integer primary key autoincrement, cliente text, endereco text, telefone text, data text, "
                + "hora text, idVendedor integer, situacao integer, "
                + "foreign key(idVendedor) references vendedor(_id))";
        db.execSQL(ddl);

        Log.i(DATABASE, "create ******* rolou");
    }

    // verifica se a base ja existe
    public boolean doesDatabaseExist(ContextWrapper context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String ddl = "drop table if exists " + tbUsuario;
        db.execSQL(ddl);
        ddl = "drop table if exists " + tbVendedor;
        db.execSQL(ddl);
        ddl = "drop table if exists " + tbVisita;
        db.execSQL(ddl);
        onCreate(db);
        Log.i(DATABASE, "upgrade ******* rolou");
    }
}