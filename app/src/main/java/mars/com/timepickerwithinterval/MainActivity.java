package mars.com.timepickerwithinterval;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements DateDialog.DateDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DateDialog dateDialog = new DateDialog(MainActivity.this);
                dateDialog.setDateDialogListener(MainActivity.this);
                dateDialog.show();
            }
        });
    }

    @Override
    public void OnDateValidate(String dateTime) {
        Toast.makeText(this, dateTime, Toast.LENGTH_SHORT).show();

    }
}
