package br.edu.capsuladofuturo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

import br.edu.capsuladofuturo.databinding.ActivityMainBinding;

/**
 * Tela 1: o usuário escreve a cápsula, escolhe o ano de abertura e a importância.
 * Ao tocar em "Selar cápsula", o formulário é validado e, se estiver correto,
 * os dados seguem por uma Intent explícita para a {@link CapsulaActivity}.
 */
public class MainActivity extends AppCompatActivity {

    /** Regra do enunciado: a cápsula só pode ser aberta depois deste ano. */
    private static final int ANO_LIMITE = 2026;
    private static final int TAMANHO_MINIMO_MENSAGEM = 20;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // O fundo do app é sempre escuro, então os ícones das barras do sistema ficam claros.
        EdgeToEdge.enable(this,
                SystemBarStyle.dark(Color.TRANSPARENT),
                SystemBarStyle.dark(Color.TRANSPARENT));

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TelaUtils.respeitarBarrasDoSistema(binding.getRoot());

        configurarSeekBarImportancia();
        limparErroAoDigitar(binding.editAutor, binding.layoutAutor);
        limparErroAoDigitar(binding.editTitulo, binding.layoutTitulo);
        limparErroAoDigitar(binding.editAno, binding.layoutAno);
        limparErroAoDigitar(binding.editMensagem, binding.layoutMensagem);

        binding.btnSelarCapsula.setOnClickListener(v -> selarCapsula());
    }

    private void configurarSeekBarImportancia() {
        atualizarImportancia(binding.seekImportancia.getProgress());

        binding.seekImportancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresso, boolean doUsuario) {
                atualizarImportancia(progresso);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Nada a fazer: o valor já é atualizado em onProgressChanged.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Nada a fazer: o valor já é atualizado em onProgressChanged.
            }
        });
    }

    /** Mostra, em tempo real, o percentual escolhido e uma descrição do nível. */
    private void atualizarImportancia(int percentual) {
        binding.txtValorImportancia.setText(getString(R.string.valor_importancia, percentual));
        binding.txtNivelImportancia.setText(NivelImportancia.descricao(percentual));
    }

    private void selarCapsula() {
        if (!formularioValido()) {
            return;
        }

        Intent intent = new Intent(this, CapsulaActivity.class);
        intent.putExtra(CapsulaActivity.EXTRA_AUTOR, textoDe(binding.editAutor));
        intent.putExtra(CapsulaActivity.EXTRA_TITULO, textoDe(binding.editTitulo));
        intent.putExtra(CapsulaActivity.EXTRA_ANO, Integer.parseInt(textoDe(binding.editAno)));
        intent.putExtra(CapsulaActivity.EXTRA_MENSAGEM, textoDe(binding.editMensagem));
        intent.putExtra(CapsulaActivity.EXTRA_IMPORTANCIA, binding.seekImportancia.getProgress());
        startActivity(intent);
    }

    /**
     * Valida todos os campos, exibe o erro em cada campo inválido e posiciona
     * o foco no primeiro deles. Retorna true somente se tudo estiver correto.
     */
    private boolean formularioValido() {
        EditText primeiroCampoInvalido = null;

        if (exibirErro(binding.layoutAutor, erroDoAutor())) {
            primeiroCampoInvalido = binding.editAutor;
        }
        if (exibirErro(binding.layoutTitulo, erroDoTitulo()) && primeiroCampoInvalido == null) {
            primeiroCampoInvalido = binding.editTitulo;
        }
        if (exibirErro(binding.layoutAno, erroDoAno()) && primeiroCampoInvalido == null) {
            primeiroCampoInvalido = binding.editAno;
        }
        if (exibirErro(binding.layoutMensagem, erroDaMensagem()) && primeiroCampoInvalido == null) {
            primeiroCampoInvalido = binding.editMensagem;
        }

        if (primeiroCampoInvalido != null) {
            focarCampo(primeiroCampoInvalido);
            return false;
        }
        return true;
    }

    @Nullable
    private String erroDoAutor() {
        return textoDe(binding.editAutor).isEmpty() ? getString(R.string.erro_autor_vazio) : null;
    }

    @Nullable
    private String erroDoTitulo() {
        return textoDe(binding.editTitulo).isEmpty() ? getString(R.string.erro_titulo_vazio) : null;
    }

    @Nullable
    private String erroDoAno() {
        String anoDigitado = textoDe(binding.editAno);
        if (anoDigitado.isEmpty()) {
            return getString(R.string.erro_ano_vazio);
        }
        try {
            if (Integer.parseInt(anoDigitado) <= ANO_LIMITE) {
                return getString(R.string.erro_ano_invalido);
            }
        } catch (NumberFormatException e) {
            return getString(R.string.erro_ano_invalido);
        }
        return null;
    }

    @Nullable
    private String erroDaMensagem() {
        String mensagem = textoDe(binding.editMensagem);
        if (mensagem.isEmpty()) {
            return getString(R.string.erro_mensagem_vazia);
        }
        if (mensagem.length() < TAMANHO_MINIMO_MENSAGEM) {
            return getString(R.string.erro_mensagem_curta, mensagem.length());
        }
        return null;
    }

    /** Aplica (ou remove, se nulo) o erro no campo. Retorna true se havia erro. */
    private boolean exibirErro(TextInputLayout campo, @Nullable String erro) {
        campo.setError(erro);
        return erro != null;
    }

    /** Coloca o foco no campo com problema e abre o teclado para corrigi-lo. */
    private void focarCampo(EditText campo) {
        campo.requestFocus();
        campo.setSelection(campo.length());
        WindowCompat.getInsetsController(getWindow(), campo).show(WindowInsetsCompat.Type.ime());
    }

    /** Remove a mensagem de erro assim que o usuário começa a corrigir o campo. */
    private void limparErroAoDigitar(EditText campo, TextInputLayout layout) {
        campo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                layout.setError(null);
            }
        });
    }

    private static String textoDe(EditText campo) {
        return campo.getText() == null ? "" : campo.getText().toString().trim();
    }
}
