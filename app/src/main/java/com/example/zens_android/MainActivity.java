package com.example.zens_android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String VOTED_JOKES_KEY = "voted_jokes";
    private static final String VIEWED_JOKES_KEY = "viewed_jokes";

    private TextView jokeTextView;
    private Button likeButton;
    private Button dislikeButton;

    private List<String> jokes;
    private Set<String> viewedJokes;
    private List<String> votedJokes;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jokeTextView = findViewById(R.id.jokeTextView);
        likeButton = findViewById(R.id.likeButton);
        dislikeButton = findViewById(R.id.dislikeButton);

        sharedPreferences = getSharedPreferences("JokeApp", MODE_PRIVATE);

        jokes = new ArrayList<>();
        jokes.add("A child asked his father, \"How were people born?\" So his father said, \"Adam and Eve made babies, then their babies became adults and made babies, and so on.\"\n" +
                "\n" +
                "The child then went to his mother, asked her the same question and she told him, \"We were monkeys then we evolved to become like we are now.\"\n" +
                "\n" +
                "The child ran back to his father and said, \"You lied to me!\" His father replied, \"No, your mom was talking about her side of the family.\"");
        jokes.add("Teacher: \"Kids,what does the chicken give you?\" Student: \"Meat!\" Teacher: \"Very good! Now what does the pig give you?\" Student: \"Bacon!\" Teacher: \"Great! And what does the fat cow give you?\" Student: \"Homework!\"");
        jokes.add("The teacher asked Jimmy, \"Why is your cat at school today Jimmy?\" Jimmy replied crying, \"Because I heard my daddy tell my mommy, 'I am going to eat that pussy once Jimmy leaves for school today!'\"");

        viewedJokes = sharedPreferences.getStringSet(VIEWED_JOKES_KEY, new HashSet<>());

        Set<String> votedJokesSet = sharedPreferences.getStringSet(VOTED_JOKES_KEY, new HashSet<>());
        votedJokes = new ArrayList<>(votedJokesSet);

        displayRandomJoke();

        likeButton.setOnClickListener(v -> {
            trackVotedJoke();
            displayRandomJoke();
        });

        dislikeButton.setOnClickListener(v -> {
            trackVotedJoke();
            displayRandomJoke();
        });
    }

    private void displayRandomJoke() {

        if (viewedJokes.size() == jokes.size()) {
            jokeTextView.setText(R.string.txt_message);
            likeButton.setEnabled(false);
            dislikeButton.setEnabled(false);
            return;
        }

        Random random = new Random();
        String joke = jokes.get(random.nextInt(jokes.size()));
        while (viewedJokes.contains(joke)) {
            joke = jokes.get(random.nextInt(jokes.size()));
        }

        // Display the joke
        jokeTextView.setText(joke);

        if (viewedJokes.size() == jokes.size()) {
            clearVotedJokes();
        }

        viewedJokes.add(joke);
    }

    private void trackVotedJoke() {

        Set<String> votedJokesSet = sharedPreferences.getStringSet(VOTED_JOKES_KEY, new HashSet<>());
        votedJokes = new ArrayList<>(votedJokesSet);

        String currentJoke = jokeTextView.getText().toString();
        votedJokes.add(currentJoke);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(VOTED_JOKES_KEY, new HashSet<>(votedJokes));
        editor.apply();
    }

    private void clearVotedJokes() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(VOTED_JOKES_KEY);
        editor.apply();
    }
}
