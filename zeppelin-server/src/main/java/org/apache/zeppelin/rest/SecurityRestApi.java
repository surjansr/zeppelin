/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.zeppelin.rest;

import org.apache.zeppelin.server.JsonResponse;
import org.apache.zeppelin.ticket.TicketContainer;
import org.apache.shiro.SecurityUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hayssams on 24/04/15.
 */

@Path("/security")
@Produces("application/json")
public class SecurityRestApi {
  /**
   * Required by Swagger.
   */
  public SecurityRestApi() {
    super();
  }

  /**
   * Get ticket
   * Returns username & ticket
   * for anonymous access, username is always anonymous.
   * After getting this ticket, access through websockets become safe
   * @return 200 response
   */
  @GET
  @Path("ticket")
  public Response ticket() {
    Object oprincipal = SecurityUtils.getSubject().getPrincipal();
    String principal;
    if (oprincipal == null)
      principal = "anonymous";
    else
      principal = oprincipal.toString();

    // ticket set to anonymous for anonymous user. Simplify testing.
    String ticket;
    if ("anonymous".equals(principal))
     ticket = "anonymous";
    else
      ticket = TicketContainer.instance.getTicket(principal);

    Map<String, String> data = new HashMap<>();
    data.put("principal", principal);
    data.put("ticket", ticket);

    return new JsonResponse(Response.Status.OK, "", data).build();
  }
}
