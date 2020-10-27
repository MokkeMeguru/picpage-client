(ns picpage-client.routes
  (:require
   [reitit.coercion :as coercion]
   [reitit.coercion.spec]
   [clojure.spec.alpha :as s]
   [re-frame.core :as re-frame]
   [reitit.core :as r]
   [reitit.frontend.easy :as rfe]
   [picpage-client.events :as events]
   [picpage-client.subs :as subs]

   [picpage-client.messages :as messages]

   [picpage-client.services.home.views :as home-views]
   [picpage-client.services.signup.views :as signup-views]
   [picpage-client.services.login.views :as login-views]
   [picpage-client.services.profile-settings.views :as profile-settings-views]
   [picpage-client.services.upload.views :as upload-views]
   [picpage-client.services.pictures.views :as pictures-views]
   ;; for develop
   [clojure.pprint :as pprint]))

(def routes
  ["/"
   [""
    {:name :routes/home
     :view home-views/home
     :link-text "Home"
     :controllers
     [{:start #(println "entering home page")
       :stop #(println "leaving home page")}]}]
   ["login"
    {:name :routes/login
     :view login-views/login
     :link-text "login"}]
   ["logout"
    {:name :routes/logout
     :view "services/logout/view"
     :link-text "logout"
     :controllers [{:start #(do
                              (re-frame/dispatch [::events/logout])
                              (set! (-> js/window .-location .-href) "#/"))}]}]
   ["signup"
    {:name :routes/signup
     :view signup-views/signup
     :link-text "signup"
     :controllers [{:start #(do (println "entering signup page"))}]}]
   ["settings/"
    {:name :routes/settings
     :controllers [{:start #(do (println "entering settings page" @(re-frame/subscribe [::subs/login?]))
                                (when-not @(re-frame/subscribe [::subs/login?])
                                  (do
                                    (re-frame/dispatch [::events/error (:login messages/error-messages)])
                                    (set! (-> js/window .-location .-href) "#/"))))
                    :stop #(println "leaving settings page")}]}
    ["profile"
     {:name :routes/profile-settings
      :view profile-settings-views/profile-settings
      :link-text "Profile Settings"}]]
   ["upload"
    {:name :rotues/upload
     :view upload-views/upload
     :link-text "upload"
     :controllers [{:start #(println "entering upload page")
                    :stop #(println "leaving upload page")}]}]
   ["users/:name/"
    {:name :routes/user-pictures
     :coercion reitit.coercion.spec/coercion
     :parameters {:path {:name string?}}
     :controllers
     [{:parameters {:path [:name]}
       :start (fn [{:keys [path]}]
                (re-frame/dispatch [::events/current-page (:name path)])
                (println "entering users page: " (:name path)))}]}
    ["pictures"
     {:name :routes/pictures
      :view pictures-views/pictures
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

(r/match-by-path router "/users/sample-user/pictures")
