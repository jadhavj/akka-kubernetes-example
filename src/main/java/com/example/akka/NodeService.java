/*# Copyright 2017 Google Inc.
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
*/

package com.example.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.ConsistentHashingRouter.ConsistentHashableEnvelope;
import akka.routing.FromConfig;


public class NodeService extends AbstractActor {

	/** The target. */
	private ActorRef target = null;
	
	/** The worker router. */
	ActorRef workerRouter = getContext().actorOf(FromConfig.getInstance().props(Props.create(NodeSupervisor.class)),
			"nodeWorkerRouter");
	
	public static Props props() {
		return Props.create(NodeService.class);
	}
	/* (non-Javadoc)
	 * @see akka.actor.AbstractActor#createReceive()
	 */
	@Override
	public Receive createReceive() {
		return receiveBuilder()
		.match(String.class, message -> {
			System.out.println("MESSAGE PROCESSED BY SERVICE ACTOR @ " + new java.util.Date().toString());
			workerRouter.tell(new ConsistentHashableEnvelope(message, Math.random()), getSender());
		})
		.matchAny(message -> unhandled(message))
		.build();
	}
}
