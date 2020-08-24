package com.kustura.myproject.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.kustura.myproject.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "com.codinginflow.architectureexample.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.codinginflow.architectureexample.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.codinginflow.architectureexample.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "com.codinginflow.architectureexample.EXTRA_PRIORITY";
    public static final String EXTRA_IMG =
            "com.codinginflow.architectureexample.EXTRA_IMG";

    public static final int TAKING_IMG = 1;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;
    private ImageView imageView;
    private String currentImgPath;
    private Button addImgButton;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);
        imageView = findViewById(R.id.edit_image);
        addImgButton = findViewById(R.id.add_img_button);
        addImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeImage();
            }
        });

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
            Picasso.get().load(intent.getStringExtra(EXTRA_IMG)).into(imageView);
            currentImgPath = intent.getStringExtra(EXTRA_IMG);
            imagePath =currentImgPath;

        } else {
            setTitle("Add Note");
            Picasso.get().load(getURLForResource(R.drawable.unknown_img)).into(imageView);
            currentImgPath = getURLForResource(R.drawable.unknown_img);
            imagePath = currentImgPath;

        }
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String imgPath = imagePath;
        int priority = numberPickerPriority.getValue();
        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);
        data.putExtra(EXTRA_IMG, imagePath);


        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    private String getURLForResource(int resourceId) {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId).toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void takeImage() {
        Intent takeImgIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takeImgIntent.resolveActivity(this.getPackageManager()) == null) {
            Toast.makeText(this, "Problem while takeing image", Toast.LENGTH_SHORT).show();
            return;
        }
        File img = null;
        try {
            img = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "Problem while creating image", Toast.LENGTH_SHORT).show();
            return;
        }
        if (img == null) {
            Toast.makeText(this, "Problem while creating image", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri imgUri = FileProvider.getUriForFile(this, "com.kustura.myproject.provider", img);
        takeImgIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(takeImgIntent, TAKING_IMG);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String imgName = "NOTE_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imgName, ".jpg", storageDir
        );

        currentImgPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKING_IMG && resultCode == Activity.RESULT_OK) {

            imagePath = "file://" + currentImgPath;
            Picasso.get().load(imagePath).into(imageView);

        }
    }
}