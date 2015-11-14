package br.com.android.check.modelo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

import br.com.android.check.Datas;
import br.com.android.check.modelo.bean.Usuario;
import br.com.android.check.modelo.bean.Vendedor;
import br.com.android.check.modelo.bean.Visita;

/**
 * Created by masasp29 on 20/10/15.
 */
public class VisitaDAO extends SQLiteOpenHelper {

    private Context ctx;

    public VisitaDAO(Context context) {
        super(context, DbOpenHelper.DATABASE, null, DbOpenHelper.VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void inserirVisita(String cliente, String endereco, String telefone, String data, String hora, int idVendedor) {
        ContentValues values = new ContentValues();

        values.put("cliente", cliente);
        values.put("endereco", endereco);
        values.put("telefone", telefone);
        values.put("data", data);
        values.put("hora", hora);
        values.put("idVendedor", idVendedor);
        values.put("situacao", 0); // 0 para visita nao realizada

        getWritableDatabase().insert(DbOpenHelper.tbVisita, null, values);
    }

    public void visitaRealizada(Visita visita) {
        String update = "update " + DbOpenHelper.tbVisita + " set situacao =" + visita.getSituacao() +
                " where _id = " + visita.getId();

        getWritableDatabase().execSQL(update);
    }

    public ArrayList<Visita> listar(Usuario user) {
        ArrayList<Visita> lista = new ArrayList<Visita>();

        String select = "select vis._id, vis.cliente, vis.endereco, vis.telefone, vis.data, vis.hora, " +
                "vis.idVendedor, vis.situacao from visita vis, vendedor ved where"; //situacao = 0";

        if (user.getPerfil().equals("vendedor")) {
            select += " ved.nome = '" + user.getLogin() + "' and";
        }

        select += " vis.idVendedor = ved._id";

        Cursor cursor = getReadableDatabase().rawQuery(select, null);

        try {
            while (cursor.moveToNext()) {
                Visita visita = new Visita();
                visita.setId(cursor.getInt(0));
                visita.setCliente(cursor.getString(1));
                visita.setEndereco(cursor.getString(2));
                visita.setTelefone(cursor.getString(3));
                visita.setSituacao(cursor.getInt(7));

                Datas util = new Datas();
                Date data = util.convertStringEmData(cursor.getString(4), "dd/MM/yyyy");

                visita.setData(data);
                visita.setHora(cursor.getString(5));

                Vendedor vendedor = new VendedorDAO(ctx).getVendedor(cursor.getInt(6));
                visita.setVendedor(vendedor);
                lista.add(visita);
            }
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            cursor.close();
        }
    }

}