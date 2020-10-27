(ns picpage-client.services.signup.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [picpage-client.subs :as subs]))

(def status-success 1)
(def status-failed 2)

(re-frame/reg-event-db
 ::success-signup
 (fn [db [_ result]]
   (set! (-> js/window .-location .-href) "#/login")
   (-> db
       (assoc :signup-status? status-success)
       (assoc :show-twirly false))))

(re-frame/reg-event-db
 ::failure-signup
 (fn [db [_ result]]
   (-> db
       (assoc :signup-status status-failed)
       (assoc :show-twirly false))))

(re-frame/reg-event-fx
 ::signup
 (fn [{:keys [db]} [_ user]]
   {:db (-> db
            (dissoc :signup-status)
            (assoc :show-twirly true))
    :http-xhrio
    {:method :post
     :uri (str (:api-url db) "api/signup")
     :params user
     :timeout 6000
     :format (ajax/json-request-format)
     :response-format (ajax/json-response-format {:keywords? true})
     :on-success [::success-signup]
     :on-failure [::failure-signup]}}))

;; (re-frame/reg-event-db
;;  ::set-error
;;  (fn [db [_ res]]
;;    (assoc db :error res)))

;; (re-frame/reg-event-db
;;  ::set-result
;;  (fn [db [_ res]]
;;    (assoc db :result res)))

;; (re-frame/reg-sub
;;  ::result
;;  (fn [db]
;;    (:result db)))

;; (re-frame/reg-sub
;;  ::error
;;  (fn [db]
;;    (:error db)))

;; (re-frame/reg-event-fx
;;  ::plus
;;  (fn [{:keys [db]} _]
;;    {:db (assoc db :show-twirly true)
;;     :http-xhrio
;;     {:timeout 6000
;;      :response-format (ajax/json-response-format)
;;      :format (ajax/json-request-format)
;;      :method :post
;;      :uri (str (:api-url db) "api/samples/math/plus")
;;      :params {:x 1 :y 2}
;;      :on-success [::set-result]
;;      :on-failure [::set-error]}}))
