(ns picpage-client.services.pictures.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::pictures
 (fn [db]
   (:pictures db)))
