package br.feevale.appameacasambientaisfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    public static final String AMEACAAMBIENTAL_KEY = "ameacasAmbientais";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference root = database.getReference();
    private DatabaseReference ameacas = root.child(AMEACAAMBIENTAL_KEY);
    private ListView listaAmeacas;
    private FirebaseListAdapter<AmeacasAmbientais> adapter;
    private Button btNovaAmeaca;
    private int idSelecaoExclusao;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btNovaAmeaca = findViewById(R.id.btNovaAmeaca);
        listaAmeacas = findViewById(R.id.listAmeacas);

        adapter = new FirebaseListAdapter<AmeacasAmbientais>(this, AmeacasAmbientais.class, R.layout.ameacas_ambientais_item, ameacas) {
            @Override
            protected void populateView(@NonNull View v, @NonNull AmeacasAmbientais model, int position) {
                TextView txtDescricao = v.findViewById(R.id.lbDescricao);
                TextView txtData = v.findViewById(R.id.lbData);
                ImageView imagem = v.findViewById(R.id.ImageItem);
                txtDescricao.setText(model.getDescricao());
                txtData.setText(model.getData());
                if (model.getImagem() != null) {
                    byte imagemData[] = Base64.decode(model.getImagem(), Base64.DEFAULT);
                    Bitmap img = BitmapFactory.decodeByteArray(imagemData,  0, imagemData.length);
                    imagem.setImageBitmap(img);
                }
            }
        };

        listaAmeacas.setAdapter(adapter);

        listaAmeacas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseReference item = adapter.getRef(i);
                Intent it = new Intent(MainActivity.this, AmeacasAmbientaisEdit.class);
                it.putExtra("KEY", item.getKey());
                it.putExtra("AME", adapter.getItem(i));
                startActivity(it);
            }
        });

        listaAmeacas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                idSelecaoExclusao = i;
                confirmaExclusao();
                return false;
            }
        });
    }

    public void incluirAmeaca(View v) {
        Intent it = new Intent(MainActivity.this, AmeacasAmbientaisAdd.class);
        startActivity(it);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void confirmaExclusao() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Aviso de exclusão!!");
        dialog.setIcon(android.R.drawable.ic_menu_delete);
        dialog.setMessage("Deseja excluir o registro?");
        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference item = adapter.getRef(idSelecaoExclusao);
                item.removeValue();
                adapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(), "Registro excluído com sucesso!", Toast.LENGTH_LONG).show();
            }
        });
        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }
}