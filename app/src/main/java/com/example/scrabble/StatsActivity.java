package com.example.scrabble;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Collections;


public class StatsActivity extends AppCompatActivity {

    public String filter = "easy";
    private RecyclerView recyclerView; // RecyclerView для отображения лидерборда.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        TextView easyScoreFilter = findViewById(R.id.easyScoreFilter);
        TextView mediumScoreFilter = findViewById(R.id.mediumScoreFilter);
        TextView hardScoreFilter = findViewById(R.id.hardScoreFilter);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.fetchAllUsersEasyScore(new DatabaseHelper.UsersCallback() {
            @Override
            public void onUsersFetched(ArrayList<DatabaseHelper.Users> users) {
                LeaderboardAdapter adapter = new LeaderboardAdapter(users);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(DatabaseError error) {
                Log.w("StatsActivity", "Error fetching users: " + error.toException());
            }
        });

        easyScoreFilter.setOnClickListener(v -> databaseHelper.fetchAllUsersEasyScore(new DatabaseHelper.UsersCallback() {

            @Override
            public void onUsersFetched(ArrayList<DatabaseHelper.Users> users) {
                filter = "easy";
                LeaderboardAdapter adapter = new LeaderboardAdapter(users);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(DatabaseError error) {
                Log.w("StatsActivity", "Error fetching users: " + error.toException());
            }
        }));

        mediumScoreFilter.setOnClickListener(v -> databaseHelper.fetchAllUsersMediumScore(new DatabaseHelper.UsersCallback() {
            @Override
            public void onUsersFetched(ArrayList<DatabaseHelper.Users> users) {
                filter = "medium";
                LeaderboardAdapter adapter = new LeaderboardAdapter(users);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(DatabaseError error) {
                Log.w("StatsActivity", "Error fetching users: " + error.toException());
            }
        }));

        hardScoreFilter.setOnClickListener(v -> databaseHelper.fetchAllUsersHardScore(new DatabaseHelper.UsersCallback() {
            @Override
            public void onUsersFetched(ArrayList<DatabaseHelper.Users> users) {
                filter = "hard";
                LeaderboardAdapter adapter = new LeaderboardAdapter(users);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(DatabaseError error) {
                Log.w("StatsActivity", "Error fetching users: " + error.toException());
            }
        }));
    }

    public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

        private ArrayList<DatabaseHelper.Users> leaderboard;

        public LeaderboardAdapter(ArrayList<DatabaseHelper.Users> leaderboard) {
            this.leaderboard = leaderboard;
            Collections.reverse(leaderboard);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            DatabaseHelper.Users user = leaderboard.get(position);
            holder.usernameTextView.setText(user.username);
            if(filter.equals("easy")) {
                holder.scoreTextView.setText(String.valueOf(user.easyScore));
            }
            if(filter.equals("medium")) {
                holder.scoreTextView.setText(String.valueOf(user.mediumScore));
            }
            if(filter.equals("hard")) {
                holder.scoreTextView.setText(String.valueOf(user.hardScore));
            }

        }

        @Override
        public int getItemCount() {
            return leaderboard.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView usernameTextView;
            public TextView scoreTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                usernameTextView = itemView.findViewById(R.id.usernameTextView);
                scoreTextView = itemView.findViewById(R.id.scoreTextView);
            }
        }
    }


}
