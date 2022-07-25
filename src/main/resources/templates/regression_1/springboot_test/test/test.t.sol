//Generated Test by TG
//[[['C', 'contract'], ['test', 'uint256', 'a', 'uint256', 'b']]]
import "forge-std/Test.sol";
import "../src/test.sol";

contract test_Test is Test {
	C c0;
	function setUp() public {
		c0 = new C();
	}
	function test_test_0() public {
		c0.contractC();
		c0.test(0, 2);
		assertTrue(true);
	}
}
