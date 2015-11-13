package br.com.android.check.modelo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import br.com.android.check.modelo.bean.Vendedor;

public class VendedorDAO extends SQLiteOpenHelper {

    public VendedorDAO(Context ctx) {
        super(ctx, DbOpenHelper.DATABASE, null, DbOpenHelper.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void inserirVendedor(String nome, String telefone, String senha) {
        ContentValues values = new ContentValues();

        values.put("nome", nome);
        values.put("telefone", telefone);

        getWritableDatabase().insert(DbOpenHelper.tbVendedor, null, values);

        ContentValues cv = new ContentValues();

        cv.put("login", nome);
        cv.put("senha", senha);
        cv.put("perfil", "vendedor");

        getWritableDatabase().insert(DbOpenHelper.tbUsuario, null, cv);
    }

    public int retornaId(String nome) {
        Cursor cursor = getReadableDatabase().query(DbOpenHelper.tbVendedor, null, "nome = ?",
                new String[]{nome}, null, null, null);

        try {
            cursor.moveToFirst();

            return cursor.getInt(0);
        } catch (Exception e) {
            Log.e(this.toString(), e.getMessage());
            return 0;
        } finally {
            cursor.close();
        }
    }

    public Vendedor getVendedor(int idVendedor) {
        String select = "select nome from vendedor where _id = " + idVendedor;

        Cursor cursor = getReadableDatabase().rawQuery(select, null);

        try {
            cursor.moveToFirst();

            Vendedor vendedor = new Vendedor();
            vendedor.setId(idVendedor);
            vendedor.setNome(cursor.getString(0));

            return vendedor;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            cursor.close();
        }
    }

    // lista vendedores
    public ArrayList<Vendedor> listaVendedores() {
        Cursor cursor = getReadableDatabase().query(DbOpenHelper.tbVendedor, null, null, new String[]{}, null, null, null);

        ArrayList<Vendedor> lista = new ArrayList<Vendedor>();

        try {
            while (cursor.moveToNext()) {
                Vendedor vendedor = new Vendedor();

                vendedor.setId(cursor.getInt(0));
                vendedor.setNome(cursor.getString(1));
                vendedor.setTelefone(cursor.getString(2));

                lista.add(vendedor);
            }
            return lista;
        } catch (Exception e) {
            Log.e(this.toString(), e.getMessage());
            return null;
        } finally {
            cursor.close();
        }
    }
}