/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.common.domain.criteria.MonitorSearchCriteria;
import com.esofthead.mycollab.common.i18n.FollowerI18nEnum;
import com.esofthead.mycollab.common.service.MonitorItemService;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.view.user.ActivityStreamComponent;
import com.esofthead.mycollab.module.project.view.user.MyProjectListComponent;
import com.esofthead.mycollab.module.project.view.user.TaskStatusComponent;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractLazyPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.LabelLink;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class UserDashboardViewImpl extends AbstractLazyPageView implements
        UserDashboardView {
    private static final long serialVersionUID = 1L;

    private LabelLink followingTicketsLink;

    private List<Integer> prjKeys;

    @Override
    protected void displayView() {
        this.removeAllComponents();

        this.withMargin(false).withWidth("100%");

        final CssLayout headerWrapper = new CssLayout();
        headerWrapper.setWidth("100%");
        headerWrapper.setStyleName("projectfeed-hdr-wrapper");

        final MHorizontalLayout header = new MHorizontalLayout().withMargin(false).withWidth("100%");
        header.addStyleName("projectfeed-hdr");

        Button avatar = UserAvatarControlFactory
                .createUserAvatarEmbeddedButton(AppContext.getUserAvatarId(),
                        64);
        avatar.addClickListener(new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                String userFullLinkStr = AccountLinkGenerator
                        .generatePreviewFullUserLink(AppContext.getSiteUrl(),
                                AppContext.getUsername());
                getUI().getPage().open(userFullLinkStr, null);
            }
        });

        header.addComponent(avatar);

        final MVerticalLayout headerContent = new MVerticalLayout().withMargin(new MarginInfo(false, false, false, true));
        headerContent.addStyleName("projectfeed-hdr-content");

        final Label headerLabel = new Label(AppContext.getSession().getDisplayName());
        headerLabel.setStyleName(Reindeer.LABEL_H1);

        final MHorizontalLayout headerContentTop = new MHorizontalLayout().withMargin(new MarginInfo(false, false, true, false));
        headerContentTop.with(headerLabel).withAlign(headerLabel, Alignment.TOP_LEFT);

        if (AppContext.canBeYes(RolePermissionCollections.CREATE_NEW_PROJECT)) {
            final Button createProjectBtn = new Button(
                    AppContext
                            .getMessage(ProjectCommonI18nEnum.BUTTON_NEW_PROJECT),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final Button.ClickEvent event) {
                            final ProjectAddWindow projectNewWindow = new ProjectAddWindow();
                            UI.getCurrent().addWindow(projectNewWindow);
                        }
                    });
            createProjectBtn.setIcon(FontAwesome.PLUS);
            createProjectBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
            headerContentTop.addComponent(createProjectBtn);
            headerContentTop.setComponentAlignment(createProjectBtn,
                    Alignment.MIDDLE_LEFT);
        }


        followingTicketsLink = new LabelLink(AppContext.getMessage(
                FollowerI18nEnum.OPT_MY_FOLLOWING_TICKETS, 0), AppContext.getSiteUrl() + "#project/following");

        followingTicketsLink.setIconLink(FontAwesome.EYE);

        LabelLink timeTrackingLink = new LabelLink(AppContext.getMessage(TimeTrackingI18nEnum.TIME_RECORD_HEADER), AppContext.getSiteUrl()
                + "#project/timetracking");
        timeTrackingLink.setIconLink(ProjectAssetsManager.getAsset(ProjectTypeConstants.TIME));

        final MHorizontalLayout headerContentBottom = new MHorizontalLayout().with(followingTicketsLink, timeTrackingLink);

        headerContent.with(headerContentTop, headerContentBottom);

        header.with(headerContent).expand(headerContent);
        headerWrapper.addComponent(header);

        this.addComponent(headerWrapper);

        final MHorizontalLayout layout = new MHorizontalLayout().withMargin(new MarginInfo(false,
                false, true, false)).withWidth("100%");

        ActivityStreamComponent activityStreamComponent = new ActivityStreamComponent();
        final MVerticalLayout leftPanel = new MVerticalLayout().withSpacing(false).withMargin(new MarginInfo(false,
                true, false, false)).withWidth("100%").with(activityStreamComponent);

        final MVerticalLayout rightPanel = new MVerticalLayout().withMargin(false).withWidth("565px");
        MyProjectListComponent myProjectListComponent = new MyProjectListComponent();
        TaskStatusComponent taskStatusComponent = new TaskStatusComponent();
        rightPanel.with(myProjectListComponent, taskStatusComponent);

        layout.with(leftPanel, rightPanel).expand(leftPanel);

        final CssLayout contentWrapper = new CssLayout();
        contentWrapper.setWidth("100%");
        contentWrapper.addStyleName("content-wrapper");
        this.addComponent(contentWrapper);
        contentWrapper.addComponent(layout);

        final ProjectService prjService = ApplicationContextUtil.getSpringBean(ProjectService.class);
        prjKeys = prjService.getProjectKeysUserInvolved(
                AppContext.getUsername(), AppContext.getAccountId());
        if (CollectionUtils.isNotEmpty(prjKeys)) {
            activityStreamComponent.showFeeds(prjKeys);
            myProjectListComponent.displayDefaultProjectsList();
            displayFollowingTicketsCount();
        }

        if (prjKeys.size() != 0) {
            taskStatusComponent.showProjectTasksByStatus(prjKeys);
        }

    }

    private void displayFollowingTicketsCount() {
        // show following ticket numbers
        MonitorSearchCriteria searchCriteria = new MonitorSearchCriteria();
        searchCriteria.setUser(new StringSearchField(SearchField.AND,
                AppContext.getUsername()));
        searchCriteria.setExtraTypeIds(new SetSearchField<>(prjKeys
                .toArray(new Integer[prjKeys.size()])));
        MonitorItemService monitorService = ApplicationContextUtil
                .getSpringBean(MonitorItemService.class);
        int followingItemsCount = monitorService.getTotalCount(searchCriteria);
        followingTicketsLink
                .setTitle(AppContext.getMessage(
                        FollowerI18nEnum.OPT_MY_FOLLOWING_TICKETS, followingItemsCount));
    }
}