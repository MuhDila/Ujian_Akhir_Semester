package com.example.ujianakhirsemester;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NewsAdd extends AppCompatActivity {

    EditText title, desc;
    Button saveNews;
    FirebaseFirestore dbNews = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_add);

        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        saveNews = findViewById(R.id.btnAdd);

        progressDialog = new ProgressDialog(NewsAdd.this);
        progressDialog.setTitle("Sedang diproses...");

        saveNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().length() > 0 && desc.getText().length() > 0){
                    saveData(title.getText().toString(), desc.getText().toString());
                } else{
                    Toast.makeText(getApplicationContext(), "Semua data harus diisi",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        Intent updateOption = getIntent();
        if (updateOption!=null){
            id = updateOption.getStringExtra("id");
            title.setText(updateOption.getStringExtra("title"));
            desc.setText(updateOption.getStringExtra("description"));
        }
    }

    private void saveData(String title, String description){
        Map<String, Object> mapNews = new HashMap<>();
        mapNews.put("title", title);
        mapNews.put("desc", description);

        progressDialog.show();
        if (id!=null){
            dbNews.collection("news").document(id)
                    .set(mapNews)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("data", "DocumentSnapshot successfully written!");
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("data", "Error writing document!", e);
                            finish();
                        }
                    });
        } else {
            dbNews.collection("news")
                    .add(mapNews)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("data", "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(getApplicationContext(), "Data berhasil disimpan",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("data", "Error adding document", e);
                            Toast.makeText(getApplicationContext(), "Data gagal disimpan",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }
        progressDialog.dismiss();
    }
}