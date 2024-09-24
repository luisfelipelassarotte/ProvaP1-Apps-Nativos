package example.aposentadoria;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends Activity {
    EditText montanteDesejado, tempoAplicado, taxaJuros;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        montanteDesejado = (EditText) findViewById(R.id.montante_desejado_id);
        tempoAplicado = (EditText) findViewById(R.id.tempo_aplicado_id);
        taxaJuros = (EditText) findViewById(R.id.taxa_juros_anual_id);
        Button button = (Button) findViewById(R.id.calcular_id);

        tempoAplicado.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, monthOfYear + 1, year1);
                        tempoAplicado.setText(selectedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);

                intent.putExtra("montante", montanteDesejado.getText().toString());
                intent.putExtra("tempo", tempoAplicado.getText().toString());
                intent.putExtra("juros", taxaJuros.getText().toString());

                startActivity(intent);
            }
        });

    }
}
