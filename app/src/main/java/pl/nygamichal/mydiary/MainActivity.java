package pl.nygamichal.mydiary;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("pl.nygamichal.mydiary",MODE_PRIVATE);
        if (savedInstanceState == null)//przykladowo podczas obrócenia ekranu, nie nadpisywało, albo nie dodało wartosci do pełnych pól?
        {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.container, NotesFragment.getInstance(), "note_list"); // "note_list" <-zdefiniowany tag
            fragmentTransaction.commit();//dopiero teraz jest dodawany do konkretnego kontenera
        }
    }

    @Override protected void onResume(){
        super.onResume();
        NoteStorage notes = new Gson().fromJson(sharedPreferences.getString("notes", ""), NoteStorage.class);
    }

    public void OnClickFab(View view)
    {
        AlertDialog.Builder alertDialogBuild = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuild.setTitle("Add new note");
        View inflate = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_add, null, false);
        final EditText editText = (EditText) inflate.findViewById(R.id.inputNote);
        alertDialogBuild.setView(R.layout.dialog_add);
        alertDialogBuild.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface , int i) {
                String input = editText.getText().toString();
                addNewNote(input);
                dialogInterface.dismiss();//ukrwyamy poprzedni widok, jesli user nacisnie TAK
            }
        });
        alertDialogBuild.show();
    }

    public void addNewNote(String text)
    {
        Gson gson = new Gson();
        NoteStorage notes = gson.fromJson(sharedPreferences.getString("notes", ""), NoteStorage.class);
        if (notes == null)
        {
            notes = new NoteStorage();
        }
        notes.notes.add(new Note(text));
        sharedPreferences.edit().putString("notes",gson.toJson(notes)).apply();
    }
}
