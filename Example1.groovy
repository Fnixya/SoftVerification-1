package groovy_spock

import spock.lang.*

// Test indeterminate involving 0,1,+INF: https://mathworld.wolfram.com/Indeterminate.html

class FloatCalculusTests extends Specification {
   def o = new Operations()
  
   def "Test Comparisons with Infinity and NaN"() {
      expect:
      Float.compare(a, b) == expected

      where:
      a | b | expected
      Float.POSITIVE_INFINITY | Float.POSITIVE_INFINITY | 0
      Float.POSITIVE_INFINITY | Float.NaN | -1
      Float.NaN | Float.POSITIVE_INFINITY | -1
      Float.NEGATIVE_INFINITY | Float.NEGATIVE_INFINITY | 0
      Float.NaN | 0.0 | -1
      Float.NaN | Float.POSITIVE_INFINITY | -1
      Float.NaN | Float.NEGATIVE_INFINITY | -1
      Float.NaN | Float.NaN | -1
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
      Float.NaN | 0.0 | false
      -Float.NaN | 0.0 | true
      0.0 | -Float.NaN | false
      0.0 | Float.NaN | true
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

   def "Test Divisions 0/0"() {
      expect:
      Float.NaN==o.Div(0.0,0.0)
   }

   def "Test 0*INF"() {
      expect:
      Float.NaN==o.Mult(0.0,Float.POSITIVE_INFINITY)
   }    

   def "Test INF/INF"() {
      expect:
      Float.NaN==o.Div(Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY)
   }

   

   def "Test +INF"() {
      expect:
      Float.POSITIVE_INFINITY==o.Div(1.0,0.0)
   }
  
   def "Test -INF"() {
      expect:
      Float.NEGATIVE_INFINITY==o.Div(-1.0,0.0)
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