package com.example.user.campuscommune;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
  private RecyclerView mBlogList;
 FirebaseDatabase database;
     DatabaseReference mDatabase;

   private TextView text2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database= FirebaseDatabase.getInstance();
        mDatabase=database.getReference().child("/blog");
        mBlogList=(RecyclerView)findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));

        text2=(TextView)findViewById(R.id.textmain1);

       /* FirebaseRecyclerAdapter<Blog, BlogViewHolder> adapter=new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(

                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(), model.getImage());
            }
        };

        mBlogList.setAdapter(adapter);*/

    }




    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Blog, BlogViewHolder> adapter=new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(

                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(), model.getImage());
            }
        };

        mBlogList.setAdapter(adapter);

    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView text_title,text_description;
        ImageView imageview;
        public BlogViewHolder(View itemView) {
            super(itemView);
            text_title=(TextView)itemView.findViewById(R.id.post_title);
            text_description=(TextView)itemView.findViewById(R.id.post_desc);
            imageview=(ImageView)itemView.findViewById(R.id.post_image);


        }
        public void setTitle(String title){
           // TextView post_title=(TextView)mView.findViewById(R.id.post_title);
            text_title.setText(title);
        }
        public void setDesc(String desc){
            //TextView post_desc=(TextView)mView.findViewById(R.id.post_desc);
            text_description.setText(desc);
        }
        public void setImage(Context ctx, String image){
           // ImageView post_image=(ImageView)mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(imageview);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_add)
        {
            startActivity(new Intent(MainActivity.this,PostActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
