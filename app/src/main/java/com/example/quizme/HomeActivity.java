package com.example.quizme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //creating dummy questions
        ArrayList<Question> questions = new ArrayList<>();
        Question tmpQuestion;
        for(int i=0;i<10;i++){
            tmpQuestion = new Question("Question "+String.valueOf(i+1),i+1,"answer 1","answer 2","answer 3","answer 4");
            questions.add(tmpQuestion);
        }


        RecyclerView recyclerView = findViewById(R.id.reView);
        QuizListAdapter adapter = new QuizListAdapter(questions);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }
}