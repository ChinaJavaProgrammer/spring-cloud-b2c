package spring.cloud.user.util;


import java.util.Random;

/**
 * @ClassName: GenerateValidateCode
 * @Description: TODO
 * @author: dh
 * @date: 2019/11/1 10:34
 * Version   1.0
 **/
public class GenerateValidateCode {

    private static final String [] WORDS=new String[59];

    static{
        for(int i=0;i<(WORDS.length+2-1)/2;i++){
                int first=i*2;
                int next=first+1;
                int ascii= (first>=1 ?  WORDS[first-2].charAt(0)+1 : i+65);
                int lowerAscii= (first>=1 ?  WORDS[first-1].charAt(0)+1 : ascii+32) ;
            if(ascii>90 && WORDS[first-2].charAt(0)==90){
                ascii=49;lowerAscii=ascii+1;
            }else if(ascii>49 && ascii<57){
                ascii=WORDS[first-1].charAt(0)+1;
                lowerAscii=ascii+1;
            }else{
                if(((char)ascii)=='O'){
                    ascii+=1;
                    lowerAscii+=1;
                }
            }
            WORDS[first]=(char)ascii+"";
            if(first!=WORDS.length-1){
                WORDS[next]=(char)lowerAscii+"";
            }
        }

        Random random = new Random();
        for(int i=0;i<(WORDS.length+2-1)/2;i++){
            int num =random.nextInt(WORDS.length);
            int next = random.nextInt(WORDS.length);
            if(num!=next){
                String temp = WORDS[num];
                WORDS[num]=WORDS[next];
                WORDS[next]=temp;
            }else{
                i-=1;
            }

        }
    }


    public static String getCode(){
        Random random = new Random();
        String result="";
        for(int i=0;i<4;i++){
            result+=WORDS[random.nextInt(WORDS.length)];
        }
        return result;
    }


}
