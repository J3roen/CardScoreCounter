package com.example.android.cardscorecounter;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerActivity extends AppCompatActivity {

    private PlayerActivity playerActivity;
    private Game game;
    private Resources res;
    private int playerCount;
    int sdk = Build.VERSION.SDK_INT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        game = new Game();
        setContentView(R.layout.activity_player);
        addPlayer(getWindow().getDecorView());

        }

    public void addPlayer(View v) {
        LinearLayout playerlistLayout = findViewById(R.id.playerlistLayout);
        playerCount = playerlistLayout.getChildCount();
        RelativeLayout playerLayout;
        TextView playerName;
            //if playerCount == 1 -> no children yet -> create new playerLayout with editText & remove button
            playerLayout = new RelativeLayout(this);
            playerLayout.setTag("");
            RelativeLayout.LayoutParams playerLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            playerlistLayout.addView(playerLayout, playerCount - 1);
            //create edittext to input player name
            EditText playerNameText = new EditText(this);
            RelativeLayout.LayoutParams playerNameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            playerNameParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            playerNameText.setHint(res.getString(R.string.playerHint));
            playerLayout.addView(playerNameText, playerNameParams);
            //create confirm name button
            Button playerConfirmNameButton = new Button(this);
            RelativeLayout.LayoutParams confirmNameParams = new RelativeLayout.LayoutParams((int) res.getDimension(R.dimen.squareButton), (int) res.getDimension(R.dimen.squareButton));
            playerConfirmNameButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    confirmName(v);
                }
            });
            //create remove button to delete player
            Button playerRemoveButton = new Button(this);
            RelativeLayout.LayoutParams removeButtonParams = new RelativeLayout.LayoutParams((int) res.getDimension(R.dimen.squareButton), (int) res.getDimension(R.dimen.squareButton));
            int id = RelativeLayout.generateViewId();
            playerRemoveButton.setId(id);
            playerRemoveButton.setTag(playerCount-1);
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                playerRemoveButton.setBackground(res.getDrawable(R.drawable.ic_clear_white_36dp));
                playerConfirmNameButton.setBackground(res.getDrawable(R.drawable.ic_done_white_36dp));
            }
            else {
                playerRemoveButton.setBackgroundDrawable(res.getDrawable(R.drawable.ic_clear_white_36dp));
                playerConfirmNameButton.setBackground(res.getDrawable(R.drawable.ic_done_white_36dp));
            }
            confirmNameParams.addRule(RelativeLayout.LEFT_OF,id);
            confirmNameParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            playerLayout.addView(playerConfirmNameButton, confirmNameParams);

            removeButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            playerRemoveButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    removePlayer(v);
                }
            });
            playerLayout.addView(playerRemoveButton, removeButtonParams);
        }

    private void confirmName(View v) {
        RelativeLayout playerLayout = (RelativeLayout) v.getParent();
        EditText playerName = (EditText) playerLayout.getChildAt(0);
        if (playerName.getText().toString().trim().equals("")) {
            Toast.makeText(this, res.getString(R.string.enterNameToast), Toast.LENGTH_LONG).show();
            playerName.requestFocus();
        }
        //if name is not empty, add new player to Game object with given name
        else {
            game.addPlayer(playerName.getText().toString().trim());
            playerName.setText(playerName.getText().toString().trim());
            playerName.setEnabled(false);
            playerLayout.setTag("confirmed");
            playerLayout.removeViewAt(1);

            //if name was confirmed, set focus to next name that isn't confirmed
            ViewGroup playerlistLayout = (ViewGroup) playerLayout.getParent();
            int index = playerlistLayout.indexOfChild(playerLayout);
            //search through playerlistLayout, look at all the children and look for first that isn't confirmed
            loop:
            for (int i = 0; i < playerlistLayout.getChildCount()-1; i++) {
                if (index != i) {
                    playerLayout = (RelativeLayout) playerlistLayout.getChildAt(i);
                    if (!(playerLayout.getTag().toString().equals("confirmed"))) {
                        playerLayout.getChildAt(0).requestFocus();
                        break loop;
                    }
                }
            }
         }
    }

    private void removePlayer(View v) {
        LinearLayout playerlistLayout = findViewById(R.id.playerlistLayout);
        playerlistLayout.removeView((View) v.getParent());

    }

    public void startGame(View v) {
        boolean allPlayersNamed = true;
        LinearLayout playerlistLayout = findViewById(R.id.playerlistLayout);
        RelativeLayout playerLayout;
            for (int i=0; i < playerlistLayout.getChildCount()-1; i++) {
               playerLayout = (RelativeLayout) playerlistLayout.getChildAt(i);
               EditText playerName = (EditText) playerLayout.getChildAt(0);
               if (playerName.getText().toString().trim().equals("")){
                   allPlayersNamed = false;
               }
            }

            if (allPlayersNamed) {
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra("currentGame", game);
                startActivity(intent);
                finish();
            } else
                Toast.makeText(this,res.getString(R.string.namePlayersToast),Toast.LENGTH_LONG).show();
    }
}
