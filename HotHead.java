package apiaryparty;

import java.util.Random;

/**
 * Attacker agent based on the ApiaryProject: https://github.com/osveliz/ApiaryParty.
 * Attacker will check how many super attacks he can perform, if he can do more than 2 it will do a normal attack
 * Else (budget <= (Parameter.SUPERATTACK_RATE * 2 + 1) && budget >= Parameter.SUPERATTACK_RATE) perform superattack
 * IMPORTANT NOTE: 	Your attacker object will be recreated for every action. Because of this,
 * 					model your Attacker to only make a decision on the current information. Do
 * 					not try to use variables that will carry on in to the next makeSingleAction()
 * 
 * Make use of the three protected variables inherited from Attacker. These variables include:
 * protected ArrayList&lt;Node&gt; capturedNodes - a list of the already captured nodes
 * protected ArrayList&lt;Node&gt; availableNodes - a list of the available nodes for attacking and probing.
 * protected int budget - the current budget of the Attacker. Be careful that your next move will not cost more than your budget.
 * 
 * @author Alex Vargas
 */
public class HotHead extends Attacker {

    private final static String attackerName = "HotHead";
    
    public Random r;

    /**
     * Constructor
     * @param defenderName defender's name
     * @param graphFile graph to read
     */
	public HotHead(String defenderName, String graphFile) {
		super(attackerName, defenderName, graphFile);
	}
	/**
	 * Default constructor do not change
	 */
	public HotHead(){
		super(attackerName);
	}
	
	/**
	 * If you need to initialize anything, do it  here
	 */
	protected void initialize(){
		r = new Random();
	}

	/**
	 * This is called by the game master while your agent still has funds. You return an action.
	 * @return your action
	 */
	public AttackerAction makeAction() {
        if(availableNodes.size() < 1 || (budget < Parameters.ATTACK_RATE && budget < Parameters.SUPERATTACK_RATE))
            return new AttackerAction(AttackerActionType.INVALID,0);
		AttackerActionType type = AttackerActionType.ATTACK;
		if(budget <= (Parameters.SUPERATTACK_RATE * 2 + 1) && budget >= Parameters.SUPERATTACK_RATE)
			type = AttackerActionType.SUPERATTACK;
		int randNumber = r.nextInt(availableNodes.size());
		int nodeId = availableNodes.get(randNumber).getNodeID();
		return new AttackerAction(type, nodeId);
	}

	/**
	 * The game master is giving you the result of the action.
	 * @param  lastNode the node successfully attacked
	 */
	protected void result(Node lastNode) {
		
	}
}
