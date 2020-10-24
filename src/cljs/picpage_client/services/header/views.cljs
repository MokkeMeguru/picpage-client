(ns picpage-client.services.header.views)

(def navbar-brand
  [:div.navbar-brand
   {:style {:align-items "center" :justify-content "space-around"}}
   [:a.navbar-item.start {:href "/#/"}
    [:strong.is-size-5 "PicPage"]]
   [:a.navbar-burger
    {:role "button" :data-target "navMenu" :aria-label "menu" :aria-expanded false}
    [:span {:aria-hidden true}]
    [:span {:aria-hidden true}]
    [:span {:aria-hidden true}]]])

(defn header [login?]
  [:nav.bd-navbar.navbar.has-shadow.is-spaced.is-fixed-top.is-dark>div.container
   {:role "navigation" :aria-label "main navigation"}
   navbar-brand
   [:div#navMenu.navbar-menu
    ;; [:div.navbar-start
    ;;  [:a.navbar-item "About"]]
    [:div.navbar-end
     [:div.navbar-item
      [:div.field.has-addons
       {:style {:margin-bottom 0}}
       [:div.control.has-icons-left
        [:input.input {:placeholder "search user" :type "text"}]
        [:span.icon.is-left>i.fas.fa-user]]
       [:div.control [:a.button.is-info [:span.icon.is-medium>i.fas.fa-search]]]]]
     (if-not login?
       [:div.navbar-item
        [:div.buttons
         [:a.button.is-primary {:href "#/signup"}
          [:strong "sign up"]]
         [:a.button.is-light {:href "#/login"}
          [:strong "log in"]]]]
       [:div.navbar-item
        [:div.buttons
         [:a.button.is-info {:href "#/upload"}
          [:strong "upload picpage"]]
         [:a.button.is-primary {:href "#/settings/profile"}
          [:strong "settings"]]
         [:a.button.is-light
          [:strong "logout"]]]])]]])
