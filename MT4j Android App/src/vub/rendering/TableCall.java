package vub.rendering;

import java.util.Vector;

import org.mt4j.MTAndroidApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.math.Vector3D;
/**
 * A class to render a table call
 * @author Ayrton Vercruysse
 *
 */
public class TableCall extends Renderer<vub.ast.TableCall>{
	MTRectangle openBracket;
	MTRectangle closeBracket;
	MTRectangle name;
	Vector<Renderer<?>> children;
		/**
		 * The initialsation of this class
		 * @param mtApplication
		 * @param ast		The AST of this node.
		 * @param children	A vector with all children rendered.
		 */
	public TableCall(MTAndroidApplication mtApplication, vub.ast.TableCall ast, Vector<Renderer<?>> children) {
		super(mtApplication, ast);
		openBracket = makeText(mtApplication, "[");
		closeBracket = makeText(mtApplication, "]");
		name = makeText(mtApplication, ast.getName());
		this.children = children;
	}
	@Override
	public void display(MTRectangle parent,Vector3D position) {
		parent.addChild(drawing);
		float height;
		float width;
		Renderer<?> index = children.get(0);
		drawing.setPositionRelativeToParent(position);
		drawing.addChild(openBracket);
		drawing.addChild(closeBracket);
		drawing.addChild(name);
		width = name.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		openBracket.setPositionRelativeToParent(new Vector3D (width,0));
		width = openBracket.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		index.display(drawing, new Vector3D(width,0));
		width = width + index.getWidth();
		closeBracket.setPositionRelativeToParent(new Vector3D (width,0));
		width = width + closeBracket.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		height = openBracket.getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
		drawing.setHeightLocal(height);
		drawing.setWidthLocal(width);
	}
}
