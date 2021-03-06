package vub.rendering;

import java.util.Vector;

import org.mt4j.MTAndroidApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;

import vub.ast.Definition;
import vub.ast.Node;
import vub.tiamat.StartTiamat;
import vub.tiamat.Tiamat;
import vub.menus.miniMenu;
import vub.rendering.Renderer;

public class RenderManager extends Renderer {
	private float width = 0;
	private float heigth = 0;
	private int indentionsteps;
	private float nodeWidth;
	private float nodeHeigth;
	Vector3D next = new Vector3D(0,0);
	Vector3D under = new Vector3D(0,0);
	vub.ast.Node ast;
	MTPolygon node;
	//MTPolygon parent;
	//MTRectangle drawing;

	public RenderManager(MTAndroidApplication mtApplication, vub.ast.Node ast) {
		super(mtApplication, ast);
		this.ast = ast;
		//this.parent = parent;
		//this.drawing = super.drawing;
		//drawing.setNoFill(true);
		//drawing.setAnchor(PositionAnchor.UPPER_LEFT);
		//parent.addChild(drawing);
		// TODO Auto-generated constructor stub
	}

	public  MTRectangle render(MTRectangle node, String type, Boolean indent, Boolean resetIndent) {
		if (resetIndent)
			indentionsteps = 0;
		return render(node, type, indent);
	}

	public MTRectangle render(final MTRectangle node, String type, Boolean indent) {
		this.node = node;
		/*if(StartTiamat.selected == null){
			StartTiamat.general.setFillColor(white);
			
			System.out.println("RenderRender");
			
		}else StartTiamat.general.setFillColor(green);*/
		StartTiamat.general.setFillColor(white);
		System.out.println("Kleur" + drawing.getFillColor());
		if(ast == StartTiamat.selected){
			System.out.println("Ja Hoor!"+ ast);
			drawing.setNoFill(false);
			drawing.setStrokeColor(red);
			//StartTiamat.general.setFillColor(green);
			System.out.println("Kleur" + drawing.getFillColor());
			
			//node.setFillColor(blue);
			
		}//else node.setFillColor(white);
		//if (ast == StartTiamat.selected){
		//	node.setNoFill(false);
		//	node.setStrokeColor(green);
		//}
		
		System.out.println("Renderer: Render");
		
		nodeHeigth = node.getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
		nodeWidth = node.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		drawing.addChild(node);
		if (indent)
			indentionsteps++;

		if (type == "next") {
			if (width > 700){
				indentionsteps++;
				under();
			}else{
				next();
			}
		} else if (type == "under") {
			under();
		} else {
			System.out.println("RenderManager: Wrong placementtype");
		}
		
		drawing.setHeightLocal(heigth+2);
		drawing.setWidthLocal(width+2);
		//super.drawing.setHeightLocal(5);
		//super.drawing.setWidthLocal(5);
		//super.drawing.setFillColor(green);
		Vector3D newPos = node.getPosition(TransformSpace.RELATIVE_TO_PARENT);
		newPos.addLocal(new Vector3D(1,1));
		node.setPositionRelativeToParent(newPos);
		/*if(selected){
			drawing.setStrokeColor(red);	
			drawing.setFillColor(white);
		}
		*/
		//drawing.setFillColor(blue); // And the color gets added.
		//
		//drawing.setNoFill(false);
		
		
		
		return drawing;
	}
	

	private void unindent() {
		indentionsteps = 0;
	}

	private void under() {
		Vector3D newPos = new Vector3D(indentionsteps*50, heigth);
		node.setPositionRelativeToParent(newPos);
		heigth = heigth + nodeHeigth;
		if(width < nodeWidth){
			width = nodeWidth;
		}
	}

	private void next() {
		Vector3D newPos = new Vector3D(width, 0);
		node.setPositionRelativeToParent(newPos);
		width = width+nodeWidth;
		if(nodeHeigth > heigth){
			heigth = nodeHeigth;
		}
	}

	@Override
	public MTRectangle display() {
		return drawing;
				
	}

	@Override
	public void display(
			org.mt4j.components.visibleComponents.shapes.MTRectangle parent,
			Vector3D position) {
		
	}



}
