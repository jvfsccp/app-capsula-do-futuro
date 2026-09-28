package br.edu.capsuladofuturo;

import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/** Ajustes de janela compartilhados pelas duas telas. */
final class TelaUtils {

    private TelaUtils() {
    }

    /**
     * Com o app desenhando de borda a borda, adiciona ao conteúdo um espaçamento igual
     * às barras do sistema, ao recorte da câmera e ao teclado, para nada ficar escondido
     * (inclusive com o aparelho rotacionado).
     */
    static void respeitarBarrasDoSistema(View raiz) {
        ViewCompat.setOnApplyWindowInsetsListener(raiz, (view, insets) -> {
            Insets espacos = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.displayCutout()
                    | WindowInsetsCompat.Type.ime());
            view.setPadding(espacos.left, espacos.top, espacos.right, espacos.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }
}
