clear
echo "Welcome : "
echo "Compiling ...."
javac -classpath "../library/weka.jar:../library/weka-src.jar" PredictPlay.java
echo "Running...."
java  -classpath "../library/weka.jar:../library/weka-src.jar:../src/" PredictPlay
