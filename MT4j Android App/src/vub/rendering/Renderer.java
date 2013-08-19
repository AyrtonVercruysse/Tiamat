package vub.rendering;

import java.util.HashMap;

import org.mt4j.MTAndroidApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.PickResult;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.AbstractComponentProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateEvent;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.zoomProcessor.ZoomEvent;
import org.mt4j.input.inputProcessors.componentProcessors.zoomProcessor.ZoomProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import vub.ast.Node;
import vub.menus.miniMenu;
import vub.tiamat.StartTiamat;
import vub.tiamat.Tiamat;

public abstract class Renderer<T extends Node> extends AbstractScene {
	MTRectangle drawing; // The drawing in which the rendering of the node happens.
	boolean selected = false;
	boolean turned = false;
	miniMenu menu;
	MTAndroidApplication mtApplication;
	float x_start;
	float y_start;

	// static IFont fontArial;

	protected T node; // The AST node that needs to be rendered.

	public Renderer(final MTAndroidApplication mtApplication, T ast) {
		super(mtApplication, "Node:" + ast.getClass().getSimpleName());
		node = ast;
		this.mtApplication = mtApplication;
		node.setComments(new Comments(mtApplication, ast));
		drawing = new MTRectangle(mtApplication, 0, 0, 1, 1);
		vub.tiamat.StartTiamat.mapping.put(drawing, this);
		drawing.setAnchor(PositionAnchor.UPPER_LEFT);
		drawing.setName("content");
		tap();
		tapandhold();
		rotate();
		zoom();
	}

	// Some colors.
	static MTColor white = new MTColor(255, 255, 255);
	static MTColor black = new MTColor(0, 0, 0);
	public static MTColor red = new MTColor(255, 0, 0);
	static MTColor blue = new MTColor(0, 0, 255);
	static MTColor green = new MTColor(0, 80, 0);
	static MTColor orange = new MTColor(255, 165, 0);

	public void tap() {
		drawing.registerInputProcessor(new TapProcessor(mtApplication, 25, true, 350));
		drawing.addGestureListener(TapProcessor.class, new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isTapped()) {
							if (StartTiamat.selected == null) {
								System.out.println("Selected!" + node);
								StartTiamat.selected = node;
								Tiamat.redraw();
								if (node.isRoot()) {
									StartTiamat.selected = null;
								}
							} else {
								if (StartTiamat.selected == node) {
									StartTiamat.selected = null;
									System.out.println("UnSelected!" + node);
									Tiamat.redraw();
								} else {
									StartTiamat.selected = node;
									Tiamat.redraw();
									if (node.isRoot()) {
										StartTiamat.selected = null;
									}
								}
							}
						}
						if (te.isDoubleTap()) {
							System.out.println("Dubbeltapped");
						/*	if (menu == null) {
								menu = new miniMenu(mtApplication, "minimenu");
								StartTiamat.menuNode = node;
								menu.show(drawing);
							} else {
								menu.hide();
								menu = null;
							}*/
						}
						return false;
					}
				});
	}

	public void tapandhold() {
		drawing.registerInputProcessor(new TapAndHoldProcessor(mtApplication, 2000));
		drawing.addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(mtApplication, getCanvas()));
		drawing.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
						TapAndHoldEvent th = (TapAndHoldEvent) ge;
						switch (th.getId()) {
						case TapAndHoldEvent.GESTURE_ENDED:
							if (th.isHoldComplete()) {
								System.out.println("Tapped and holded");
								node.getParent().setChild(node, (Node) StartTiamat.selected.clone());
								System.out.println("Tapped and holded2");
								Tiamat.redraw();
								System.out.println("Tapped and holded3");
								}
							
							break;
						default:
							break;
						}
						return false;
					}
				});

	}
	
	public void zoom() {
		drawing.registerInputProcessor(new ZoomProcessor(mtApplication));
		// drawing.addGestureListener(RotateProcessor.class, new
		// IGestureEventListener(mtApplication, getCanvas()));
		drawing.addGestureListener(ZoomProcessor.class,
				new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						ZoomEvent th = (ZoomEvent) ge;
						switch (th.getId()) {
						case ZoomEvent.GESTURE_UPDATED:
							// if (th.getRotationDegrees() > 1) {
							// th.setRotationDegrees(0);
							System.out.println("Zoomed yes!"+ th.getCamZoomAmount());

							// }else{
							// System.out.print("Rotaternot!");
							// System.out.print("Rotator" +
							// th.getRotationDegrees());
							// th.setRotationDegrees(0);

							// Tiamat.redraw();
							// }

						case ZoomEvent.GESTURE_ENDED:
							System.out.println("Zoomed");
						return false;
						}
						return false;
					}
				});

		
	}

	public void rotate() {

		for (AbstractComponentProcessor ip : drawing.getInputProcessors()) {
			if (ip instanceof ScaleProcessor) {
				drawing.unregisterInputProcessor(ip);
			}
		}
		drawing.removeAllGestureEventListeners(ScaleProcessor.class);
		/*
		 * for (AbstractComponentProcessor ip : drawing.getInputProcessors()){
		 * if (ip instanceof DragProcessor) {
		 * drawing.unregisterInputProcessor(ip); } }
		 * drawing.removeAllGestureEventListeners(DragProcessor.class);
		 */

		drawing.registerInputProcessor(new RotateProcessor(mtApplication));
		// drawing.addGestureListener(RotateProcessor.class, new
		// IGestureEventListener(mtApplication, getCanvas()));
		drawing.addGestureListener(RotateProcessor.class,
				new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						RotateEvent th = (RotateEvent) ge;
						switch (th.getId()) {
						case RotateEvent.GESTURE_UPDATED:
							// if (th.getRotationDegrees() > 1) {
							// th.setRotationDegrees(0);
							System.out.println("Rotator yest!"+ th.getRotationDegrees());
							if(th.getRotationDegrees() > 0.45){
								System.out.println("Rotator draaien");
								turned = true;
								
							}
						case RotateEvent.GESTURE_ENDED:
							if(turned){
								System.out.println("Rotator draaien2");
								//Tiamat.redraw();
								turned = false;
							}
							// }else{
							// System.out.print("Rotaternot!");
							// System.out.print("Rotator" +
							// th.getRotationDegrees());
							// th.setRotationDegrees(0);

							// Tiamat.redraw();
							// }
							return false;
						}
						return false;
					}
				});

		drawing.registerInputProcessor(new DragProcessor(mtApplication));
		drawing.addGestureListener(DragProcessor.class,
				new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						DragEvent th = (DragEvent) ge;
						Vector3D from = null;
						Vector3D to = null;
						switch (th.getId()) {
						case DragEvent.GESTURE_STARTED:
							from = th.getFrom();
							x_start = from.getX();
							y_start = from.getY();
							break;
						case DragEvent.GESTURE_ENDED:
							Tiamat.redraw();
							to = th.getTo();
							float x_dis = Math.abs(to.getX() - x_start);
							float y_dis = Math.abs(to.getY() - y_start);
							if (5 < x_dis || 5 < y_dis) {
								MTComponent test = StartTiamat.general.pick(to.getX(), to.getY()).getNearestPickResult();
								test.getID();
								if (test.getName().equals("deletebutton")) {
									vub.ast.Node parent = node.getParent();
									parent.setChild(node,
											new vub.ast.Placeholder(parent,
													"deleted", false));
									Tiamat.redraw();

								}
								if (test.getName().equals("content")) {
								}
								if (test.getName().equals("placeholder")) {
									Renderer bla = StartTiamat.mapping.get(test);
									vub.ast.Node selNode = bla.getNode();
									System.out.println("selNode:" + selNode + bla);
									//vub.ast.Node parent = selNode.getParent();
									//parent.setChild(selNode, node);
									//node.setParent(parent);
									//selNode.setParent(null);
								}
							}

							return false;
						}
						return false;
					}
				});
	}

	public static MTTextArea makeTextArea(MTAndroidApplication mtApplication,
			String text) {
		MTTextArea temp = new MTTextArea(mtApplication, Tiamat.fontArial);
		temp.setAnchor(PositionAnchor.UPPER_LEFT);
		temp.setNoFill(true);
		temp.setFontColor(black);
		temp.setText(text);
		temp.setPickable(false);
		return temp;
	}

	static MTTextArea makeTextArea(MTAndroidApplication mtApplication,
			String text, MTColor color) {
		MTTextArea temp = makeTextArea(mtApplication, text); // A normal text
																// area
																// get created.
		temp.setFillColor(color); // And the color gets added.
		temp.setNoFill(false);
		return temp;
	}

	abstract public void display(MTRectangle parent, Vector3D position);

	public float getHeight() {
		return drawing.getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
	}

	public float getWidth() {
		return drawing.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
	}

	public Vector3D getPosition() {
		return drawing.getPosition(TransformSpace.RELATIVE_TO_PARENT);
	}

	public void unselect() {
		drawing.setStrokeColor(white);
	}

	public T getNode() {
		return node;
	}

	@Override
	public void init() {
	}

	@Override
	public void shutDown() {
	}

	abstract public MTRectangle display();
}