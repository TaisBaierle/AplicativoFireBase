package br.feevale.appameacasambientaisfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AutenticacaoUsuario extends AppCompatActivity {

    private TextView edEmail, edSenha;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Boolean show = false;
    private ImageButton botaoSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao_usuario);

        edEmail = findViewById(R.id.edEmail);
        edSenha = findViewById(R.id.edSenha);
        botaoSenha = findViewById(R.id.btSenha);
        botaoSenha.setBackgroundResource(R.drawable.hide);
        mAuth = FirebaseAuth.getInstance();

    }

    public void logIn(View v) {
        if (edEmail.getText().toString().isEmpty() && edSenha.getText().toString().isEmpty()){
            Toast.makeText(AutenticacaoUsuario.this, "Deve preencher email e senha!", Toast.LENGTH_SHORT).show();
        }else {
            autenticarUsuario(edEmail.getText().toString(), edSenha.getText().toString());
        }
    }

    public void signIn(View v) {
        if (edEmail.getText().toString().isEmpty() && edSenha.getText().toString().isEmpty()) {
            Toast.makeText(AutenticacaoUsuario.this, "Deve preencher email e senha!", Toast.LENGTH_SHORT).show();
        }else {
            criarUsuario(edEmail.getText().toString(), edSenha.getText().toString());
        }
    }

    public void autenticarUsuario(String email, String senha) {
        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = mAuth.getCurrentUser();
                    Intent it = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(it);
                } else {
                    Toast.makeText(AutenticacaoUsuario.this, "Erro ao fazer o login!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void criarUsuario(String email, String senha) {
        mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = mAuth.getCurrentUser();
                    Intent it = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(it);
                    Toast.makeText(AutenticacaoUsuario.this, "Cadastro criado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AutenticacaoUsuario.this, "Erro ao criar cadastro!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void mostrarSenha(View v) {
        if (show) {
            edSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
            botaoSenha.setBackgroundResource(R.drawable.hide);
            show = false;
        } else {
            edSenha.setTransformationMethod(SingleLineTransformationMethod.getInstance());
            botaoSenha.setBackgroundResource(R.drawable.show);
            show = true;
        }
    }
}


