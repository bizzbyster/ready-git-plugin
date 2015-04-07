package com.smartbear.readyapi.plugin.git;

import com.eviware.soapui.plugins.PluginAdapter;
import com.eviware.soapui.plugins.PluginConfiguration;

@PluginConfiguration(groupId = "com.smartbear.readyapi.plugins", name ="Ready! API Git Plugin",
version = "0.1", autoDetect = true,
description = "A git plugin to share composite projects", infoUrl = "https://github.com/SmartBear/ready-git-plugin")
public class GitPluginConfig extends PluginAdapter {

}