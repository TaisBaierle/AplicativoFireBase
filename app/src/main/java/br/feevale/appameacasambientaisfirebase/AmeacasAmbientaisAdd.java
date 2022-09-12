package br.feevale.appameacasambientaisfirebase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Calendar;

public class AmeacasAmbientaisAdd extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference root = database.getReference();
    private DatabaseReference ameacas = root.child(MainActivity.AMEACAAMBIENTAL_KEY);
    public static final int CHAMADA_CAMERA = 1022;
    private EditText edEndereco, edDescricao;
    private Button btData;
    private DatePickerDialog datePickerDialog;
    private Bitmap bmp;
    private ImageView imagem;
    private Boolean temImagem = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ameacas_ambientais_add);

        edEndereco = findViewById(R.id.edEnderecoNovo);
        edDescricao = findViewById(R.id.edDescricaoNova);
        btData = findViewById(R.id.btData);
        imagem = findViewById(R.id.addImagem);
        initDatePicker();

        btData.setText(pegarData());
    }

    private String pegarData() {
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = mes + 1;
        return formataDia(String.valueOf(dia)) + "/" + formataMes(String.valueOf(mes)) + "/" + ano;
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                mes = mes + 1;
                String data = criarStringData(dia, mes, ano);
                btData.setText(data);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, dateSetListener, ano, mes, dia);
    }

    private String criarStringData(int dia, int mes, int ano) {
        return formataDia(String.valueOf(dia)) + "/" + formataMes(String.valueOf(mes)) + "/" + ano;
    }

    public void addAmeaca(View v) {
        AmeacasAmbientais ameaca = new AmeacasAmbientais();
        ameaca.setEndereco(edEndereco.getText().toString());
        ameaca.setData(btData.getText().toString());
        ameaca.setDescricao(edDescricao.getText().toString());

        if (temImagem) {
            String bmpEncoded = carregarImagem();
            temImagem = false;
            ameaca.setImagem(bmpEncoded);
        }

        if (ameaca.getDescricao().isEmpty() && !temImagem){
            Toast.makeText(getBaseContext(), "Obrigatório informar pelo menos descrição e foto!", Toast.LENGTH_SHORT).show();
        } else {
            String key = ameacas.push().getKey();
            ameacas.child(key).setValue(ameaca);
            Toast.makeText(getBaseContext(), "Ameaça ambiental adicionada com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public String carregarImagem() {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteOut);
        return Base64.getEncoder().encodeToString(byteOut.toByteArray());
    }

    public void tirarFoto(View v) {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CHAMADA_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHAMADA_CAMERA && resultCode == RESULT_OK) {
            bmp = (Bitmap) data.getExtras().get("data");
            imagem.setImageBitmap(bmp);
            temImagem = true;
        }
    }

    public void abrirSeletorData(View view) {

        datePickerDialog.show();
    }

    public String formataMes(String mes) {
        if (mes.length() == 2) {
            return mes;
        } else {
            return "0" + mes;
        }
    }

    public String formataDia(String dia) {
        if (dia.length() == 2) {
            return dia;
        } else {
            return "0" + dia;
        }
    }
}