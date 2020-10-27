(ns picpage-client.services.login.events
  (:require [re-frame.core :as re-frame]
            [ajax.core :as ajax]))

(def login-success 1)
(def login-failed 2)

(re-frame/reg-event-db
 ::success-login
 (fn [db [_ result]]
   (-> db
       (assoc :login? true)
       (assoc :login-info result)
       (assoc :name (str "picpage user @" (:userid result)))
       (assoc :login-status login-success)
       (assoc :show-twirly false))))

(re-frame/reg-event-db
 ::failure-login
 (fn [db [_ result]]
   (println result)
   (-> db
       (assoc :login-status login-failed)
       (assoc :show-twirly false))))

(re-frame/reg-event-fx
 ::login
 (fn [{:keys [db]} [_ email password]]
   {:db (-> db
            (dissoc :login-status)
            (assoc :show-twirly true))
    :http-xhrio
    {:method :post
     :uri (str (:api-url db) "api/login")
     :params {:email email :password password}
     :timeout 6000
     :format (ajax/json-request-format)
     :response-format (ajax/json-response-format {:keywords? true})
     :on-success [::success-login]
     :on-failure [::failure-login]}}))
