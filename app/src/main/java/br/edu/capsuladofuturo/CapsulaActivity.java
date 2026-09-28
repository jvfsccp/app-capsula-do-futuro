package br.edu.capsuladofuturo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import br.edu.capsuladofuturo.databinding.ActivityCapsulaBinding;

/**
 * Tela 2: exibe a cápsula selada com os dados recebidos da {@link MainActivity}.
 * "Editar" volta ao formulário (que mantém o que foi digitado) e "Compartilhar"
 * envia o texto da cápsula para outro aplicativo por uma Intent implícita.
 */
public class CapsulaActivity extends AppCompatActivity {

    public static final String EXTRA_AUTOR = "br.edu.capsuladofuturo.EXTRA_AUTOR";
    public static final String EXTRA_TITULO = "br.edu.capsuladofuturo.EXTRA_TITULO";
    public static final String EXTRA_ANO = "br.edu.capsuladofuturo.EXTRA_ANO";
    public static final String EXTRA_MENSAGEM = "br.edu.capsuladofuturo.EXTRA_MENSAGEM";
    public static final String EXTRA_IMPORTANCIA = "br.edu.capsuladofuturo.EXTRA_IMPORTANCIA";

    private ActivityCapsulaBinding binding;

    private String autor;
    private String titulo;
    private int ano;
    private String mensagem;
    private int importancia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this,
                SystemBarStyle.dark(Color.TRANSPARENT),
                SystemBarStyle.dark(Color.TRANSPARENT));

        binding = ActivityCapsulaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TelaUtils.respeitarBarrasDoSistema(binding.getRoot());

        recuperarDadosDaIntent();
        exibirCapsula();

        binding.btnEditar.setOnClickListener(v -> finish());
        binding.btnCompartilhar.setOnClickListener(v -> compartilharCapsula());
    }

    private void recuperarDadosDaIntent() {
        Intent intent = getIntent();
        autor = intent.getStringExtra(EXTRA_AUTOR);
        titulo = intent.getStringExtra(EXTRA_TITULO);
        ano = intent.getIntExtra(EXTRA_ANO, 0);
        mensagem = intent.getStringExtra(EXTRA_MENSAGEM);
        importancia = intent.getIntExtra(EXTRA_IMPORTANCIA, 0);
    }

    private void exibirCapsula() {
        binding.txtTituloCapsula.setText(titulo);
        binding.txtCriadaPor.setText(getString(R.string.criada_por, autor, ano));
        binding.txtContagem.setText(textoDaContagem());
        binding.txtMensagemCapsula.setText(mensagem);
        binding.txtImportanciaCapsula.setText(getString(R.string.valor_importancia, importancia));
        binding.progressImportancia.setProgress(importancia);
        binding.txtNivelImportanciaCapsula.setText(NivelImportancia.descricao(importancia));
    }

    /** Ex.: "Faltam 4 anos para a abertura". */
    private String textoDaContagem() {
        int anosRestantes = ano - Calendar.getInstance().get(Calendar.YEAR);
        if (anosRestantes <= 0) {
            return getString(R.string.pronta_para_abrir);
        }
        return getResources().getQuantityString(R.plurals.anos_restantes, anosRestantes, anosRestantes);
    }

    private void compartilharCapsula() {
        String texto = getString(R.string.texto_compartilhado, ano, tituloComPontuacao(), mensagem);

        Intent envio = new Intent(Intent.ACTION_SEND);
        envio.setType("text/plain");
        envio.putExtra(Intent.EXTRA_SUBJECT, titulo);
        envio.putExtra(Intent.EXTRA_TEXT, texto);

        try {
            startActivity(Intent.createChooser(envio, getString(R.string.titulo_escolher_app)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.erro_sem_app_compartilhar, Toast.LENGTH_SHORT).show();
        }
    }

    /** Fecha o título com ponto, sem gerar "Título!." quando já houver pontuação no final. */
    private String tituloComPontuacao() {
        return titulo.matches(".*[.!?…]$") ? titulo : titulo + ".";
    }
}
