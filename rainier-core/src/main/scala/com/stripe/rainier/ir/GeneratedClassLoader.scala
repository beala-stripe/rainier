package com.stripe.rainier.ir

private[ir] class GeneratedClassLoader(cf: ApplyClassGenerator,
                                       helpers: Seq[ExprClassGenerator],
                                       parent: ClassLoader)
    extends ClassLoader(parent) {
  val cfClass = defineClass(cf.name, cf.bytes, 0, cf.bytes.length)
  val helperClasses: Map[String, Class[_]] =
    helpers.map {
      case cg =>
        cg.name -> defineClass(cg.name, cg.bytes, 0, cg.bytes.length)
    }.toMap

  override def findClass(name: String): Class[_] =
    if (name == cf.name)
      cfClass
    else
      helperClasses(name)

  def newInstance: CompiledFunction =
    cfClass.newInstance.asInstanceOf[CompiledFunction]
}
