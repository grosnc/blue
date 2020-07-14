package process;

import java.util.ArrayList;
import model.InputModel;
import model.OutputModel;

public class MainProcess extends AbstractMainProcess<InputModel,OutputModel>{

	@Override
	public ArrayList<OutputModel> process(ArrayList<InputModel> source) {
		ArrayList<OutputModel> list = new ArrayList<OutputModel>();
		for (InputModel input : source) {
			OutputModel output = new OutputModel();
			output.setName(input.getName());
			list.add(output);
		}
		return list;
	}
}
