package aspects;

import java.util.WeakHashMap;
import figures.FigureElement;
import figures.Group;
import figures.gui.FigureSurface;

public aspect Ex2a_b {

	private WeakHashMap<Group, FigureSurface> perSubjectCanvas = new WeakHashMap<Group, FigureSurface>();
	
	pointcut addToGroup(FigureElement fe, Group gr): execution(* Group.add(..)) && this(fe) && target(gr);
	
	pointcut canvasCreation(): execution(FigureSurface.new(..));
	
	after(FigureElement fe, Group gr):addToGroup(fe, gr){
		FigureSurface canvas = perSubjectCanvas.get(gr);
		Ex2a_a.aspectOf().addObserver(fe, canvas);
		canvas.repaint(); 
	}

	after(FigureSurface fs): canvasCreation() && this(fs){
		perSubjectCanvas.put(fs.getCanvas(), fs);
	}
}