(ns picpage-client.domain.picture
  (:require [clojure.spec.alpha :as s]
            [picpage-client.domain.utils :refer [check-trim]]))

(def title-min-length 1)
(def title-max-length 18)
(def description-min-length 0)
(def description-max-length 255)

(s/def ::title
  (s/and string?
         check-trim
         #(<= title-min-length (count %) title-max-length)))

(s/def ::description
  (s/and string?
         check-trim
         #(<= description-min-length (count %) description-max-length)))
