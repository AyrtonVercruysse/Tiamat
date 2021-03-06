package rendering;

import java.util.Vector;

import org.mt4j.MTAndroidApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.math.Vector3D;
/**
 * The class to render a begin.
 * @author Ayrton Vercruysse
 *
 */
public class Begin extends Renderer<AST.Begin>{
	MTRectangle open;
	MTRectangle close;
	Vector<Renderer<?>> children;
		/**
		 * The initialisation of the class.
		 * @param mtApplication	
		 * @param ast			The AST of the begin.
		 * @param children		A vector with the rendered children of the node.
		 */
	public Begin(MTAndroidApplication mtApplication, AST.Begin ast, Vector<Renderer<?>> children) {
		super(mtApplication, ast);
		System.out.println("In den beginne");
		open = makeText(mtApplication, "{");
		close = makeText(mtApplication, "}");
		this.children = children;
	}
	@Override
	public void display(MTRectangle parent,Vector3D position) {
		Vector3D newPos;
		parent.addChild(drawing);
		float openHeight;
		float closeHeight;
		float height;
		drawing.setPositionRelativeToParent(position);
		drawing.addChild(open);
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
