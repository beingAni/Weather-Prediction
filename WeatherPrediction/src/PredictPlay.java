import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

/**
 * This program reads from arff dataset file or from the user and predicts the
 * value of the unlabeled attribute .It trains the model using 10 cross split
 * validation and calculates the accuracy of the built model and also displays
 * the built decision tree
 */
public class PredictPlay {

	private static final int numberOfFolds = 10; // n-cross validation

	private static Scanner sc = new Scanner(System.in); // to input values

	/**
	 * Reads a dataset file
	 * 
	 * @param filename
	 *            from which the data has to be read
	 * @return inputReader
	 */
	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;

		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}

		return inputReader;
	}

	/**
	 * returns Evaluation object for predictions
	 * 
	 * @param model
	 * @param trainingSet
	 * @param testingSet
	 * @return
	 * @throws Exception
	 */

	public static Evaluation evaluate(Classifier model, Instances trainingSet,
			Instances testingSet) throws Exception {
		Evaluation evaluation = new Evaluation(trainingSet);
		model.buildClassifier(trainingSet);
		evaluation.evaluateModel(model, testingSet);
		return evaluation;
	}

	/**
	 * 
	 * @param predictions
	 * @return
	 */
	public static double calculateAccuracy(FastVector predictions) {
		double correct = 0;

		for (int i = 0; i < predictions.size(); i++) {

			NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
			if (np.predicted() == np.actual()) {
				correct++;
			}
		}

		return 100 * correct / predictions.size();
	}

	public static Instances[][] crossValidationSplit(Instances data,
			int numberOfFolds) {
		Instances[][] split = new Instances[2][numberOfFolds];

		for (int i = 0; i < numberOfFolds; i++) {
			split[0][i] = data.trainCV(numberOfFolds, i);
			split[1][i] = data.testCV(numberOfFolds, i);

		}

		return split;
	}

	public static void main(String[] args) throws Exception {
		// getting the present working directory
		String pwd = System.getProperty("user.dir");
		// System.out.println(pwd);
		BufferedReader datafile = readDataFile(pwd + "/weather.arff");
		BufferedReader testfile = readDataFile(pwd + "/predictweather.arff");

		Instances data = new Instances(datafile);
		Instances testdata = null;

		System.out
				.println("Do you want to\n [1] Use your own Instances \n [2] Label an unlabeled dataset from a file \n [3] Use sample file ");
		int choice = sc.nextInt();
		switch (choice) {
		case 1:
			testdata = addInstances();
			// System.out.println(testdata.toString());
			break;
		case 2:
			System.out.println("Enter filename : ");
			String str = sc.next();

			testfile = readDataFile(str);
			testdata = new Instances(testfile);

			break;
		case 3:
			testdata = new Instances(testfile);
			break;
		default:
			System.out.println("Wrong Option ! Exiting !");
			System.exit(0);
			break;
		}

		// setting the class index
		data.setClassIndex(data.numAttributes() - 1);
		testdata.setClassIndex(testdata.numAttributes() - 1);

		// Using a J48() classifier
		Classifier model = new J48();

		// Do 10-split cross validation
		Instances[][] split = crossValidationSplit(data, 10);

		// Separate split into training and testing set
		Instances[] trainingSplits = split[0];
		Instances[] testingSplits = split[1];

		// Collect every group of predictions for current model in a FastVector
		FastVector predictions = new FastVector();

		System.out.println();

		// For each training-testing split pair, train and test the classifier
		for (int i = 0; i < trainingSplits.length; i++) {
			Evaluation validation = evaluate(model, trainingSplits[i],
					testingSplits[i]);
			model.buildClassifier(trainingSplits[i]);
			predictions.appendElements(validation.predictions());
		}

		// find the correct predicted percentage
		double accuracy = calculateAccuracy(predictions);

		System.out.println("Accuracy of the model is "
				+ String.format("%.2f%%", accuracy) + "\n");

		// Classification based on the given training data
		for (int i = 0; i < testdata.numInstances(); i++) {
			Instance instance = testdata.instance(i);
			System.out.println("instance : " + (i + 1) + " ");
			System.out.println(instance.toString());
			

			System.out.print("Predicted value : ");
			if (model.classifyInstance(instance) == 0.0) {
				System.out.println("yes");
			} else if (model.classifyInstance(instance) == 1.0) {
				System.out.println("no");
			} else {
			}
System.out.println();
		}

		// display
		J48 cls = new J48();
		cls = (J48) model;
		System.out.println("Do you want to view the J48 tree ?? {yes/no}");

		String DISPLAY = sc.next();
		if (DISPLAY.equals("yes") || DISPLAY.equals("y") || DISPLAY.equals("Y")
				|| DISPLAY.equals("YES"))
			display(cls);
		else
			System.exit(0);

	}

	/**
	 * Build a fresh test data
	 * 
	 * @return customData
	 */
	private static Instances addInstances() {

		// Numeric
		Attribute Attribute1 = new Attribute("MaxTemp");
		// Numeric
		Attribute Attribute2 = new Attribute("Mean_Temp");
		// Numeric
		Attribute Attribute3 = new Attribute("MinTemp");
		// Numeric
		Attribute Attribute4 = new Attribute("MaxHumidity");
		// Numeric
		Attribute Attribute5 = new Attribute("MeanHumidity");
		// Numeric
		Attribute Attribute6 = new Attribute("MinHumidity");
		// Numeric
		Attribute Attribute7 = new Attribute("WindSpeed");
		// Numeric
		Attribute Attribute8 = new Attribute("CloudCover");

		// Declare play [class attribute] along with its values
		FastVector NominalValues = new FastVector(2);
		NominalValues.addElement("yes");
		NominalValues.addElement("no");
		Attribute Attribute9 = new Attribute("play", NominalValues);

		// Create the attribute list
		FastVector AttributesList = new FastVector(9);
		AttributesList.addElement(Attribute1);
		AttributesList.addElement(Attribute2);
		AttributesList.addElement(Attribute3);
		AttributesList.addElement(Attribute4);
		AttributesList.addElement(Attribute5);
		AttributesList.addElement(Attribute6);
		AttributesList.addElement(Attribute7);
		AttributesList.addElement(Attribute8);
		AttributesList.addElement(Attribute9);

		System.out
				.println("Enter the max number of instances that you want to add :");
		int NumInstances = sc.nextInt();

		// Create the empty training set
		Instances customData = new Instances("My_New_Weather", AttributesList,
				NumInstances);

		// Set class index - "play"
		customData.setClassIndex(customData.numAttributes() - 1);

		int choice = 0;
		do {
			int Htemp, Ltemp, MeanHumd, Meantemp, HHumd, LHumd, WindS, CloudCover;

			// Enter the values for the instance
			System.out.println("Enter High Temp (in F)");
			Htemp = sc.nextInt();

			System.out.println("Enter Low Temp (in F)");
			Ltemp = sc.nextInt();

			Meantemp = (Htemp + Ltemp) / 2;

			System.out.println("Enter the Max Humidity ");
			HHumd = sc.nextInt();

			System.out.println("Enter the Min Humidity ");
			LHumd = sc.nextInt();

			MeanHumd = (HHumd + LHumd) / 2;

			System.out.println("Enter Wind Speed (in km/h)");
			WindS = sc.nextInt();

			System.out.println("Enter the CLoudCover (0-8 Oktas) ");
			CloudCover = sc.nextInt();

			// Create the instance
			Instance iNew = new Instance(9);
			iNew.setValue((Attribute) AttributesList.elementAt(0), Htemp);
			iNew.setValue((Attribute) AttributesList.elementAt(1), Ltemp);
			iNew.setValue((Attribute) AttributesList.elementAt(2), Meantemp);
			iNew.setValue((Attribute) AttributesList.elementAt(3), HHumd);
			iNew.setValue((Attribute) AttributesList.elementAt(4), LHumd);
			iNew.setValue((Attribute) AttributesList.elementAt(5), MeanHumd);
			iNew.setValue((Attribute) AttributesList.elementAt(6), WindS);
			iNew.setValue((Attribute) AttributesList.elementAt(7), CloudCover);

			// add the instance
			customData.add(iNew);

			System.out
					.println("Do you want to continue [1 for yes , 0 for no ] :");
			choice = sc.nextInt();
		} while (choice == 1);

		// add the created instance set
		System.out.println("This is your created dataset");
		System.out.print(customData.toString());

		// return customData
		return customData;
	}

	/**
	 * Displays the model in tree format
	 * 
	 * @param a
	 *            J48 classifier modal
	 * @return Displays a decision tree
	 * @throws Exception
	 */
	private static void display(J48 cls) throws Exception {
		// TODO Auto-generated method stub
		final javax.swing.JFrame jf = new javax.swing.JFrame

		("Weka Classifier Tree Visualizer: J48");
		jf.setSize(1200, 1000);
		jf.getContentPane().setLayout(new BorderLayout());
		TreeVisualizer tv = new TreeVisualizer(null, cls.graph(),
				new PlaceNode2());
		jf.getContentPane().add(tv, BorderLayout.CENTER);
		jf.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				jf.dispose();
			}
		});

		jf.setVisible(true);
		tv.fitToScreen();
	}
}
