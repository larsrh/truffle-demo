package info.hupel

import com.oracle.truffle.api.Truffle
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.nodes.{Node, RootNode}
import com.oracle.truffle.api.nodes.Node.Child


abstract class Expression extends Node {
  def execute(arguments: Array[Int]): Int
}

case class Add(@Child left: Expression, @Child right: Expression) extends Expression {
  override def execute(arguments: Array[Int]): Int =
    left.execute(arguments) + right.execute(arguments)
}

case class Arg(index: Int) extends Expression {
  override def execute(arguments: Array[Int]): Int =
    arguments(index)
}

case class Function(body: Expression) extends RootNode(null) {
  override def execute(frame: VirtualFrame): Object = {
    val arguments = frame.getArguments
    val first = arguments(0).asInstanceOf[Array[Int]]
    body.execute(first): Integer
  }
}

object Main {

  def main(args: Array[String]): Unit = {
    val sample = Function(Add(Add(Arg(0), Arg(1)), Arg(2)))
    val target = Truffle.getRuntime().createCallTarget(sample)
    println(target.call(Array(10, 11, 21)))
  }

}
