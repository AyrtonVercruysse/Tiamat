package aspects;

import ca.ubc.cs.spl.aspectPatterns.patternLibrary.ObserverProtocol;
import figures.Group;
import figures.Point;
import figures.gui.tools.LineDrawingTool;

public aspect Ex2b_a extends ObserverProtocol {

	declare parents: LineDrawingTool implements Subject;
	declare parents: Group implements Observer;

	protected pointcut subjectChange(Subject s): execution(* LineDrawingTool.createPoint(..)) && this(s);
	protected pointcut lineDrawToolCreated(LineDrawingTool ldt, Group gr):
		execution(LineDrawingTool.new(..)) &&
		this(ldt) && args(gr, int, int);

	@Override
	protected void updateObserver(Subject subject, Observer observer) {
		Point newPoint = ((LineDrawingTool) subject).getNewPoint();
		((Group) observer).add(newPoint);
	}
	
	before(LineDrawingTool ldt, Group gr): lineDrawToolCreated(ldt, gr){
		Ex2b_a.aspectOf().addObserver(ldt, gr);
	}

}
