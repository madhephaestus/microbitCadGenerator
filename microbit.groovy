import eu.mihosoft.vrl.v3d.parametrics.*;
import java.util.stream.Collectors;
import com.neuronrobotics.bowlerstudio.vitamins.Vitamins;
import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Cube;
import eu.mihosoft.vrl.v3d.Cylinder
CSG generate(){
	String type= "microbit"
	if(args==null)
		args=["elecfreak"]
	// The variable that stores the current size of this vitamin
	StringParameter size = new StringParameter(	type+" Default",args.get(0),Vitamins.listVitaminSizes(type))
	HashMap<String,Object> measurments = Vitamins.getConfiguration( type,size.getStrValue())

	def massCentroidXValue = measurments.massCentroidX
	def massCentroidYValue = measurments.massCentroidY
	def massCentroidZValue = measurments.massCentroidZ
	def massKgValue = measurments.massKg
	def priceValue = measurments.price
	def sourceValue = measurments.source
	for(String key:measurments.keySet().stream().sorted().collect(Collectors.toList())){
		println "microbit value "+key+" "+measurments.get(key);
}
	// Stub of a CAD object
	CSG motherboard;
	def mbX = 40
	
	if(args.get(0).contentEquals("elecfreak"))
		motherboard = new Cube(mbX,58.12,24.53).toCSG().toZMin()
	CSG microbit = new Cube(11.65,51.60,42).toCSG()
					.toZMin().movez(motherboard.getTotalZ())
					.toXMin()
	double stud = 8;
	CSG pin = new Cylinder(4.8/2.0+0.2, stud).toCSG()
	CSG knotch = new Cylinder(6.2/2.0, 1).toCSG()
	CSG hole = pin.union(knotch).toZMax().movex(mbX/2-stud/2)
	CSG fl = hole.movey(stud*1.5)
	CSG fr = hole.movey(-stud*1.5)
	CSG f = fr.union(fl);
	CSG b = f.movex(-stud*4)
	
	return CSG.unionAll(motherboard,microbit,f,b)
		.setParameter(size)
		.setRegenerate({generate()})
}
return generate() 