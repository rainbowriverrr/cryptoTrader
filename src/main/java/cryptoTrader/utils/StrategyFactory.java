package cryptoTrader.utils;

public class StrategyFactory {
	
	protected static Strategy create(String strategyName) {
		
		//get the specific strategy letter from strategy name ex. "StrategyA" -> "A"
		
		char strategyLetter = strategyName.charAt(strategyName.length() -1);

		System.out.println(strategyLetter);
		
		switch(strategyLetter) {
		
		case 'A':
			return new StrategyA(strategyName);
		
		case 'B':
			return new StrategyB(strategyName);

		case 'C':
			return new EthBtcC(strategyName);

		case 'D':
			return new DogeD(strategyName);

		case 'E':
			return new AdaE(strategyName);
		}
		
		return null;
		
		
	}
}
