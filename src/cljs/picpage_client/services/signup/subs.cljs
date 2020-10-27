(ns picpage-client.services.signup.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::signup-status
 (fn [db]
   (:signup-status db)))
