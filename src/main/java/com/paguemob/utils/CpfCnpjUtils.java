package com.paguemob.utils;

import org.springframework.util.ObjectUtils;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;

public class CpfCnpjUtils {

    private CpfCnpjUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean isValidCnpj(String cnpj) {
        if (ObjectUtils.isEmpty(cnpj))
            return false;

        cnpj = clearCpfCnpj(cnpj);

        if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111") || cnpj.equals("22222222222222")
                || cnpj.equals("33333333333333") || cnpj.equals("44444444444444") || cnpj.equals("55555555555555")
                || cnpj.equals("66666666666666") || cnpj.equals("77777777777777") || cnpj.equals("88888888888888")
                || cnpj.equals("99999999999999") || (cnpj.length() != 14)) {
            return Boolean.FALSE;
        }

        // 48 é a posição do 0 em ASCII
        final int posicaoZero = 48;
        int soma;
        int resultado;
        int peso;
        int primeiroDigito;
        int segundoDigito;
        soma = 0;
        peso = 2;

        // calculo primeiro digito
        for (int i = 11; i >= 0; i--) {
            soma += (cnpj.charAt(i) - posicaoZero) * peso;
            peso += 1;
            if (peso == 10)
                peso = 2;
        }
        resultado = soma % 11;

        if (resultado == 0 || resultado == 1) {
            primeiroDigito = 0;
        } else {
            primeiroDigito = 11 - resultado;
        }

        soma = 0;
        peso = 2;

        // Calculo segundo digito
        for (int i = 12; i >= 0; i--) {
            soma += (cnpj.charAt(i) - posicaoZero) * peso;
            peso += 1;
            if (peso == 10)
                peso = 2;
        }

        resultado = soma % 11;
        if (resultado == 0 || resultado == 1) {
            segundoDigito = 0;
        } else {
            segundoDigito = 11 - resultado;
        }

        // verificação com os dados
        if (primeiroDigito == (cnpj.charAt(12) - posicaoZero) && segundoDigito == (cnpj.charAt(13) - posicaoZero)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public static String clearCpfCnpj(String string) {
        return string.replaceAll("\\D", "");
    }

    public static boolean isValidCpf(String cpf) {
        if (ObjectUtils.isEmpty(cpf))
            return false;

        cpf = tirarPontos(cpf);

        if (cpf.equals("00000000000") || cpf.equals("11111111111") || cpf.equals("22222222222")
                || cpf.equals("33333333333") || cpf.equals("44444444444") || cpf.equals("55555555555")
                || cpf.equals("66666666666") || cpf.equals("77777777777") || cpf.equals("88888888888")
                || cpf.equals("99999999999") || (cpf.length() != 11)) {
            return Boolean.FALSE;
        }

        // 48 é a posição do 0 em ASCII
        final int posicaoZero = 48;

        int peso;
        int soma;
        int resultado;
        int primeiroDigito;
        int segundoDigito;
        peso = 10;
        soma = 0;

        // calculo primeiro digito
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - posicaoZero) * peso;
            peso -= 1;
        }

        resultado = 11 - (soma % 11);
        if (resultado == 10 || resultado == 11) {
            primeiroDigito = 0;
        } else {
            primeiroDigito = resultado;
        }

        peso = 11;
        soma = 0;

        // calculando segundo digito
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - posicaoZero) * peso;
            peso -= 1;
        }

        resultado = 11 - (soma % 11);

        if (resultado == 10 || resultado == 11) {
            segundoDigito = 0;
        } else {
            segundoDigito = (char) resultado;
        }

        // verificar o digitos calculados conferem com os digitos
        if (primeiroDigito == (cpf.charAt(9) - posicaoZero)
                && segundoDigito == (cpf.charAt(10) - posicaoZero)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    private static String tirarPontos(String string) {
        return string.replace(".", "").replace("-", "");
    }

    public static String formatCnpj(String cnpj) {
        return format(cnpj, false);
    }

    public static String formatCpf(String cpf) {
        return format(cpf, true);
    }

    private static String format(String cpfCnpj, boolean isCpf) {
        if (ObjectUtils.isEmpty(cpfCnpj))
            return cpfCnpj;

        MaskFormatter maskFormatter = getMask(isCpf);
        if (maskFormatter != null) {
            try {
                return maskFormatter.valueToString(cpfCnpj);
            } catch (ParseException e) {
                return cpfCnpj;
            }
        }
        return cpfCnpj;
    }

    private static MaskFormatter getMask(boolean isCpf) {
        try {
            MaskFormatter maskFormatter = new MaskFormatter(isCpf ? "###.###.###-##" : "###.###.###/####-##");
            maskFormatter.setValueContainsLiteralCharacters(false);
            return maskFormatter;
        } catch (ParseException e) {
            return null;
        }
    }
}
