package com.example.scrabble;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper {
    private final DatabaseReference scoresRef;

    public DatabaseHelper() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        scoresRef = database.getReference("user");
    }



    public void updateUserScore(final String username, final String difficulty, final int newScore) {
        scoresRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Users existingUser = userSnapshot.getValue(Users.class);
                        if (existingUser != null) {
                            int userEasyScore = existingUser.getEasyScore();
                            int userMediumScore = existingUser.getMediumScore();
                            int userHardScore = existingUser.getHardScore();
                            if(difficulty.equals("easy")) {
                                if (newScore > userEasyScore) {
                                    // Новый результат больше существующего, обновляем данные
                                    userSnapshot.getRef().child("easyScore").setValue(newScore);
                                }
                            }
                            if(difficulty.equals("medium")) {
                                if (newScore > userMediumScore) {
                                    // Новый результат больше существующего, обновляем данные
                                    userSnapshot.getRef().child("mediumScore").setValue(newScore);
                                }
                            }
                            if(difficulty.equals("hard")) {
                                if (newScore > userHardScore) {
                                    // Новый результат больше существующего, обновляем данные
                                    userSnapshot.getRef().child("hardScore").setValue(newScore);
                                }
                            }
                            Log.i("MyAppTag", "Запись обновлена");
                        }
                    }
                } else {
                    // Пользователя нет, создаем новую запись
                    DatabaseReference newUserRef = scoresRef.push();
                    Map<String, Object> newUserValues = new HashMap<>();
                    newUserValues.put("username", username);
                    if(difficulty.equals("easy")) {
                        newUserValues.put("easyScore", newScore);
                    } else {
                        newUserValues.put("easyScore", 0);
                    }
                    if(difficulty.equals("medium")) {
                        newUserValues.put("mediumScore", newScore);
                    } else {
                        newUserValues.put("mediumScore", 0);
                    }
                    if(difficulty.equals("hard")) {
                        newUserValues.put("hardScore", newScore);
                    } else {
                        newUserValues.put("hardScore", 0);
                    }

                    newUserRef.setValue(newUserValues);
                    Log.i("MyAppTag", "Запись добавлена");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибки
                System.out.println("Database error: " + databaseError.getMessage());
            }
        });
    }

    public void fetchAllUsersEasyScore(final UsersCallback usersCallback) {
        scoresRef.orderByChild("easyScore").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Users> userList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                usersCallback.onUsersFetched(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                usersCallback.onError(databaseError);
            }
        });
    }

    public void fetchAllUsersMediumScore(final UsersCallback usersCallback) {
        scoresRef.orderByChild("mediumScore").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Users> userList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                usersCallback.onUsersFetched(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                usersCallback.onError(databaseError);
            }
        });
    }

    public void fetchAllUsersHardScore(final UsersCallback usersCallback) {
        scoresRef.orderByChild("hardScore").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Users> userList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                usersCallback.onUsersFetched(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                usersCallback.onError(databaseError);
            }
        });
    }

    public interface UsersCallback {
        void onUsersFetched(ArrayList<Users> users);
        void onError(DatabaseError error);
    }
    public static class Users {
        public String username;
        public int easyScore;
        public int mediumScore;
        public int hardScore;

        // Конструктор без аргументов для Firebase
        public Users() {

        }

        public int getEasyScore() {
            return this.easyScore;
        }

        public int getMediumScore() {
            return this.mediumScore;
        }

        public int getHardScore() {
            return this.hardScore;
        }
    }

}