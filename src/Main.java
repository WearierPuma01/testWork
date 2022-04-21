import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        String inputPath = "D:\\lng-big.csv";
        //String inputPath = "lng.csv";
        String outputPath = "newlng.csv";
        int columns = 3;
        GroupLines.groupToSetsFromCsv(inputPath, outputPath, columns);
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date1 = new Date();
        System.out.println(dateFormat1.format(date1));
    }
}
