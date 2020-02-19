package com.taewon.cal.ui;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.taewon.cal.R;
import com.taewon.cal.calculate.Calculator;
import com.taewon.cal.db.DBControl;
import com.taewon.cal.db.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private EditText edittext = null;
    private TextView textview;
    private Button RDButtonUp;
    private Button RDButtonDown;

    // 툴바
    private Toolbar myToolbar;

    private int something_cal = 99;  // 초기 99 숫자 0 단일연산 1 괄호열기 및 삼각함수 및 로그 2 괄호닫기 3 루트 파워 4 팩토리얼 5
    private ArrayList<Integer> some_cal_before = new ArrayList<Integer>(); //이전 something_cal 저장
    private int bracket_open = 0; //연 괄호 개수 체크
    private int bracket_close = 0; //닫은 괄호 개수 체크

    // 현재시간 체크
    private long now = System.currentTimeMillis();
    private Date mDate;
    private SimpleDateFormat date;
    private String getTime;

    //db 인스턴스화 해서 액세스
    private DBHelper dbHelper;
    private DBControl dBcontrol;
    private SQLiteDatabase dbW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //툴바생성
        myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        //텍스트 출력부 결과, 숫자
        textview = (TextView) findViewById(R.id.cal_result);
        edittext = (EditText) findViewById(R.id.cal_text);
        edittext.setText("");
        textview.setText("");

        //db 인스턴스화 해서 액세스
        dbHelper = new DBHelper(getBaseContext());
        dbW = dbHelper.getWritableDatabase();
        dBcontrol = new DBControl(dbW);

        //시간 받기
        date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        mDate = new Date(now);
        getTime = date.format(mDate);

        //래디안 디그리 버튼
        RDButtonUp = (Button) findViewById(R.id.raddegUp);
        RDButtonUp.setOnClickListener(RD);
        RDButtonDown = (Button) findViewById(R.id.raddegDown);
        RDButtonDown.setOnClickListener(RD);

        //사칙연산 정의
        findViewById(R.id.minus).setOnClickListener(sansul);
        findViewById(R.id.plus).setOnClickListener(sansul);
        findViewById(R.id.mul).setOnClickListener(sansul);
        findViewById(R.id.div).setOnClickListener(sansul);
        findViewById(R.id.mod).setOnClickListener(sansul);

        //괄호 정의
        findViewById(R.id.left_bracket).setOnClickListener(bracket);
        findViewById(R.id.right_bracket).setOnClickListener(bracket);

        //삭제정의
        findViewById(R.id.clear).setOnClickListener(delete);
        findViewById(R.id.del).setOnClickListener(delete);

        //삼각함수 정의
        findViewById(R.id.sin).setOnClickListener(triFun);
        findViewById(R.id.cos).setOnClickListener(triFun);
        findViewById(R.id.tan).setOnClickListener(triFun);

        Button make_result = (Button) findViewById(R.id.make_result);
        make_result.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String may = RDButtonUp.getText().toString();
                String raw = edittext.getText().toString();

                //괄호갯수, 마지막에 들어갈 수 있는 문자인지 확인 - 예외처리
                if(raw == null || raw.equals("")){
                    textview.setText("수식을 입력하세요");
                    return;
                }

                if (v.getId() == R.id.make_result && bracket_open == bracket_close && "0123456789)!".contains(raw.substring(raw.length()-1,raw.length()))) {
                    Calculator calculator = new Calculator(raw);
                    String resultText = calculator.createResult(may);
                    textview.setText(resultText);
                    long wow = dBcontrol.insertColumn("n", getTime,raw,resultText);
                    System.out.println(wow);
                } else {
                    textview.setText("잘못된 식입니다.");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    // 사칙연산 클릭부분
    Button.OnClickListener sansul = new Button.OnClickListener() {
        public void onClick(View v) {
            if (something_cal == 0 || something_cal == 3) {
                switch (v.getId()) {
                    case R.id.minus:
                        edittext.setText(edittext.getText() + "-");
                        break;
                    case R.id.div:
                        edittext.setText(edittext.getText() + "÷");
                        break;
                    case R.id.mul:
                        edittext.setText(edittext.getText() + "×");
                        break;
                    case R.id.plus:
                        edittext.setText(edittext.getText() + "+");
                        break;
                    case R.id.mod:
                        edittext.setText(edittext.getText() + "%");
                        break;
                }
                some_cal_before.add(something_cal);
                something_cal = 1;
            } else {
                textview.setText("올바른 연산자가 아닙니다.");
            }
        }
    };

    //괄호 클릭부분
    Button.OnClickListener bracket = new Button.OnClickListener() {
        public void onClick(View v) {
            if (v.getId() == R.id.left_bracket) {
                if (something_cal == 0 || something_cal == 3 || something_cal == 5) {
                    textview.setText("올바른 괄호가 아닙니다.");
                } else {
                    edittext.setText(edittext.getText() + "(");
                    some_cal_before.add(something_cal);
                    something_cal = 2;
                    bracket_open++;
                }
            } else if (v.getId() == R.id.right_bracket) {
                if (!(something_cal == 0 || something_cal == 3)) {
                    textview.setText("올바른 괄호가 아닙니다.");
                } else {
                    edittext.setText(edittext.getText() + ")");
                    some_cal_before.add(something_cal);
                    something_cal = 3;
                    bracket_close++;
                }
            }
        }
    };

    //숫자 클릭부분
    public void numberInput(View v) {
        switch (v.getId()) {
            case R.id.one:
                edittext.setText(edittext.getText() + "1");
                break;
            case R.id.two:
                edittext.setText(edittext.getText() + "2");
                break;
            case R.id.three:
                edittext.setText(edittext.getText() + "3");
                break;
            case R.id.four:
                edittext.setText(edittext.getText() + "4");
                break;
            case R.id.five:
                edittext.setText(edittext.getText() + "5");
                break;
            case R.id.six:
                edittext.setText(edittext.getText() + "6");
                break;
            case R.id.seven:
                edittext.setText(edittext.getText() + "7");
                break;
            case R.id.eight:
                edittext.setText(edittext.getText() + "8");
                break;
            case R.id.nine:
                edittext.setText(edittext.getText() + "9");
                break;
            case R.id.zero:
                edittext.setText(edittext.getText() + "0");
                break;
            case R.id.point:
                edittext.setText(edittext.getText() + ".");
                break;
        }
        some_cal_before.add(something_cal);
        something_cal = 0;
    }

    //C, del 클릭부분
    Button.OnClickListener delete = new Button.OnClickListener() {
        public void onClick(View v) {
            if (v.getId() == R.id.clear) {
                edittext.setText("");
                textview.setText("");
                something_cal = 99;
                bracket_close = 0;
                bracket_open = 0;
                some_cal_before.clear();
            }
            if (v.getId() == R.id.del) {
                try{
                    if (edittext.getText().toString().charAt(edittext.getText().toString().length() - 1) == ')') {
                        bracket_close--;
                    } else if (edittext.getText().toString().charAt(edittext.getText().toString().length() - 1) == '(') {
                        bracket_open--;
                    }
                } catch(Exception e){
                    return;
                }
                edittext.setText(edittext.getText().toString().substring(0, edittext.getText().toString().length() - 1));
            }
        }
    };

    //삼각함수 클릭부분
    Button.OnClickListener triFun = new Button.OnClickListener() {
        public void onClick(View v) {
            some_cal_before.add(something_cal);
            switch (v.getId()) {
                case R.id.sin:
                    edittext.setText(edittext.getText() + "sin(");
                    something_cal = 2;
                    bracket_open++;
                    break;
                case R.id.cos:
                    edittext.setText(edittext.getText() + "cos(");
                    something_cal = 2;
                    bracket_open++;
                    break;
                case R.id.tan:
                    edittext.setText(edittext.getText() + "tan(");
                    something_cal = 2;
                    bracket_open++;
                    break;
            }
        }
    };

    // 라디안 부분 글자변환
    Button.OnClickListener RD = new Button.OnClickListener() {
        public void onClick(View v) {
            if (v.getId() == R.id.raddegUp) {
                CharSequence scout = RDButtonUp.getText();
                RDButtonUp.setText(RDButtonDown.getText());
                RDButtonDown.setText(scout);
            } else {
                CharSequence scout = RDButtonDown.getText();
                RDButtonDown.setText(RDButtonUp.getText());
                RDButtonUp.setText(scout);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_theme :
                Toast.makeText(getApplicationContext(), "테마", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_save :
                Toast.makeText(getApplicationContext(), "기록", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LineActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
