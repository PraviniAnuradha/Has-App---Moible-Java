package lk.javainstitute.has;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import lk.javainstitute.has.dto.FundTransfer;
import lk.javainstitute.has.service.FundTransferService;

public class FundTransferActivity extends AppCompatActivity {
    private String bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_transfer);
        findViewById(R.id.fundTransferBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String[] banks = new String[]{"Select a bank","People's Bank","Bank of cylon","HNB"};
        Spinner bankSelect = findViewById(R.id.fundSpinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(FundTransferActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                banks);
        bankSelect.setAdapter(adapter);

        bankSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                FundTransferActivity.this.bank = banks[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        findViewById(R.id.fundBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText amount = findViewById(R.id.fundAmount);
                EditText name = findViewById(R.id.fundName);
                Spinner bankSelect = findViewById(R.id.fundSpinner);
                EditText accNo = findViewById(R.id.fundAccNo);
                EditText des = findViewById(R.id.fundDes);

                FundTransfer transfer = new FundTransfer(
                        Double.parseDouble(String.valueOf(amount.getText())),
                        String.valueOf(name.getText()),
                        String.valueOf(bankSelect.getSelectedItem()),
                        String.valueOf(accNo.getText()),
                        String.valueOf(des.getText())
                );

                new FundTransferService(FundTransferActivity.this).transfer(transfer);

            }
        });
    }
}