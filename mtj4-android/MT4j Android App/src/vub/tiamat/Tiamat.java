package vub.tiamat;

import java.io.FileOutputStream;

import org.mt4j.MTAndroidApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import processing.core.PImage;
import rendering.RenderVisitor;
import rendering.Renderer;
import Menus.BeginMenu;
import Menus.DefinitionsMenu;
import Menus.FunctionsMenu;
import Menus.MyFunctionsMenu;
import Menus.OperationsMenu;
import Menus.VariablesMenu;
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

	public static AST.Node main = new AST.Begin(null); // The root node.
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
	/**
	 * Initializes a Tiamat instance.
	 * 
	 * @param mtApplication
	 *            The mtApplication on what it is made.
	 * @param name
	 *            The name of this surrouding.
	 */
	public Tiamat(MTAndroidApplication mtApplication, String name) {
		super(mtApplication, name);
		menuFont =FontManager.getInstance().createFont(mtApplication,"arial20.fnt", 12, MTColor.WHITE);
		makeTemplates();
		fontArial = FontManager.getInstance().createFont(mtApplication, 
					"courier20.fnt",  
					40, 	//Font size
					new MTColor(0, 0, 0));	//Font outline color
		Tiamat.mtApplication = mtApplication;
		Tiamat.name = name;
		StartTiamat.general = new MTRectangle(mtApplication, 0, 0, 1920, 3200);
		StartTiamat.general.setPickable(false);
		getCanvas().addChild(StartTiamat.general);
		beginMenu = new BeginMenu(mtApplication, name);
		functionsMenu = new FunctionsMenu(mtApplication, name);
		myFunctionsMenu = new MyFunctionsMenu(mtApplication, name);
		operationsMenu = new OperationsMenu(mtApplication, name);
		variablesMenu = new VariablesMenu(mtApplication, name);
		definitionsMenu = new DefinitionsMenu(mtApplication, name);
		visitor = new RenderVisitor(mtApplication);
		redraw();
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
									runAT at = new runAT();
									at.execute(main.toString());
								}});

							

						}
						return true;
					}
				});
		runButton.setAnchor(PositionAnchor.UPPER_LEFT);
		runButton.setPositionRelativeToParent(new Vector3D(1720, 1050));
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
								AST.Node parent = StartTiamat.selected
										.getParent();
								parent.setChild(StartTiamat.selected,
										new AST.Placeholder(parent, "deleted",
												false));
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
		return runButton;
	}

	/**
	 * The redraw function rerenders the entire screen.
	 */
	public static void redraw() {
		Vector3D pos = new Vector3D(250, 0); // The position where the nodes are

		StartTiamat.general.removeAllChildren(); // All currently used childeren
													// are removed.

		beginRenderer = (Renderer<?>) visitor.visit(main); // Rendering the AST
															// starting from the
												// root.
		beginRenderer.display(StartTiamat.general, pos); // Display the rendered
											// AST.
		beginMenu.Make(mtApplication, name, false); // Make the begin menu.

		StartTiamat.general.addChild(runButton(mtApplication)); // Add the run
												// button.
		StartTiamat.general.addChild(deleteButton(mtApplication)); // Add the
																	// delete
																	// button.
	}

	/**
	 * This function adds all the templates to the vectors which contains them.
	 */
	public void makeTemplates() {
		// Functions
		StartTiamat.functions.add(IfThenElse());
		StartTiamat.functions.add(WhenDiscovered());
		StartTiamat.functions.add(WheneverDiscovered());
		StartTiamat.functions.add(WhileDo());
		StartTiamat.functions.add(WhenBecomes());
		StartTiamat.functions.add(WheneverDisconnected());
		StartTiamat.functions.add(WheneverReconnected());
		StartTiamat.functions.add(PrintLine());

		// Definitions
		StartTiamat.definitions.add(Value());
		StartTiamat.definitions.add(Definition());
		StartTiamat.definitions.add(FunctionDefinition());
		StartTiamat.definitions.add(TableDefinition());
		StartTiamat.definitions.add(Begin());
		StartTiamat.definitions.add(Block());

		// Operations
		StartTiamat.operations.add(Plus());
		StartTiamat.operations.add(Minus());
		StartTiamat.operations.add(Devide());
		StartTiamat.operations.add(Multiply());
		StartTiamat.operations.add(LesserThan());
	}

	/**
	 * The implementation of the templates.
	 * */
	// Definitions
	public Templates Value() {
		AST.Node node = new AST.Value(null, "Empty");
		return new Templates("Value", node);
	}

	public Templates Definition() {
		AST.Node node = new AST.Definition(null);
		return new Templates("Definition", node);
	}

	public Templates FunctionDefinition() {
		AST.Node node = new AST.FunctionDefinition(null, 0);
		return new Templates("Function", node);
	}

	public Templates TableDefinition() {
		AST.Node node = new AST.TableDefinition(null);
		return new Templates("Table", node);
	}

	public Templates Begin() {
		AST.Node node = new AST.Begin(null);
		return new Templates("Begin", node);
	}

	public Templates Block() {
		AST.Node node = new AST.Block(null, 0);
		return new Templates("Block", node);
	}

	// Functions
	public Templates IfThenElse() {
		String names[] = { "if:", "then:", "else:" };
		String contents[] = { "predicate", "consequent", "alternative" };
		AST.Node node = new AST.Function(null, names, contents);
		return new Templates("If:Then:Else", node);
	}

	public Templates WhenDiscovered() {
		String names[] = { "when:", "discovered:" };
		String contents[] = { "predicate", "content" };
		AST.Node node = new AST.Function(null, names, contents);
		return new Templates("When:Discovered", node);
	}

	public Templates WheneverDiscovered() {
		String names[] = { "whenever:", "discovered:" };
		String contents[] = { "predicate", "content" };
		AST.Node node = new AST.Function(null, names, contents);
		return new Templates("Whenever:Discovered", node);
	}

	public Templates WhileDo() {
		String names[] = { "while:", "do:" };
		String contents[] = { "predicate", "content" };
		AST.Node node = new AST.Function(null, names, contents);
		return new Templates("While:Do", node);
	}

	public Templates WhenBecomes() {
		String names[] = { "when:", "becomes:" };
		String contents[] = { "predicate", "content" };
		AST.Node node = new AST.Function(null, names, contents);
		return new Templates("When:Becomes", node);
	}

	public Templates WheneverDisconnected() {
		String names[] = { "whenever:", "disconnected:" };
		String contents[] = { "predicate", "content" };
		AST.Node node = new AST.Function(null, names, contents);
		return new Templates("Whenever:Disconnected", node);
	}

	public Templates WheneverReconnected() {
		String names[] = { "whenever:", "Reconnected:" };
		String contents[] = { "predicate", "content" };
		AST.Node node = new AST.Function(null, names, contents);
		return new Templates("Whenever:Reconnected", node);
	}

	public Templates PrintLine() {
		String names[] = { "system.prinln" };
		String contents[] = { "content" };
		AST.Node node = new AST.Function(null, names, contents);
		return new Templates("PrintLine", node);
	}

	// Operations
	public Templates Plus() {
		AST.Node node = new AST.Operation(null, "+");
		return new Templates("+", node);
	}

	public Templates Minus() {
		AST.Node node = new AST.Operation(null, "-");
		return new Templates("-", node);
	}

	public Templates Devide() {
		AST.Node node = new AST.Operation(null, "/");
		return new Templates("/", node);
	}

	public Templates Multiply() {
		AST.Node node = new AST.Operation(null, "*");
		return new Templates("*", node);
	}
	public Templates LesserThan() {
		AST.Node node = new AST.Operation(null, "<");
		return new Templates("<", node);
	}

	@Override
	public void init() {

	}

	@Override
	public void shutDown() {

	}
}
