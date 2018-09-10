# Copyright 2017 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License

package com.example.akka;

import akka.actor.AbstractActor;
import akka.actor.AbstractActor.*;
import akka.pattern.Patterns;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import static akka.dispatch.Futures.future;
import scala.concurrent.*;

/**
 * The Class BulletinBoardMessageWorker.
 */
public class NodeWorker extends AbstractActor {

	/* (non-Javadoc)
	 * @see akka.actor.AbstractActor#createReceive()
	 */
	@Override
	public Receive createReceive() {
		return receiveBuilder()
		.match(String.class, message -> {
			System.out.println("Message processed by WORKER ACTOR " + getSelf() + " @ " + LocalDateTime.now() + "\n");
			Patterns.pipe(future(() -> {return message;}, (ExecutionContext) SimpleClusterMain.dispatcher()), SimpleClusterMain.dispatcher()).to(getSender());
		})
		.matchAny(message -> unhandled(message))
		.build();
	}
	
}
