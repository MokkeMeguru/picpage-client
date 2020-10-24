(ns picpage-client.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [picpage-client.events :as events]
   [picpage-client.views :as views]
   [picpage-client.config :as config]
   [picpage-client.routes :as routes]))

(defn dev-setup []
  (when config/debug?
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
