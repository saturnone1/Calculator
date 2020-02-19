package com.taewon.cal;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

//List에서 save num 의 숫자는 save_char의 연산자 숫자보다 딱 하나 많음을 이용.
//deep_q는 깊이및 계산크기 순서이다.

public class Calculator {

    private String raw;

    private ArrayList<Float>deep_q = new <Float>ArrayList(); // 깊이 및 순서 큐 생성
    private ArrayList<BigDecimal> saveNum = new <BigDecimal>ArrayList(); // 숫자 저장
    private ArrayList<Character> saveChar = new <Character>ArrayList(); //기호저장

    float deep = 0; //깊이 -> 계산의 범주적 순서 - 괄호만 계산
    float PM = 1; //덧뺄셈 - 1
    float MD = 2; //곱나눗셈 - 2 로 고정 % 포함한다
    float tri = 3; //삼각함수 3으로 고정
    float forBracket = 10; // 괄호의 깊이 증가율 10

    public Calculator(String raw) {
        this.raw = raw;
    }

    //연산자 읽어오기 - 여기서 우선순위 스택을 쌓는다.
    private void readStack(String raw) {
        for (int i = 0; i < raw.length(); i++) { //String의 마지막까지 스캔하기
            //연산자들 모음
            if ("+-".contains(raw.substring(i,i+1))) { //+-일때 취할 것
                deep_q.add(deep+PM);
                saveChar.add(raw.charAt(i));
            }
            else if ("×÷%".contains(raw.substring(i,i+1))) { //×÷%일때 취할 것
                deep_q.add(deep+MD);
                saveChar.add(raw.charAt(i));
            }

            //괄호 여닫기는 deep의 숫자만을 변경한다. sin, cos, tan, log, ln
            else if (raw.charAt(i) == '(') { //괄호열기
                deep = deep+forBracket;
            } else if (raw.charAt(i) == ')') { //괄호닫기
                deep = deep - forBracket; //괄호가 닫혔으므로 깊이 순서는 하나 감소
            }

            else if ("sct".contains(raw.substring(i,i+1))){ //삼각함수 있는지 확인하기!
                if("ioa".contains(raw.substring(i+1,i+2))){
                    saveChar.add(raw.charAt(i)); //sct넣어주기
                    deep_q.add(deep+tri); //깊이는 3
                }
            }
        }
    }

    // String을 숫자로 변환해서 저장하기
    private void Read_num(String raw) { //적어놓은 숫자들을 따서 저장.
        int index = 0;
        int begin = 0;
        int end;
        char tag = 'x'; // 숫자가 아닌 상황
        while(index < raw.length()){
            if("0123456789.".contains(raw.substring(index,index+1))){
                if(tag =='x')
                    begin = index;
                    tag = 'o';
                if(index == raw.length()-1){
                    BigDecimal save = new BigDecimal(raw.substring(begin));
                    saveNum.add(save);
                }
            }
            else{
                if(tag =='o'){
                    end = index;
                    BigDecimal save = new BigDecimal(raw.substring(begin, end));
                    saveNum.add(save);
                }
                else if(tag =='x'){
                    if("sct".contains(raw.substring(index,index+1))){
                        if("ioa".contains(raw.substring(index+1,index+2))){
                            BigDecimal save = new BigDecimal("1"); // 삼각함수일때 넣어줄 인덱스의 숫자는 1로 고정(앞에 배수가 안붙어 있을 때.)
                            saveNum.add(save);
                        }
                    }
                }
                tag = 'x';
            }
            index++;
        }
    }

    public String cal_main(String why) {// 메인 계산부분
        Read_num(raw);
        readStack(raw);

        while(!deep_q.isEmpty()){
            int deep_index = 0;
            float deep_max = 0; // 최대깊이

            //최대 deep_q 인덱스 찾아내기
            int i = 0;
            while(i < saveChar.size()){
                if(deep_max < deep_q.get(i)){
                    deep_max = deep_q.get(i);
                    deep_index = i;
                }
                i++;
            }

            //max의 index의 양쪽의 숫자를 받은 후 계산, 갱신 그리고 필요없어진 부분 삭제.
            BigDecimal a = saveNum.get(deep_index);
            BigDecimal b = saveNum.get(deep_index+1);
            BigDecimal c = new BigDecimal("0");

            //삼각함수 계산용 라디안 변환
            double num = Double.parseDouble(b.toString());
            if(why.contains("DEG")) {
                num = (num*Math.PI)/180;
            }

            //단순 사칙연산
            if(saveChar.get(deep_index) == '+'){ // 덧셈계산
                c = a.add(b, MathContext.DECIMAL32);
            }
            else if(saveChar.get(deep_index) == '-'){ // 뺄셈계산
                c = a.subtract(b,MathContext.DECIMAL32);
            }
            else if(saveChar.get(deep_index) == '×'){ //곱셈
                c = a.multiply(b,MathContext.DECIMAL32);
            }
            else if(saveChar.get(deep_index) == '÷'){ //몫
                c = a.divide(b, MathContext.DECIMAL32);
            }
            else if(saveChar.get(deep_index) == '%'){ //나머지
                c = a.remainder(b,MathContext.DECIMAL32);
            }

            //삼각함수 계산부분
            else if(saveChar.get(deep_index) == 's'){
                double cow = Math.sin(num);
                c = a.multiply(BigDecimal.valueOf(cow), MathContext.DECIMAL32);
            }
            else if(saveChar.get(deep_index) == 'c'){
                double cow = Math.cos(num);
                c = a.multiply(BigDecimal.valueOf(cow), MathContext.DECIMAL32);
            }
            else if(saveChar.get(deep_index) == 't'){
                double cow = Math.tan(num);
                c = a.multiply(BigDecimal.valueOf(cow), MathContext.DECIMAL32);
            }

            //갱신 후 삭제
            saveNum.set(deep_index, c);
            saveNum.remove(deep_index+1);
            saveChar.remove(deep_index);
            deep_q.remove(deep_index);
        }
        return saveNum.get(0).toString();
    }
}
