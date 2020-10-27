(ns picpage-client.services.login.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::login-status
 (fn [db]
   (:login-status db)))
