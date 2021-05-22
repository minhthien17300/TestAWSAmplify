package com.example.quanlychitieu;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.DataStoreConfiguration;
import com.amplifyframework.datastore.generated.model.ChiTieuDetail;

public class MainActivity extends AppCompatActivity {

    Button btnSave;
    EditText edtName;
    EditText edtDate;
    EditText edtCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Amplify.addPlugin(new AWSApiPlugin()); // UNCOMMENT this line once backend is deployed
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());
            Log.i("Amplify", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("Amplify", "Could not initialize Amplify", error);
        }

        AnhXa();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChiTieuDetail item = ChiTieuDetail.builder()
                        .name(edtName.getText().toString())
                        .date(edtDate.getText().toString())
                        .cost(Integer.parseInt(edtCost.getText().toString()))
                        .build();
                Amplify.DataStore.save(
                        item,
                        success -> Log.i("Amplify", "Saved item: " + success.item().getName()),
                        error -> Log.e("Amplify", "Could not save item to DataStore", error)
                );
                try {
                    Amplify.addPlugin(new AWSDataStorePlugin(DataStoreConfiguration.builder()
                            .syncExpression(ChiTieuDetail.class, () -> ChiTieuDetail.DATE.contains("-"))
                            .build()));
                } catch (AmplifyException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void AnhXa() {
        btnSave = (Button) findViewById(R.id.btn_save);
        edtName = (EditText) findViewById(R.id.editTxt_name);
        edtDate = (EditText) findViewById(R.id.editText_date);
        edtCost = (EditText) findViewById(R.id.editText_cost);
    }
}