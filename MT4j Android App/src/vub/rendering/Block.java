package vub.rendering;

import java.util.Vector;

import org.mt4j.MTAndroidApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.math.Vector3D;
/**
 * The class to render a block.
 * @author Ayrton Vercruysse
 *
 */
public class Block extends Renderer<vub.ast.Block>{
	MTRectangle open;
	MTRectangle close;
	MTRectangle bar1;
	MTRectangle bar2;
	MTRectangle[] commas;
	Vector<Renderer<?>> children;
	vub.ast.Node ast;
		/**
		 * Initializes the class
		 * @param mtApplication	
		 * @param ast		The AST of the node.
		 * @param children	A vector with the rendered childeren of the node.
		 */
	public Block(MTAndroidApplication mtApplication, vub.ast.Block ast, Vector<Renderer<?>> children) {
		super(mtApplication, ast);
		this.ast = ast;
		open = makeTextArea(mtApplication, "{");
		close = makeTextArea(mtApplication, "}");
		bar1 = makeTextArea(mtApplication, "|");
		bar2 = makeTextArea(mtApplication, "|");
		this.children = children;
		commas = new MTRectangle[children.size()]; 
		for (int i = 0; i < children.size(); i++){
			commas[i] = makeTextArea(mtApplication, ",");
		}
	}
	@Override
	public void display(MTRectangle parent,Vector3D position) {
		Vector3D newPos;
		parent.addChild(drawing);
		float openHeight;
		float closeHeight;
		float contentHeight;
		float openWidth;
		float barWidth;
		float width;
		float commaWidth;
		drawing.setPositionRelativeToParent(position);
		drawing.addChild(open);
		drawing.addChild(close);
		drawing.addChild(bar1);
		drawing.addChild(bar2);
        openHeight = open.getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
        openWidth = open.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
        closeHeight = close.getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
		barWidth = bar1.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
        commaWidth = commas[0].getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		newPos = new Vector3D(openWidth, 0);
        bar1.setPositionRelativeToParent(newPos);
        width = openWidth+barWidth;
        for(int i = 0; i < children.size()-1; i++){
        	Renderer<?> child = children.get(i);
        	child.display(drawing, new Vector3D (width, 0));
        	width = width + child.getWidth();
        	if(i < children.size()-2){
        		drawing.addChild(commas[i]);
        		commas[i].setPositionRelativeToParent(new Vector3D(width,0));
        		width = width + commaWidth;
        	}
        }
        bar2.setPositionRelativeToParent(new Vector3D(width,0));
        children.lastElement().display(drawing, new Vector3D(0,openHeight));
		contentHeight = children.lastElement().getHeight();
        newPos.setY(openHeight+contentHeight);
        newPos.setX(0);
		close.setPositionRelativeToParent(newPos);
		drawing.setHeightLocal(openHeight+contentHeight+closeHeight);
	}
	@Override
	public MTRectangle display(){
		RenderManager renderManager= new RenderManager(mtApplication, ast);
		MTRectangle childRectangle;
		renderManager.render(open, "next", false);
		renderManager.render(bar1, "next", false);
		for(int i = 0; i < children.size()-2; i++){
        	Renderer<?> child = children.get(i);
        	childRectangle = child.display();
        	renderManager.render(childRectangle, "next", false);
        	renderManager.render(commas[i], "next", false);
        }
		childRectangle = children.get(children.size()-2).display();
		renderManager.render(bar2, "next", false);
		renderManager.render(childRectangle, "next", false);
		childRectangle = children.get(children.size()-1).display();
		renderManager.render(childRectangle, "next", false);
		return renderManager.render(close, "next", false);
		
	}
	@Override
	public void unselect() {
		((MTRectangle)drawing).setStrokeColor(white);
	}
}

