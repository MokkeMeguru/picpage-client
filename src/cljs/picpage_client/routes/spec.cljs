(ns picpage-client.routes.spec
  (:require [clojure.spec.alpha :as s]))

(s/def ::home #(= (-> % :data :name) :routes/home))
(s/def ::login #(= (-> % :data :name) :routes/login))
(s/def ::logout #(= (-> % :data :name) :routes/logout))
(s/def ::signin #(= (-> % :data :name) :routes/signin))
(s/def ::profile-settings #(= (-> % :data :name) :routes/profile-settings))
(s/def ::user-pictures #(= (-> % :data :name) :routes/pictures))
(s/def ::picture #(= (-> % :data :name) :routes/picture))
(s/def ::upload #(= (-> % :data :name) :routes/upload))
