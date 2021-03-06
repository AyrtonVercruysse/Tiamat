package rendering;

import org.mt4j.MTAndroidApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import vub.tiamat.StartTiamat;
import vub.tiamat.Tiamat;

import AST.Node;
import Menus.miniMenu;

/**
 * The superclass of all the rendering classes.
 * 
 * @author Ayrton Vercruysse
 * 
 * @param <T>
 */
public abstract class Renderer<T extends Node> extends AbstractScene {
	MTRectangle drawing; // The drawing in which the rendering of the node
							// happens.
	miniMenu menu;
	//static IFont fontArial;
	protected T node; // The AST node that needs to be rendered.

	/**
	 * The initialization of this class.
	 * 
	 * @param mtApplication
	 * @param ast
	 *            The ast of the node that needs to be rendered.
	 */
	public Renderer(final MTAndroidApplication mtApplication, T ast) {
		super(mtApplication, "Node:" + ast.getClass().getSimpleName());
		node = ast;
		node.setComments(new Comments(mtApplication, ast));
		//fontArial = FontManager.getInstance().createFont(mtApplication, 
			//	"arial20.fnt",  
				//40, 	//Font size
				//black);	//Font outline color
		drawing = new MTRectangle(mtApplication, 0, 0, 200, 200);
		drawing.setNoFill(true);
		drawing.setAnchor(PositionAnchor.UPPER_LEFT);
		// The animation to be able to comment code.
		drawing.registerInputProcessor(new TapAndHoldProcessor(mtApplication,
				2000));
		drawing.addGestureListener(TapAndHoldProcessor.class,
				new TapAndHoldVisualizer(mtApplication, getCanvas()));
		drawing.addGestureListener(TapAndHoldProcessor.class,
				new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapAndHoldEvent th = (TapAndHoldEvent) ge;
						switch (th.getId()) {
						case TapAndHoldEvent.GESTURE_ENDED:
							if (th.isHoldComplete()) {
								if (drawing.isNoFill() == false) {
									drawing.setNoFill(true);
									AST.Node parent = node.getParent();
									System.out.println("Node" + node.isRoot());
									System.out.println("Parent" + parent);
									if (parent == null) {
										Tiamat.main = ((AST.Comment) node)
												.getContent();
									} else {
										parent.setChild(node,
												((AST.Comment) node)
														.getContent());
									}

								} else {
									drawing.setNoFill(false);
									drawing.setFillColor(green);
									AST.Node parent = node.getParent();
									AST.Node newNode = new AST.Comment(parent,
											node);
									if (parent == null) {
										Tiamat.main = newNode;
									} else {
										parent.setChild(node, newNode);
									}
								}
							}
							break;
						default:
							break;
						}
						return false;
					}
				});
		// The animation to be able to select a node.
		drawing.registerInputProcessor(new TapProcessor(mtApplication, 25,
				true, 350));
		drawing.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isTapped()) {
							if (StartTiamat.selected == null) {
								System.out.println("new selected");
								StartTiamat.selected = node;
								drawing.setStrokeColor(red);
								if(node.getComments().inUse() ){
									node.getComments().show();
								}
								//if ne value dan show comments.
								//comments.show();
							} else {
								if (StartTiamat.selected == node) {
									System.out.println("selected selected");
									drawing.setStrokeColor(white);
									if(StartTiamat.selected.getComments().inUse()){
										StartTiamat.selected.getComments().hide();
									}
									StartTiamat.selected = null;
								} else {
									System.out.println("other selected");
									drawing.setStrokeColor(red);
									RenderVisitor.mapping.get(
											StartTiamat.selected).unselect();
									StartTiamat.selected = node;
									if(node.getComments().inUse() ){
										node.getComments().show();
									}
								}
							}
						}
						if (te.isDoubleTap()){
							System.out.println("Dubbeltapped");
							
							if(menu == null){
							menu = new miniMenu(mtApplication, "minimenu");
							StartTiamat.menuNode = node;
							menu.show(drawing);
							}else{
								menu.hide();
								menu = null;
							
						}}
						return false;
					}
				});
	}

	// Some colors.
	static MTColor white = new MTColor(255, 255, 255);
	static MTColor black = new MTColor(0, 0, 0);
	static MTColor red = new MTColor(255, 0, 0);
	static MTColor blue = new MTColor(0, 0, 255);
	static MTColor green = new MTColor(0, 80, 0);
	

	
	/**
	 * The making of a text area.
	 * 
	 * @param mtApplication
	 * @param text
	 *            The text for the text area.
	 *            
	 * @return A MTTextArea gets returned.
	 */
	
	public static MTTextArea makeText(MTAndroidApplication mtApplication,
			String text) {

		
		MTTextArea temp = new MTTextArea(mtApplication, Tiamat.fontArial);
		temp.setAnchor(PositionAnchor.UPPER_LEFT);
		temp.setNoFill(true);
		temp.setText(text);
		temp.setPickable(false);
		return temp;
	}

	/**
	 * The making of a text area in a different color.
	 * 
	 * @param mtApplication
	 * @param text
	 *            The text of the text area.
	 * @param color
	 *            The color of the text area.
	 * @return Returns an MTTextArea.
	 */
	static MTTextArea makeTextArea(MTAndroidApplication mtApplication,
			String text, MTColor color) {
		MTTextArea temp = makeText(mtApplication, text); // A normal text area
															// get created.
		temp.setFillColor(color); // And the color gets added.
		temp.setNoFill(false);
		temp.setPickable(false);
		return temp;
	}

	/**
	 * The abstact of the display class. This class is made in all rendering
	 * classes.
	 * 
	 * @param parent
	 *            This is the parent drawing of the node.
	 * @param position
	 *            This is the position relative to the parent where this drawing
	 *            should be placed.
	 */
	abstract public void display(MTRectangle parent, Vector3D position);

	/**
	 * 
	 * @return Returns the height of the drawing
	 */
	public float getHeight() {
		return drawing.getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
	}

	/**
	 * 
	 * @return Returns the width of the drawing.
	 */
	public float getWidth() {
		return drawing.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
	}

	/**
	 * 
	 * @return Returns the postion of the drawing relative to his parent.
	 */
	public Vector3D getPosition() {
		return drawing.getPosition(TransformSpace.RELATIVE_TO_PARENT);
	}

	/**
	 * Unselects the selected node.
	 */
	public void unselect() {
		drawing.setStrokeColor(white);
	}

	@Override
	public void init() {
	}

	@Override
	public void shutDown() {
	}
}