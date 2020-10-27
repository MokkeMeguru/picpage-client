(ns picpage-client.css
  (:require [garden.def :refer [defstyles]]
            [garden.stylesheet :refer [at-media]]))

(defstyles screen
  (at-media {:screen true :min-width "1024px"}
            [:.container {:max-width "960px"}])
  [:.bd-main {:margin-top "2rem"
              :min-height "100vh"
              :display "flex"}]
  [:.bd-warn {:background-color "#c87575"}
   [:p {:color "white"}
    [:strong {:color "white"}]]
   [:button {:color "white"}]
   [:a {:color "white"
        :text-decoration "underline"}]]
  [:.bd-header-titles
   {:margin-bottom "3rem"}]
  [:.titles
   {:margin-bottom "2rem"}]
  [:.bd-header
   [:.subtitle
    {:color "#7a7a7a"}]]
  [:.bd-lead {:position "relative"
              :padding "1.5rem"}])
