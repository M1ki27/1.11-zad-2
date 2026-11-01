package com.example.projekt2;

import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    Spinner spPizza;
    RadioGroup rgSize;
    CheckBox cbCheese, cbHam, cbMushrooms, cbContact;
    SeekBar sbQuantity;
    EditText etAddress;
    Button btnOrder;
    TextView tvQuantity, tvSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spPizza = findViewById(R.id.spPizza);
        rgSize = findViewById(R.id.rgSize);

        cbCheese = findViewById(R.id.cbCheese);
        cbHam = findViewById(R.id.cbHam);
        cbMushrooms = findViewById(R.id.cbMushrooms);
        cbContact = findViewById(R.id.cbContact);

        sbQuantity = findViewById(R.id.sbQuantity);
        etAddress = findViewById(R.id.etAddress);
        btnOrder = findViewById(R.id.btnOrder);

        tvQuantity = findViewById(R.id.tvQuantity);
        tvSummary = findViewById(R.id.tvSummary);

        ustawSpinner();
        ustawListenery();
    }

    private void ustawSpinner() {

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.pizza,
                        android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spPizza.setAdapter(adapter);
    }

    private void ustawListenery() {

        cbContact.setOnCheckedChangeListener(
                (button, checked) -> sprawdzPrzycisk());

        etAddress.addTextChangedListener(
                new android.text.TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(
                            CharSequence s, int start, int before, int count) {
                        sprawdzPrzycisk();
                    }

                    @Override
                    public void afterTextChanged(
                            android.text.Editable s) {}
                });

        sbQuantity.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar, int progress, boolean fromUser) {

                        tvQuantity.setText("Ilość: " + (progress + 1));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });

        btnOrder.setOnClickListener(v -> {

            if (sprawdzFormularz()) {
                pokazPotwierdzenie();
            }
        });
    }

    private void sprawdzPrzycisk() {

        boolean adres = !etAddress.getText().toString().trim().isEmpty();

        boolean zgoda = cbContact.isChecked();

        btnOrder.setEnabled(adres && zgoda);
    }

    private boolean sprawdzFormularz() {

        if (spPizza.getSelectedItemPosition() == 0) {
            Toast.makeText(this,
                    "Wybierz pizzę", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (rgSize.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this,
                    "Wybierz rozmiar", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etAddress.getText().toString().trim().isEmpty()) {
            etAddress.setError("Podaj adres");
            etAddress.requestFocus();
            return false;
        }

        if (!cbContact.isChecked()) {
            Toast.makeText(this,
                    "Zaznacz zgodę na kontakt", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void pokazPotwierdzenie() {

        new AlertDialog.Builder(this)
                .setTitle("Potwierdzenie")
                .setMessage("Czy chcesz zamówić pizzę?")
                .setPositiveButton("Tak", (dialog, which) ->
                        zamowienie())
                .setNegativeButton("Nie", null)
                .show();
    }

    private void zamowienie() {

        String rozmiar = "Mała";

        if (rgSize.getCheckedRadioButtonId() == R.id.rbMedium) {
            rozmiar = "Średnia";
        }

        if (rgSize.getCheckedRadioButtonId() == R.id.rbLarge) {
            rozmiar = "Duża";
        }

        String dodatki = "";

        if (cbCheese.isChecked()) {
            dodatki += "Ser ";
        }

        if (cbHam.isChecked()) {
            dodatki += "Szynka ";
        }

        if (cbMushrooms.isChecked()) {
            dodatki += "Pieczarki ";
        }

        String podsumowanie =
                "Pizza: " + spPizza.getSelectedItem() +
                        "\nRozmiar: " + rozmiar +
                        "\nDodatki: " + dodatki +
                        "\nIlość: " + (sbQuantity.getProgress() + 1) +
                        "\nAdres: " + etAddress.getText().toString();

        tvSummary.setText(podsumowanie);

        Snackbar.make(
                        findViewById(R.id.btnOrder),
                        "Zamówienie przyjęte",
                        Snackbar.LENGTH_LONG)
                .setAction("Cofnij", v ->
                        tvSummary.setText(""))
                .show();
    }
}