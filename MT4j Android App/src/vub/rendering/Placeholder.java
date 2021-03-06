package vub.rendering;

import org.mt4j.MTAndroidApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.math.Vector3D;
/**
 * A class to render a placeholder.
 * @author Ayrton Vercruysse
 *
 */
public class Placeholder extends Renderer<vub.ast.Placeholder>{
	MTTextArea textArea;
	RenderManager renderManager;
	vub.ast.Node ast;
	/**
	 * The initialsation of this class.
	 * @param mtApplication
	 * @param ast	The AST of this node.
	 */
	public Placeholder(MTAndroidApplication mtApplication, vub.ast.Placeholder ast) {
		super(mtApplication, ast);
		this.ast = ast;
		String contentName = node.getName();
		textArea = makeTextArea(mtApplication, contentName, red);
		textArea.setFontColor(red);
		drawing.setName("placeholder");
	}
	@Override
	public void display(MTRectangle parent, Vector3D position) {
		float width;
		float height;
		parent.addChild(drawing);
		drawing.addChild(textArea);
		drawing.setPositionRelativeToParent(position);
		width = textArea.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		height = textArea.getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
		textArea.setPositionRelativeToParent(new Vector3D(1,1));
		drawing.setHeightLocal(height+2);
		drawing.setWidthLocal(width+2);
	}
	@Override
	public MTRectangle display(){
		renderManager= new RenderManager(mtApplication, ast);
		System.out.println("Render In Placeholderrender");
		return renderManager.render(textArea, "under", false);
	}
}