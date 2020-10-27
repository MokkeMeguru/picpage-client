(ns picpage-client.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::current-route
 (fn [db]
   (:current-route db)))

(re-frame/reg-sub
 ::error
 (fn [db]
   (:error db)))

(re-frame/reg-sub
 ::login?
 (fn [db]
   (:login? db)))

(re-frame/reg-sub
 ::icon-image
 (fn [db]
   (:icon-image db)))

(re-frame/reg-sub
 ::api-url
 (fn [db]
   (:api-url db)))

(re-frame/reg-sub
 ::show-twirly
 (fn [db]
   (:show-twirly db)))

(re-frame/reg-sub
 ::current-page
 (fn [db]
   (:current-page db)))
