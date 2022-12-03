package com.example.application.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.charts.model.Navigation;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.MainLayout;
import com.example.application.views..View;
import com.example.application.views.login.LoginView;
import com.vaadin.flow.component.avatar.Avatar;
import com.example.application.data.entity.User;
import com.example.application.security.AuthenticatedUser;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import com.vaadin.flow.component.icon.Icon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    /**
     * A simple navigation item component, based on ListItem element.
     */
    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, String iconClass, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            // Use Lumo classnames for various styling
            link.addClassNames("flex", "gap-xs", "h-m", "items-center", "px-s", "text-body");
            link.setRoute(view);

            Span text = new Span(menuTitle);
            // Use Lumo classnames for various styling
            text.addClassNames("font-medium", "text-m", "whitespace-nowrap");

            link.add(new LineAwesomeIcon(iconClass), text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

        /**
         * Simple wrapper to create icons using LineAwesome iconset.
         * See https://icons8.com/line-awesome
         */
        @NpmPackage(value = "line-awesome", version = "1.3.0")
        public static class LineAwesomeIcon extends Span {
            public LineAwesomeIcon(String lineawesomeClassnames) {
                // Use Lumo classnames for suitable font styling
                addClassNames("text-l", "text-secondary");
                if (!lineawesomeClassnames.isEmpty()) {
                    addClassNames(lineawesomeClassnames);
                }
            }
        }

    }

    private AuthenticatedUser authenticatedUser;
private AccessAnnotationChecker accessChecker;

public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
           this.authenticatedUser = authenticatedUser;
           this.accessChecker = accessChecker;

        addToNavbar(createHeaderContent());
    }

    private Component createHeaderContent() {
        Header header = new Header();
        header.addClassNames("box-border", "flex", "flex-col", "w-full");

        Div layout = new Div();
        layout.addClassNames("flex", "items-center", "px-l");

        H1 appName = new H1("My App");
        appName.addClassNames("my-m", "me-auto", "text-l");
        layout.add(appName);

        Optional<User> maybeUser = authenticatedUser.get();
if (maybeUser.isPresent()) {
    User user = maybeUser.get();

    Avatar avatar = new Avatar(user.getName());
    StreamResource resource = new StreamResource("profile-pic", () -> new ByteArrayInputStream(user.getProfilePicture()));
    avatar.setImageResource(resource);
    avatar.setThemeName("xsmall");
    avatar.getElement().setAttribute("tabindex", "-1");

    MenuBar userMenu = new MenuBar();
    userMenu.setThemeName("tertiary-inline contrast");

    MenuItem userName = userMenu.addItem("");
    Div div = new Div();
    div.add(avatar);
    div.add(user.getName());
    div.add(new Icon("lumo", "dropdown"));
    div.getElement().getStyle().set("display", "flex");
    div.getElement().getStyle().set("align-items", "center");
    div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
    userName.add(div);
    userName.getSubMenu().addItem("Sign out", e -> {
        authenticatedUser.logout();
    });

    layout.add(userMenu);
} else {
    Anchor loginLink = new Anchor("login", "Sign in");
    layout.add(loginLink);
}


        Nav nav = new Nav();
        nav.addClassNames("flex", "overflow-auto", "px-m", "py-xs");

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames("flex", "gap-s", "list-none", "m-0", "p-0");
        nav.add(list);

        for (MenuItemInfo menuItem : createMenuItems()) {
            if (accessChecker.hasAccess(menuItem.getView())) {
    list.add(menuItem);
}

        }

        header.add(layout, nav);
        return header;
    }

    private MenuItemInfo[] createMenuItems() {
        return new MenuItemInfo[] { //
                new MenuItemInfo("1", "la la-globe", View.class), //

new MenuItemInfo("2", "la la-file", View.class), //

new MenuItemInfo("3", "la la-th-list", View.class), //

new MenuItemInfo("4", "la la-user", View.class), //

new MenuItemInfo("5", "la la-map-marker", View.class), //

        };
    }

}
