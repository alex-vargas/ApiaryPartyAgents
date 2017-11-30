package apiaryparty;

import java.util.Random;

/**
 * Attacker agent based on the ApiaryProject: https://github.com/osveliz/ApiaryParty.
 * Attacker will chose randomly a node, if the SV is less than the threshold it will perform a normal attack
 * If the SV is greater than the threshold it will scan other available node randomly
 * If no node is available given the threshold, it will perfom a super attack on the node with the weakest SV if possible
 * If not, it will perform a normal attack in the node that has the strongrest SV.
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
public class BongoBongo extends Attacker {

    private final static String attackerName = "BongoBongo";
    
    public Random r;

    private final int threshold = 15;

    /**
     * Constructor
     * @param defenderName defender's name
     * @param graphFile graph to read
     */
	public BongoBongo(String defenderName, String graphFile) {
		super(attackerName, defenderName, graphFile);
	}
	/**
	 * Default constructor do not change
	 */
	public BongoBongo(){
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
		Node mNodeToAttack  = null;
		
		for(Node mNode : availableNodes){
			if (mNode.getSv() == -1)
			{
				int nodeID = mNode.getNodeID();
				type = AttackerActionType.PROBE_POINTS;
				return new AttackerAction(type, nodeID);
			}
			if(mNode.getSv() < threshold){
				type = AttackerActionType.ATTACK;
				mNodeToAttack = mNode;
				break;
			}
		}
		if(mNodeToAttack == null){
			if(Parameters.SUPERATTACK_RATE < budget){ //Perform superattack on weakest node
				type = AttackerActionType.SUPERATTACK;
				for(Node mNode : availableNodes){
					if(mNodeToAttack == null || mNodeToAttack.getSv() > mNode.getSv())
						mNodeToAttack = mNode;
				}
			}else{ //Perform normal attack on strongest node
				type = AttackerActionType.ATTACK;
				for(Node mNode : availableNodes){
					if(mNodeToAttack == null || mNodeToAttack.getSv() < mNode.getSv())
						mNodeToAttack = mNode;
				}
			}
		}
		int nodeId = mNodeToAttack.getNodeID();
		return new AttackerAction(type, nodeId);
	}

	/**
	 * The game master is giving you the result of the action.
	 * @param  lastNode the node successfully attacked
	 */
	protected void result(Node lastNode) {
		
	}
}
