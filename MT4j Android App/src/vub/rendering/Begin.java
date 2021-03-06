package vub.rendering;

import java.util.Vector;

import org.mt4j.MTAndroidApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.math.Vector3D;
import vub.tiamat.Tiamat;
/**
 * The class to render a begin.
 * @author Ayrton Vercruysse
 *
 */
public class Begin extends Renderer<vub.ast.Begin>{
	MTRectangle open;
	MTRectangle close;
	Vector<Renderer<?>> children;
	RenderManager renderManager;
	vub.ast.Node ast;
		/**
		 * The initialisation of the class.
		 * @param mtApplication	
		 * @param ast			The AST of the begin.
		 * @param children		A vector with the rendered children of the node.
		 */
	public Begin(MTAndroidApplication mtApplication, vub.ast.Begin ast, Vector<Renderer<?>> children) {
		super(mtApplication, ast);
		this.ast=ast;
		open = makeTextArea(mtApplication, "{");
		close = makeTextArea(mtApplication, "}");
		this.children = children;
	}
	
	@Override
	public MTRectangle display(){
		renderManager= new RenderManager(mtApplication, ast);
		renderManager.render(open, "next", false);
		Renderer<?> child = children.get(0);
    	MTRectangle childRectangle = child.display();
    	renderManager.render(childRectangle,"next", false); 
		for(int i = 1; i < children.size(); i++){
			child = children.get(i);
        	childRectangle = child.display();
        	renderManager.render(childRectangle,"under", false); 
        	
		}
		return renderManager.render(close, "next", false);
	}
	@Override
	public void display(MTRectangle parent,Vector3D position) {
		Vector3D newPos;
		parent.addChild(drawing);
		float openHeight;
		float closeHeight;
		float height;
		drawing.setPositionRelativeToParent(position);
		//drawing.addChild(open);
		drawing.addChild(close);
		openHeight = open.getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
        closeHeight = close.getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
        height = openHeight;
		newPos = new Vector3D(0, openHeight);
		for(int i = 0; i < children.size(); i++){
        	Renderer<?> child = children.get(i);
        	child.display(drawing, new Vector3D (0, height));
        	height = height + child.getHeight();
        }
		newPos.setY(height);
		height = height + closeHeight;
		close.setPositionRelativeToParent(newPos);
		// The height of the drawing gets set.
		drawing.setHeightLocal(height);
		
	}
	
	@Override
	public void unselect() {
		((MTRectangle)drawing).setStrokeColor(white);
	}
}
