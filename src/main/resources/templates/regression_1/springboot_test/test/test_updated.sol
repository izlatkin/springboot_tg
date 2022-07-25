contract C {

	function test(uint256 a, uint256 b) public pure {
		assert(nested_if(a,b) != 42); 
		assert(nested_if(a,b) == 1);  
	}

	function nested_if(uint256 a, uint256 b) internal pure returns (uint256) {
		if (a < 5) {
			if (b > 1) {
				return 0;
			}
		}
		if (a == 2 && b == 2) {
			return 42; 
		}
		else {
			return 1;
		}
	}
}

