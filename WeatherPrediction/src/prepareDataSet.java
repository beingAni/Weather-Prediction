import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
/**
 * This program creates a valid arff dataset
 * from a given CSV text file 
 *
 */
public class prepareDataSet {
	public static void main(String args[]) throws IOException {
		BufferedReader br = null;
		String[] files = { "January", "February", "March", "April", "May",
				"June", "July", "August", "September", "October", "November",
				"December" };
		String str;
		String[] year = { "2013", "2014" };

		File file = new File(
				"/home/giri/223gb/Projects/WeatherPrediction/WeatherPrediction/weather.arff");

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		bw.write("@relation weather");
		bw.newLine();
		bw.newLine();

		bw.write("@attribute MaxTemp numeric");
		bw.newLine();
		bw.write("@attribute MeanTemp numeric");
		bw.newLine();
		bw.write("@attribute MinTemp numeric");
		bw.newLine();
		bw.write("@attribute MaxHumidity numeric");
		bw.newLine();
		bw.write("@attribute MeanHumidity numeric");
		bw.newLine();
		bw.write("@attribute MinHumidity numeric");
		bw.newLine();
		bw.write("@attribute WindSpeed numeric");
		bw.newLine();
		bw.write("@attribute CloudCover numeric");
		bw.newLine();
		bw.write("@attribute play {yes,no}");
		bw.newLine();// .newLine();
		bw.write("@data");
		bw.newLine();

		for (int a = 0; a < files.length; a++)
			for (int b = 0; b < year.length; b++) {
				str = files[a] + year[b];
				try {
					br = new BufferedReader(new FileReader(
							"/home/giri/223gb/Projects/WeatherPrediction/WeatherPrediction/data/" + str));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					str = "";
					continue;
				}

				// for(String s)
				System.out.println("Read : " + str);
				String[] info = null;
				int i = 0;
				String sCurrentLine;
				sCurrentLine = br.readLine();

				i = 1;
				try {
					while ((sCurrentLine = br.readLine()) != null) {
						info = sCurrentLine.split(",");

						// for(String s : info){
						// if(s.equals("")){
						// System.out.println("123123");
						// }
						// }

						if (info.length > 3) {
							
							if (!(info[1].equals("")))
								bw.write(info[1]);
							else
								bw.write("?");

							bw.write(',');

							if (!(info[2].equals("")))
								bw.write(info[2]);
							else
								bw.write("?");
							
							bw.write(',');

							if (!(info[3].equals("")))
								bw.write(info[3]);
							else
								bw.write("?");

							bw.write(',');

							if (!(info[7].equals("")))
								bw.write(info[7]);
							else
								bw.write("?");
							
							bw.write(',');

							if (!(info[8].equals("")))
								bw.write(info[8]);
							else
								bw.write("?");

							bw.write(',');

							if (!(info[9].equals("")))
								bw.write(info[9]);
							else
								bw.write("?");

							bw.write(',');

							if (!(info[16].equals("")))
								bw.write(info[16]);
							else
								bw.write("?");

							bw.write(',');

							if (!(info[20].equals("")))
								bw.write(info[20]);
							else
								bw.write("?");

							bw.write(',');

							// whether to play or not 
							Random r = new Random();
							if (r.nextInt(2) == 1) {
								System.out.println("yes");
								bw.write("yes");
							} else {
								System.out.println("no");
								bw.write("no");
							}

							// Enter a newline at the end
							bw.newLine();
							i++;
						}

					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("DONE" + str);

				str = "";
			}
		bw.close();
	}
}