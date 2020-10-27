(ns picpage-client.services.picture.views
  (:require [re-frame.core :as re-frame]
            [picpage-client.subs :as subs]))

(defn picture-content
  {:title "PicPage"
   :subtitle [(fn [] (str "PicPage @" @(re-frame/subscribe [::subs/current-page])))]
   :contents (fn []
               (re-frame/dispatch [::picture-events/load-info @(re-frame/subscribe [::subs/current-picture])])
               (let [api-url @(re-frame/subscribe [::subs/api-url])
                     userid @(re-frame/subscribe [::subs/current-page])
                     info @(re-frame/subscribe [::picture-subs/info])
                     link-base   (str api-url
                                      "api/authed/users/"
                                      userid
                                      "/pictures/"
                                      (:picture_id picture))
                     real-link (str link-base "/detail")]
                 (fn []
                   [:div.container>div.columns.is-centered>div.column.is-8
                    [:div.card
                     [:div.card-image
                      [:figure.image
                       [:img {:src real-link}]]]
                     [:div.card-content
                      [:div.media-content
                       [:p.title.is-4 (:title info)]]]]])))})
