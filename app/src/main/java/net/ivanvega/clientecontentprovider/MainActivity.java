package net.ivanvega.clientecontentprovider;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView cajaMostrar;
    EditText txtNombre;
    EditText txtApellido;
    EditText txtID;
    String mostrarUsuarios="";

    private void consultarContentProvider(){
        Cursor cursor = getContentResolver().query(
                UsuarioProviderContract.CONTENT_URI,
                UsuarioProviderContract.COLUMNS,
                null,null,null
        );
        mostrarUsuarios="";
        if(cursor!=null) {

            while (cursor.moveToNext()) {
                Log.d("CPCliente",
                        cursor.getInt(0) + " - " + cursor.getString(1)

                );
                mostrarUsuarios=mostrarUsuarios.concat(cursor.getInt(0) + " - " + cursor.getString(1)
                        +" "+cursor.getString(2)+"\n");
            }
        }else{
            Log.d("USUARIOCONTENTPROVIDER",
                    "NO DEVUELVE"
            );
        }
        cajaMostrar.setText(mostrarUsuarios);

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cajaMostrar= findViewById(R.id.txtMostrar);
        txtNombre=findViewById(R.id.txtNombre);
        txtApellido=findViewById(R.id.txtApellido);
        txtID=findViewById(R.id.txtID);


        Cursor c = getContentResolver().query(UserDictionary.Words.CONTENT_URI,
                new String[] {UserDictionary.Words.WORD,
                        UserDictionary.Words.LOCALE},
                null,null,null
        );
        if(c!=null) {
            while (c.moveToNext()) {
                Log.d("DICCIONARIOUSARUI",
                        c.getString(0) + " - " + c.getString(1)
                );
            }
        }

        consultarContentProvider();

        findViewById(R.id.btnInsert).setOnClickListener(
                view -> {

                    ContentValues cv = new ContentValues();
                    cv.put(UsuarioProviderContract.FIRSTNAME_COLUMN,txtNombre.getText().toString() );
                    cv.put(UsuarioProviderContract.LASTNAME_COLUMN, txtApellido.getText().toString());

                    Uri uriInsert = getContentResolver().insert(
                            UsuarioProviderContract.CONTENT_URI,
                            cv
                    );
                    Log.d("CPCliente", uriInsert.toString() );
                    Toast.makeText(this, "Usuario insertado: \n"+
                            uriInsert.getLastPathSegment(), Toast.LENGTH_SHORT).show();

                }
        );


        findViewById(R.id.btnUpdate).setOnClickListener(
                view -> {

                    ContentValues cv = new ContentValues();
                    cv.put(UsuarioProviderContract.FIRSTNAME_COLUMN, txtNombre.getText().toString() );
                    cv.put(UsuarioProviderContract.LASTNAME_COLUMN, txtApellido.getText().toString() );

                    int elemtosAfectados = getContentResolver().update(
                            Uri.withAppendedPath(UsuarioProviderContract.CONTENT_URI, txtID.getText().toString() )   ,
                            cv,
                            null, null
                    );
                    Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show();
                }
        );

        findViewById(R.id.btnConsultar).setOnClickListener(v -> {
            consultarContentProvider();
        });


        findViewById(R.id.btnDelete).setOnClickListener(v ->{
            int elemtosAfectados = getContentResolver().delete(
                    Uri.withAppendedPath(UsuarioProviderContract.CONTENT_URI,txtID.getText().toString() ),
                    null, null);
            Log.d("CPCliente", "Elementos afectados eliminados: " +elemtosAfectados );
            Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();

        });

        findViewById(R.id.btnConsultarApellido).setOnClickListener(v->{
            Cursor cursor = getContentResolver().query(
                    Uri.withAppendedPath(UsuarioProviderContract.CONTENT_URI,txtApellido.getText().toString() ),
                    UsuarioProviderContract.COLUMNS,
                    null,null,null
            );
            if(cursor!=null) {

                String mostrarUsuarios="";
                while (cursor.moveToNext()) {
                    Log.d("CPCliente",
                            cursor.getInt(0) + " - " + cursor.getString(1)
                    );
                    mostrarUsuarios=mostrarUsuarios.concat(cursor.getInt(0) + " - " + cursor.getString(1)
                            +" "+cursor.getString(2)+"\n");
                    cajaMostrar.setText(mostrarUsuarios);
                }
            }else{
                Log.d("USUARIOCONTENTPROVIDER", "NO DEVUELVE");
                Toast.makeText(this,"No existe el usuario", Toast.LENGTH_SHORT ).show();
            }

        });


    }
}