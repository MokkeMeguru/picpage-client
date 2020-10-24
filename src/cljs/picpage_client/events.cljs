(ns picpage-client.events
  (:require
   [re-frame.core :as re-frame]
   [picpage-client.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [reitit.frontend.easy :as rfe]
   [reitit.frontend.controllers :as rfc]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            db/default-db))

(re-frame/reg-event-fx
 ::navigate
 (fn [_cofx [_ & route]]
   {:navigate! route}))

(re-frame/reg-fx
 ::navigate!
 (fn [route]
   (println "navigate! to  " route)
   (apply rfe/push-state route)))

(re-frame/reg-event-db
 ::navigated
 (fn [db [_ new-match]]
   (let [old-match (:current-route db)
         controllers (rfc/apply-controllers (:controllers old-match) new-match)]
     (when-not (= new-match old-match) (.scrollTo js/window 0 0))
     (assoc db :current-route (assoc new-match :controllers controllers)))))

(re-frame/reg-event-db
 ::error
 (fn [db [_ error-message]]
   (assoc db :error (:default error-message))))

(re-frame/reg-event-db
 ::delete-error
 (fn [db [_]]
   (dissoc db :error)))
