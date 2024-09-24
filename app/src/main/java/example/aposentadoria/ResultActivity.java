package example.aposentadoria;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class ResultActivity extends Activity {
    TextView montante_inicial, contribuicao_mesnal, taxa_juros_anual, taxa_juros_mensal, tempo_aplicado_mes, tempo_aplicado_ano;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_layout);

        montante_inicial = findViewById(R.id.valor_montante_inicial_id);
        contribuicao_mesnal = findViewById(R.id.valor_contribuicao_id);
        taxa_juros_anual = findViewById(R.id.valor_taxa_juros_anual_id);
        taxa_juros_mensal = findViewById(R.id.valor_taxa_juros_mensal_id);
        tempo_aplicado_mes = findViewById(R.id.tempo_aplicado_anos_id);
        tempo_aplicado_ano = findViewById(R.id.tempo_aplicado_meses_id);
        Button button = findViewById(R.id.voltar_id);

        Intent intent = getIntent();

        String montanteStr = intent.getStringExtra("montante");
        String tempoStr = intent.getStringExtra("tempo");
        String jurosStr = intent.getStringExtra("juros");

        DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate data = LocalDate.parse(tempoStr, formatterEntrada);

        try {
            double montante = Double.parseDouble(montanteStr);
            double jurosAnual = Double.parseDouble(jurosStr);
            long tempo = calcularDiasCorridos(data);
            double meses = tempo / 30;
            double ano = meses / 12;
            double juros = jurosAnual / 100;
            double jurosMensal = jurosAnual / 12;

            double contribuicao = calcularContrib(montante, juros, meses);

            montante_inicial.setText(String.format("R$ %.2f", montante));
            taxa_juros_anual.setText(String.format("%.2f%% de taxa anual", jurosAnual));
            taxa_juros_mensal.setText(String.format("%.2f%% de taxa mensal", jurosMensal));
            tempo_aplicado_ano.setText(String.format("%.2f anos", ano));
            tempo_aplicado_mes.setText(String.format("%.2f meses", meses));
            contribuicao_mesnal.setText(String.format("R$ %.2f", contribuicao));


        } catch (NumberFormatException e) {
            montante_inicial.setText("Erro: Formato de número inválido");
            taxa_juros_anual.setText("Erro: Formato de número inválido");
        } catch (DateTimeParseException e) {
            tempo_aplicado_ano.setText("Erro: Formato de data inválido");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static long calcularDiasCorridos(LocalDate dataSelecionada) {
        LocalDate dataAtual = LocalDate.now();
        return ChronoUnit.DAYS.between(dataAtual, dataSelecionada);
    }

    private static double calcularContrib(double montante, double juros, double meses) {
        double resultado = montante * (1 - (juros)) / meses;
        return resultado;
    }

    private static double calcJuros(double montante, double taxaJurosMensal, double meses) {
        double resultado = montante * (Math.pow(1 + taxaJurosMensal, meses));
        return resultado;
    }
}
