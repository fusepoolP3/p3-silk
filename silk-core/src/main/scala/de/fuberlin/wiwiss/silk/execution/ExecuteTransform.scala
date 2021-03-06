package de.fuberlin.wiwiss.silk.execution

import de.fuberlin.wiwiss.silk.runtime.task.Task
import de.fuberlin.wiwiss.silk.datasource.Source
import de.fuberlin.wiwiss.silk.output.Output
import de.fuberlin.wiwiss.silk.linkagerule.TransformRule
import de.fuberlin.wiwiss.silk.entity.EntityDescription
import de.fuberlin.wiwiss.silk.config.Dataset

/**
 * Executes a transformation rule.
 */
class ExecuteTransform(source: Source,
                       dataset: Dataset,
                       rule: TransformRule,
                       outputs: Traversable[Output] = Traversable.empty) extends Task[Any] {

  def execute(): Unit = {
    // Retrieve entities
    val entityDesc =
      new EntityDescription(
        variable = dataset.variable,
        restrictions = dataset.restriction,
        paths = rule.paths.toIndexedSeq
      )
    val entities = source.retrieve(entityDesc)

    // Open outputs
    for(output <- outputs) output.open()

    // Transform all entities and write to outputs
    for { entity <- entities
          value <- rule(entity)
          output <- outputs } {
      output.writeLiteralStatement(entity.uri, rule.targetProperty, value)
    }

    // Close outputs
    for(output <- outputs) output.close()
  }
}

object ExecuteTransform {
  def empty = new ExecuteTransform(null, null, null, null)
}