package vub.tiamat;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.mt4j.MTAndroidApplication;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.gestureAction.DefaultPanAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanEvent;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanProcessorTwoFingers;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.text.Editable;
import android.widget.EditText;

import processing.core.PImage;
import vub.menus.BeginMenu;
import vub.menus.DefinitionsMenu;
import vub.menus.FunctionsMenu;
import vub.menus.MyFunctionsMenu;
import vub.menus.OperationsMenu;
import vub.menus.VariablesMenu;
import vub.rendering.RenderManager;
import vub.rendering.RenderVisitor;
import vub.rendering.Renderer;
import vub.templates.TemplateWriter;
import vub.templates.Templates;
import vub.templates.templateReader;
import vub.tiamat.StartTiamat;

/**
 * The Tiamat class. This class initializes and orchestrates the entire program.
 * 
 * @author Ayrton Vercruysse
 * 
 */
public class Tiamat extends AbstractScene {
	private static String imagePath = ""; // The image path to where the images
											// are stored.
	static Renderer<?> beginRenderer; // The renderer of the begin node
										// (the root).
	static RenderVisitor visitor; // The visitor to the renderers.
	
	public static vub.ast.Node main = new vub.ast.Begin((vub.ast.Node)null); // The root node.
	static MTAndroidApplication mtApplication;
	static String name;
	public static BeginMenu beginMenu;
	public static FunctionsMenu functionsMenu;
	public static MyFunctionsMenu myFunctionsMenu;
	public static OperationsMenu operationsMenu;
	public static VariablesMenu variablesMenu;
	public static DefinitionsMenu definitionsMenu;
	public static MTTextArea t;
	public static IFont menuFont;
	public static IFont fontArial;
	//AssetManager assetManager;
	static MTRectangle mapMenu;
	static MTRectangle listLabel;
	static MTListCell cell;
	static MTRectangle codeRectangle;
	static TemplateWriter b = new TemplateWriter();
	static String ActionText;
	static String ActionReply;
	templateReader teplateReade = new templateReader();
	
	/**
	 * Initializes a Tiamat instance.
	 * 
	 * @param mtApplication
	 *            The mtApplication on what it is made.
	 * @param name
	 *            The name of this surrouding.
	 */
	public Tiamat(MTAndroidApplication mtApplication, String name, AssetManager am) {
		super(mtApplication, name);
		//this.assetManager = am;
		this.mtApplication = mtApplication;
		menuFont =FontManager.getInstance().createFont(mtApplication,"arial20.fnt", 12, MTColor.WHITE);
		makeTemplates();
		run();		
		fontArial = FontManager.getInstance().createFont(mtApplication, 
					"courier20.fnt",  
					40, 	//Font size
					new MTColor(0, 0, 0));	//Font outline color
		Tiamat.mtApplication = mtApplication;
		Tiamat.name = name;
		StartTiamat.general = new MTRectangle(mtApplication, 0, 0, 1920, 1200);
		StartTiamat.general.setPickable(false);
		codeRectangle = new MTRectangle(mtApplication, 0,0, 1920, 3200);
		codeRectangle.setAnchor(PositionAnchor.UPPER_LEFT);
		//codeRectangle.setFillColor(new MTColor(0, 0, 0));
		codeRectangle.setNoFill(true);
		codeRectangle.setPositionRelativeToParent(new Vector3D(0,0));
		getCanvas().addChild(StartTiamat.general);
		StartTiamat.general.addChild(codeRectangle);
		beginMenu = new BeginMenu(mtApplication, name);
		functionsMenu = new FunctionsMenu(mtApplication, name);
		myFunctionsMenu = new MyFunctionsMenu(mtApplication, name);
		operationsMenu = new OperationsMenu(mtApplication, name);
		variablesMenu = new VariablesMenu(mtApplication, name);
		definitionsMenu = new DefinitionsMenu(mtApplication, name);
		visitor = new RenderVisitor(mtApplication);
		


		codeRectangle.unregisterAllInputProcessors();
		codeRectangle.registerInputProcessor(new PanProcessorTwoFingers(mtApplication));
		codeRectangle.addGestureListener(PanProcessorTwoFingers.class, new IGestureEventListener() {
			
            float maxY = 3000;
            float minY = 10;
            public boolean processGestureEvent(MTGestureEvent ge) {
                PanEvent de = (PanEvent)ge;
                Vector3D moveVector = de.getTranslationVector();
                if (de.getTarget() instanceof AbstractShape){
                    AbstractShape s = (AbstractShape)de.getTarget();
                    de.getTarget().translateGlobal(new Vector3D(0, moveVector.y));
                    Vector3D center = s.getCenterPointGlobal();
                    if (center.y > maxY){
                    	s.setPositionGlobal(new Vector3D(center.x, maxY));
                        //s.setPositionGlobal(new Vector3D(maxX, center.y));
                    }else if (center.y < minY){
                    	s.setPositionGlobal(new Vector3D(center.x, minY));
                        //s.setPositionGlobal(new Vector3D(minX, center.y));
                    }
                }
                return false;
            }
        });
	
		redraw();
	}
	
	public void run() {
		final AlertDialog.Builder alert = new AlertDialog.Builder(mtApplication);
		alert.setTitle("TIAMAT");
		ActionText = "Do you want to load the files?";
		alert.setMessage(ActionText);
		// Set an EditText view to get user input
		//final EditText input = new EditText(mtApplication);
		//alert.setView(input);
		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				templateReader.readSave(StartTiamat.assetManager);
			}
		});
		alert.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});
		mtApplication.runOnUiThread(new Runnable() {
			public void run() {
				// * The Complete ProgressBar does not appear
				alert.show();
			}
		});
	}
	
	public static void removeWhitespaceNodes(Element e) {
		NodeList children = e.getChildNodes();
		for (int i = children.getLength() - 1; i >= 0; i--) {
		Node child = children.item(i);
		if (child instanceof Text && ((Text) child).getData().trim().length() == 0) {
		e.removeChild(child);
		}
		else if (child instanceof Element) {
		removeWhitespaceNodes((Element) child);
		}
		}
		}

	/**
	 * The runButton makes a button what runs the made code true the AmbientTalk
	 * interpreter.
	 * 
	 * @param mtApplication
	 *            The mtApplication.
	 * @return Returns the runButton.
	 */
	public static MTImageButton runButton(final MTAndroidApplication mtApplication) {
		PImage arrow = mtApplication.loadImage(imagePath + "run.png");
		MTImageButton runButton = new MTImageButton(mtApplication, arrow);
		runButton.setNoStroke(true);
		if (MT4jSettings.getInstance().isOpenGlMode())
			runButton.setUseDirectGL(true);
		runButton.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isTapped()) {

							System.out.println("run clicked!");
							mtApplication.runOnUiThread(new Runnable() {
								public void run() {
									// * The Complete ProgressBar does not appear
									//runAT at = new runAT();
									try {
										 
										StartTiamat.at.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
									//at.execute(main.toString());
								}});

							

						}
						return true;
					}
				});
		runButton.setAnchor(PositionAnchor.UPPER_LEFT);
		runButton.setPositionRelativeToParent(new Vector3D(1720, 1050));
		runButton.setName("runbutton");
		return runButton;
	}

	/**
	 * Creates a delete button.
	 * 
	 * @param mtApplication
	 * @return Returns a delete button.
	 */
	public static MTImageButton deleteButton(MTAndroidApplication mtApplication) {
		PImage arrow = mtApplication.loadImage(imagePath + "trashcan.jpg");
		MTImageButton runButton = new MTImageButton(mtApplication, arrow);
		runButton.setNoStroke(true);
		if (MT4jSettings.getInstance().isOpenGlMode())
			runButton.setUseDirectGL(true);
		runButton.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isTapped()) {
							System.out.println("delete clicked");
							if (StartTiamat.selected == null) {
								System.out.println("Nothing selected");
							} else {
								vub.ast.Node parent = StartTiamat.selected.getParent();
								parent.setChild(StartTiamat.selected,
										new vub.ast.Placeholder(parent, "deleted", false));
								StartTiamat.selected = null;
								System.out.println("Deleting");
								redraw();
							}
						}
						return true;
					}
				});
		runButton.setAnchor(PositionAnchor.UPPER_LEFT);
		runButton.setPositionRelativeToParent(new Vector3D(1750, 900));
		runButton.setName("deletebutton");
		return runButton;
	}

	
	public static MTImageButton saveButton(MTAndroidApplication mtApplication) {
		PImage arrow = mtApplication.loadImage(imagePath + "save.jpg");
		MTImageButton runButton = new MTImageButton(mtApplication, arrow);
		runButton.setNoStroke(true);
		if (MT4jSettings.getInstance().isOpenGlMode())
			runButton.setUseDirectGL(true);
		runButton.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isTapped()) {
							System.out.println("save clicked");
							b.write2();
							//b.write(); 
						}
						return true;
					}
				});
		runButton.setAnchor(PositionAnchor.UPPER_LEFT);
		runButton.setPositionRelativeToParent(new Vector3D(1750, 800));
		runButton.setName("savebutton");
		return runButton;
	}
	
	
	
	/**
	 * The redraw function rerenders the entire screen.
	 */
	public static void redraw() {
		Vector3D pos = new Vector3D(250, 0); // The position where the nodes are

		codeRectangle.removeAllChildren(); // All currently used childeren
													// are removed.
		StartTiamat.general.removeAllChildren();
		StartTiamat.general.addChild(codeRectangle);
		System.out.println("Testertje: REDRAW");
	//	StartTiamat.general.addChild(mapMenu);

		beginRenderer = (Renderer<?>) visitor.visit(main); // Rendering the AST starting from the root.
		MTRectangle result = beginRenderer.display();
		//listLabel.addChild(result);
		codeRectangle.addChild(result);
		result.setPositionRelativeToParent(new Vector3D(250,20));
		//beginRenderer.display(codeRectangle, pos); // Display the rendered
											// AST.
		beginMenu.Make(mtApplication, name, false); // Make the begin menu.

		StartTiamat.general.addChild(runButton(mtApplication)); // Add the run button.
		StartTiamat.general.addChild(deleteButton(mtApplication)); // Add the delete button.
		StartTiamat.general.addChild(saveButton(mtApplication));
	}

	/**
	 * This function adds all the templates to the vectors which contains them.
	 */
	public void makeTemplates() {
		// Functions
	/*	StartTiamat.functions.add(IfThenElse());
		StartTiamat.functions.add(WhenDiscovered());
		StartTiamat.functions.add(WheneverDiscovered());
		StartTiamat.functions.add(WhileDo());
		StartTiamat.functions.add(WhenBecomes());
		StartTiamat.functions.add(WheneverDisconnected());
		StartTiamat.functions.add(WheneverReconnected());
		StartTiamat.functions.add(PrintLine());*/
		templateReader.read(StartTiamat.assetManager);

		// Definitions
	/*	StartTiamat.definitions.add(Value());
		StartTiamat.definitions.add(Definition());
		StartTiamat.definitions.add(FunctionDefinition());
		StartTiamat.definitions.add(TableDefinition());
		StartTiamat.definitions.add(Begin());
		StartTiamat.definitions.add(Block());*/

		// Operations
		//StartTiamat.operations.add(Plus());
		//StartTiamat.operations.add(Minus());
		//StartTiamat.operations.add(Devide());
		//StartTiamat.operations.add(Multiply());
		//StartTiamat.operations.add(LesserThan());
	}

	/**
	 * The implementation of the templates.
	 * */
	// Definitions
/*	public Templates Value() {
		vub.ast.Node node = new vub.ast.Value(null, "Empty");
		return new Templates("Value", node);
	}

	public Templates Definition() {
		vub.ast.Node node = new vub.ast.Definition(null);
		return new Templates("Definition", node);
	}

	public Templates FunctionDefinition() {
		vub.ast.Node node = new vub.ast.FunctionDefinition(null, 0);
		return new Templates("Function", node);
	}

	public Templates TableDefinition() {
		vub.ast.Node node = new vub.ast.TableDefinition(null);
		return new Templates("Table", node);
	}

	public Templates Begin() {
		vub.ast.Node node = new vub.ast.Begin(null);
		return new Templates("Begin", node);
	}

	public Templates Block() {
		vub.ast.Node node = new vub.ast.Block(null, 0);
		return new Templates("Block", node);
	}

	// Functions
	public Templates IfThenElse() {
		String names[] = { "if:", "then:", "else:" };
		String contents[] = { "predicate", "consequent", "alternative" };
		vub.ast.Node node = new vub.ast.Function(null, names, contents);
		return new Templates("If:Then:Else", node);
	}

	public Templates WhenDiscovered() {
		String names[] = { "when:", "discovered:" };
		String contents[] = { "predicate", "content" };
		vub.ast.Node node = new vub.ast.Function(null, names, contents);
		return new Templates("When:Discovered", node);
	}

	public Templates WheneverDiscovered() {
		String names[] = { "whenever:", "discovered:" };
		String contents[] = { "predicate", "content" };
		vub.ast.Node node = new vub.ast.Function(null, names, contents);
		return new Templates("Whenever:Discovered", node);
	}

	public Templates WhileDo() {
		String names[] = { "while:", "do:" };
		String contents[] = { "predicate", "content" };
		vub.ast.Node node = new vub.ast.Function(null, names, contents);
		return new Templates("While:Do", node);
	}

	public Templates WhenBecomes() {
		String names[] = { "when:", "becomes:" };
		String contents[] = { "predicate", "content" };
		vub.ast.Node node = new vub.ast.Function(null, names, contents);
		return new Templates("When:Becomes", node);
	}

	public Templates WheneverDisconnected() {
		String names[] = { "whenever:", "disconnected:" };
		String contents[] = { "predicate", "content" };
		vub.ast.Node node = new vub.ast.Function(null, names, contents);
		return new Templates("Whenever:Disconnected", node);
	}

	public Templates WheneverReconnected() {
		String names[] = { "whenever:", "Reconnected:" };
		String contents[] = { "predicate", "content" };
		vub.ast.Node node = new vub.ast.Function(null, names, contents);
		return new Templates("Whenever:Reconnected", node);
	}

	public Templates PrintLine() {
		String names[] = { "system.prinln" };
		String contents[] = { "content" };
		vub.ast.Node node = new vub.ast.Function(null, names, contents);
		return new Templates("PrintLine", node);
	}

	// Operations
	public Templates Plus() {
		vub.ast.Node node = new vub.ast.Operation(null, "+");
		return new Templates("+", node);
	}

	public Templates Minus() {
		vub.ast.Node node = new vub.ast.Operation(null, "-");
		return new Templates("-", node);
	}

	public Templates Devide() {
		vub.ast.Node node = new vub.ast.Operation(null, "/");
		return new Templates("/", node);
	}

	public Templates Multiply() {
		vub.ast.Node node = new vub.ast.Operation(null, "*");
		return new Templates("*", node);
	}
	public Templates LesserThan() {
		vub.ast.Node node = new vub.ast.Operation(null, "<");
		return new Templates("<", node);
	}*/

	@Override
	public void init() {

	}

	@Override
	public void shutDown() {

	}
}
