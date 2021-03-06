package vub.rendering;

import java.util.Vector;

import org.mt4j.MTAndroidApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.math.Vector3D;

public class Definition extends Renderer<vub.ast.Definition>{
	MTRectangle def;
	MTRectangle colequal;
	Vector<Renderer<?>> children;
	vub.ast.Node ast;
		
	public Definition(MTAndroidApplication mtApplication, vub.ast.Definition ast, Vector<Renderer<?>> children) {
		super(mtApplication, ast);
		this.ast = ast;
		System.out.println("In den beginne");
		def = makeTextArea(mtApplication, "def");
		colequal = makeTextArea(mtApplication, ":=");
		this.children = children;
	}
	@Override
	public void display(MTRectangle parent,Vector3D position) {
		Vector3D newPos;
		parent.addChild(drawing);
		float defHeight;
		float width;
		float contentHeight;
		Renderer<?> functionName = children.get(0);
		Renderer<?> content = children.get(1);
		drawing.setPositionRelativeToParent(position);
		drawing.addChild(def);
		drawing.addChild(colequal);
        defHeight = def.getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
        width = def.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		newPos = new Vector3D(width, 0);
		functionName.display(drawing, newPos);
		width = width + functionName.getWidth();
		colequal.setPositionRelativeToParent(new Vector3D (width,0));
		content.display(drawing, new Vector3D(0,defHeight));
		contentHeight = content.getHeight();
		newPos = new Vector3D(0,defHeight);
		drawing.setHeightLocal(defHeight+contentHeight);
	}
	
	@Override
	public MTRectangle display(){
		RenderManager renderManager= new RenderManager(mtApplication, ast);
		Renderer<?> functionName = children.get(0);
		Renderer<?> content = children.get(1);
		renderManager.render(functionName.display(), "next", false);
		renderManager.render(def, "next", false);
		renderManager.render(colequal, "next", false);
		return renderManager.render(content.display(), "next", false);
	}
	
	@Override
	public void unselect() {
		drawing.setStrokeColor(white);
	}
}