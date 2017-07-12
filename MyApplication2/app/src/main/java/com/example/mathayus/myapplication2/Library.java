package com.example.mathayus.myapplication2;

import java.util.ArrayList;

class Library{
    static double[] arrayMaker(int lengthToMake, double iVal){
        double[] ans = new double[lengthToMake];
        for(int i=0;i<lengthToMake;i++){
            ans[i] = iVal;
        }
        return ans;
    }

    static double[] conv(double[] a, double[] b){
        int l1 = a.length;
        int l2 = b.length;
        double ans[] = new double[l1+l2-1];
        for(int i=0;i<l2;i++){
            for(int j=0;j<l1;j++){
                ans[i+j] += b[i]*a[j];
            }
        }
        return ans;
    }
     static double[] filter13_3(double[] b, double[] a, double[] x){
        int n = x.length;
        double[] ans = new double[n];
        for(int i=0;i<n;i++){
            if(i==0){
                ans[i] = (b[0]/a[0]) * x[i];
            }else if(i==1){
                ans[i] = (b[0]/a[0]) * x[i] + (b[1]/a[0])*x[i-1] - (a[1]/a[0]) * ans[i-1];
            }else{
                for(int j=0; j<Math.min(i+1, 13); j++){
                    ans[i]+=(b[j]/a[0]) * x[i-j];
                }
                ans[i]-=(a[1]/a[0]) * ans[i-1];
                ans[i]-=(a[2]/a[0]) *  ans[i-2];
            }
        }
        return ans;
    }
     static double mean(double[] x){
        int l = x.length;
        double acc = 0;
        for(double entry: x){
            acc += entry;
        }
        return acc/l;
    }
     static double max(double[] x){
        double ans = 0;
        for(double entry: x){
            if(entry > ans){
                ans = entry;
            }
        }
        return ans;
    }
     static double[] abs(double[] x){
        int l = x.length;
        double ans[] = new double[l];
        for(int i=0;i<l;i++){
            if(x[i]<0){
                ans[i] = -x[i];
            }else{
                ans[i] = x[i];
            }
        }
        return ans;
    }
     static double[] arrayDivider(double[] x, double d){
        int l = x.length;
        double ans[] = new double[l];
        for(int i=0;i<l;i++){
            ans[i] = x[i]/d;
        }
        return ans;
    }
     static double[] arraySubtractor(double[] x, double d) {
         int l = x.length;
         double ans[] = new double[l];
         for (int i = 0; i < l; i++) {
             ans[i] = x[i] - d;
         }
         return ans;
     }

     static double[] diff(double[] x){
        int l = x.length;
        double[] ans = new double[l-1];
        for(int i=0; i<l-1; i++){
            ans[i] = x[i+1] - x[i];
        }
        return ans;
    }
     static ArrayList<Integer> find(double[] x, double toFind){
        int l = x.length;
        ArrayList<Integer> ans = new ArrayList<>();
        for(int i=0;i<l;i++){
            if(x[i]==toFind){
                ans.add(i);
            }
        }
        return ans;
    }
     static double[] arrayComparer(double[] x, double limit){
        int l = x.length;
        double[] ans = new double[l];
        for(int i=0;i<l;i++){
            if(x[i]<=limit){
                ans[i] = 0;
            }else{
                ans[i] = 1;
            }
        }
        return ans;
    }
     static double[] arrayMerger(double[] x, double[] y){
        int l1 = x.length;
        int l2 = y.length;
        double[] ans = new double[l1+l2];
         System.arraycopy(x,0,ans,0,l1);
         System.arraycopy(y,0,ans,l1,l2);
        return ans;
    }
     static double[] arrayMaxAndIndex(double[] x, int from, int to){
        int l= x.length;
        int index = from;
        double[] ans = new double[2];
        double maxVal = x[from];
        for(int i=from; i<Math.min(to, l-1); i++){
            if(x[i]>maxVal){
                maxVal = x[i];
                index = i;
            }
        }
        ans[0] = maxVal;
        ans[1] = index;
        return ans;
    }
    static double[] arrayMinAndIndex(double[] x, int from, int to){
        int l= x.length;
        int index = from;
        double[] ans = new double[2];
        double minVal = x[from];
        for(int i=from; i<Math.min(to, l-1); i++){
            if(x[i]<minVal){
                minVal = x[i];
                index = i;
            }
        }
        ans[0] = minVal;
        ans[1] = index;
        return ans;
    }
    static double[] arrayMultiplier(double[] x, double d){
        int l = x.length;
        double ans[] = new double[l];
        for(int i=0;i<l;i++){
            ans[i] = x[i]*d;
        }
        return ans;
    }
    static int getInflectionPoint(double[] x, int start, int end, int step){
        int answer=-1;
        for(int i=start; i<=Math.min(end, x.length-1); i++){
            if(x[i-step]*x[i+step]<0){
                answer = i;
                break;
            }
        }
        return answer;
    }
    static double[] getSubArray(double[] x, ArrayList<Integer> index){
        double[] ans = new double[index.size()];
        for(int i=0;i<index.size();i++){
            int ind = index.get(i);
            ans[i] = x[ind];
        }
        return ans;
    }
    static double[] getSubArray2(ArrayList<Double> x, ArrayList<Integer> index){
        double[] ans = new double[index.size()];
        for(int i=0;i<index.size();i++){
            int ind = index.get(i);
            ans[i] = x.get(ind);
        }
        return ans;
    }
    static double sumArray(double[] x, int start, int end){
        double ans = 0.0;
        for(int i=start; i<=end; i++){
            ans += x[i];
        }
        return ans;
    }
    static double[] twoArrayAdder(double[] x, double[] a){
        int l = x.length;
        double ans[] = new double[l];
        for(int i=0;i<l;i++){
            ans[i] = x[i]+a[i];
        }
        return ans;
    }
    static double[] twoArrayDivider(double[] x, double[] a){
        int l = x.length;
        double ans[] = new double[l];
        for(int i=0;i<l;i++){
            ans[i] = x[i]/a[i];
        }
        return ans;
    }
    static double[] twoArraySubtractor(double[] x, double[] a){
        int l = x.length;
        double ans[] = new double[l];
        for(int i=0;i<l;i++){
            ans[i] = x[i]-a[i];
        }
        return ans;
    }
}
