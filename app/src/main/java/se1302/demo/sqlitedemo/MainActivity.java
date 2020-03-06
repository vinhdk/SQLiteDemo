package se1302.demo.sqlitedemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.*;

import se1302.demo.sqlitedemo.adapters.StudentAdapter;
import se1302.demo.sqlitedemo.models.*;
import se1302.demo.sqlitedemo.models.StudentModel;
import se1302.demo.sqlitedemo.services.StudentService;

public class MainActivity extends AppCompatActivity {
    private ListView listStudentView;
    private StudentModel selectedStudent;
    private Button btnCreate, btnDetail, btnDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listStudentView = (ListView) findViewById(R.id.listStudentView);
        listStudentView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listStudentView.setTextFilterEnabled(true);
        btnCreate = findViewById(R.id.btnCreate);
        btnDetail = findViewById(R.id.btnDetail);
        btnDelete = findViewById(R.id.btnDelete);

        listStudentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedStudent = (StudentModel)parent.getAdapter().getItem(position);
                btnDetail.setEnabled(true);
                btnDelete.setEnabled(true);
            }
        });
        this.loadStudent();
    }

    private void loadStudent() {
        btnDetail.setEnabled(false);
        btnDelete.setEnabled(false);
        StudentService studentService = new StudentService(this);
        ArrayList<StudentModel> result = studentService.getAll();
        listStudentView.setAdapter(new StudentAdapter(this, result));
    }

    public void clickToCreateNewStudent(View view) {
        Intent intent = new Intent(this, CreateStudentActivity.class);
        startActivityForResult(intent, 1006);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            this.loadStudent();

        }
    }

    public void clickToGetDetailOfStudent(View view) {
        Intent intent = new Intent(this, DetailStudentActivity.class);
        intent.putExtra("id", selectedStudent.getId());
        startActivityForResult(intent, 1007);
    }

    public void clickToDeleteStudent(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Lựa chọn")
                .setMessage("Bạn thật sự muốn xóa học sinh này?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        StudentService studentService = new StudentService(getApplicationContext());
                        boolean check = studentService.delete(selectedStudent.getId());
                        if (check) {
                            loadStudent();
                            Toast.makeText(getApplicationContext(), "Xóa học sinh thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Xóa học sinh thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }
}
