package answers4;

public aspect Answer3 {
	
	pointcut ex3(): execution(* figures.Group.move(..));
	
	before(figures.Group group): ex3() && target(group){
		A.aspectOf(group).Var = null;
	}
}
