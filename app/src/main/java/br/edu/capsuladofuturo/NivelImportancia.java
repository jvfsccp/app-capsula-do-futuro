package br.edu.capsuladofuturo;

import androidx.annotation.StringRes;

/** Traduz o percentual da SeekBar em uma descrição amigável, usada nas duas telas. */
final class NivelImportancia {

    private NivelImportancia() {
    }

    @StringRes
    static int descricao(int percentual) {
        if (percentual >= 75) {
            return R.string.nivel_importancia_essencial;
        }
        if (percentual >= 50) {
            return R.string.nivel_importancia_alta;
        }
        if (percentual >= 25) {
            return R.string.nivel_importancia_media;
        }
        return R.string.nivel_importancia_leve;
    }
}
