package com.example.user.campuscommune;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {
    private ImageButton mSelectimage;
    private EditText mPostTitle;
    private EditText mPostDesc;

    private Button mSubmitBtn;

    private Uri mImageUri=null;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private DatabaseReference mDataBase;
    private static final int GALLERY_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mSelectimage=(ImageButton)findViewById(R.id.imageSelect);
        mPostTitle=(EditText)findViewById(R.id.titleField);
        mPostDesc=(EditText)findViewById(R.id.descField);
        mStorage= FirebaseStorage.getInstance().getReference();
        mSubmitBtn=(Button)findViewById(R.id.submitBtn);
        mProgress=new ProgressDialog(this);
        mDataBase= FirebaseDatabase.getInstance().getReference().child("blog");
        mSelectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });
    }

    private void startPosting() {
        mProgress.setMessage("POSTING to BLOG ....");
        mProgress.show();
        final String title_val=mPostTitle.getText().toString().trim();
        final String desc_val=mPostDesc.getText().toString().trim();
        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri!=null)
        {
            StorageReference filepath=mStorage.child("blog_images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl=taskSnapshot.getDownloadUrl();

                    DatabaseReference newPost=mDataBase.push();
                    newPost.child("title").setValue(title_val);
                    newPost.child("desc").setValue(desc_val);
                    newPost.child("image").setValue(downloadUrl.toString());
                    mProgress.dismiss();
                    startActivity(new Intent(PostActivity.this,MainActivity.class));
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK)
        {
          mImageUri=data.getData();
            mSelectimage.setImageURI(mImageUri);
        }
    }
}
