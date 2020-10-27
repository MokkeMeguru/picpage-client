(ns picpage-client.core
  (:require
   [reagent.dom :as rdom]
   [day8.re-frame.http-fx]
   [re-frame.core :as re-frame]
   [picpage-client.events :as events]
   [picpage-client.views :as views]
   [picpage-client.config :as config]
   [picpage-client.routes :as routes]
   [picpage-client.subs :as subs]))

(defn dev-setup []
  (when config/debug?
    (re-frame/dispatch [::events/api-url "http://localhost:3000/"])
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  ;; router initialization
  (routes/init-routes!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))


;; (re-frame/dispatch [::events/initialize-db])
;; (dev-setup)

;; @(re-frame/subscribe [::subs/api-url])
