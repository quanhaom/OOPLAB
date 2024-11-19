package garbage;

public class ConcatenationInLoops {
    public static void main(String[] args) {
        int n = 100000;
        long startTime, endTime;

        String result = "";
        startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            result += i;
        }
        endTime = System.currentTimeMillis();
        System.out.println("String (+ operator) time: " + (endTime - startTime) + "ms");

        StringBuilder sb = new StringBuilder();
        startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            sb.append(i);
        }
        endTime = System.currentTimeMillis();
        System.out.println("StringBuilder time: " + (endTime - startTime) + "ms");

        StringBuffer sbf = new StringBuffer();
        startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            sbf.append(i);
        }
        endTime = System.currentTimeMillis();
        System.out.println("StringBuffer time: " + (endTime - startTime) + "ms");
    }
}


//String (+ operator) time: 2976ms
//StringBuilder time: 7ms
//StringBuffer time: 9ms
