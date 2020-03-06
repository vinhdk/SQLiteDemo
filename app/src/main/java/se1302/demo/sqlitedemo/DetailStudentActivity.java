package se1302.demo.sqlitedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import se1302.demo.sqlitedemo.models.StudentModel;
import se1302.demo.sqlitedemo.services.StudentService;

public class DetailStudentActivity extends AppCompatActivity {
    EditText edtName, edtCore;
    RadioGroup rdGraduate;
    StudentModel student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_student);
        edtName = findViewById(R.id.edtName);
        edtCore = findViewById(R.id.edtCore);
        StudentService studentService = new StudentService(this);
        Intent intent = this.getIntent();
        student = studentService.getById(intent.getStringExtra("id"));
        edtName.setText(student.getName());
        edtCore.setText(student.getCore() + "");
        rdGraduate = (RadioGroup) findViewById(R.id.rdGraduate);
        ((RadioButton) findViewById(student.isGradute() == 0 ? R.id.graduated : R.id.studying)).setChecked(true);
        rdGraduate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.graduated) {
                    student.setGradute(0);
                } else {
                    student.setGradute(1);
                }
            }
        });
    }

    public void clickToUpdateStudent(View view) {
        student.setName(edtName.getText().toString());
        student.setCore(Integer.parseInt(edtCore.getText().toString()));
        StudentService studentService = new StudentService(this);
        Intent intent = this.getIntent();
        boolean check = studentService.update(student);
        if (check) {
            Toast.makeText(this, "Cập nhật học sinh thành công", Toast.LENGTH_SHORT).show();
            this.setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Cập nhật học sinh thất bại", Toast.LENGTH_SHORT).show();
            this.setResult(RESULT_CANCELED, intent);
            finish();
        }
    }

}
