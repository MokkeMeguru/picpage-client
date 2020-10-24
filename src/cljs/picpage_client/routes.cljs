(ns picpage-client.routes
  (:require
   [reitit.coercion :as coercion]
   [reitit.coercion.spec]
   [clojure.spec.alpha :as s]
   [re-frame.core :as re-frame]
   [reitit.core :as r]
   [reitit.frontend.easy :as rfe]
   [picpage-client.events :as events]
   [picpage-client.messages :as messages]
   ;; for develop
   [clojure.pprint :as pprint]))

(def routes
  ["/"
   [""
    {:name :routes/home
     :view "services/home/view"
     :link-text "Home"
     :controllers
     [{:start #(println "entering home page")
       :stop #(println "leaving home page")}]}]
   ["login"
    {:name :routes/login
     :view "services/login/view"
     :link-text "login"}]
   ["logout"
    {:name :routes/logout
     :view "services/logout/view"
     :link-text "logout"}]
   ["signup"
    {:name :routes/signup
     :view "services/signup/view"
     :link-text "signup"
     :controllers [{:start #(do (println "entering signup page"))}]}]
   ["settings/"
    {:name :routes/settings
     :controllers [{:start #(do (println "entering settings page")
                                (when true
                                  (do
                                    (re-frame/dispatch [::events/error (:login messages/error-messages)])
                                    (set! (-> js/window .-location .-href) "/#/"))))
                    :stop #(println "leaving settings page")}]}
    ["profile"
     {:name :routes/profile-settings
      :link-text "Profile Settings"}]]
   ["upload"
    {:name :rotues/upload
     :view "services/upload/view"
     :link-text "upload"
     :controllers [{:start #(println "entering upload page")
                    :stop #(println "leaving upload page")}]}]
   ["users/:name/"
    {:name :routes/user-pictures
     :coercion reitit.coercion.spec/coercion
     :parameters {:path {:name string?}}
     :controllers
     [{:start (fn [{:keys [path]}]
                (let  [user-name (:name path)]
                  (println "entering users page: " user-name)))}]}
    ["pictures"
     {:name :routes/pictures
      :view "services/pictures/view"
      :link-text "Pictures"}]
    ["pictures/:id"
     {:name :routes/picture
      :coercion reitit.coercion.spec/coercion
      :parameters {:path {:name string?
                          :id string?}}
      :view "service/picture"
      :link-text "Picture"}]]])

(def router (r/router routes))

(defn on-navigate [new-match]
  (when new-match
    (re-frame/dispatch [::events/navigated new-match])))

(defn init-routes! []
  (rfe/start!
   router
   on-navigate
   {:use-fragment true}))
