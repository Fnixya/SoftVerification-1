package groovy_spock

import spock.lang.*

// Test indeterminate involving 0,1,+INF: https://mathworld.wolfram.com/Indeterminate.html

// 21 tests, 4 failed
class BoundaryValueTests extends Specification {
   def o = new Operations()   

   def "Additions"() {
      expect:
      o.Sum(a, b) == expected

      where:
      a | b | expected
      Float.MAX_VALUE | Float.MAX_VALUE | Float.POSITIVE_INFINITY
      Float.MIN_VALUE | Float.MAX_VALUE | Float.MAX_VALUE
      -Float.MAX_VALUE | -Float.MAX_VALUE | Float.NEGATIVE_INFINITY
   }
   
   def "Multiplications"() {
      expect:
      o.Mult(a, b) == expected as float

      where:
      a | b | expected
      Float.MAX_VALUE | 2 | Float.POSITIVE_INFINITY
      Float.MIN_VALUE | 0.1 | 0.0f
      Float.MAX_VALUE | -2 | Float.NEGATIVE_INFINITY
      Float.MIN_VALUE | -0.1 as float | -0.0f
   }
}

class ArithmeticTests extends Specification {
   def o = new Operations()

   def "Addition with Infinities"() {
      expect:
      o.Sum(a, b) == expected
      
      where:
      a | b | expected
      0.0 | Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY
      0.0 | Float.NEGATIVE_INFINITY | Float.NEGATIVE_INFINITY
      Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY
      Float.NEGATIVE_INFINITY | Float.NEGATIVE_INFINITY | Float.NEGATIVE_INFINITY
   }

   def "Addition with NaNs"() {
      expect:
      o.Sum(a, b) == expected
      
      where:
      a | b | expected
      Float.NaN | 1 | Float.NaN
   }

   def "Multiplication with Infinities"() {
      expect:
      o.Mult(a, b) == expected

      where:
      a | b | expected
      0.0 | Float.POSITIVE_INFINITY | Float.NaN
      0.0 | Float.NEGATIVE_INFINITY | Float.NaN
      Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY
      Float.NEGATIVE_INFINITY | Float.NEGATIVE_INFINITY | Float.POSITIVE_INFINITY
   }

   def "0/b Divisions with 0.0 and 0.0f"() {
      expect:
      Float.compare(o.Div(a, b), 0.0f) == expected

      where:
      a | b | expected
      0.0 | 1.0 | 0
      0.0 | -1.0 | -1
      -0.0 | 1.0 | -1
      -0.0 | -1.0 | 0
      0.0f | 1.0f | 0
      0.0f | -1.0f | -1
      -0.0f | 1.0f | -1
      -0.0f | -1.0f | 0
   }

   def "Exponents"() {
      expect:
      o.Exp(a, b) == expected

      where:
      a | b | expected
      Math.exp(1) as float | Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY
      Math.exp(1) as float | Float.NEGATIVE_INFINITY | 0.0
      Math.exp(1) as float | Float.NaN | Float.NaN                                
      Math.exp(1) as float | 710 | Float.POSITIVE_INFINITY
      Math.exp(1) as float | -745 | 0.0
      Float.NaN | 0 | 1
      Float.NaN | 1 | Float.NaN
   }

   def "Logarithms"() {
      expect:
      o.Log(a) == expected

      where:
      a | expected
      0.0 | Float.NEGATIVE_INFINITY
      1.0 | 0.0
      -1.0 | Float.NaN
      Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY
      Float.NaN | Float.NaN
   }
}

// 12 tests
class TrigonometricTests extends Specification {
   def o = new Operations()

   def "With Sine"() {
      expect:
      o.Sin(a) == expected

      where:
      a | expected
      Float.NaN | Float.NaN
      Float.POSITIVE_INFINITY | Float.NaN
      Float.NEGATIVE_INFINITY | Float.NaN
   }

   def "With Cosine"() {
      expect:
      o.Cos(a) == expected

      where:
      a | expected
      Float.NaN | Float.NaN
      Float.POSITIVE_INFINITY | Float.NaN
      Float.NEGATIVE_INFINITY | Float.NaN
   }

   def "With Tangent"() {
      expect:
      o.Tan(a) == expected

      where:
      a | expected
      Float.NaN | Float.NaN
      Float.POSITIVE_INFINITY | Float.NaN
      Float.NEGATIVE_INFINITY | Float.NaN
   }

   def "With ArcTan"() {
      expect:
      o.ArcTan(a) == expected

      where:
      a | expected
      Float.NaN | Float.NaN
      Float.POSITIVE_INFINITY | Math.PI/2 as float
      Float.NEGATIVE_INFINITY | -Math.PI/2 as float
   }
}

// 27 tests, 11 failure
class ComparisonTests extends Specification {
   def o = new Operations()

   def "NaN Less than"() {
      expect:
      (a < b) == expected

      where:
      a | b | expected
      Float.NaN | Float.NaN | false
      Float.NaN | 0.0f | false
      Float.NaN | -0.0f | false
      Float.NaN | Float.POSITIVE_INFINITY | false
      Float.NaN | Float.NEGATIVE_INFINITY | false
   }

   def "NaN Greater than"() {
      expect:
      (a > b) == expected

      where:
      a | b | expected
      Float.NaN | 0.0f | false
      Float.NaN | -0.0f | false
      Float.NaN | Float.POSITIVE_INFINITY | false
      Float.NaN | Float.NEGATIVE_INFINITY | false
   }

   def "Less Than Infinity"() {
      expect:
      (a < b) == expected

      where:
      a | b | expected
      Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY | false
      Float.POSITIVE_INFINITY | Float.NEGATIVE_INFINITY | false
      Float.NEGATIVE_INFINITY | Float.POSITIVE_INFINITY | true
      Float.NEGATIVE_INFINITY | Float.NEGATIVE_INFINITY | false
      Float.MAX_VALUE | Float.POSITIVE_INFINITY | true
      Float.NEGATIVE_INFINITY | -Float.MAX_VALUE | true
   }

   def "Equility of Signed Zeros"() {
      expect:
      (a == b) == expected

      where:
      a | b | expected
      0.0f | -0.0f | false
   }

   def "with Signed Zeros"() {
      expect:
      Float.compare(a, b) == expected

      where:
      a | b | expected
      0.0f | 0.0f | 0
      -0.0f | 0.0f | -1
      0.0f | -0.0f | 1
      -0.0f | -0.0f | 0
      -Float.MIN_VALUE | -0.0f | -1
      0.0f | Float.MIN_VALUE | -1
   }

   def "Equalities with Infinities"() {
      expect:
      (a == b) == expected

      where:
      a | b | expected      
      Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY | true
      Float.POSITIVE_INFINITY | Float.NEGATIVE_INFINITY | false
      Float.NEGATIVE_INFINITY | Float.NEGATIVE_INFINITY | true
   }

   def "Equalities with NaN"() {
      expect:
      (a == b) == expected

      where:
      a | b | expected      
      Float.NaN | 0.0 | false
      Float.NaN | Float.POSITIVE_INFINITY | false
      Float.NaN | Float.NEGATIVE_INFINITY | false
      Float.NaN | Float.NaN | false
   }
}

// 15 tests
class IndeterminateTests extends Specification {
   def o = new Operations()

   def "0/0"() {
      expect:
      o.Div(0.0,0.0) == Float.NaN 
   }

   def "Division with zero"() {
      expect:
      o.Div(a, b) == expected

      where:
      a | b | expected
      1.0f | 0.0f | Float.POSITIVE_INFINITY
      1.0f | -0.0f | Float.NEGATIVE_INFINITY
      -1.0f | 0.0f | Float.NEGATIVE_INFINITY
      -1.0f | -0.0f | Float.POSITIVE_INFINITY
   }


   def "INF/INF Divisions"() {
      expect:
      o.Div(a, b) == expected

      where:
      a | b | expected
      Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY | Float.NaN
      Float.NEGATIVE_INFINITY | Float.POSITIVE_INFINITY | Float.NaN
      Float.POSITIVE_INFINITY | Float.NEGATIVE_INFINITY | Float.NaN
      Float.NEGATIVE_INFINITY | Float.NEGATIVE_INFINITY | Float.NaN
   }  

   def "0*INF"() {
      expect:
      o.Mult(a, b) == expected

      where:
      a | b | expected
      0.0 | Float.POSITIVE_INFINITY | Float.NaN
      0.0 | Float.NEGATIVE_INFINITY | Float.NaN
   }    

   def "INF-INF"() {
      expect:
      o.Sum(a, b) == expected

      where:
      a | b | expected
      Float.POSITIVE_INFINITY | Float.NEGATIVE_INFINITY | Float.NaN
   }

   def "Test Exponential Inderterminates"() {
      expect:
      o.Exp(a, b) == expected

      where:
      a | b | expected
      1 | Float.POSITIVE_INFINITY | Float.NaN   
      1 | Float.NEGATIVE_INFINITY | Float.NaN
      0 | 0 | 1                                
      Float.POSITIVE_INFINITY | 0 | 1          
   } 
}

// 7 tests
class RootTests extends Specification {
   def o = new Operations()

   def "Square Root Of Positive Numbers"() {
      expect:
      o.SquareRoot(a) == expected

      where:
      a | expected
      0.0f | 0.0f
      1.0f | 1.0f
      Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY
      Float.NaN | Float.NaN
   }

   def "Square Root Of Negative Numbers"() {
      expect:
      o.SquareRoot(a) == expected

      where:
      a | expected
      -0.0f | -0.0f          
      -1.0f | Float.NaN
      -Float.POSITIVE_INFINITY | Float.NaN
   }
}

// SUT: Operations
class Operations {

   float Div (float a=0.0, float b=0.0) {
      float res=a/b
      return res
   }

   float Sum (float a=0.0, float b=0.0) {
      float res=a+b
      return res
   }

   float Mult (float a=0.0, float b=0.0) {
      float res=a*b
      return res
   }

   float Exp (float a=0.0, float b=0.0) {
      float res=a**b
      return res
   }

   float Log (float a=0.0, float b=Math.exp(1)) {
      return Math.log(a)/Math.log(b)
   }

   float SquareRoot (float a=0.0) {
      float res=Math.sqrt(a)
      return res
   }

   float Sin (float a=0.0) {
      return Math.sin(a)
   }

   float Cos (float a=0.0) {
      return Math.cos(a)
   }

   float Tan (float a=0.0) {
      return Math.tan(a)
   }

   float ArcTan (float a=0.0) {
      return Math.atan(a)
   }
}