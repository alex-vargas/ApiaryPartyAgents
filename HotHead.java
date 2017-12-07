package apiaryparty;

import java.util.Random;

/**
 * Attacker agent based on the ApiaryProject: https://github.com/osveliz/ApiaryParty.
 * If the attacker has enough budget to probe for honeypots and perform a normal attack, it will probe all nodes
 * Attacker will check how many super attacks she can perform, 
 * She will perform superattack if the superattack cost is less than three times of a normal attack (i.e., 3*8 = 24)
 * AND if she can do more than 2 superattacks it will do a normal attack
 * Else (budget <= (Parameter.SUPERATTACK_RATE * 2 + 1) && budget >= Parameter.SUPERATTACK_RATE) perform superattack
 *
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
		//If there are no available nodes or budget to perform attacks, end game
        if(availableNodes.size() < 1 || (budget < Parameters.ATTACK_RATE && budget < Parameters.SUPERATTACK_RATE))
            return new AttackerAction(AttackerActionType.END_TURN,0);
        int nodeId;
        //default attack type is normal attack
		AttackerActionType type = AttackerActionType.ATTACK;
		//If there's budget to probe for honeypots, do it
		if(Parameters.ATTACKER_RATE >= Parameters.ATTACKER_RATE + Parameters.PROBE_HONEY_RATE &&
			budget <= Parameters.PROBE_HONEY_RATE)
	        for(Node x: availableNodes)
			{
				if (x.getHoneyPot() == -1)
				{
					nodeId = x.getNodeID();
					type = AttackerActionType.PROBE_HONEYPOT;
					return new AttackerAction(type, nodeId);
				}			
			}

		int randNumber = r.nextInt(availableNodes.size());
		Node mNodeAttack = null;
		Node uncertainNode = availableNodes.get(randNumber);

		//Select node if there's one that is not a honeypot
		for(int i = 0; i<availableNodes.size(); i++){
			uncertainNode = availableNodes.get(i);
			if(uncertainNode.getHoneyPot() == 0){
				mNodeAttack = uncertainNode;
				break;
			}
			else if(uncertainNode.getHoneyPot() == -1)
				mNodeAttack = uncertainNode;
		}
		//if there are no nodes to attack (rest of nodes in network are honeypots) end game
        if(mNodeAttack == null)
            return new AttackerAction(AttackerActionType.END_TURN,0);
		nodeId = mNodeAttack.getNodeID();
		int superattackThreshold = Parameters.ATTACKER_RATE * 3;
		//if there are budget to perform no more than 2 superattacks execute a superattack

		if(Parameters.SUPERATTACK_RATE < superattackThreshold)
			if(budget <= (Parameters.SUPERATTACK_RATE * 2 + 1) && budget >= Parameters.SUPERATTACK_RATE)
				type = AttackerActionType.SUPERATTACK;
		return new AttackerAction(type, nodeId);
	}

	/**
	 * The game master is giving you the result of the action.
	 * @param  lastNode the node successfully attacked
	 */
	protected void result(Node lastNode) {
		
	}
}
