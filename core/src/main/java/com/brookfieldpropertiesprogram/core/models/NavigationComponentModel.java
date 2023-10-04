/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Adobe
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
package com.brookfieldpropertiesprogram.core.models;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { Resource.class })
public class NavigationComponentModel {

	@Inject
	@Optional
	private List<NavigationLink> socialLinks;

	@Inject
	@Optional
	private List<NavigationLink> menuLinks;

	@Inject
	@Optional
	private List<ParagraphModel> paragraphList;

	Logger LOG = LoggerFactory.getLogger(NavigationComponentModel.class);

	public List<NavigationLink> getSocialLinks() {
		return new ArrayList(socialLinks);
	}

	public List<NavigationLink> getMenuLinks() {
		return (menuLinks != null) ? new ArrayList(menuLinks) : null;
	}

	public List<ParagraphModel> getParagraphList() {
		return (paragraphList != null) ? new ArrayList(paragraphList) : null;
	}
}
