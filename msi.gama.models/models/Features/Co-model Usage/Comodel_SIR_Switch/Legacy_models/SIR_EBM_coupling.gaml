model SIR_EBM_coupling

import "SIR_EBM.gaml"
experiment SIR_EBM_coupling_exp type: gui parent: SIR_EBM_exp
{
	int get_num_S
	{
		return first(agent_with_SIR_dynamic).S;
	}

	int get_num_I
	{
		return first(agent_with_SIR_dynamic).I;
	}

	int get_num_R
	{
		return first(agent_with_SIR_dynamic).R;
	}

	action set_num_S_I_R (int numS, int numI, int numR)
	{
		unknown call;
		call <- set_num_S(numS);
		call <- set_num_I(numI);
		call <- set_num_R(numR);
	}

	action set_num_S (int num)
	{
		first(agent_with_SIR_dynamic).S <- num;
	}

	action set_num_I (int num)
	{
		first(agent_with_SIR_dynamic).I <- num;
	}

	action set_num_R (int num)
	{
		first(agent_with_SIR_dynamic).R <- num;
	}

	output
	{
	}

}