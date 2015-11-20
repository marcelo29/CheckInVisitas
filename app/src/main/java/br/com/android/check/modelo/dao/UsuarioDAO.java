package br.com.android.check.modelo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import br.com.android.check.modelo.bean.Usuario;

public class UsuarioDAO extends SQLiteOpenHelper {

    public UsuarioDAO(Context context) {
        super(context, DbOpenHelper.DATABASE, null, DbOpenHelper.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insereUsuario(String login, String senha, String perfil) {
        ContentValues valores = new ContentValues();

        valores.put("login", login);
        valores.put("senha", senha);
        valores.put("perfil", DbOpenHelper.PERFIL_ADM);

        getWritableDatabase().insert(DbOpenHelper.tbUsuario, null, valores);
    }

    public boolean Logar(String usuario, String senha) {
        Cursor cursor = getReadableDatabase().query(DbOpenHelper.tbUsuario, null, "login = ? and senha = ?",
                new String[]{usuario, senha}, null, null, null);

        try {
            return cursor.moveToFirst();
        } catch (Exception e) {
            Log.e(DbOpenHelper.tbUsuario, e.getMessage());
        } finally {
            cursor.close();
        }

        return false;
    }

    public Usuario usuarioLogado(String usuarioNome) {
        Cursor cursor = getReadableDatabase().query(DbOpenHelper.tbUsuario, null, "login = ?",
                new String[]{usuarioNome}, null, null, null);
        try {
            cursor.moveToFirst();
            Usuario usuario = new Usuario();
            usuario.setId(cursor.getInt(0));
            usuario.setLogin(cursor.getString(1));
            usuario.setPerfil(cursor.getString(3));
            return usuario;
        } catch (Exception e) {
            Log.e(DbOpenHelper.tbUsuario, e.getMessage());
            return null;
        } finally {
            cursor.close();
        }
    }
}