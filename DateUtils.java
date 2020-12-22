package application;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DateUtils {

	public static String getTimeToString(Timestamp timestamp)
	{
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy HH:mm");
		String time = format.format(timestamp);
		
		return "[" + time + "]";
	}
}
