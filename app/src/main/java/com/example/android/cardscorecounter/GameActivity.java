package com.example.android.cardscorecounter;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    private Game game;
    private Resources res;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MobileAds.initialize(this, "ca-app-pub-5998541896858053~3615074240");
        res = getResources();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        game = (Game) getIntent().getSerializableExtra("currentGame");
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        createLayout();
    }

    public void newGame(View v) {
        Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
        finish();
    }

    private void createLayout() {
        TextView gameHeader = findViewById(R.id.gameHeader);
        gameHeader.setText(res.getString(R.string.round1Header));
        ArrayList<Player> playerArrayList = game.getPlayerArrayList();
        //fill playerNameList with names of current players & fill playerScoreList with scores
        // first child of playerNameList = empty TextView for layout purpose || first child of playerScoreList = "Scores:"
        LinearLayout playerNameList = findViewById(R.id.playerNameListLayout);
        LinearLayout playerScoreList = findViewById(R.id.playerScoreListLayout);
        //for each Player, create new TextView with player name & create new TextView with player score
        for (Player player : playerArrayList) {
            TextView playerNameLayout = new TextView(this);
            LinearLayout.LayoutParams playerNameParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            playerNameLayout.setGravity(Gravity.CENTER);
            playerNameLayout.setText(player.getName());
            playerNameList.addView(playerNameLayout, playerNameParams);

            TextView playerScoreLayout = new TextView(this);
            LinearLayout.LayoutParams playerScoreParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            playerScoreLayout.setGravity(Gravity.CENTER);
            playerScoreLayout.setText(Integer.toString(player.getScore()));
            playerScoreList.addView(playerScoreLayout, playerScoreParams);

        }
        createRowlayout();
    }

    public void nextRound(View v) {
        //check for empty score fields, fill with hint text
        LinearLayout scoreLayout = findViewById(R.id.scoreLayout);
        LinearLayout scoreRow = (LinearLayout) scoreLayout.getChildAt(game.getCurrentRound() - 1);
        TextView textView;
        for (int i = 1; i < scoreRow.getChildCount(); i++) {
            textView = (EditText) scoreRow.getChildAt(i);
            if (textView.getText().length() == 0)
                textView.setText(textView.getHint().toString().trim());
        }
        if (game.getCurrentRound() == (6 + game.getPlayerCount())) {
            game.setNextRound();
            updateScores();
            Button button = findViewById(R.id.newRoundButton);
            ViewGroup layout = (ViewGroup) button.getParent();
            layout.removeView(button);
            endGame();
        } else {
            if (game.getCurrentRound() == (5 + game.getPlayerCount())) {
                Button button = findViewById(R.id.newRoundButton);
                button.setText(res.getString(R.string.endGameButton));
            }
            game.setNextRound();
            createRowlayout();
            updateScores();

            //pas header aan aan juiste ronde
            textView = findViewById(R.id.gameHeader);
            switch (game.getCurrentRound()) {
                case 2:
                    textView.setText(res.getString(R.string.round2Header));
                    break;
                case 3:
                    textView.setText(res.getString(R.string.round3Header));
                    break;
                case 4:
                    textView.setText(res.getString(R.string.round4Header));
                    break;
                case 5:
                    textView.setText(res.getString(R.string.round5Header));
                    break;
                case 6:
                    textView.setText(res.getString(R.string.round6Header));
                    break;
                case 7:
                    textView.setText(res.getString(R.string.round7Header));
                    break;
                case 8:
                    textView.setText(res.getString(R.string.round8Header));
                    break;
                case 9:
                    textView.setText(res.getString(R.string.round9Header));
                    break;
                case 10:
                    textView.setText(res.getString(R.string.round10Header));
                    break;
                case 11:
                    textView.setText(res.getString(R.string.round11Header));
                    break;
            }

        }
    }

    private void updateScores() {
        LinearLayout totalScoreRow = (LinearLayout) findViewById(R.id.playerScoreListLayout);
        LinearLayout scoreLayout = findViewById(R.id.scoreLayout);
        // -2 because current round is already added by 1 and last child is the button
        LinearLayout scoreRow = (LinearLayout) scoreLayout.getChildAt(game.getCurrentRound() - 2);
        TextView textView;
        String text;
        ArrayList<Player> players = game.getPlayerArrayList();
        for (int i = 1; i < scoreRow.getChildCount(); i++) {
            textView = (EditText) scoreRow.getChildAt(i);
            text = textView.getText().toString();
            //in first rounds, add points. Afterwards, substract them
            if (game.getCurrentRound() < 7)
                players.get(i - 1).addScore(Integer.parseInt(text));
            else
                players.get(i - 1).subtractScore(Integer.parseInt(text));
            textView.setEnabled(false);
            //update total scores
            textView = (TextView) totalScoreRow.getChildAt(i);
            textView.setText(Integer.toString(players.get(i - 1).getScore()));
        }
    }

    private void endGame() {
        LinearLayout scoreList = findViewById(R.id.playerScoreListLayout);
        ArrayList<String> winningNames = new ArrayList<>();
        int winner = 1;
        TextView textView;
        TextView textViewWinner = (TextView) scoreList.getChildAt(1);
        for (int i = 2; i < game.getPlayerCount() + 1; i++) {
            textView = (TextView) scoreList.getChildAt(i);
            if (Integer.parseInt(textView.getText().toString()) < Integer.parseInt(textViewWinner.getText().toString())) {
                winningNames = new ArrayList<>();
                winner = i;
                textViewWinner = textView;
            }
        }
        textView = findViewById(R.id.gameHeader);
        String winnerName = game.getPlayerArrayList().get(winner - 1).getName();
        textView = findViewById(R.id.gameHeader);
        textView.setText(res.getString(R.string.endGameHeader));
    }

    private void createRowlayout() {
        //create first row in scoreLayout
        LinearLayout scoreLayout = findViewById(R.id.scoreLayout);
        LinearLayout scoreLayoutRow = new LinearLayout(this);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        scoreLayout.addView(scoreLayoutRow, game.getCurrentRound() - 1, rowParams);

        for (int i = 0; i < game.getPlayerCount() + 1; i++) {
            TextView textView;
            String text;
            float weight;
            if (i == 0) {
                weight = (float) 0.5;
                text = res.getString(R.string.roundText, game.getCurrentRound());
                textView = new TextView(this);
                textView.setText(text);
            } else {
                weight = (float) 1;
                textView = new EditText(this);
                textView.setInputType(InputType.TYPE_CLASS_NUMBER);
                text = "0";
                textView.setHint(text);
            }
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, weight);
            textView.setGravity(Gravity.CENTER);
            scoreLayoutRow.addView(textView, textParams);
        }
    }
}
