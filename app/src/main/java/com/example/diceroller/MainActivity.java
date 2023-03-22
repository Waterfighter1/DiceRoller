package com.example.diceroller;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    // Variables to be used LATER
    private Menu mMenu;
    private CountDownTimer mTimer;
    public static final int MAX_DICE = 3;

    private int mVisibleDice;
    private Dice[] mDice;
    private ImageView[] mDiceImageViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an array of Dice
        mDice = new Dice[MAX_DICE];
        // Add a new dice for as long as it doesn't exceed the max number
        for (int i = 0; i < MAX_DICE; i++) {
            mDice[i] = new Dice(i + 1);
        }

        // Create an array of ImageViews
        mDiceImageViews = new ImageView[MAX_DICE];
        mDiceImageViews[0] = findViewById(R.id.dice1);
        mDiceImageViews[1] = findViewById(R.id.dice2);
        mDiceImageViews[2] = findViewById(R.id.dice3);

        // All dice are initially visible
        mVisibleDice = MAX_DICE;

        showDice();
    }

    // Using our Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    // Showing the Dice that are supposed to be visible
    private void showDice() {
        // Display only the number of dice visible
        for (int i = 0; i < mVisibleDice; i++) {
            Drawable diceDrawable = ContextCompat.getDrawable(this, mDice[i].getImageId());
            mDiceImageViews[i].setImageDrawable(diceDrawable);
            mDiceImageViews[i].setContentDescription(Integer.toString(mDice[i].getNumber()));
        }
    }

    // When something on the menu is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Determine which menu option was chosen
        // One Dice
        if (item.getItemId() == R.id.action_one) {
            changeDiceVisibility(1);
            showDice();
            return true;
        }
        // Two Dice
        else if (item.getItemId() == R.id.action_two) {
            changeDiceVisibility(2);
            showDice();
            return true;
        }
        // Three Dice
        else if (item.getItemId() == R.id.action_three) {
            changeDiceVisibility(3);
            showDice();
            return true;
        }
        // Stop Dice
        else if (item.getItemId() == R.id.action_stop) {
            mTimer.cancel();
            item.setVisible(false);
            return true;
        }
        // Roll Dice
        else if (item.getItemId() == R.id.action_roll) {
            rollDice();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Roll The Dice
    private void rollDice() {
        // Set The Stop Button To True
        mMenu.findItem(R.id.action_stop).setVisible(true);

        // Remove A Preexisting Timer
        if (mTimer != null) {
            mTimer.cancel();
        }

        // Start A New Timer
        mTimer = new CountDownTimer(2000, 100) {
            public void onTick(long millisUntilFinished) {
                for (int i = 0; i < mVisibleDice; i++) {
                    mDice[i].roll();
                }
                showDice();
            }

            // When Its Done, Set The Stop Button To Invisible Again
            public void onFinish() {
                mMenu.findItem(R.id.action_stop).setVisible(false);
            }
        }.start();
    }

    // Decide how many dice are visible
    private void changeDiceVisibility(int numVisible) {
        mVisibleDice = numVisible;

        // Make dice visible
        for (int i = 0; i < numVisible; i++) {
            mDiceImageViews[i].setVisibility(View.VISIBLE);
        }

        // Hide remaining dice
        for (int i = numVisible; i < MAX_DICE; i++) {
            mDiceImageViews[i].setVisibility(View.GONE);
        }
    }
}