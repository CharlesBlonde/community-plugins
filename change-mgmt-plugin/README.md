Description
===========

A Deployit 3.6 community plugin that supports validation of Change Tickets in an ITIL Change Management system before deployment.

Installation
============

Place the 'change-mgmt-plugin-&lt;version&gt;.jar' file into your SERVER_HOME/plugins directory. 

The change-mgmt-plugin requires the deployment-checklist-plugin (ships with Deployit in the SERVER_HOME/available_plugins directory)

Configuration
=============

The change-mgmt-plugin uses a hidden deployment checklist property defined on a deployment package ('udm.Version'). Its value is calculated during the planning phase and is not intended to be set by users.

The property needs be hidden and boolean, and defined/added to your existing deployment checklist properties in your _SERVER_HOME/ext/synthetic.xml_ file:

    <type-modification type="udm.Version">
      <!-- will be set automatically during deployment planning, not by users -->
      <property name="satisfiesChangeTicket" kind="boolean" hidden="true" required="false" default="false" category="Deployment Checklist" />
    </type-modification>

The complementary property that determines whether an environment requires change tickets for deployments is configured as follows (again in _SERVER_HOME/ext/synthetic.xml_):

    <type-modification type="udm.Environment">
      <property name="requiresChangeTicket" kind="boolean" required="false" default="false" category="Deployment Checklist" />
    </type-modification>

If the default suffix 'ChangeTicket' clashes with an existing release authorization property, it can be changed by modifying the value of 'chg.ChangeManager.changeTicketChecklistItemSuffix' in _SERVER_HOME/conf/deployit-defaults.properties_.

Notes
=====

The change-mgmt-plugin adds the ability to include Change Requests ('chg.ChangeRequests') in deployment packages, which become Change Tickets ('chg.ChangeTicket') when deployed to change managers ('chg.ChangeManager'), representing a system that tracks change tickets such as Service Desk, in an environment.

If the deployment checklist item 'requiresChangeTicket' is set for an environment, deployments that do *not* create or update a 'chg.ChangeTicket' will result in a validation error. Since Change Tickets are validated against 'chg.ChangeManager' containers, there needs to be at least one such container in any environment for which 'requiresChangeTicket' is set.

It is recommended to include a blank 'chg.ChangeRequest' of the same name in each deployment package, and to set or modify, as appropriate, the 'requestId' property of the 'chg.ChangeTicket' resulting from the request.