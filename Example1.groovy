package groovy_spock

import spock.lang.*

// Test indeterminate involving 0,1,+INF: https://mathworld.wolfram.com/Indeterminate.html

class BoundaryValuesTests extends Specification {
   def o = new Operations()
   
   def "Additions"() {
      expect:
      o.Sum(a, b) == expected

      where:
      a | b | expected
      Float.MAX_VALUE | Float.MAX_VALUE | Float.POSITIVE_INFINITY
      Float.MIN_VALUE | Float.MAX_VALUE | 0.0
      -Float.MAX_VALUE | -Float.MAX_VALUE | Float.NEGATIVE_INFINITY
      -Float.MIN_VALUE | -Float.MIN_VALUE | 0.0
   }

   def "Multiplications"() {
      expect:
      o.Sum(a, b) == expected

      where:
      a | b | expected
      Float.MAX_VALUE | 2 | Float.POSITIVE_INFINITY
      Float.MIN_VALUE | 0.5 | 0.0
      Float.MAX_VALUE | -2 | Float.NEGATIVE_INFINITY
      Float.MIN_VALUE | -0.5 | -0.0
   }
}

class FloatCalculusTests extends Specification {
   def o = new Operations()
  
   def "Test Total Order Comparisons with NaN"() {
      expect:
      (a < b) == expected

      where:
      a | b | expected
      Float.NaN | Float.NaN | false
      Float.NaN | 0.0 | false
      Float.NaN | -0.0 | true
      Float.NaN | Float.POSITIVE_INFINITY | false
      Float.NaN | Float.NEGATIVE_INFINITY | false
      Float.NaN | Float.NaN | false
      0.0 | Float.NaN | false
      -0.0 | Float.NaN | false
      Float.POSITIVE_INFINITY | Float.NaN | false
      Float.NEGATIVE_INFINITY | Float.NaN | false
   }

   def "Test Comparisons with Infinity"() {
      expect:
      Float.compare(a, b) == expected

      where:
      a | b | expected
      Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY | 0
      Float.POSITIVE_INFINITY | Float.NEGATIVE_INFINITY | 1
      Float.NEGATIVE_INFINITY | Float.POSITIVE_INFINITY | -1
      Float.NEGATIVE_INFINITY | Float.NEGATIVE_INFINITY | 0
      Float.MAX_VALUE | Float.POSITIVE_INFINITY | -1
      Float.NEGATIVE_INFINITY | -Float.MAX_VALUE | -1
   }

   def "Test Comparisons with Zeros"() {
      expect:
      Float.compare(a, b) == expected

      where:
      a | b | expected
      0.0 | 0.0 | 0
      -0.0 | 0.0 | -1
      0.0 | -0.0 | 1
      -0.0 | -0.0 | 0
      -Float.MIN_VALUE | -0.0 | -1
      0.0 | Float.MIN_VALUE | -1
   }

   def "Test Equality with NaN"() {
      expect:
      (a == b) == expected

      where:
      a | b | expected      
      Float.NaN | 0.0 | false
      0.0 | Float.NaN | false
      Float.NaN | -Float.NaN | false
      -Float.NaN | Float.NaN | false
      Float.NaN | Float.NaN | false
      -Float.NaN | -Float.NaN | false
   }
   
   def "Test Sum of Infinities"() {
      expect:
      o.Sum(a, b) == expected
      
      where:
      a | b | expected
      Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY
      Float.NaN | Float.POSITIVE_INFINITY | Float.NEGATIVE_INFINITY
      Float.NaN | Float.NEGATIVE_INFINITY | Float.POSITIVE_INFINITY
      Float.NEGATIVE_INFINITY | Float.NEGATIVE_INFINITY | Float.NEGATIVE_INFINITY
   }

   def "Test Sum of NaNs"() {
      expect:
      o.Sum(a, b) == expected
      
      where:
      a | b | expected
      Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY
      Float.NaN | Float.POSITIVE_INFINITY | Float.NEGATIVE_INFINITY
      Float.NaN | Float.NEGATIVE_INFINITY | Float.POSITIVE_INFINITY
      Float.NEGATIVE_INFINITY | Float.NEGATIVE_INFINITY | Float.NEGATIVE_INFINITY
   }

   def "Test Indeterminate Form 0/0"() {
      expect:
      Float.NaN==o.Div(0.0,0.0)
   }

   def "Test Indeterminate Form 0*INF"() {
      expect:
      Float.NaN==o.Mult(0.0,Float.POSITIVE_INFINITY)
   }    

   def "Test Indeterminate Form INF/INF"() {
      expect:
      Float.NaN==o.Div(Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY)
   }   

   def "Test Division with zero"() {
      expect:
      o.Div(a, b) == expected

      where:
      a | b | expected
      1.0 | 0.0 | Float.POSITIVE_INFINITY
      1.0 | -0.0 | Float.NEGATIVE_INFINITY
      -1.0 | 0.0 | Float.NEGATIVE_INFINITY
      -1.0 | -0.0 | Float.POSITIVE_INFINITY
   }

   def "Test -0,0 - False Negative"() {
      expect:
      // new Float (-0.0) == o.Div(0.0,-1.0)
      o.Div(0.0,-1.0) == -0.0
   }

   def "Test Results Divisions 0/b"() {
      expect:
      Float.compare(o.Div(a, b), 0.0) == expected

      where:
      a | b | expected
      0.0 | 1.0 | 0
      0.0 | -1.0 | -1
      -0.0 | 1.0 | -1
      -0.0 | -1.0 | 0
   }

   def "Test SquareRoots"() {
      expect:
      o.SquareRoot(a) == expected

      where:
      a | expected
      -1 | Float.NaN
      0.0 | 0.0
      1 | 1
      -0.0 | -0.0
   }

   def "Test sqrt -1"() {
      expect:
      Float.NaN==o.SquareRoot(-1.0)
   }

   def "INF/-INF"() {
      expect:
      o.Div(a, b) == expected

      where:
      a | b | expected
      Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY | Float.NaN
      Float.NEGATIVE_INFINITY | Float.POSITIVE_INFINITY | Float.NaN
      Float.POSITIVE_INFINITY | Float.NEGATIVE_INFINITY | Float.NaN
      Float.NEGATIVE_INFINITY | Float.NEGATIVE_INFINITY | Float.NaN
   }

   def "Test sqrt -1"() {
      expect:
      Float.NaN==o.SquareRoot(-1.0)
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


// class FloatTests extends Specification {
//    def "INF/-INF"() {
//       expect:
//       o.Div(a, b) == expected

//       where:
//       a | b | expected
//       Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY | Float.NaN
//       Float.NEGATIVE_INFINITY | Float.POSITIVE_INFINITY | Float.NaN
//       Float.POSITIVE_INFINITY | Float.NEGATIVE_INFINITY | Float.NaN
//       Float.NEGATIVE_INFINITY | Float.NEGATIVE_INFINITY | Float.NaN
//    }
// }

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

}