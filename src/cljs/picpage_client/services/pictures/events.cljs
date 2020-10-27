(ns picpage-client.services.pictures.events
  (:require [re-frame.core :as re-frame]
            [ajax.core :as ajax]))

(def pictures-list-success 1)
(def pictures-list-failed 2)

(re-frame/reg-event-db
 ::success-picture-list
 (fn [db [_ result]]
   (-> db
       (assoc :pictures result)
       (assoc :pictures-list-status pictures-list-success)
       (assoc :show-twirly false))))

(re-frame/reg-event-db
 ::failure-picture-list
 (fn [db [_ result]]
   (-> db
       (assoc :pictures [])
       (assoc :pictures-list-status pictures-list-failed)
       (assoc :show-twirly false))))

(re-frame/reg-event-fx
 ::login
 (fn [{:keys [db]} [_ userid]]
   {:db (-> db
            (dissoc :pictures-list-status)
            (assoc :show-twirly true))
    :http-xhrio
    {:method :get
     :uri (str (:api-url db) "api/authed/users/" (:current-page db) "/pictures/")
     :format (ajax/json-request-format)
     :response-format (ajax/json-response-format {:keywords? true})
     :on-success [::success-picture-list]
     :on-failure [::failure-picture-list]}}))

;; (re-frame/dispatch [::login "meguru"])
