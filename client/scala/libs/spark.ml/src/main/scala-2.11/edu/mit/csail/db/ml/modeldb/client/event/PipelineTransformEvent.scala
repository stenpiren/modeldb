package edu.mit.csail.db.ml.modeldb.client.event

import com.twitter.util.Await
import edu.mit.csail.db.ml.modeldb.client.ModelDbSyncer
import modeldb.ModelDBService.FutureIface

/**
  * Event indicating that a Pipeline performed a transformation.
  *
  * @param transformEvents - The individual TransformEvents.
  */
case class PipelineTransformEvent(transformEvents: TransformEvent*) extends ModelDbEvent {
  override def sync(client: FutureIface, mdbs: Option[ModelDbSyncer]): Unit = {
    val res = Await.result(client.storePipelineTransformEvent(transformEvents.map(_.makeEvent(mdbs.get))))

    res.zipWithIndex.foreach { pair =>
      transformEvents(pair._2).associate(pair._1, mdbs.get)
    }
  }
}