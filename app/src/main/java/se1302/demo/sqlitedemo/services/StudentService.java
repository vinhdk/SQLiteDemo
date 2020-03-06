package se1302.demo.sqlitedemo.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import se1302.demo.sqlitedemo.helpers.BaseHelper;
import se1302.demo.sqlitedemo.models.ColumnModel;
import se1302.demo.sqlitedemo.models.StudentModel;
import se1302.demo.sqlitedemo.models.TableModel;

public class StudentService {
    private SQLiteDatabase db;
    private Context context;
    private BaseHelper studentHelper;
    private final String DATABASENAME = "StudyManagement.db";
    private TableModel studentTable;


    public StudentService(Context context) {
        this.context = context;
        ArrayList<ColumnModel> columns = new ArrayList<>();
        columns.add(new ColumnModel("id", "text", true, true));
        columns.add(new ColumnModel("name", "text", false, true));
        columns.add(new ColumnModel("core", "integer", false, true));
        columns.add(new ColumnModel("isGraduate", "bit", false, true));
        this.studentTable = new TableModel("Students", columns);
        this.studentHelper = new BaseHelper(context, DATABASENAME, 1, this.studentTable);
    }

    public StudentService open() throws Exception {
        db = studentHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public ArrayList<StudentModel> getAll() {
        ArrayList<StudentModel> students = new ArrayList<StudentModel>();
        try {
            this.open();
            Cursor cursor = db.query(studentTable.getName(), studentTable.getArrayColumnName(), null, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    students.add(new StudentModel(
                            cursor.getString(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getInt(cursor.getColumnIndex("core")),
                            cursor.getInt(cursor.getColumnIndex("isGraduate"))
                    ));
                    while (cursor.moveToNext()) {
                        StudentModel student = new StudentModel(
                                cursor.getString(cursor.getColumnIndex("id")),
                                cursor.getString(cursor.getColumnIndex("name")),
                                cursor.getInt(cursor.getColumnIndex("core")),
                                cursor.getInt(cursor.getColumnIndex("isGraduate"))
                        );
                        students.add(student);
                    }
                }
                cursor.close();
            }

        } catch (Exception e) {
            Log.d("StudentService", "Error at getAll with message: " + e.getMessage());
        } finally {
            this.close();
        }
        return students;
    }

    public StudentModel getById(String id) {
        StudentModel student = new StudentModel();
        try {
            this.open();
            Cursor cursor = db.query(studentTable.getName(), studentTable.getArrayColumnName(), studentTable.getPrimaryKeyColumnName() + " = ?", new String[]{id}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    student = new StudentModel(
                            cursor.getString(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getInt(cursor.getColumnIndex("core")),
                            cursor.getInt(cursor.getColumnIndex("isGraduate"))
                    );
                }
                cursor.close();
            }

        } catch (Exception e) {
            Log.d("StudentService", "Error at getById with message: " + e.getMessage());
        } finally {
            this.close();
        }
        return student;
    }

    public boolean create(StudentModel student) {
        boolean check = false;
        try {
            this.open();
            ContentValues content = new ContentValues();
            content.put("id", student.getId());
            content.put("name", student.getName());
            content.put("core", student.getCore());
            content.put("isGraduate", student.isGradute());
            check = db.insert(studentTable.getName(), null, content) > 0;
        } catch (Exception e) {
            Log.d("StudentService", "Error at create with message: " + e.getMessage());
        } finally {
            this.close();
        }
        return check;
    }

    public boolean update(StudentModel student) {
        boolean check = false;
        try {
            this.open();
            ContentValues content = new ContentValues();
            content.put("name", student.getName());
            content.put("core", student.getCore());
            content.put("isGraduate", student.isGradute());
            check = db.update(studentTable.getName(), content, studentTable.getPrimaryKeyColumnName() + " = ?", new String[]{student.getId()}) > 0;
        } catch (Exception e) {
            Log.d("StudentService", "Error at update with message: " + e.getMessage());
        } finally {
            this.close();
        }
        return check;
    }

    public boolean delete(String id) {
        boolean check = false;
        try {
            this.open();
            ContentValues content = new ContentValues();
            check = db.delete(studentTable.getName(), studentTable.getPrimaryKeyColumnName() + " = ?", new String[]{id}) > 0;
        } catch (Exception e) {
            Log.d("StudentService", "Error at update with message: " + e.getMessage());
        } finally {
            this.close();
        }
        return check;
    }

}
