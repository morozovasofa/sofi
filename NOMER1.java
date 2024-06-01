package org.example;
public class NOMER1{public static void massive(int[] mas) {
        int i,j;
        for (i=mas.length-1;i>=0;i--) {
            boolean t=true;
            for (j=0;j<i;j++) {
                if (mas[j]>mas[j+1]) {
                    int temp=mas[j];
                    mas[j]=mas[j+1];
                    mas[j+1]=temp;
                    t=false;
                }
            }
            if (t)
                break;
            System.out.print (mas[i]+" ");
        }
    }
    public static void main(String[] args) {
        int[] vot={13,4,5,6,12,2,3};
        massive(vot);
    }
}